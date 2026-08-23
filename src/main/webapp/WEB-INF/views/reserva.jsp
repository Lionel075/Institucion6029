<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reserva de Vacante 2027 | IE 6029</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 20px; }
        .card { max-width: 600px; margin: 40px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
        h1 { color: #1e3a8a; font-size: 1.6em; margin-bottom: 10px; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; font-weight: bold; margin-bottom: 8px; color: #334155; }
        input[type="text"], select { width: 100%; padding: 10px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; background-color: #f8fafc; }
        input[readonly] { background-color: #e2e8f0; color: #475569; cursor: not-allowed; }
        .info-box { background-color: #eff6ff; border-left: 4px solid #3b82f6; padding: 15px; margin-bottom: 25px; border-radius: 4px; }
        .info-box p { margin: 0; font-size: 0.9em; color: #1e40af; line-height: 1.5; }
        .btn-container { display: flex; justify-content: space-between; align-items: center; margin-top: 25px; }
        .btn-submit { background-color: #10b981; color: white; padding: 12px 24px; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 1em; }
        .btn-submit:hover { background-color: #059669; }
        .btn-cancel { color: #64748b; text-decoration: none; font-weight: bold; font-size: 0.95em; }
        .btn-cancel:hover { color: #334155; }
    </style>
</head>
<body>

<div class="card">
    <h1>Formulario de Reserva de Vacante</h1>
    
    <div class="info-box">
        <p><strong>Políticas Corporativas de Matrícula (Periodo 2027):</strong></p>
        <p>• El costo institucional de la matrícula y pensión está fijado en S/. 0.00 por defecto.</p>
        <p>• Una vez enviada la solicitud, el sistema validará el aforo (máximo 32 alumnos por sección).</p>
        <p>• La vacante pasará a un estado <strong>PENDIENTE</strong>, otorgándole una ventana de atención de 48 horas
             para realizar el pago. Caso no se culmine el pago se procederá a <strong>LIBERAR</strong> la vacante</p>
        <p>• El grado seleccionado debe corresponder a la edad del estudiante cumplida al 31 de marzo:
             1° (6-8 años), 2° (7-9 años), 3° (8-10 años), 4° (9-11 años), 5° (10-12 años), 6° (11-13 años).</p>
    </div>

    <!-- El formulario apunta al servlet /matricula mediante método POST -->
    <form action="${pageContext.request.contextPath}/matricula" method="POST">

        <!-- Token anti-CSRF: emitido por CsrfFilter en el GET, validado en el POST -->
        <input type="hidden" name="csrfToken" value="${csrfToken}">

        <!-- Parámetro de acción operativa requerido por el switch-case del Servlet -->
        <input type="hidden" name="accion" value="reservar">

        <div class="form-group">
            <label for="idAlumno">Código del Estudiante Seleccionado:</label>
            <!-- Recupera el ID transferido dinámicamente desde el controlador doGet -->
            <input type="text" id="idAlumno" name="idAlumno" value="${idAlumnoSeleccionado}" readonly required>
                <c:if test="${not empty nombreAlumnoSeleccionado}">
                    <small style="display: block; margin-top: 6px; color: #64748b;">
                    ${nombreAlumnoSeleccionado}
                    </small>
                </c:if>
        </div>

        <div class="form-group">
            <label for="grado">Seleccione el Grado Académico para el 2027:</label>
            <select id="grado" name="grado" required>
                <option value="" disabled selected>-- Seleccione una opción --</option>
                <option value="1° Primaria">1° Grado de Primaria (Turno Mañana)</option>
                <option value="2° Primaria">2° Grado de Primaria (Turno Mañana)</option>
                <option value="3° Primaria">3° Grado de Primaria (Turno Mañana)</option>
                <option value="4° Primaria">4° Grado de Primaria (Turno Tarde)</option>
                <option value="5° Primaria">5° Grado de Primaria (Turno Tarde)</option>
                <option value="6° Primaria">6° Grado de Primaria (Turno Tarde)</option>
            </select>
        </div>

        <div class="btn-container">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn-cancel">Volver al Panel</a>
            <button type="submit" class="btn-submit">Confirmar Reserva 2027</button>
        </div>
        
    </form>
</div>

</body>
</html>
