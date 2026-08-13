package com.institucion6029.factory;

import com.institucion6029.dao.*;
import com.institucion6029.dao.impl.*;

public class DAOFactory {

    private static UsuarioDAO usuarioDAO;
    private static AlumnoDAO alumnoDAO;
    private static SeccionDAO seccionDAO;
    private static MatriculaDAO matriculaDAO;

    // Constructor privado
    private DAOFactory() {
    }

    public static synchronized UsuarioDAO getUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAOImpl();
        }
        return usuarioDAO;
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

    public static synchronized MatriculaDAO getMatriculaDAO() {
        if (matriculaDAO == null) {
            matriculaDAO = new MatriculaDAOImpl();
        }
        return matriculaDAO;
    }
    
    private static AnioEscolarDAO anioEscolarDAO;

    public static synchronized AnioEscolarDAO getAnioEscolarDAO() {
        if (anioEscolarDAO == null) {
            anioEscolarDAO = new AnioEscolarDAOImpl();
        }
        return anioEscolarDAO;
    }
}

