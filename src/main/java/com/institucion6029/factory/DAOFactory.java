package com.institucion6029.factory;

import com.institucion6029.dao.*;
import com.institucion6029.dao.impl.*;

public class DAOFactory {

    // Instancias únicas de los DAOs (Patrón Singleton interno para optimizar memoria)
    private static UsuarioDAO usuarioDAO;
    private static DocenteDAO docenteDAO;
    private static PadreApoderadoDAO padreApoderadoDAO;
    private static AlumnoDAO alumnoDAO;
    private static SeccionDAO seccionDAO;
    private static HorarioDAO horarioDAO;
    private static MatriculaDAO matriculaDAO;
    private static AsistenciaDAO asistenciaDAO;
    private static RendimientoDAO rendimientoDAO;
    private static LandingPageDAO landingPageDAO;

    // Constructor privado para evitar que la fábrica sea instanciada con 'new'
    private DAOFactory() {
    }

    public static synchronized UsuarioDAO getUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAOImpl();
        }
        return usuarioDAO;
    }

    public static synchronized DocenteDAO getDocenteDAO() {
        if (docenteDAO == null) {
            docenteDAO = new DocenteDAOImpl();
        }
        return docenteDAO;
    }

    public static synchronized PadreApoderadoDAO getPadreApoderadoDAO() {
        if (padreApoderadoDAO == null) {
            padreApoderadoDAO = new PadreApoderadoDAOImpl();
        }
        return padreApoderadoDAO;
    }

    public static synchronized AlumnoDAO getAlumnoDAO() {
        if (alumnoDAO == null) {
            alumnoDAO = new AlumnoDAOImpl();
        }
        return alumnoDAO;
    }

    public static synchronized SeccionDAO getSeccionDAO() {
        if (seccionDAO == null) {
            seccionDAO = new SeccionDAOImpl();
        }
        return seccionDAO;
    }

    public static synchronized HorarioDAO getHorarioDAO() {
        if (horarioDAO == null) {
            horarioDAO = new HorarioDAOImpl();
        }
        return horarioDAO;
    }

    public static synchronized MatriculaDAO getMatriculaDAO() {
        if (matriculaDAO == null) {
            matriculaDAO = new MatriculaDAOImpl();
        }
        return matriculaDAO;
    }

    public static synchronized AsistenciaDAO getAsistenciaDAO() {
        if (asistenciaDAO == null) {
            asistenciaDAO = new AsistenciaDAOImpl();
        }
        return asistenciaDAO;
    }

    public static synchronized RendimientoDAO getRendimientoDAO() {
        if (rendimientoDAO == null) {
            rendimientoDAO = new RendimientoDAOImpl();
        }
        return rendimientoDAO;
    }

    public static synchronized LandingPageDAO getLandingPageDAO() {
        if (landingPageDAO == null) {
            landingPageDAO = new LandingPageDAOImpl();
        }
        return landingPageDAO;
    }
}

