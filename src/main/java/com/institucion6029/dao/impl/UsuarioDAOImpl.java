package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.institucion6029.dao.UsuarioDAO;
import com.institucion6029.model.Usuario;
import com.institucion6029.utility.Conexion;
import com.institucion6029.utility.PasswordUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioDAOImpl implements UsuarioDAO {

	private static final Logger LOG = LoggerFactory.getLogger(UsuarioDAOImpl.class);
	
    @Override
    public Usuario validarAcceso(String usuario, String clave) {
        Usuario user = null;
        // Ya NO se filtra por contrasenia en el SQL: el hash se valida en Java
        String sqlBuscar = "SELECT id_usuario, correo, contrasenia, id_rol FROM acc_usuarios "
                          + "WHERE (id_usuario = ? OR correo = ?)";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sqlBuscar)) {

            pstmt.setString(1, usuario.trim());
            pstmt.setString(2, usuario.trim());

            String hashAlmacenado = null;

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    hashAlmacenado = rs.getString("contrasenia");
                    user = new Usuario();
                    user.setIdUsuario(rs.getString("id_usuario"));
                    user.setCorreo(rs.getString("correo"));
                    user.setClave(hashAlmacenado);
                    user.setIdRol(rs.getInt("id_rol"));
                }
            }

            if (user == null) {
                return null; // Usuario no existe
            }

            String claveIngresada = clave.trim();

            if (PasswordUtil.esHashBCrypt(hashAlmacenado)) {
                // Caso normal: ya migrado a BCrypt
                if (!PasswordUtil.verificar(claveIngresada, hashAlmacenado)) {
                    return null;
                }
            } else {
                // Caso legacy: contraseña todavía en texto plano
                if (!hashAlmacenado.equals(claveIngresada)) {
                    return null;
                }
                // Login correcto con clave legacy -> migración silenciosa a BCrypt
                rehashSilencioso(con, user.getIdUsuario(), claveIngresada);
            }

            return user;

        } catch (SQLException e) {
        	LOG.error("Error en autenticación relacional", e);
            return null;
        }
    }

    /**
     * Re-hashea la contraseña de un usuario justo después de un login legacy exitoso.
     * No lanza excepción hacia arriba: si falla, el login igual se considera válido
     * (se reintentará la migración en el siguiente login).
     */
    private void rehashSilencioso(Connection con, String idUsuario, String claveEnTextoPlano) {
        String sqlUpdate = "UPDATE acc_usuarios SET contrasenia = ? WHERE id_usuario = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, PasswordUtil.hash(claveEnTextoPlano));
            pstmt.setString(2, idUsuario);
            pstmt.executeUpdate();
            System.out.println("[UsuarioDAOImpl] Contraseña migrada a BCrypt para: " + idUsuario);
        } catch (SQLException e) {
            System.err.println("[UsuarioDAOImpl] No se pudo migrar la contraseña de "
                    + idUsuario + " a BCrypt: " + e.getMessage());
        }
    }
}