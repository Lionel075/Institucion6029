package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.institucion6029.dao.DocenteDAO;
import com.institucion6029.model.Docente;
import com.institucion6029.model.Seccion;
import com.institucion6029.utility.Conexion;

public class DocenteDAOImpl implements DocenteDAO {

    @Override
    public Docente obtenerPorIdUsuario(String idUsuario) {
        Docente docente = null;
        String sql = "SELECT id_docente, id_usuario, dni, nombres, apellidos, telefono, correo_personal "
                   + "FROM per_docentes WHERE id_usuario = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, idUsuario.trim());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    docente = new Docente();
                    docente.setIdDocente(rs.getInt("id_docente"));
                    docente.setIdUsuario(rs.getString("id_usuario"));
                    docente.setDni(rs.getString("dni"));
                    docente.setNombres(rs.getString("nombres"));
                    docente.setApellidos(rs.getString("apellidos"));
                    docente.setTelefono(rs.getString("telefono"));
                    docente.setCorreoPersonal(rs.getString("correo_personal"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DocenteDAOImpl] Error al obtener docente por id_usuario: " + e.getMessage());
        }
        return docente;
    }

    @Override
    public Seccion obtenerSeccionTutorada(String idDocente, int idAno) {
        Seccion seccion = null;
        // CORRECCIÓN: Ajustado milimétricamente a tu tabla 'sch_secciones' y columna 'id_docente_tutor'
        String sql = "SELECT id_seccion, id_ano, grado, seccion, id_docente_tutor, vacantes_disponibles "
                   + "FROM sch_secciones WHERE id_docente_tutor = ? AND id_ano = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, idDocente.trim());
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
            System.err.println("[DocenteDAOImpl] Error al obtener sección tutorada: " + e.getMessage());
        }
        return seccion;
    }
}
