<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Intranet | Institución Educativa 6029</title>

    <!-- Bootstrap 5 local -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">

    <style>
        :root {
            --azul-corporativo: #0d4a8f;
            --azul-corporativo-hover: #0a3b73;
        }
        body {
            background-color: #f4f6f9;
        }
        .btn-corporativo {
            background-color: var(--azul-corporativo);
            border-color: var(--azul-corporativo);
            color: #fff;
        }
        .btn-corporativo:hover,
        .btn-corporativo:focus {
            background-color: var(--azul-corporativo-hover);
            border-color: var(--azul-corporativo-hover);
            color: #fff;
        }
        .card-hijo {
            transition: transform .15s ease, box-shadow .15s ease;
            border: none;
            border-top: 4px solid var(--azul-corporativo);
        }
        .card-hijo:hover {
            transform: translateY(-4px);
            box-shadow: 0 .75rem 1.5rem rgba(13, 74, 143, 0.12);
        }
        .avatar-circle {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            background-color: #e7f0fa;
            color: var(--azul-corporativo);
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 1.1rem;
        }
        .codigo-badge {
            font-size: .75rem;
            letter-spacing: .03em;
        }
    </style>
</head>
<body>

    <!-- Contenedor Principal Adaptable de Bootstrap -->
    <div class="container my-5">

        <!-- Cabecera de Usuario / Tarjeta Superior -->
        <div class="d-flex justify-content-between align-items-center p-3 mb-4 bg-white rounded shadow-sm">
            <span class="text-muted">
                Bienvenido Apoderado: 
                <strong class="text-primary">
                ${not empty sessionScope.usuario.nombreCompleto ? sessionScope.usuario.nombreCompleto : sessionScope.usuario.idUsuario}
                </strong>
            </span>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
        </div>

        <!-- Bloque del Título -->
        <div class="mb-4 d-flex justify-content-between align-items-center">
            <div>
                <h1 class="h3 text-dark fw-bold">Panel de Matrícula Operativo 2027</h1>
                <p class="text-secondary small mb-0">Costo Institucional Regular: S/. 0.00 (Por defecto)</p>
            </div>
            <a href="${pageContext.request.contextPath}/matricula/mis-reservas" class="btn btn-outline-primary btn-sm">
                Ver Mis Reservas
            </a>
        </div>

        <!-- Alertas Dinámicas del Sistema (Flash Attributes vía HttpSession) -->
        
        <c:if test="${flashError eq 'AlumnoRetirado'}">
            <div class="alert alert-dark alert-dismissible fade show" role="alert">
                <strong>No disponible.</strong> El estudiante seleccionado tiene estado "Retirado" y no puede generar una nueva reserva de matrícula.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${flashError eq 'PeriodoMatriculaCerrado'}">
            <div class="alert alert-secondary alert-dismissible fade show" role="alert">
                <strong>Periodo cerrado.</strong> El periodo de matrícula 2027 no está actualmente activo o está fuera de las fechas establecidas.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${flashError eq 'AlumnoYaMatriculado'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <strong>Ya matriculado.</strong> Este estudiante ya cuenta con una reserva de vacante vigente para el periodo 2027.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${flashMsg eq 'ReservaExitosa'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>¡Éxito!</strong> La reserva de vacante se generó correctamente. Dispone de un plazo de 48 horas.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${flashError eq 'NoAutorizadoAlumno'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Acceso denegado.</strong> El estudiante seleccionado no está asociado a su cuenta.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${flashError eq 'GradoInvalido'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Grado inválido.</strong> El grado académico seleccionado no es válido. Intente nuevamente desde el formulario.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <c:if test="${flashError eq 'EdadNoCorrespondeGrado'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Edad no corresponde al grado.</strong> La edad del estudiante no está dentro del rango permitido
                para el grado académico seleccionado. Verifique la fecha de nacimiento registrada o elija otro grado.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${flashError eq 'GradoSinVacantes'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Sin vacantes.</strong> Las secciones A, B y C del grado seleccionado ya alcanzaron el aforo máximo (32 alumnos).
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <c:if test="${flashError eq 'ErrorInterno'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Error interno.</strong> Ocurrió un problema al procesar su solicitud. Intente nuevamente o contacte a soporte.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <c:if test="${flashError eq 'ErrorTransaccion'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Error de transacción.</strong> No se pudo registrar la reserva en la base de datos. Intente nuevamente.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <!-- Bloque de Integración del AlumnoDAO (Lista de Hijos) -->
        <div class="mb-3 d-flex align-items-center justify-content-between">
            <h2 class="h4 text-dark mb-0">
                <i class="text-primary">&#9679;</i> Mis Hijos Registrados
            </h2>
        </div>

        <!-- Grid de tarjetas de hijos -->
        <div class="row g-4">
            <!-- Iteración dinámica JSTL -->
            <c:forEach var="hijo" items="${listaHijos}">
                <div class="col-12 col-md-6 col-lg-4">
                    <div class="card card-hijo h-100 shadow-sm">
                        <div class="card-body d-flex flex-column">
                            <div class="d-flex align-items-center mb-3">
                                <div class="avatar-circle me-3">
                                    <c:out value="${hijo.nombres}" />
                                </div>
                                <div>
                                    <span class="badge bg-light text-secondary border codigo-badge">
                                        ALU-${hijo.idAlumno}
                                    </span>
                                </div>
                            </div>

                            <h5 class="card-title fw-semibold text-dark mb-1">
                                <c:out value="${hijo.apellidos}" />, <c:out value="${hijo.nombres}" />
                            </h5>

                            <p class="text-muted small mb-4">
                                <strong>Fecha de nacimiento:</strong><br>
                                ${hijo.fechaNacimiento}
                            </p>

                            <a href="matricula?idAlumno=${hijo.idAlumno}"
                               class="btn btn-corporativo mt-auto shadow-sm">
                                Reservar Vacante 2027
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <!-- Bloque por si la lista está vacía -->
            <c:if test="${empty listaHijos}">
                <div class="col-12">
                    <div class="card border-0 shadow-sm">
                        <div class="card-body text-center py-5 text-muted small">
                            No se encontraron estudiantes asociados a su código de apoderado.
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

    </div>

    <!-- Script de Bootstrap local (ajusta la ruta si tu bundle JS está en otro directorio) -->
    <script>
    // Limpia ?msg= o ?error= de la barra de direcciones una vez mostrada la alerta,
    // para que un F5 o volver a esta pestaña desde el historial no la repita.
    if (window.location.search) {
        const urlLimpia = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, urlLimpia);
    }
</script>
</body>
</html>
