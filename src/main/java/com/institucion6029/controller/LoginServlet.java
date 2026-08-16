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
import com.institucion6029.utility.CsrfUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
	private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            session.invalidate(); // Destrucción segura ante cierres o reinicios de sesión autenticada
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String txtUsuario = request.getParameter("txtUsuario"); // Puede ser correo o ID alfanumérico
        String txtClave = request.getParameter("txtClave");
        
        try {
            Usuario usuarioAutenticado = DAOFactory.getUsuarioDAO().validarAcceso(txtUsuario, txtClave);
            
            if (usuarioAutenticado != null) {
                HttpSession session = request.getSession(true);

                request.changeSessionId();

                CsrfUtil.regenerarToken(session);

                session.setAttribute("usuario", usuarioAutenticado);
                
                LOG.info("Sesión iniciada con éxito para el ID: {}", usuarioAutenticado.getIdUsuario());
                
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            } else {
                // Credenciales inválidas
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=CredencialesIncorrectas");
                return;
            }
            
        } catch (Exception e) {
        	LOG.error("Error crítico en el proceso de login", e);
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=ErrorInterno");
        }
    }
}