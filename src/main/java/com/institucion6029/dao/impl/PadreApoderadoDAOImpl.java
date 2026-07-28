package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.institucion6029.dao.PadreApoderadoDAO;
import com.institucion6029.model.PadreApoderado;
import com.institucion6029.utility.Conexion;

public class PadreApoderadoDAOImpl implements PadreApoderadoDAO {

    @Override
    public PadreApoderado obtenerPorIdUsuario(String idUsuario) {
        PadreApoderado apoderado = null;
        String sql = "SELECT id_usuario, dni, nombres, apellidos, telefono, direccion "
                   + "FROM per_padres_apoderados WHERE id_usuario = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, idUsuario.trim());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    apoderado = new PadreApoderado();
                    apoderado.setIdUsuario(rs.getString("id_usuario"));
                    apoderado.setDni(rs.getString("dni"));
                    apoderado.setNombres(rs.getString("nombres"));
                    apoderado.setApellidos(rs.getString("apellidos"));
                    apoderado.setTelefono(rs.getString("telefono"));
                    apoderado.setDireccion(rs.getString("direccion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[PadreApoderadoDAOImpl] Error al obtener apoderado por id_usuario: " + e.getMessage());
        }
        return apoderado;
    }
}

