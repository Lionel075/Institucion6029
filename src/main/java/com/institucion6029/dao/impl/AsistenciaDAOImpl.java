package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.institucion6029.dao.AsistenciaDAO;
import com.institucion6029.model.Alumno;
import com.institucion6029.model.AsistenciaDiaria;
import com.institucion6029.utility.Conexion;

public class AsistenciaDAOImpl implements AsistenciaDAO {

    @Override
    public List<Alumno> listarAlumnosPorSeccion(int idSeccion) {
        List<Alumno> lista = new ArrayList<>();
        // Consulta limpia para traer los alumnos asociados a una sección en el año activo
        String sql = "SELECT a.id_alumno, a.nombres, a.apellidos "
                   + "FROM mtr_alumnos a "
                   + "INNER JOIN sch_secciones_alumnos sa ON a.id_alumno = sa.id_alumno "
                   + "WHERE sa.id_seccion = ? AND sa.ano_escolar = 2 "
                   + "ORDER BY a.apellidos, a.nombres ASC";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSeccion);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Alumno alu = new Alumno();
                    alu.setIdAlumno(rs.getInt("id_alumno"));
                    alu.setNombres(rs.getString("nombres"));
                    alu.setApellidos(rs.getString("apellidos"));
                    lista.add(alu);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AsistenciaDAOImpl] Error al listar alumnos por sección: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean registrarAsistencia(List<AsistenciaDiaria> listaAsistencia) {
        if (listaAsistencia == null || listaAsistencia.isEmpty()) return false;

        // Ajustado a los campos exactos de tu POJO: id_alumno, id_curso, id_docente, fecha, estado_inicial, estado_final
        String sql = "INSERT INTO sch_asistencias_diarias (id_alumno, id_curso, id_docente, fecha, estado_inicial, estado_final) "
                   + "VALUES (?, ?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE estado_inicial = ?, estado_final = ?";

        Connection con = null;
        try {
            con = Conexion.obtenerConexion();
            con.setAutoCommit(false); // Transacción atómica

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                for (AsistenciaDiaria ast : listaAsistencia) {
                    pstmt.setInt(1, ast.getIdAlumno());
                    pstmt.setInt(2, ast.getIdCurso());
                    pstmt.setString(3, ast.getIdDocente());
                    pstmt.setDate(4, new java.sql.Date(ast.getFecha().getTime()));
                    
                    // Al tomar lista por primera vez, el estado inicial y final suelen arrancar iguales (ej: "Presente" o "Falta")
                    pstmt.setString(5, ast.getEstadoInicial()); 
                    pstmt.setString(6, ast.getEstadoFinal() != null ? ast.getEstadoFinal() : ast.getEstadoInicial());
                    
                    // Valores para el UPDATE si el docente vuelve a guardar la misma fecha/curso/alumno
                    pstmt.setString(7, ast.getEstadoInicial());
                    pstmt.setString(8, ast.getEstadoFinal());
                    
                    pstmt.addBatch();
                }
                
                int[] resultados = pstmt.executeBatch();
                con.commit();
                return resultados.length > 0;
            }
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("[AsistenciaDAOImpl] Error al registrar asistencia con POJO real: " + e.getMessage());
            return false;
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    @Override
    public boolean justificarFalta(int idAsistencia, String motivoJustificado) {
        // Actualiza el estado_final e introduce la fecha y hora de modificación automática de auditoría
        String sql = "UPDATE sch_asistencias_diarias SET estado_final = ?, fecha_modificacion = NOW() WHERE id_asistencia = ?";
        
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, motivoJustificado); // Ej: "Falta/Justificada"
            pstmt.setInt(2, idAsistencia);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AsistenciaDAOImpl] Error al justificar falta: " + e.getMessage());
            return false;
        }
    }
}
