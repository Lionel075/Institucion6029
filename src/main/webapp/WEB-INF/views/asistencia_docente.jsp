<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Control de Asistencia | IE 6029</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 20px; }
        .container { max-width: 900px; margin: auto; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
        h1 { color: #1e3a8a; margin-bottom: 5px; }
        .subtitle { color: #64748b; font-size: 0.95em; margin-bottom: 20px; }
        .info-grid { display: flex; gap: 20px; background: #f8fafc; padding: 15px; border-radius: 6px; margin-bottom: 25px; border: 1px solid #e2e8f0; }
        .info-item { flex: 1; font-size: 0.9em; color: #334155; }
        .info-item strong { color: #1e3a8a; }
        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { border: 1px solid #cbd5e1; padding: 12px; text-align: left; }
        th { background-color: #f1f5f9; color: #1e293b; }
        tr:hover { background-color: #f8fafc; }
        .radio-group { display: flex; gap: 15px; }
        .radio-option { display: flex; align-items: center; gap: 5px; cursor: pointer; font-weight: bold; }
        .text-presente { color: #16a34a; }
        .text-falta { color: #dc2626; }
        .btn-submit { background-color: #2563eb; color: white; padding: 12px 25px; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 1em; float: right; margin-top: 20px; }
        .btn-submit:hover { background-color: #1d4ed8; }
        .alert { padding: 12px; margin-bottom: 20px; border-radius: 4px; font-weight: bold; text-align: center; }
        .alert-success { background-color: #d1e7dd; color: #0f5132; }
    </style>
</head>
<body>

<div class="container">
    <h1>Registro de Asistencia Diaria</h1>
    <div class="subtitle">Plana Docente Corporativa | Año Operativo 2027</div>

    <c:if test="${param.msg eq 'AsistenciaExitosa'}">
        <div class="alert alert-success">¡Lote de asistencia guardado y sincronizado con éxito en la base de datos!</div>
    </c:if>

    <div class="info-grid">
        <div class="info-item"><strong>Tutor:</strong> ${docente.apellidos}, ${docente.nombres}</div>
        <div class="info-item"><strong>Grado y Sección:</strong> ${seccion.grado} "${seccion.seccion}"</div>
        <div class="info-item"><strong>Turno Académico:</strong> ${turnoAcademico}</div>
    </div>

    <!-- Formulario que envía el lote al AsistenciaServlet -->
    <form action="${pageContext.request.contextPath}/docente/asistencia" method="POST">
        
        <!-- Parámetros ocultos necesarios para la persistencia transaccional -->
        <input type="hidden" name="idSeccion" value="${seccion.idSeccion}">
        <input type="hidden" name="idCurso" value="1"> <!-- Curso base de tutoría -->

        <table>
            <thead>
                <tr>
                    <th style="width: 15%;">Código Alumno</th>
                    <th style="width: 50%;">Apellidos y Nombres</th>
                    <th style="width: 35%;">Control de Estado (Inicial)</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="alumno" items="${listaAlumnos}">
                    <tr>
                        <td>
                            <!-- Input oculto para enviar la colección de IDs al String[] arrIdAlumnos -->
                            <input type="hidden" name="idAlumno" value="${alumno.idAlumno}">
                            ALU-${alumno.idAlumno}
                        </td>
                        <td><c:out value="${alumno.apellidos}, ${alumno.nombres}" /></td>
                        <td>
                            <div class="radio-group">
                                <!-- El name se concatena dinámicamente: estado_ID para ser leído individualmente -->
                                <label class="radio-option text-presente">
                                    <input type="radio" name="estado_${alumno.idAlumno}" value="Presente" checked required> Presente
                                </label>
                                <label class="radio-option text-falta">
                                    <input type="radio" name="estado_${alumno.idAlumno}" value="Falta" required> Falta
                                </label>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <button type="submit" class="btn-submit">Guardar Lista de Hoy</button>
    </form>
</div>

</body>
</html>
