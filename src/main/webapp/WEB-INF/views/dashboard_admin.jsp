<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Dirección | Institución Educativa 6029</title>

    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">

    <style>
        :root {
            --azul-corporativo: #0d4a8f;
            --azul-corporativo-hover: #0a3b73;
        }
        body { background-color: #f4f6f9; }
        .btn-corporativo {
            background-color: var(--azul-corporativo);
            border-color: var(--azul-corporativo);
            color: #fff;
        }
        .btn-corporativo:hover, .btn-corporativo:focus {
            background-color: var(--azul-corporativo-hover);
            border-color: var(--azul-corporativo-hover);
            color: #fff;
        }
        .stat-card {
            border: none;
            border-top: 4px solid var(--azul-corporativo);
            transition: transform .15s ease, box-shadow .15s ease;
        }
        .stat-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 .75rem 1.5rem rgba(13, 74, 143, 0.12);
        }
        .stat-card .stat-numero { font-size: 2.1rem; font-weight: 700; color: var(--azul-corporativo); }
        .stat-card.stat-pendiente { border-top-color: #f59e0b; }
        .stat-card.stat-pendiente .stat-numero { color: #b45309; }
        .stat-card.stat-aprobada { border-top-color: #10b981; }
        .stat-card.stat-aprobada .stat-numero { color: #047857; }
        .stat-card.stat-expirada { border-top-color: #ef4444; }
        .stat-card.stat-expirada .stat-numero { color: #b91c1c; }
        .vacantes-bar-bg { background-color: #e2e8f0; border-radius: 4px; height: 8px; overflow: hidden; }
        .vacantes-bar-fill { background-color: var(--azul-corporativo); height: 100%; }
    </style>
</head>
<body>

    <div class="container my-5">

        <!-- Cabecera de Usuario -->
        <div class="d-flex justify-content-between align-items-center p-3 mb-4 bg-white rounded shadow-sm">
            <span class="text-muted">
                Bienvenido, Dirección:
                <strong class="text-primary">
                ${not empty sessionScope.usuario.nombreCompleto ? sessionScope.usuario.nombreCompleto : sessionScope.usuario.idUsuario}
                </strong>
            </span>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
        </div>

        <!-- Título -->
        <div class="mb-4">
            <h1 class="h3 text-dark fw-bold mb-1">Panel de Dirección</h1>
            <c:choose>
                <c:when test="${not empty anioEscolar}">
                    <p class="text-secondary small mb-0">
                        Año escolar operativo: <strong>${anioEscolar.anioCalendario}</strong>
                        — Estado: <strong>${anioEscolar.estadoAnio}</strong>
                        · Periodo preferencial: <fmt:formatDate value="${anioEscolar.fechaInicioPreferencial}" pattern="dd/MM/yyyy"/>
                        al <fmt:formatDate value="${anioEscolar.fechaFinPreferencial}" pattern="dd/MM/yyyy"/>
                        · Periodo general: <fmt:formatDate value="${anioEscolar.fechaInicioGeneral}" pattern="dd/MM/yyyy"/>
                        al <fmt:formatDate value="${anioEscolar.fechaFinGeneral}" pattern="dd/MM/yyyy"/>
                    </p>
                </c:when>
                <c:otherwise>
                    <p class="text-secondary small mb-0">No hay un año escolar activo configurado actualmente.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Alertas -->
        <c:if test="${sinAnioActivo}">
            <div class="alert alert-secondary alert-dismissible fade show" role="alert">
                <strong>Atención.</strong> No hay ningún año escolar con estado "Activo". No es posible revisar matrículas hasta que se configure uno.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${flashMsg eq 'ReservaAprobada'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>¡Listo!</strong> La reserva de matrícula fue aprobada.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${flashMsg eq 'ReservaRechazada'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>Listo.</strong> La reserva de matrícula fue rechazada y la vacante fue liberada.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- Tarjetas de resumen de matrículas -->
        <div class="mb-3">
            <h2 class="h5 text-dark mb-0">Estado del Filtro de Matrículas</h2>
        </div>
        <div class="row g-4 mb-4">
            <div class="col-12 col-sm-6 col-lg-3">
                <div class="card stat-card stat-pendiente shadow-sm h-100">
                    <div class="card-body">
                        <div class="stat-numero">${empty conteosReservas['Pendiente'] ? 0 : conteosReservas['Pendiente']}</div>
                        <div class="text-muted small">Pendientes por revisar</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-6 col-lg-3">
                <div class="card stat-card stat-aprobada shadow-sm h-100">
                    <div class="card-body">
                        <div class="stat-numero">${empty conteosReservas['Aprobada'] ? 0 : conteosReservas['Aprobada']}</div>
                        <div class="text-muted small">Aprobadas</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-6 col-lg-3">
                <div class="card stat-card stat-expirada shadow-sm h-100">
                    <div class="card-body">
                        <div class="stat-numero">${empty conteosReservas['Expirada'] ? 0 : conteosReservas['Expirada']}</div>
                        <div class="text-muted small">Expiradas</div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-6 col-lg-3">
                <div class="card stat-card shadow-sm h-100 d-flex align-items-center justify-content-center">
                    <div class="card-body text-center">
                        <a href="${pageContext.request.contextPath}/direccion/matriculas"
                           class="btn btn-corporativo w-100 shadow-sm">
                            Revisar Matrículas
                        </a>
                        <p class="text-muted small mt-2 mb-0">Aprobar o rechazar solicitudes</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabla de vacantes por sección -->
        <div class="mb-3 d-flex align-items-center justify-content-between">
            <h2 class="h5 text-dark mb-0">Vacantes por Grado y Sección</h2>
        </div>
        <div class="card border-0 shadow-sm mb-4">
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty listaSecciones}">
                        <p class="text-muted small text-center py-4 mb-0">No hay secciones registradas para el año escolar activo.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table align-middle">
                                <thead>
                                    <tr>
                                        <th>Grado</th>
                                        <th>Sección</th>
                                        <th>Docente Tutor</th>
                                        <th>Vacantes Disponibles</th>
                                        <th style="width: 25%;">Ocupación</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${listaSecciones}">
                                        <tr>
                                            <td><c:out value="${s.grado}" /></td>
                                            <td>"<c:out value="${s.seccion}" />"</td>
                                            <td><c:out value="${s.idDocenteTutor}" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${s.vacantesDisponibles == 0}">
                                                        <span class="badge bg-danger">Sin cupo (0)</span>
                                                    </c:when>
                                                    <c:when test="${s.vacantesDisponibles le 5}">
                                                        <span class="badge bg-warning text-dark">${s.vacantesDisponibles} restantes</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-success">${s.vacantesDisponibles} restantes</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="vacantes-bar-bg">
                                                    <div class="vacantes-bar-fill"
                                                         style="width: ${(32 - s.vacantesDisponibles) * 100 / 32}%;"></div>
                                                </div>
                                                <span class="text-muted small">${32 - s.vacantesDisponibles} / 32 matriculados</span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>

    <script>
    if (window.location.search) {
        const urlLimpia = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, urlLimpia);
    }
    </script>
</body>
</html>
