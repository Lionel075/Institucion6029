<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Revisión de Matrículas | Institución Educativa 6029</title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <style>
        body { background-color: #f4f6f9; }
        .filtro-pill {
            border: 1px solid #cbd5e1;
            border-radius: 999px;
            padding: 6px 16px;
            font-size: .85rem;
            font-weight: 600;
            color: #475569;
            text-decoration: none;
            display: inline-block;
        }
        .filtro-pill.activo {
            background-color: #0d4a8f;
            border-color: #0d4a8f;
            color: #fff;
        }
        .filtro-pill:hover { text-decoration: none; opacity: .85; }
    </style>
</head>
<body>
    <div class="container my-5">

        <div class="d-flex justify-content-between align-items-center p-3 mb-4 bg-white rounded shadow-sm">
            <span class="text-muted">
                Bienvenido, Dirección: <strong class="text-primary">
                    ${not empty sessionScope.usuario.nombreCompleto ? sessionScope.usuario.nombreCompleto : sessionScope.usuario.idUsuario}
                </strong>
            </span>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
        </div>

        <div class="mb-4 d-flex justify-content-between align-items-center flex-wrap gap-2">
            <h1 class="h3 text-dark fw-bold mb-0">Revisión del Filtro de Matrículas</h1>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary btn-sm">Volver al Panel</a>
        </div>

        <!-- Alertas -->
        <c:if test="${flashMsg eq 'ReservaAprobada'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>¡Listo!</strong> La reserva fue aprobada correctamente.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${flashMsg eq 'ReservaRechazada'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>Listo.</strong> La reserva fue rechazada y la vacante quedó liberada.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${flashError eq 'NoSePudoProcesar'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <strong>No se pudo procesar.</strong> La reserva ya no está en estado "Pendiente" (puede que otra persona ya la haya gestionado, o haya expirado).
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${flashError eq 'ErrorTransaccion' or flashError eq 'ErrorInterno'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Error.</strong> No se pudo procesar la solicitud. Intente nuevamente.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- Filtros por estado -->
        <div class="mb-4 d-flex flex-wrap gap-2">
            <a class="filtro-pill ${estadoFiltro eq 'Pendiente' ? 'activo' : ''}"
               href="${pageContext.request.contextPath}/direccion/matriculas?estado=Pendiente">
               Pendientes <c:if test="${not empty conteos['Pendiente']}">(${conteos['Pendiente']})</c:if>
            </a>
            <a class="filtro-pill ${estadoFiltro eq 'Aprobada' ? 'activo' : ''}"
               href="${pageContext.request.contextPath}/direccion/matriculas?estado=Aprobada">
               Aprobadas <c:if test="${not empty conteos['Aprobada']}">(${conteos['Aprobada']})</c:if>
            </a>
            <a class="filtro-pill ${estadoFiltro eq 'Expirada' ? 'activo' : ''}"
               href="${pageContext.request.contextPath}/direccion/matriculas?estado=Expirada">
               Expiradas <c:if test="${not empty conteos['Expirada']}">(${conteos['Expirada']})</c:if>
            </a>
            <a class="filtro-pill ${estadoFiltro eq 'Todas' ? 'activo' : ''}"
               href="${pageContext.request.contextPath}/direccion/matriculas?estado=Todas">
               Todas
            </a>
        </div>

        <div class="card border-0 shadow-sm">
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty reservas}">
                        <p class="text-muted small text-center py-4 mb-0">
                            No hay reservas de matrícula en estado "<c:out value="${estadoFiltro}" />" por el momento.
                        </p>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table align-middle">
                                <thead>
                                    <tr>
                                        <th>Estudiante</th>
                                        <th>Apoderado</th>
                                        <th>Grado y Sección</th>
                                        <th>Tipo</th>
                                        <th>Estado</th>
                                        <th>Fecha de Reserva</th>
                                        <th class="text-end">Acción</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="r" items="${reservas}">
                                        <tr>
                                            <td>
                                                <c:out value="${r.apellidosAlumno}" />, <c:out value="${r.nombreAlumno}" />
                                                <br>
                                                <span class="badge bg-light text-secondary border">ALU-${r.idAlumno}</span>
                                                <c:if test="${not empty r.dniAlumno}">
                                                    <span class="badge bg-light text-secondary border">DNI ${r.dniAlumno}</span>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:out value="${r.apellidosPadre}" />, <c:out value="${r.nombrePadre}" />
                                                <br>
                                                <span class="text-muted small">
                                                    <c:out value="${r.idPadre}" />
                                                    <c:if test="${not empty r.telefonoPadre}"> · <c:out value="${r.telefonoPadre}" /></c:if>
                                                </span>
                                            </td>
                                            <td><c:out value="${r.grado}" /> "<c:out value="${r.seccion}" />"</td>
                                            <td><c:out value="${r.tipoReserva}" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${r.estadoReserva eq 'Pendiente'}">
                                                        <span class="badge bg-warning text-dark">Pendiente</span>
                                                    </c:when>
                                                    <c:when test="${r.estadoReserva eq 'Aprobada'}">
                                                        <span class="badge bg-success">Aprobada</span>
                                                    </c:when>
                                                    <c:when test="${r.estadoReserva eq 'Expirada'}">
                                                        <span class="badge bg-danger">Expirada</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">${r.estadoReserva}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${r.fechaHoraReserva}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td class="text-end">
                                                <c:if test="${r.estadoReserva eq 'Pendiente'}">
                                                    <button type="button" class="btn btn-success btn-sm"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#confirmAprobar-${r.idReserva}">
                                                        Aprobar
                                                    </button>
                                                    <button type="button" class="btn btn-outline-danger btn-sm"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#confirmRechazar-${r.idReserva}">
                                                        Rechazar
                                                    </button>

                                                    <!-- Modal Aprobar -->
                                                    <div class="modal fade" id="confirmAprobar-${r.idReserva}" tabindex="-1" aria-hidden="true">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title">Aprobar reserva de matrícula</h5>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    ¿Confirma la aprobación de la reserva de
                                                                    <strong><c:out value="${r.nombreAlumno}" /> <c:out value="${r.apellidosAlumno}" /></strong>
                                                                    para <c:out value="${r.grado}" /> "<c:out value="${r.seccion}" />"?
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                                                    <form action="${pageContext.request.contextPath}/direccion/matriculas" method="POST">
                                                                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                                        <input type="hidden" name="accion" value="aprobar">
                                                                        <input type="hidden" name="idReserva" value="${r.idReserva}">
                                                                        <input type="hidden" name="estado" value="${estadoFiltro}">
                                                                        <button type="submit" class="btn btn-success">Sí, aprobar</button>
                                                                    </form>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Modal Rechazar -->
                                                    <div class="modal fade" id="confirmRechazar-${r.idReserva}" tabindex="-1" aria-hidden="true">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title">Rechazar reserva de matrícula</h5>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    ¿Confirma el rechazo de la reserva de
                                                                    <strong><c:out value="${r.nombreAlumno}" /> <c:out value="${r.apellidosAlumno}" /></strong>
                                                                    para <c:out value="${r.grado}" /> "<c:out value="${r.seccion}" />"?
                                                                    La vacante quedará disponible nuevamente para otros postulantes.
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No, mantener</button>
                                                                    <form action="${pageContext.request.contextPath}/direccion/matriculas" method="POST">
                                                                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                                        <input type="hidden" name="accion" value="rechazar">
                                                                        <input type="hidden" name="idReserva" value="${r.idReserva}">
                                                                        <input type="hidden" name="estado" value="${estadoFiltro}">
                                                                        <button type="submit" class="btn btn-danger">Sí, rechazar</button>
                                                                    </form>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test="${r.estadoReserva ne 'Pendiente'}">
                                                    <span class="text-muted small">— Sin acciones —</span>
                                                </c:if>
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

    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
