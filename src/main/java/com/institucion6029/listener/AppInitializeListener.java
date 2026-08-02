package com.institucion6029.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// SE CAMBIÓ REQUISITO DE javax A jakarta PARA COMPATIBILIDAD CON TOMCAT 11
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.institucion6029.model.InstitucionWeb;
import com.institucion6029.utility.Conexion;
import com.institucion6029.factory.DAOFactory;

@WebListener
public class AppInitializeListener implements ServletContextListener {

    // Un solo hilo demonio: la tarea es breve y corre en background sin bloquear el shutdown de Tomcat.
    private ScheduledExecutorService expiradorReservas;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[6029-Listener] Iniciando el servidor web e instalando configuraciones globales...");
        
        ServletContext servletContext = sce.getServletContext();
        InstitucionWeb datosColegio = cargarDatosInstitucion();

        if (datosColegio != null) {
            // Guardamos el objeto en el ámbito global de la aplicación web (Tomcat Context Cache)
            servletContext.setAttribute("DATOS_COLEGIO", datosColegio);
            System.out.println("[6029-Listener] Datos de la institución cargados en caché web con éxito.");
        } else {
            System.err.println("[6029-Listener] CRÍTICO: No se pudieron precargar los datos del colegio.");
        }

        // Corrección [Fallo Crítico #3]: antes ninguna reserva 'Pendiente' liberaba
        // su vacante. Cada 15 min se expira lo que superó las 48h anunciadas en
        // reserva.jsp y se libera el cupo en sch_secciones (ver MatriculaDAOImpl.expirarReservasVencidas()).
        expiradorReservas = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "expirador-reservas-matricula");
            t.setDaemon(true);
            return t;
        });
        expiradorReservas.scheduleAtFixedRate(() -> {
            try {
                DAOFactory.getMatriculaDAO().expirarReservasVencidas();
                // Transición Preferencial → General: cancela ('Expirada') lo que
                // siga Pendiente 48h después del cierre del periodo preferencial.
                DAOFactory.getMatriculaDAO().expirarReservasPreferencialesVencidas();
            } catch (Exception e) {
                System.err.println("[6029-Listener] Error al ejecutar expirador de reservas: " + e.getMessage());
            }
        }, 1, 15, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[6029-Listener] Apagando el servidor web y liberando recursos globales.");
        if (expiradorReservas != null) {
            expiradorReservas.shutdown();
        }
    }

    /**
     * Consulta directa a la base de datos de manera limpia para precargar el Landing Page
     */
    private InstitucionWeb cargarDatosInstitucion() {
        InstitucionWeb institucion = null;
        String sql = "SELECT id_institucion, codigo_modular, nombre_institucion, descripcion, "
                   + "direccion, nivel_educativo, max_estudiantes_aula, pension_preferencial, "
                   + "modalidad FROM web_datos_institucion WHERE id_institucion = 1";

        // Usamos tu clase Conexion que ya arroja "Conexión exitosa"
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
            System.err.println("[6029-Listener] Error SQL al precargar la institución: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[6029-Listener] Error general en Listener: " + e.getMessage());
        }
        return institucion;
    }
}
