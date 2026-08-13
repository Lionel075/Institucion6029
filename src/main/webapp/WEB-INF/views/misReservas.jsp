<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Reservas | Institución Educativa 6029</title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <style>
        body { background-color: #f4f6f9; }
    </style>
</head>
<body>
    <div class="container my-5">

        <div class="d-flex justify-content-between align-items-center p-3 mb-4 bg-white rounded shadow-sm">
            <span class="text-muted">
                Bienvenido Apoderado: <strong class="text-primary">
                    ${not empty sessionScope.usuario.nombreCompleto ? sessionScope.usuario.nombreCompleto : sessionScope.usuario.idUsuario}
                </strong>
            </span>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
        </div>

        <div class="mb-4 d-flex justify-content-between align-items-center">
            <h1 class="h3 text-dark fw-bold mb-0">Mis Reservas de Matrícula</h1>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary btn-sm">Volver al Panel</a>
        </div>

        <c:if test="${flashMsg eq 'CancelacionExitosa'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>¡Listo!</strong> La reserva fue cancelada y la vacante quedó liberada.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${flashError eq 'NoSePudoCancelar'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>No se pudo cancelar.</strong> La reserva ya no está disponible para cancelación o no pertenece a su cuenta.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${flashError eq 'ErrorTransaccion' or flashError eq 'ErrorInterno'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Error.</strong> No se pudo procesar la solicitud. Intente nuevamente.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="card border-0 shadow-sm">
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty misReservas}">
                        <p class="text-muted small text-center py-4 mb-0">Aún no ha generado ninguna reserva de matrícula.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table align-middle">
                                <thead>
                                    <tr>
                                        <th>Estudiante</th>
                                        <th>Grado y Sección</th>
                                        <th>Tipo</th>
                                        <th>Estado</th>
                                        <th>Fecha de Reserva</th>
                                        <th class="text-end">Acción</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="r" items="${misReservas}">
                                        <tr>
                                            <td>
                                                <c:out value="${r.apellidosAlumno}" />, <c:out value="${r.nombreAlumno}" />
                                                <br><span class="badge bg-light text-secondary border">ALU-${r.idAlumno}</span>
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
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">${r.estadoReserva}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${r.fechaHoraReserva}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td class="text-end">
                                                <c:if test="${r.estadoReserva eq 'Pendiente'}">
                                                    <button type="button" class="btn btn-outline-danger btn-sm"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#confirmCancelar-${r.idReserva}">
                                                        Cancelar
                                                    </button>

                                                    <div class="modal fade" id="confirmCancelar-${r.idReserva}" tabindex="-1" aria-hidden="true">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title">Confirmar cancelación</h5>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    ¿Está seguro de que desea cancelar la reserva de
                                                                    <strong><c:out value="${r.nombreAlumno}" /></strong>
                                                                    para <c:out value="${r.grado}" />? La vacante quedará disponible nuevamente.
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No, mantener</button>
                                                                    <form action="${pageContext.request.contextPath}/matricula" method="POST">
                                                                        <input type="hidden" name="accion" value="cancelar">
                                                                        <input type="hidden" name="idReserva" value="${r.idReserva}">
                                                                        <button type="submit" class="btn btn-danger">Sí, cancelar</button>
                                                                    </form>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
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

    <!-- Necesario para que funcionen los modales (data-bs-toggle/data-bs-dismiss) -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>