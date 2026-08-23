package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.institucion6029.dao.AsistenciaDAO;
import com.institucion6029.exception.ErrorTransaccionException;
import com.institucion6029.model.AlumnoAsistencia;
import com.institucion6029.model.SeccionDocente;
import com.institucion6029.utility.Conexion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsistenciaDAOImpl implements AsistenciaDAO {

	private static final Logger LOG = LoggerFactory.getLogger(AsistenciaDAOImpl.class);

	private static final String SQL_SECCIONES = """
			SELECT s.id_seccion, s.grado, s.seccion, s.turno, s.aula,
			       (SELECT COUNT(*) FROM mat_reservas_matricula r
			         WHERE r.id_seccion = s.id_seccion
			           AND r.id_ano = s.id_ano
			           AND r.estado_reserva IN ('Pendiente','Aprobada'))          AS alumnos,
			       (SELECT max_estudiantes_aula FROM web_datos_institucion LIMIT 1) AS capacidad,
			       (SELECT COUNT(*) FROM asi_asistencias a
			         WHERE a.id_seccion = s.id_seccion
			           AND a.fecha_asistencia = CURDATE())                        AS registrados,
			       (SELECT DATE_FORMAT(MIN(a2.fecha_hora_registro), '%h:%i %p')
			          FROM asi_asistencias a2
			         WHERE a2.id_seccion = s.id_seccion
			           AND a2.fecha_asistencia = CURDATE())                       AS hora_registro
			  FROM sch_secciones s
			 WHERE s.id_docente_tutor = ?
			   AND s.id_ano = ?
			 ORDER BY s.grado, s.seccion
			""";

	private static final String SQL_SECCION_UNA = """
			SELECT s.id_seccion, s.grado, s.seccion, s.turno, s.aula,
			       (SELECT COUNT(*) FROM mat_reservas_matricula r
			         WHERE r.id_seccion = s.id_seccion
			           AND r.id_ano = s.id_ano
			           AND r.estado_reserva IN ('Pendiente','Aprobada'))          AS alumnos,
			       (SELECT max_estudiantes_aula FROM web_datos_institucion LIMIT 1) AS capacidad,
			       (SELECT COUNT(*) FROM asi_asistencias a
			         WHERE a.id_seccion = s.id_seccion
			           AND a.fecha_asistencia = CURDATE())                        AS registrados,
			       (SELECT DATE_FORMAT(MIN(a2.fecha_hora_registro), '%h:%i %p')
			          FROM asi_asistencias a2
			         WHERE a2.id_seccion = s.id_seccion
			           AND a2.fecha_asistencia = CURDATE())                       AS hora_registro
			  FROM sch_secciones s
			 WHERE s.id_seccion = ?
			   AND s.id_docente_tutor = ?
			   AND s.id_ano = ?
			""";

	private static final String SQL_ALUMNOS = """
			SELECT a.id_alumno, a.nombres, a.apellidos,
			       (SELECT x.estado_asistencia FROM asi_asistencias x
			         WHERE x.id_alumno = a.id_alumno
			           AND x.fecha_asistencia = CURDATE()) AS estado
			  FROM per_alumnos a
			  INNER JOIN mat_reservas_matricula m ON m.id_alumno = a.id_alumno
			 WHERE m.id_seccion = ?
			   AND m.id_ano = ?
			   AND m.estado_reserva IN ('Pendiente','Aprobada')
			 ORDER BY a.apellidos, a.nombres
			""";

	private static final String SQL_GUARDAR = """
			INSERT INTO asi_asistencias
			  (id_alumno, id_seccion, id_ano, fecha_asistencia, estado_asistencia, id_docente_registro)
			VALUES (?,?,?,?,?,?)
			ON DUPLICATE KEY UPDATE
			  estado_asistencia       = VALUES(estado_asistencia),
			  id_seccion              = VALUES(id_seccion),
			  modificado              = 1,
			  fecha_hora_modificacion = CURRENT_TIMESTAMP
			""";

	@Override
	public List<SeccionDocente> listarSecciones(String idDocente, int idAno) {
		List<SeccionDocente> lista = new ArrayList<>();

		try (Connection con = Conexion.obtenerConexion(); PreparedStatement ps = con.prepareStatement(SQL_SECCIONES)) {

			ps.setString(1, idDocente);
			ps.setInt(2, idAno);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(mapearSeccion(rs));
				}
			}
		} catch (SQLException e) {
			LOG.error("Error al listar secciones del docente. idDocente={}, idAno={}", idDocente, idAno, e);
		}
		return lista;
	}

	@Override
	public SeccionDocente obtenerSeccion(int idSeccion, String idDocente, int idAno) {
		try (Connection con = Conexion.obtenerConexion();
				PreparedStatement ps = con.prepareStatement(SQL_SECCION_UNA)) {

			ps.setInt(1, idSeccion);
			ps.setString(2, idDocente);
			ps.setInt(3, idAno);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapearSeccion(rs);
				}
			}
		} catch (SQLException e) {
			LOG.error("Error al obtener sección. idSeccion={}, idDocente={}, idAno={}", idSeccion, idDocente, idAno, e);
		}
		return null;
	}

	@Override
	public List<AlumnoAsistencia> listarAlumnos(int idSeccion, int idAno) {
		List<AlumnoAsistencia> lista = new ArrayList<>();

		try (Connection con = Conexion.obtenerConexion(); PreparedStatement ps = con.prepareStatement(SQL_ALUMNOS)) {

			ps.setInt(1, idSeccion);
			ps.setInt(2, idAno);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					AlumnoAsistencia al = new AlumnoAsistencia();
					int id = rs.getInt("id_alumno");

					al.setIdAlumno(id);
					al.setCodigo("ALU-" + id);
					al.setNombres(rs.getString("nombres"));
					al.setApellidos(rs.getString("apellidos"));

					String estado = rs.getString("estado");
					al.setRegistrado(estado != null);
					al.setEstado(estado != null ? estado : "Presente");

					lista.add(al);
				}
			}
		} catch (SQLException e) {
			LOG.error("Error al listar alumnos de la sección. idSeccion={}, idAno={}", idSeccion, idAno, e);
		}
		return lista;
	}

	@Override
	public void guardarAsistencia(int idSeccion, int idAno, String idDocente, Map<Integer, String> estados)
			throws ErrorTransaccionException {

		Connection con = null;
		try {
			con = Conexion.obtenerConexion();
			con.setAutoCommit(false);

			try (PreparedStatement ps = con.prepareStatement(SQL_GUARDAR)) {
				Date hoy = Date.valueOf(LocalDate.now());

				for (Map.Entry<Integer, String> e : estados.entrySet()) {
					ps.setInt(1, e.getKey());
					ps.setInt(2, idSeccion);
					ps.setInt(3, idAno);
					ps.setDate(4, hoy);
					ps.setString(5, e.getValue());
					ps.setString(6, idDocente);
					ps.addBatch();
				}
				ps.executeBatch();
			}

			con.commit();

		} catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
					LOG.error("Error al hacer rollback de asistencia. idSeccion={}", idSeccion, ex);
				}
			}
			throw new ErrorTransaccionException(
					"Fallo de base de datos al guardar asistencia. idSeccion=" + idSeccion
					+ ", idAno=" + idAno + ": " + e.getMessage(), e);
		} finally {
			if (con != null) {
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (SQLException e) {
					LOG.error("Error al cerrar conexión tras guardar asistencia", e);
				}
			}
		}
	}

	private SeccionDocente mapearSeccion(ResultSet rs) throws java.sql.SQLException {
		SeccionDocente s = new SeccionDocente();
		s.setIdSeccion(rs.getInt("id_seccion"));
		s.setGrado(rs.getString("grado"));
		s.setSeccion(rs.getString("seccion"));
		s.setTurno(rs.getString("turno"));
		s.setAula(rs.getString("aula"));
		s.setAlumnos(rs.getInt("alumnos"));
		s.setCapacidad(rs.getInt("capacidad"));
		s.setRegistrados(rs.getInt("registrados"));

		String hora = rs.getString("hora_registro");
		if (hora != null) {
			hora = hora.replace("AM", "a.m.").replace("PM", "p.m.");
		}
		s.setHoraRegistro(hora);
		return s;
	}
}