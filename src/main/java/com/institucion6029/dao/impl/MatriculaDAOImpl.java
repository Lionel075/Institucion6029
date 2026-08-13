package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.institucion6029.dao.MatriculaDAO;
import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.utility.Conexion;
import com.institucion6029.model.Seccion;
import com.institucion6029.exception.ReservaDuplicadaException;
import com.institucion6029.exception.PeriodoMatriculaCerradoException;
import com.institucion6029.exception.ErrorTransaccionException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatriculaDAOImpl implements MatriculaDAO {

	private static final Logger LOG = LoggerFactory.getLogger(MatriculaDAOImpl.class);
	
	@Override
	public Seccion registrarReservaConControlDeCupo(ReservaMatricula reserva, String grado) 
	        throws ReservaDuplicadaException, PeriodoMatriculaCerradoException, ErrorTransaccionException {

	    String sqlPeriodo = "SELECT CASE "
	                       + "  WHEN CURDATE() BETWEEN fecha_inicio_preferencial AND fecha_fin_preferencial THEN 'Preferencial' "
	                       + "  WHEN CURDATE() BETWEEN fecha_inicio_general AND fecha_fin_general THEN 'General' "
	                       + "  ELSE NULL "
	                       + "END AS tipo_calculado "
	                       + "FROM cfg_anos_escolares WHERE id_ano = ? AND estado_ano = 'Activo'";

	    String sqlDuplicado = "SELECT id_reserva FROM mat_reservas_matricula "
	                         + "WHERE id_alumno = ? AND id_ano = ? AND estado_reserva IN ('Pendiente','Aprobada') "
	                         + "LIMIT 1";

	    String sqlSeccion = "SELECT id_seccion, id_ano, grado, seccion, id_docente_tutor, vacantes_disponibles "
	                       + "FROM sch_secciones WHERE grado = ? AND id_ano = ? AND vacantes_disponibles > 0 "
	                       + "ORDER BY seccion ASC LIMIT 1 FOR UPDATE";

	    String sqlInsertReserva = "INSERT INTO mat_reservas_matricula (id_alumno, id_seccion, id_ano, tipo_reserva, estado_reserva) "
	                             + "VALUES (?, ?, ?, ?, ?)";

	    String sqlDescuentoVacante = "UPDATE sch_secciones SET vacantes_disponibles = vacantes_disponibles - 1 "
	                                + "WHERE id_seccion = ? AND vacantes_disponibles > 0";

	    Connection con = null;
	    try {
	        con = Conexion.obtenerConexion();
	        con.setAutoCommit(false);

	        String tipoCalculado = null;
	        try (PreparedStatement pstmtPeriodo = con.prepareStatement(sqlPeriodo)) {
	            pstmtPeriodo.setInt(1, reserva.getIdAnio());
	            try (ResultSet rsPeriodo = pstmtPeriodo.executeQuery()) {
	                if (rsPeriodo.next()) {
	                    tipoCalculado = rsPeriodo.getString("tipo_calculado");
	                }
	            }
	        }

	        if (tipoCalculado == null) {
	            con.rollback();
	            throw new PeriodoMatriculaCerradoException(
	                "El periodo de matrícula para id_ano=" + reserva.getIdAnio()
	                + " está cerrado, fuera de fechas, o el año no está 'Activo'.");
	        }
	        reserva.setTipoReserva(tipoCalculado);

	        try (PreparedStatement pstmtDup = con.prepareStatement(sqlDuplicado)) {
	            pstmtDup.setInt(1, reserva.getIdAlumno());
	            pstmtDup.setInt(2, reserva.getIdAnio());

	            try (ResultSet rsDup = pstmtDup.executeQuery()) {
	                if (rsDup.next()) {
	                    con.rollback();
	                    throw new ReservaDuplicadaException(
	                        "El alumno con id=" + reserva.getIdAlumno()
	                        + " ya tiene una reserva Pendiente o Aprobada para el año " + reserva.getIdAnio());
	                }
	            }
	        }

	        Seccion seccionAsignada = null;

	        try (PreparedStatement pstmtSeccion = con.prepareStatement(sqlSeccion)) {
	            pstmtSeccion.setString(1, grado);
	            pstmtSeccion.setInt(2, reserva.getIdAnio());

	            try (ResultSet rs = pstmtSeccion.executeQuery()) {
	                if (rs.next()) {
	                    seccionAsignada = new Seccion();
	                    seccionAsignada.setIdSeccion(rs.getInt("id_seccion"));
	                    seccionAsignada.setIdAnio(rs.getInt("id_ano"));
	                    seccionAsignada.setGrado(rs.getString("grado"));
	                    seccionAsignada.setSeccion(rs.getString("seccion"));
	                    seccionAsignada.setIdDocenteTutor(rs.getString("id_docente_tutor"));
	                    seccionAsignada.setVacantesDisponibles(rs.getInt("vacantes_disponibles"));
	                }
	            }
	        }

	        if (seccionAsignada == null) {
	            con.rollback();
	            return null;
	        }

	        try (PreparedStatement pstmtInsert = con.prepareStatement(sqlInsertReserva)) {
	            pstmtInsert.setInt(1, reserva.getIdAlumno());
	            pstmtInsert.setInt(2, seccionAsignada.getIdSeccion());
	            pstmtInsert.setInt(3, reserva.getIdAnio());
	            pstmtInsert.setString(4, reserva.getTipoReserva());
	            pstmtInsert.setString(5, reserva.getEstadoReserva());
	            pstmtInsert.executeUpdate();
	        }

	        try (PreparedStatement pstmtUpdate = con.prepareStatement(sqlDescuentoVacante)) {
	            pstmtUpdate.setInt(1, seccionAsignada.getIdSeccion());
	            if (pstmtUpdate.executeUpdate() == 0) {
	                con.rollback();
	                return null;
	            }
	        }

	        con.commit();
	        return seccionAsignada;

	    } catch (ReservaDuplicadaException | PeriodoMatriculaCerradoException e) {
	        throw e;
	    } catch (SQLException e) {
	    	LOG.error("Error en transacción de reserva con control de cupo. idAlumno={}", reserva.getIdAlumno(), e);
	        if (con != null) {
	            try { con.rollback(); } catch (SQLException ex) {
	            	LOG.error("Error al hacer rollback de la reserva", ex);
	            }
	        }
	        throw new ErrorTransaccionException(
	            "Fallo de base de datos al procesar la reserva de matrícula para idAlumno="
	            + reserva.getIdAlumno() + ": " + e.getMessage(), e);
	    } finally {
	        if (con != null) {
	            try {
	                con.setAutoCommit(true);
	                con.close();
	            } catch (SQLException e) {
	            	LOG.error("Error al cerrar conexión tras registrar reserva", e);
	            }
	        }
	    }
	}
	
	@Override
	public List<ReservaMatricula> listarReservasPorApoderado(String idPadre, int idAno) {
	    List<ReservaMatricula> lista = new ArrayList<>();
	    String sql = "SELECT r.id_reserva, r.id_alumno, r.id_seccion, r.id_ano, r.fecha_hora_reserva, "
	               + "r.tipo_reserva, r.estado_reserva, a.nombres, a.apellidos, s.grado, s.seccion "
	               + "FROM mat_reservas_matricula r "
	               + "JOIN per_alumnos a ON r.id_alumno = a.id_alumno "
	               + "JOIN sch_secciones s ON r.id_seccion = s.id_seccion "
	               + "WHERE a.id_padre = ? AND r.id_ano = ? "
	               + "ORDER BY r.fecha_hora_reserva DESC";

	    try (Connection con = Conexion.obtenerConexion();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        pstmt.setString(1, idPadre);
	        pstmt.setInt(2, idAno);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                ReservaMatricula r = new ReservaMatricula();
	                r.setIdReserva(rs.getInt("id_reserva"));
	                r.setIdAlumno(rs.getInt("id_alumno"));
	                r.setIdSeccion(rs.getInt("id_seccion"));
	                r.setIdAnio(rs.getInt("id_ano"));
	                r.setFechaHoraReserva(rs.getTimestamp("fecha_hora_reserva"));
	                r.setTipoReserva(rs.getString("tipo_reserva"));
	                r.setEstadoReserva(rs.getString("estado_reserva"));
	                r.setNombreAlumno(rs.getString("nombres"));
	                r.setApellidosAlumno(rs.getString("apellidos"));
	                r.setGrado(rs.getString("grado"));
	                r.setSeccion(rs.getString("seccion"));
	                lista.add(r);
	            }
	        }
	    } catch (SQLException e) {
	        LOG.error("Error al listar reservas del apoderado. idPadre={}", idPadre, e);
	    }
	    return lista;
	}

	@Override
	public boolean cancelarReserva(int idReserva, String idPadreSolicitante) throws ErrorTransaccionException {

	    String sqlBuscar = "SELECT r.id_seccion, r.estado_reserva, a.id_padre "
	                      + "FROM mat_reservas_matricula r "
	                      + "JOIN per_alumnos a ON r.id_alumno = a.id_alumno "
	                      + "WHERE r.id_reserva = ? FOR UPDATE";

	    String sqlEliminar = "DELETE FROM mat_reservas_matricula WHERE id_reserva = ?";

	    String sqlDevolverVacante = "UPDATE sch_secciones SET vacantes_disponibles = vacantes_disponibles + 1 "
	                               + "WHERE id_seccion = ?";

	    Connection con = null;
	    try {
	        con = Conexion.obtenerConexion();
	        con.setAutoCommit(false);

	        int idSeccion;
	        try (PreparedStatement pstmtBuscar = con.prepareStatement(sqlBuscar)) {
	            pstmtBuscar.setInt(1, idReserva);

	            try (ResultSet rs = pstmtBuscar.executeQuery()) {
	                if (!rs.next()) {
	                    con.rollback();
	                    return false; // La reserva no existe
	                }

	                String idPadreReal = rs.getString("id_padre");
	                String estadoActual = rs.getString("estado_reserva");
	                idSeccion = rs.getInt("id_seccion");

	                if (!idPadreReal.equals(idPadreSolicitante)) {
	                    LOG.warn("Intento de cancelación no autorizado. idReserva={} — solicitante: {}",
	                            idReserva, idPadreSolicitante);
	                    con.rollback();
	                    return false;
	                }

	                if (!"Pendiente".equals(estadoActual)) {
	                    LOG.warn("Cancelación rechazada: idReserva={} está en estado '{}', no 'Pendiente'",
	                            idReserva, estadoActual);
	                    con.rollback();
	                    return false;
	                }
	            }
	        }

	        try (PreparedStatement pstmtEliminar = con.prepareStatement(sqlEliminar)) {
	            pstmtEliminar.setInt(1, idReserva);
	            pstmtEliminar.executeUpdate();
	        }

	        try (PreparedStatement pstmtDevolver = con.prepareStatement(sqlDevolverVacante)) {
	            pstmtDevolver.setInt(1, idSeccion);
	            pstmtDevolver.executeUpdate();
	        }

	        con.commit();
	        return true;

	    } catch (SQLException e) {
	        LOG.error("Error en transacción de cancelación. idReserva={}", idReserva, e);
	        if (con != null) {
	            try { con.rollback(); } catch (SQLException ex) {
	                LOG.error("Error al hacer rollback de la cancelación", ex);
	            }
	        }
	        throw new ErrorTransaccionException(
	            "Fallo de base de datos al cancelar la reserva idReserva=" + idReserva + ": " + e.getMessage(), e);
	    } finally {
	        if (con != null) {
	            try {
	                con.setAutoCommit(true);
	                con.close();
	            } catch (SQLException e) {
	                LOG.error("Error al cerrar conexión tras cancelar reserva", e);
	            }
	        }
	    }
	}
	
	@Override
	public int expirarReservasVencidas(int horasLimite) throws ErrorTransaccionException {

	    String sqlBuscarVencidas = "SELECT id_reserva, id_seccion FROM mat_reservas_matricula "
	                              + "WHERE estado_reserva = 'Pendiente' "
	                              + "AND fecha_hora_reserva < DATE_SUB(NOW(), INTERVAL ? HOUR) "
	                              + "FOR UPDATE";

	    String sqlExpirar = "UPDATE mat_reservas_matricula SET estado_reserva = 'Expirada' WHERE id_reserva = ?";

	    String sqlDevolverVacante = "UPDATE sch_secciones SET vacantes_disponibles = vacantes_disponibles + 1 "
	                               + "WHERE id_seccion = ?";

	    Connection con = null;
	    try {
	        con = Conexion.obtenerConexion();
	        con.setAutoCommit(false);

	        List<int[]> vencidas = new ArrayList<>(); // {idReserva, idSeccion}

	        try (PreparedStatement pstmtBuscar = con.prepareStatement(sqlBuscarVencidas)) {
	            pstmtBuscar.setInt(1, horasLimite);
	            try (ResultSet rs = pstmtBuscar.executeQuery()) {
	                while (rs.next()) {
	                    vencidas.add(new int[]{ rs.getInt("id_reserva"), rs.getInt("id_seccion") });
	                }
	            }
	        }

	        if (vencidas.isEmpty()) {
	            con.commit();
	            return 0;
	        }

	        try (PreparedStatement pstmtExpirar = con.prepareStatement(sqlExpirar);
	             PreparedStatement pstmtDevolver = con.prepareStatement(sqlDevolverVacante)) {

	            for (int[] par : vencidas) {
	                pstmtExpirar.setInt(1, par[0]);
	                pstmtExpirar.executeUpdate();

	                pstmtDevolver.setInt(1, par[1]);
	                pstmtDevolver.executeUpdate();
	            }
	        }

	        con.commit();
	        LOG.info("Expiración automática: {} reserva(s) vencida(s) liberada(s).", vencidas.size());
	        return vencidas.size();

	    } catch (SQLException e) {
	        LOG.error("Error al expirar reservas vencidas", e);
	        if (con != null) {
	            try { con.rollback(); } catch (SQLException ex) {
	                LOG.error("Error al hacer rollback de expiración de reservas", ex);
	            }
	        }
	        throw new ErrorTransaccionException(
	            "Fallo de base de datos al expirar reservas vencidas: " + e.getMessage(), e);
	    } finally {
	        if (con != null) {
	            try {
	                con.setAutoCommit(true);
	                con.close();
	            } catch (SQLException e) {
	                LOG.error("Error al cerrar conexión tras expirar reservas", e);
	            }
	        }
	    }
	}
}