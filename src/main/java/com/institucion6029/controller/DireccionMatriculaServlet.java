package com.institucion6029.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.institucion6029.factory.DAOFactory;
import com.institucion6029.model.Usuario;
import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.exception.ErrorTransaccionException;
import com.institucion6029.utility.ConfiguracionAcademica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel de Dirección (idRol = 3) para revisar el filtro de matrículas: lista
 * las reservas generadas por los apoderados (Pendiente / Aprobada / Expirada)
 * y permite aprobar o rechazar las que están Pendientes.
 *
 * La autorización de acceso (solo idRol == 3) se aplica también en AuthFilter,
 * pero se revalida aquí por seguridad en profundidad.
 */
@WebServlet("/direccion/matriculas")
public class DireccionMatriculaServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionMatriculaServlet.class);
    private static final long serialVersionUID = 1L;

    private static final String[] ESTADOS_VALIDOS = { "Pendiente", "Aprobada", "Expirada", "Todas" };

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=SesionExpirada");
            return;
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        if (user.getIdRol() != 3) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=NoAutorizadoDireccion");
            return;
        }

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        int anioOperativo;
        try {
            anioOperativo = ConfiguracionAcademica.obtenerAnioOperativoActivo();
        } catch (IllegalStateException e) {
            LOG.warn("[DireccionMatriculaServlet] Sin año activo al listar reservas: {}", e.getMessage());
            session.setAttribute("flashError", "SinAnioActivo");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String estadoFiltro = request.getParameter("estado");
        if (estadoFiltro == null || estadoFiltro.isBlank() || !esEstadoValido(estadoFiltro)) {
            estadoFiltro = "Pendiente"; // Por defecto: lo que Dirección necesita revisar primero
        }

        List<ReservaMatricula> reservas = DAOFactory.getMatriculaDAO()
                .listarReservasPorEstado(anioOperativo, estadoFiltro);
        Map<String, Integer> conteos = DAOFactory.getMatriculaDAO().contarReservasPorEstado(anioOperativo);

        request.setAttribute("reservas", reservas);
        request.setAttribute("conteos", conteos);
        request.setAttribute("estadoFiltro", estadoFiltro);

        // --- Flash attributes ---
        if (session.getAttribute("flashMsg") != null) {
            request.setAttribute("flashMsg", session.getAttribute("flashMsg"));
            session.removeAttribute("flashMsg");
        }
        if (session.getAttribute("flashError") != null) {
            request.setAttribute("flashError", session.getAttribute("flashError"));
            session.removeAttribute("flashError");
        }

        request.getRequestDispatcher("/WEB-INF/views/direccion_matriculas.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=SesionExpirada");
            return;
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        if (user.getIdRol() != 3) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=NoAutorizadoDireccion");
            return;
        }

        String estadoFiltro = request.getParameter("estado");
        String redireccion = request.getContextPath() + "/direccion/matriculas"
                + (estadoFiltro != null && esEstadoValido(estadoFiltro) ? "?estado=" + estadoFiltro : "");

        String accion = request.getParameter("accion");
        String idReservaParam = request.getParameter("idReserva");

        if (idReservaParam == null || idReservaParam.isBlank()
                || !("aprobar".equals(accion) || "rechazar".equals(accion))) {
            LOG.warn("Solicitud inválida en /direccion/matriculas. accion={}, idReserva={} — usuario: {}",
                    accion, idReservaParam, user.getIdUsuario());
            session.setAttribute("flashError", "ErrorInterno");
            response.sendRedirect(redireccion);
            return;
        }

        try {
            int idReserva = Integer.parseInt(idReservaParam.trim());

            if ("aprobar".equals(accion)) {
                boolean aprobada = DAOFactory.getMatriculaDAO().aprobarReserva(idReserva, user.getIdUsuario());
                if (aprobada) {
                    session.setAttribute("flashMsg", "ReservaAprobada");
                } else {
                    session.setAttribute("flashError", "NoSePudoProcesar");
                }
            } else { // rechazar
                boolean rechazada = DAOFactory.getMatriculaDAO().rechazarReserva(idReserva, user.getIdUsuario());
                if (rechazada) {
                    session.setAttribute("flashMsg", "ReservaRechazada");
                } else {
                    session.setAttribute("flashError", "NoSePudoProcesar");
                }
            }

        } catch (NumberFormatException e) {
            LOG.warn("idReserva inválido recibido en /direccion/matriculas: {}", idReservaParam);
            session.setAttribute("flashError", "ErrorInterno");
        } catch (ErrorTransaccionException e) {
            LOG.error("Error de transacción al procesar reserva en Dirección", e);
            session.setAttribute("flashError", "ErrorTransaccion");
        } catch (Exception e) {
            LOG.error("Error inesperado al procesar reserva en Dirección", e);
            session.setAttribute("flashError", "ErrorInterno");
        }

        response.sendRedirect(redireccion);
    }

    private boolean esEstadoValido(String estado) {
        for (String e : ESTADOS_VALIDOS) {
            if (e.equalsIgnoreCase(estado)) {
                return true;
            }
        }
        return false;
    }
}

