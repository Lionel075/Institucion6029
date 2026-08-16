package com.institucion6029.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.institucion6029.utility.CsrfUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(urlPatterns = {"/login", "/login.jsp", "/matricula/*"})
public class CsrfFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(CsrfFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            HttpSession session = req.getSession(false);
            String tokenRecibido = req.getParameter("csrfToken");

            if (!CsrfUtil.validar(session, tokenRecibido)) {
                LOG.warn("Token CSRF inválido o ausente en POST a '{}' — IP: {}",
                        req.getRequestURI(), req.getRemoteAddr());
                res.sendRedirect(req.getContextPath() + "/login.jsp?error=SesionExpirada");
                return;
            }
        } else {
            HttpSession session = req.getSession(true);
            String token = CsrfUtil.obtenerOGenerarToken(session);
            req.setAttribute("csrfToken", token);
        }

        chain.doFilter(request, response);
    }
}