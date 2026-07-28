package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.institucion6029.dao.SeccionDAO;
import com.institucion6029.model.Seccion;
import com.institucion6029.utility.Conexion;

public class SeccionDAOImpl implements SeccionDAO {

    @Override
    public int obtenerVacantesDisponiblesPorGrado(String grado, int idAno) {
        int totalVacantes = 0;
        String sql = "SELECT SUM(vacantes_disponibles) FROM sch_secciones WHERE LEFT(grado, 1) = LEFT(?, 1) AND id_ano = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, grado);
            pstmt.setInt(2, idAno);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalVacantes = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[SeccionDAOImpl] Error al obtener vacantes por grado: " + e.getMessage());
        }
        return totalVacantes;
    }

    @Override
    public Seccion buscarSeccionDisponibleParaAsignacion(String grado, int idAno) {
        Seccion seccion = null;
        // Selecciona la primera sección (A, B o C) que tenga al menos 1 vacante libre
        String sql = "SELECT id_seccion, id_ano, grado, seccion, id_docente_tutor, vacantes_disponibles "
                   + "FROM sch_secciones WHERE grado = ? AND id_ano = ? AND vacantes_disponibles > 0 "
                   + "ORDER BY seccion ASC LIMIT 1";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, grado);
            pstmt.setInt(2, idAno);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    seccion = new Seccion();
                    seccion.setIdSeccion(rs.getInt("id_seccion"));
                    seccion.setIdAnio(rs.getInt("id_ano"));
                    seccion.setGrado(rs.getString("grado"));
                    seccion.setSeccion(rs.getString("seccion"));
                    seccion.setIdDocenteTutor(rs.getString("id_docente_tutor"));
                    seccion.setVacantesDisponibles(rs.getInt("vacantes_disponibles"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[SeccionDAOImpl] Error al buscar sección disponible: " + e.getMessage());
        }
        return seccion;
    }

    @Override
    public boolean modificarContadorVacantes(int idSeccion, int cambio) {
        String sql = "UPDATE sch_secciones SET vacantes_disponibles = vacantes_disponibles + ? "
                   + "WHERE id_seccion = ? AND (vacantes_disponibles + ?) BETWEEN 0 AND 32";
        
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, cambio);
            pstmt.setInt(2, idSeccion);
            pstmt.setInt(3, cambio);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[SeccionDAOImpl] Error al modificar contador de vacantes: " + e.getMessage());
            return false;
        }
    }
}

