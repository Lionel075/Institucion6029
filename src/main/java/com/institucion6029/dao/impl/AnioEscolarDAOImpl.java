package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.institucion6029.dao.AnioEscolarDAO;
import com.institucion6029.model.AnioEscolar;
import com.institucion6029.utility.Conexion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnioEscolarDAOImpl implements AnioEscolarDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(AnioEscolarDAOImpl.class);

    @Override
    public AnioEscolar obtenerAnioActivo() {
        AnioEscolar anio = null;
        // Regla de negocio: solo debe existir UN año escolar en estado 'Activo' a la vez.
        // Si hubiera más de uno por error de datos, se toma el de id_ano más alto (el más reciente).
        String sql = "SELECT id_ano, anio_calendario, fecha_inicio_preferencial, fecha_fin_preferencial, "
                   + "fecha_inicio_general, fecha_fin_general, estado_ano "
                   + "FROM cfg_anos_escolares WHERE estado_ano = 'Activo' "
                   + "ORDER BY id_ano DESC LIMIT 1";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                anio = new AnioEscolar();
                anio.setIdAnio(rs.getInt("id_ano"));
                anio.setAnioCalendario(rs.getInt("anio_calendario"));
                anio.setFechaInicioPreferencial(rs.getDate("fecha_inicio_preferencial"));
                anio.setFechaFinPreferencial(rs.getDate("fecha_fin_preferencial"));
                anio.setFechaInicioGeneral(rs.getDate("fecha_inicio_general"));
                anio.setFechaFinGeneral(rs.getDate("fecha_fin_general"));
                anio.setEstadoAnio(rs.getString("estado_ano"));
            }
        } catch (SQLException e) {
        	LOG.error("Error al obtener el año escolar activo", e);
        }
        return anio;
    }
}