<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Intranet Login | IE 6029</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #0f172a; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh; }
        .login-card { background: white; padding: 40px; border-radius: 8px; box-shadow: 0 10px 15px -3px rgba(0,0,0,0.3); width: 100%; max-width: 400px; }
        h2 { color: #1e3a8a; text-align: center; margin-bottom: 5px; }
        .subtitle { text-align: center; color: #64748b; font-size: 0.9em; margin-bottom: 25px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; font-weight: bold; margin-bottom: 8px; color: #334155; }
        input[type="text"], input[type="password"] { width: 100%; padding: 12px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; font-size: 1em; }
        .btn-login { width: 100%; background-color: #1e40af; color: white; padding: 12px; border: none; border-radius: 4px; font-weight: bold; font-size: 1em; cursor: pointer; margin-top: 10px; }
        .btn-login:hover { background-color: #1d4ed8; }
        .alert { background-color: #f8d7da; color: #842029; padding: 12px; border-radius: 4px; font-size: 0.85em; font-weight: bold; margin-bottom: 20px; text-align: center; }
    </style>
</head>
<body>

<div class="login-card">
    <h2>Institución Educativa 6029</h2>
    <div class="subtitle">Intranet Operativa - Año Escolar 2027</div>

    <!-- Gestión de Mensajes de Error mediante Expresiones de Lenguaje (EL) -->
    <c:if test="${param.error eq 'CredencialesIncorrectas'}">
        <div class="alert">El usuario o la contraseña ingresados son incorrectos.</div>
    </c:if>
    <c:if test="${param.error eq 'SesionExpirada'}">
        <div class="alert">Su sesión ha expirado o no tiene autorización para ver este recurso.</div>
    </c:if>
    <c:if test="${param.error eq 'NoAutorizado'}">
        <div class="alert">Acceso denegado. No cuenta con el rol requerido.</div>
    </c:if>

    <form action="login" method="POST">
        <!-- Token anti-CSRF: emitido por CsrfFilter en el GET, validado en el POST -->
        <input type="hidden" name="csrfToken" value="${csrfToken}">

        <div class="form-group">
            <label for="txtUsuario">Código de Usuario o Correo:</label>
            <input type="text" id="txtUsuario" name="txtUsuario" placeholder="Ej: PAD-00101 o docente@6029.edu.pe" required autocomplete="off">
        </div>

        <div class="form-group">
            <label for="txtClave">Contraseña Institucional:</label>
            <input type="password" id="txtClave" name="txtClave" placeholder="••••••••" required>
        </div>

        <button type="submit" class="btn-login">Ingresar Al Sistema</button>
    </form>
</div>

</body>
</html>

