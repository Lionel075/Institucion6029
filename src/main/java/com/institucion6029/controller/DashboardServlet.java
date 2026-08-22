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
import com.institucion6029.model.AnioEscolar;
import com.institucion6029.model.Seccion;
import com.institucion6029.utility.ConfiguracionAcademica;
import com.institucion6029.utility.GradosAcademicos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
	private static final Logger LOG = LoggerFactory.getLogger(DashboardServlet.class);
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }   
            
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

        Usuario user = (Usuario) session.getAttribute("usuario");
        
        if (user.getIdRol() == 1) { // 1 = PADRE / APODERADO
            String idPadre = user.getIdUsuario();

            List<Alumno> hijos = DAOFactory.getAlumnoDAO().listarHijosPorPadre(idPadre);
            request.setAttribute("listaHijos", hijos);

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
            
        } else if (user.getIdRol() == 2) { // 2 = DOCENTE
        	response.sendRedirect(request.getContextPath() + "/asistencia/docente");
            return;
            
        } else if (user.getIdRol() == 3) { // 3 = DIRECTOR / DESARROLLADOR

            AnioEscolar anioEscolar = null;
            try {
                anioEscolar = ConfiguracionAcademica.obtenerAnioEscolarActivo();
            } catch (IllegalStateException e) {
                LOG.warn("[DashboardServlet] Sin año activo al cargar el panel de Dirección: {}", e.getMessage());
                request.setAttribute("sinAnioActivo", true);
            }

            if (anioEscolar != null) {
                int idAnio = anioEscolar.getIdAnio();

                Map<String, Integer> conteosReservas = DAOFactory.getMatriculaDAO().contarReservasPorEstado(idAnio);
                List<Seccion> secciones = DAOFactory.getSeccionDAO().listarTodasLasSecciones(idAnio);

                request.setAttribute("anioEscolar", anioEscolar);
                request.setAttribute("conteosReservas", conteosReservas);
                request.setAttribute("listaSecciones", secciones);
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

        	request.getRequestDispatcher("/WEB-INF/views/dashboard_admin.jsp").forward(request, response);
            return;
        } else {
        	LOG.warn("idRol no reconocido al cargar el dashboard: {} — usuario: {}",
                    user.getIdRol(), user.getIdUsuario());
            session.setAttribute("flashError", "RolNoReconocido");
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=NoAutorizado");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}