package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.institucion6029.dao.SeccionDAO;
import com.institucion6029.utility.Conexion;

public class SeccionDAOImpl implements SeccionDAO {

	private static final Logger LOG = LoggerFactory.getLogger(SeccionDAOImpl.class);
	
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
        	LOG.error("Error al obtener vacantes por grado: ", e);
        }
        return totalVacantes;
    }
}

