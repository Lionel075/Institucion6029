package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.institucion6029.dao.LandingPageDAO;
import com.institucion6029.model.InstitucionWeb;
import com.institucion6029.utility.Conexion;

public class LandingPageDAOImpl implements LandingPageDAO {

    @Override
    public InstitucionWeb obtenerDatosInstitucion() {
        InstitucionWeb institucion = null;
        
        // Consulta SQL para extraer la información oficial del Landing Page
        String sql = "SELECT id_institucion, codigo_modular, nombre_institucion, descripcion, "
                   + "direccion, nivel_educativo, max_estudiantes_aula, pension_preferencial, "
                   + "modalidad FROM web_datos_institucion WHERE id_institucion = 1";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                institucion = new InstitucionWeb();
                institucion.setIdInstitucion(rs.getInt("id_institucion"));
                institucion.setCodigoModular(rs.getString("codigo_modular"));
                institucion.setNombreInstitucion(rs.getString("nombre_institucion"));
                institucion.setDescripcion(rs.getString("descripcion"));
                institucion.setDireccion(rs.getString("direccion"));
                institucion.setNivelEducativo(rs.getString("nivel_educativo"));
                institucion.setMaxEstudiantesAula(rs.getInt("max_estudiantes_aula"));
                institucion.setPensionPreferencial(rs.getDouble("pension_preferencial"));
                institucion.setModalidad(rs.getString("modalidad"));
            }
            
        } catch (SQLException e) {
            System.err.println("[LandingPageDAOImpl] Error SQL al recuperar los datos del colegio: " + e.getMessage());
        }
        
        return institucion;
    }
}
