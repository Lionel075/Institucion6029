package com.institucion6029.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.institucion6029.model.InstitucionWeb;
import com.institucion6029.utility.Conexion;
import com.institucion6029.scheduler.ExpiracionReservasScheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class AppInitializeListener implements ServletContextListener {

	private static final Logger LOG = LoggerFactory.getLogger(AppInitializeListener.class);
	
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	LOG.info("Iniciando el servidor web e instalando configuraciones globales...");
        
        ServletContext servletContext = sce.getServletContext();
        InstitucionWeb datosColegio = cargarDatosInstitucion();

        if (datosColegio != null) {
            servletContext.setAttribute("DATOS_COLEGIO", datosColegio);
            LOG.info("Datos de la institución cargados en caché web con éxito.");
        } else {
        	LOG.error("CRÍTICO: No se pudieron precargar los datos del colegio.");
        }
        ExpiracionReservasScheduler.iniciar();
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    	LOG.info("Apagando el servidor web y liberando recursos globales.");
    	
    	ExpiracionReservasScheduler.detener();
    }
    
    private InstitucionWeb cargarDatosInstitucion() {
        InstitucionWeb institucion = null;
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
        	LOG.error("Error SQL al precargar la institución", e);
        } catch (Exception e) {
        	LOG.error("Error general en Listener", e);
        }
        return institucion;
    }
}
