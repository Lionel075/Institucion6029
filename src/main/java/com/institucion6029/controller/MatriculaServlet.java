package com.institucion6029.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import com.institucion6029.factory.DAOFactory;
import com.institucion6029.model.Usuario;
import com.institucion6029.model.Seccion;
import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.model.Alumno;
import com.institucion6029.exception.ReservaDuplicadaException;
import com.institucion6029.exception.PeriodoMatriculaCerradoException;
import com.institucion6029.exception.ErrorTransaccionException;
import com.institucion6029.utility.ConfiguracionAcademica;
import com.institucion6029.utility.GradosAcademicos;

@WebServlet("/matricula")
public class MatriculaServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario user = (Usuario) session.getAttribute("usuario");
            
            if (user.getIdRol() == 1) { 
                
                // Transferir el ID del alumno que viene desde el dashboard al formulario de reserva
                String idAlumno = request.getParameter("idAlumno");
                if (idAlumno != null) {
                    request.setAttribute("idAlumnoSeleccionado", idAlumno);
                }
                
                request.getRequestDispatcher("/WEB-INF/views/reserva.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=NoAutorizado");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario user = (Usuario) session.getAttribute("usuario");

        String accion = request.getParameter("accion");

        if ("reservar".equals(accion)) {

            // 1. Captura y validación de parámetros: nulos o vacíos no evalúan aforo
            String idAlumnoParam = request.getParameter("idAlumno");
            String grado = request.getParameter("grado");

            if (idAlumnoParam == null || idAlumnoParam.trim().isEmpty()
                    || grado == null || grado.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }
            
            // 1-B. NUEVO: rechaza cualquier "grado" que no esté en la lista blanca,
            // ANTES de resolver el año activo o tocar la BD. Evita que un valor
            // manipulado en el <select> del formulario caiga como "GradoSinVacantes"
            // (mensaje engañoso: el problema no es aforo, es un grado que no existe).
            if (!GradosAcademicos.esValido(grado)) {
                System.err.println("[MatriculaServlet] Grado inválido recibido: '" + grado
                        + "' — usuario: " + user.getIdUsuario());
                session.setAttribute("flashError", "GradoInvalido");
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            // 2. Año operativo activo. Resuelto ANTES del
            // try genérico y con su propio catch, para no confundir "sin año activo"
            // con "ErrorInterno".
            final int anioOperativo;
            try {
                anioOperativo = ConfiguracionAcademica.obtenerAnioOperativoActivo();
            } catch (IllegalStateException e) {
                System.err.println("[MatriculaServlet] " + e.getMessage());
                session.setAttribute("flashError", "PeriodoMatriculaCerrado");
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            try {
                int idAlumno = Integer.parseInt(idAlumnoParam.trim());

                // 3. Verificación de pertenencia (evita IDOR): el alumno debe ser hijo
                // del apoderado autenticado en sesión, no de otro cualquiera.
                Alumno alumno = DAOFactory.getAlumnoDAO().buscarPorId(idAlumno);

                if (alumno == null || !alumno.getIdPadre().equals(user.getIdUsuario())) {
                    System.err.println("[MatriculaServlet] Intento de matrícula no autorizado. "
                            + "Usuario: " + user.getIdUsuario() + " intentó reservar idAlumno=" + idAlumno);
                    session.setAttribute("flashError", "NoAutorizadoAlumno");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                }
                
                if ("Retirado".equals(alumno.getEstadoAcademico())) {
                    System.err.println("[MatriculaServlet] Intento de matrícula sobre alumno Retirado. "
                            + "idAlumno=" + idAlumno);
                    session.setAttribute("flashError", "AlumnoRetirado");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                }
                
                // 4. Arma el objeto de reserva (sin id_seccion aún: lo asigna la transacción)
                ReservaMatricula reserva = new ReservaMatricula();
                reserva.setIdAlumno(idAlumno);
                reserva.setIdAnio(anioOperativo);
                reserva.setEstadoReserva("Pendiente");

                // 5. Búsqueda de sección + inserción de reserva + descuento de vacante,
                // todo en UNA sola transacción con bloqueo pesimista (FOR UPDATE).
                Seccion seccionAsignada;
                try {
                    seccionAsignada = DAOFactory.getMatriculaDAO()
                            .registrarReservaConControlDeCupo(reserva, grado);
                } catch (PeriodoMatriculaCerradoException e) {
                    System.err.println("[MatriculaServlet] " + e.getMessage());
                    session.setAttribute("flashError", "PeriodoMatriculaCerrado");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                } catch (ReservaDuplicadaException e) {
                    System.err.println("[MatriculaServlet] " + e.getMessage());
                    session.setAttribute("flashError", "AlumnoYaMatriculado");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                } catch (ErrorTransaccionException e) {
                    System.err.println("[MatriculaServlet] " + e.getMessage());
                    session.setAttribute("flashError", "ErrorTransaccion");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                }

                if (seccionAsignada != null) {
                    session.setAttribute("flashMsg", "ReservaExitosa");
                } else {
                    session.setAttribute("flashError", "GradoSinVacantes");
                }

                response.sendRedirect(request.getContextPath() + "/dashboard");

            } catch (NumberFormatException e) {
                System.err.println("[MatriculaServlet] idAlumno inválido: " + e.getMessage());
                session.setAttribute("flashError", "ErrorInterno");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } catch (Exception e) {
                System.err.println("[MatriculaServlet] Error en proceso de reserva: " + e.getMessage());
                session.setAttribute("flashError", "ErrorInterno");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }

        } else {
            // 6. NUEVO: rama por defecto. Antes, cualquier "accion" distinto de "reservar"
            // (nulo, vacío, o un valor manipulado en el campo hidden del formulario)
            // dejaba el método sin sendRedirect ni forward -> respuesta vacía silenciosa.
            System.err.println("[MatriculaServlet] Acción no reconocida en /matricula: '" + accion
                    + "' — usuario: " + user.getIdUsuario());
            session.setAttribute("flashError", "ErrorInterno");
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}
