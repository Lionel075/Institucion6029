package com.institucion6029.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import com.institucion6029.model.Usuario;

@WebFilter(urlPatterns = {"/dashboard/*", "/matricula/*", "/asistencia/*", "/direccion/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        // 1. REGLA DE EXCLUSIÓN TOTAL: Si la URL es el login o recursos públicos, déjalo pasar siempre
        if (uri.endsWith("/login") || uri.endsWith("/login.jsp") || uri.contains("/css/") || uri.contains("/js/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Verificar autenticación básica
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        if (loggedIn) {
            Usuario user = (Usuario) session.getAttribute("usuario");
            
            if (uri.endsWith("/dashboard")) {
                chain.doFilter(request, response);
                return;
            }

            // 3. REGLA ESTRICTA PARA DIRECCIÓN (Solo Rol 3 puede pasar)
            if (esRutaOSubruta(uri, contextPath, "/direccion")) {
                if (user.getIdRol() == 3) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(contextPath + "/dashboard?error=NoAutorizadoDireccion");
                }
                return;
            }

            // 4. REGLA ESTRICTA PARA ASISTENCIA (Solo Rol 2 puede pasar)
            if (esRutaOSubruta(uri, contextPath, "/asistencia")) {
                if (user.getIdRol() == 2) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(contextPath + "/dashboard?error=NoAutorizadoDocente");
                }
                return;
            }

            // 5. REGLA ESTRICTA PARA MATRÍCULA (Solo Rol 1 puede pasar)
            if (esRutaOSubruta(uri, contextPath, "/matricula")) {
                if (user.getIdRol() == 1) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(contextPath + "/dashboard?error=NoAutorizadoPadre");
                }
                return;
            }
            
            // Cualquier otra ruta autenticada permitida
            chain.doFilter(request, response);
            return;

        } else {
            // 5. Redirección limpia al login si la sesión expiró o no existe
            res.sendRedirect(contextPath + "/login.jsp?error=SesionExpirada");
            return;
        }
    }

    /**
     * Verifica que 'uri' sea exactamente contextPath+base o un subrecurso de esa ruta
     * (contextPath+base+"/..."). Se usa en vez de String.contains() porque nombres de
     * rutas que comparten prefijo (ej. "/matricula" y "/direccion/matriculas") generaban
     * falsos positivos y bloqueaban por error a Dirección.
     */
    private boolean esRutaOSubruta(String uri, String contextPath, String base) {
        String rutaBase = contextPath + base;
        return uri.equals(rutaBase) || uri.startsWith(rutaBase + "/");
    }

}