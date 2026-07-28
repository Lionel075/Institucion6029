package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.institucion6029.dao.UsuarioDAO;
import com.institucion6029.model.Usuario;
import com.institucion6029.utility.Conexion;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario validarAcceso(String usuario, String clave) {
        Usuario user = null;
        // CORRECCIÓN: Ajustado a tu tabla 'acc_usuarios' y columna 'contrasenia'
        String sql = "SELECT id_usuario, correo, contrasenia, id_rol FROM acc_usuarios "
                   + "WHERE (id_usuario = ? OR correo = ?) AND contrasenia = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.trim());
            pstmt.setString(2, usuario.trim());
            pstmt.setString(3, clave.trim());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new Usuario();
                    user.setIdUsuario(rs.getString("id_usuario"));
                    user.setCorreo(rs.getString("correo"));
                    user.setClave(rs.getString("contrasenia")); // Mapea 'contrasenia' al atributo de tu POJO
                    user.setIdRol(rs.getInt("id_rol")); 
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAOImpl] Error en autenticación relacional: " + e.getMessage());
        }
        return user;
    }

}
