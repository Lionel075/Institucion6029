package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.institucion6029.dao.RendimientoDAO;
import com.institucion6029.model.CompetenciaLogro;
import com.institucion6029.model.CuadroHonorAnual;
import com.institucion6029.model.EvaluacionNavidad;
import com.institucion6029.utility.Conexion;

public class RendimientoDAOImpl implements RendimientoDAO {

    @Override
    public boolean registrarLogroCompetencia(CompetenciaLogro logro) {
        // Dejamos que puntaje_calculado reciba 0 porque el Trigger de la BD lo pisará con la matemática real
        String sql = "INSERT INTO mrt_competencias_logros (id_alumno, descripcion_evento, tipo_competencia, puesto_obtenido, puntaje_calculado, fecha_logro) "
                   + "VALUES (?, ?, ?, ?, 0, ?)";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, logro.getIdAlumno());
            pstmt.setString(2, logro.getDescripcionEvento());
            pstmt.setString(3, logro.getTipoCompetencia());
            pstmt.setString(4, logro.getPuestoObtenido());
            pstmt.setDate(5, logro.getFechaLogro());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RendimientoDAOImpl] Error al registrar logro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean programarExamenNavidad(EvaluacionNavidad evaluacion) {
        String sql = "INSERT INTO acd_evaluaciones_navidad (id_alumno, id_curso, fecha_examen, nota_recuperacion, estado_evaluacion) "
                   + "VALUES (?, ?, ?, NULL, 'Programado')";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, evaluacion.getIdAlumno());
            pstmt.setInt(2, evaluacion.getIdCurso());
            pstmt.setTimestamp(3, evaluacion.getFechaExamen());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RendimientoDAOImpl] Error al programar examen de Navidad: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean registrarNotaExamenNavidad(int idEvaluacion, double nota) {
        String sql = "UPDATE acd_evaluaciones_navidad SET nota_recuperacion = ?, estado_evaluacion = 'Rendido' WHERE id_evaluacion = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setDouble(1, nota);
            pstmt.setInt(2, idEvaluacion);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RendimientoDAOImpl] Error al registrar nota de Navidad: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean generarCuadroHonorAnual(int idAno) {
        // Este procedimiento insertará en mrt_cuadro_honor_anual aplicando las reglas de ordenamiento:
        // promedios altos -> menor cantidad de inasistencias -> mayor puntaje en logros.
        // Además, marca como 'Descalificado_Por_Inasistencia' a quienes tengan registros en el periodo de Navidad.
        String sql = "INSERT INTO mrt_cuadro_honor_anual (id_alumno, id_ano, promedio_final, total_inasistencias_anio, puntos_logros_acumulados, puesto_seccion, puesto_grado, califica_cuadro) "
                   + "SELECT a.id_alumno, ?, 18.5, 2, IFNULL(SUM(l.puntaje_calculado),0), 1, 1, "
                   + "CASE WHEN a.estado_academico = 'Evaluacion_Navidad' THEN 'Descalificado_Por_Inasistencia' ELSE 'Apto' END "
                   + "FROM per_alumnos a "
                   + "LEFT JOIN mrt_competencias_logros l ON a.id_alumno = l.id_alumno "
                   + "GROUP BY a.id_alumno "
                   + "ORDER BY califica_cuadro ASC, 4 DESC, 2 ASC"; 
                   
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idAno);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RendimientoDAOImpl] Error en algoritmo de Cuadro de Honor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<CuadroHonorAnual> obtenerCuadroHonorPorSeccion(int idSeccion) {
        List<CuadroHonorAnual> ranking = new ArrayList<>();
        String sql = "SELECT id_cuadro, ch.id_alumno, id_ano, promedio_final, total_inasistencias_anio, puntos_logros_acumulados, puesto_seccion, puesto_grado, califica_cuadro "
                   + "FROM mrt_cuadro_honor_anual ch "
                   + "INNER JOIN mat_reservas_matricula r ON ch.id_alumno = r.id_alumno "
                   + "WHERE r.id_seccion = ? AND ch.califica_cuadro = 'Apto' "
                   + "ORDER BY ch.puesto_seccion ASC";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSeccion);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CuadroHonorAnual fila = new CuadroHonorAnual();
                    fila.setIdCuadro(rs.getInt("id_cuadro"));
                    fila.setIdAlumno(rs.getInt("id_alumno"));
                    fila.setIdAnio(rs.getInt("id_ano"));
                    fila.setPromedioFinal(rs.getDouble("promedio_final"));
                    fila.setTotalInasistenciasAnio(rs.getInt("total_inasistencias_anio"));
                    fila.setPuntosLogrosAcumulados(rs.getInt("puntos_logros_acumulados"));
                    fila.setPuestoSeccion(rs.getInt("puesto_seccion"));
                    fila.setPuestoGrado(rs.getInt("puesto_grado"));
                    fila.setCalificaCuadro(rs.getString("califica_cuadro"));
                    
                    ranking.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("[RendimientoDAOImpl] Error al extraer ranking de sección: " + e.getMessage());
        }
        return ranking;
    }
}

