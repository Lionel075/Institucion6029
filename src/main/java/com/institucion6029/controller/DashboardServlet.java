package com.institucion6029.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.institucion6029.factory.DAOFactory;
import com.institucion6029.model.Usuario;
import com.institucion6029.model.Alumno;
import com.institucion6029.utility.ConfiguracionAcademica;
import com.institucion6029.utility.GradosAcademicos;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }   
            
        //Evita que el navegador sirva una versión cacheada de esta respuesta
        //(por ejemplo, al volver con el botón "Atrás" tras un ?error=GradoSinVacantes)
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

        Usuario user = (Usuario) session.getAttribute("usuario");
        
        // El control de flujos corporativo sincronizado con la base de datos
        if (user.getIdRol() == 1) { // 1 = PADRE / APODERADO
            String idPadre = user.getIdUsuario();

            List<Alumno> hijos = DAOFactory.getAlumnoDAO().listarHijosPorPadre(idPadre);
            request.setAttribute("listaHijos", hijos);

            // Resolución centralizada del año operativo activo (antes: literal "2" hardcodeado).
            // Si no hay ningún año escolar en estado 'Activo', se degrada con vacantes=0 en vez
            // de romper el dashboard completo del padre.
            int anioOperativo;
            try {
                anioOperativo = ConfiguracionAcademica.obtenerAnioOperativoActivo();
            } catch (IllegalStateException e) {
                System.err.println("[DashboardServlet] " + e.getMessage());
                anioOperativo = -1;
            }

            Map<String, Integer> vacantesPorGrado = new HashMap<>();
            String[] grados = GradosAcademicos.TODOS;

            for (String g : grados) {
                int vacantes = (anioOperativo == -1) ? 0
                        : DAOFactory.getSeccionDAO().obtenerVacantesDisponiblesPorGrado(g, anioOperativo);
                vacantesPorGrado.put(g, vacantes);
            }
            request.setAttribute("vacantesGrados", vacantesPorGrado);

            // Si no hay año activo, el padre lo ve reflejado como "todo en 0" — opcional:
            // podrías añadir un flashError distinto aquí para mostrar un aviso explícito
            // en vez de dejar que lo infiera de vacantes=0 en todos los grados.
            if (anioOperativo == -1) {
                request.setAttribute("sinAnioActivo", true);
            }

            // --- Flash attributes ---
            if (session.getAttribute("flashMsg") != null) {
                request.setAttribute("flashMsg", session.getAttribute("flashMsg"));
                session.removeAttribute("flashMsg");
            }
            if (session.getAttribute("flashError") != null) {
                request.setAttribute("flashError", session.getAttribute("flashError"));
                session.removeAttribute("flashError");
            }

            request.getRequestDispatcher("/WEB-INF/views/dashboard_padre.jsp").forward(request, response);
            return;
            
        } else if (user.getIdRol() == 2) { // 2 = DOCENTE (Se mantiene igual)
        	response.sendRedirect(request.getContextPath() + "/asistencia/docente");
            return;
            
        } else if (user.getIdRol() == 3) { // NUEVO CONTENIDO: 3 = DIRECTOR / DESARROLLADOR
            // Redirección o vista para el director (aquí pones la ruta real de tu panel de control)
        	request.getRequestDispatcher("/WEB-INF/views/dashboard_admin.jsp").forward(request, response);
            return;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
