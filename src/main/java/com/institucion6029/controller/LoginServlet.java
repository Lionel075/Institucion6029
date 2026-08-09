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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    /**
     * Si intentan entrar por GET al login, limpiamos sesión anterior y mostramos la vista
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destrucción segura ante cierres o reinicios
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Procesa la autenticación corporativa de Directores, Docentes y Padres
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String txtUsuario = request.getParameter("txtUsuario"); // Puede ser correo o ID alfanumérico
        String txtClave = request.getParameter("txtClave");
        
        try {
            // 1. Invocar a tu interfaz e implementación a través de la fábrica estructurada
            Usuario usuarioAutenticado = DAOFactory.getUsuarioDAO().validarAcceso(txtUsuario, txtClave);
            
            if (usuarioAutenticado != null) {
                // 2. Crear (o recuperar) la sesión antes de autenticar
                HttpSession session = request.getSession(true);
                
                // 2-B. NUEVO: regenera el ID de sesión tras un login exitoso.
                // Previene fijación de sesión: si el ID ya existía antes del login
                // (por ejemplo, fijado por un atacante), este nuevo ID lo invalida.
                // request.changeSessionId() conserva los atributos ya seteados en
                // la sesión (ninguno en este punto, ya que se llama antes de
                // setAttribute("usuario", ...) más abajo).
                request.changeSessionId();
                
                session.setAttribute("usuario", usuarioAutenticado);
                
                System.out.println("[6029-Login] Sesión iniciada con éxito para el ID: " + usuarioAutenticado.getIdUsuario());
                
                // 3. Redirección centralizada al Dashboard general (este ya distribuye internamente por ID de rol)
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            } else {
                // Credenciales inválidas
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=CredencialesIncorrectas");
                return;
            }
            
        } catch (Exception e) {
            System.err.println("[LoginServlet] Error crítico en el proceso de login: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=ErrorInterno");
        }
    }
}

