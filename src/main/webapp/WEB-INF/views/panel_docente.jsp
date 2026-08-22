<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Panel Docente | Institución Educativa 6029</title>

<link href="${pageContext.request.contextPath}/css/bootstrap.min.css"
	rel="stylesheet" type="text/css">

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

.stat-card .stat-numero {
	font-size: 2.1rem;
	font-weight: 700;
	color: var(--azul-corporativo);
}

.stat-card.stat-registradas {
	border-top-color: #10b981;
}

.stat-card.stat-registradas .stat-numero {
	color: #047857;
}

.stat-card.stat-alumnos {
	border-top-color: #f59e0b;
}

.stat-card.stat-alumnos .stat-numero {
	color: #b45309;
}

.aula-badge {
	font-size: .75rem;
	letter-spacing: .03em;
}
</style>
</head>
<body>

	<div class="container my-5">

		<!-- Cabecera de Usuario -->
		<div
			class="d-flex justify-content-between align-items-center p-3 mb-4 bg-white rounded shadow-sm">
			<span class="text-muted"> Docente: <strong
				class="text-primary"> ${not empty sessionScope.usuario.nombreCompleto ? sessionScope.usuario.nombreCompleto : sessionScope.usuario.idUsuario}
			</strong>
			</span> <a href="${pageContext.request.contextPath}/login"
				class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
		</div>

		<!-- Título -->
		<div class="mb-4">
			<h1 class="h3 text-dark fw-bold mb-1">Panel de Secciones a Cargo</h1>
			<c:choose>
				<c:when test="${empty sinAnioActivo}">
					<p class="text-secondary small mb-0">
						Periodo Académico Operativo <strong>${anioCalendario}</strong>
						&mdash; Tiene <strong>${secciones.size()}</strong>
						${secciones.size() == 1 ? 'sección asignada' : 'secciones asignadas'}
						como docente a cargo.
					</p>
				</c:when>
				<c:otherwise>
					<p class="text-secondary small mb-0">No hay un año escolar
						activo configurado actualmente.</p>
				</c:otherwise>
			</c:choose>
		</div>

		<!-- Alertas -->
		<c:if test="${sinAnioActivo}">
			<div class="alert alert-secondary alert-dismissible fade show"
				role="alert">
				<strong>Atención.</strong> No hay ningún año escolar con estado
				"Activo". No es posible registrar asistencia hasta que Dirección
				configure uno.
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
		</c:if>
		<c:if test="${param.error eq 'SeccionNoAsignada'}">
			<div class="alert alert-danger alert-dismissible fade show"
				role="alert">
				<strong>Acceso denegado.</strong> La sección solicitada no está
				asignada a su cuenta docente.
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
		</c:if>
		<c:if test="${param.ok eq '1'}">
			<div class="alert alert-success alert-dismissible fade show"
				role="alert">
				<strong>¡Listo!</strong> La asistencia fue registrada correctamente.
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
		</c:if>

		<c:if test="${empty sinAnioActivo}">

			<!-- Recordatorio operativo -->
			<div class="alert alert-primary" role="alert">
				<strong>Recordatorio:</strong> la asistencia diaria se registra
				dentro de la primera hora de clase. <span class="d-block small mt-1">Fecha
					de hoy: ${fechaHoy}</span>
			</div>

			<!-- Tabla de secciones a cargo -->
			<div class="mb-3 d-flex align-items-center justify-content-between">
				<h2 class="h5 text-dark mb-0">Mis Secciones a Cargo</h2>
			</div>
			<div class="card border-0 shadow-sm mb-4">
				<div class="card-body">
					<c:choose>
						<c:when test="${empty secciones}">
							<p class="text-muted small text-center py-4 mb-0">No tiene
								secciones asignadas para el periodo ${anioCalendario}.</p>
						</c:when>
						<c:otherwise>
							<div class="table-responsive">
								<table class="table align-middle">
									<thead>
										<tr>
											<th>Grado / Sección</th>
											<th>Turno</th>
											<th>Alumnos</th>
											<th>Asistencia de Hoy</th>
											<th class="text-end" style="width: 18%;">Acción</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="s" items="${secciones}">
											<tr>
												<td>
													<div class="fw-semibold text-dark">
														<c:out value="${s.grado}" />
														"
														<c:out value="${s.seccion}" />
														"
													</div> <span
													class="badge bg-light text-secondary border aula-badge">
														Aula <c:out value="${s.aula}" />
												</span>
												</td>
												<td><c:out value="${s.turno}" /></td>
												<td class="text-muted small">${s.alumnos}/
													${s.capacidad} matriculados</td>
												<td><c:choose>
														<c:when test="${s.registrada}">
															<span class="badge bg-success"> Registrada<c:if
																	test="${not empty s.horaRegistro}"> ${s.horaRegistro}</c:if>
															</span>
														</c:when>
														<c:otherwise>
															<span class="badge bg-warning text-dark">Pendiente</span>
														</c:otherwise>
													</c:choose></td>
												<td class="text-end"><c:choose>
														<c:when test="${s.registrada}">
															<a
																href="${pageContext.request.contextPath}/asistencia/tomar?idSeccion=${s.idSeccion}"
																class="btn btn-outline-primary btn-sm">Ver / Editar</a>
														</c:when>
														<c:otherwise>
															<a
																href="${pageContext.request.contextPath}/asistencia/tomar?idSeccion=${s.idSeccion}"
																class="btn btn-corporativo btn-sm shadow-sm">Abrir
																Asistencia</a>
														</c:otherwise>
													</c:choose></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>

			<!-- Resumen del día -->
			<div class="mb-3">
				<h2 class="h5 text-dark mb-0">Resumen del Día</h2>
			</div>
			<div class="row g-4 mb-4">
				<div class="col-12 col-md-4">
					<div class="card stat-card shadow-sm h-100">
						<div class="card-body">
							<div class="stat-numero">${secciones.size()}</div>
							<div class="text-muted small">Secciones a cargo</div>
						</div>
					</div>
				</div>
				<div class="col-12 col-md-4">
					<div class="card stat-card stat-registradas shadow-sm h-100">
						<div class="card-body">
							<div class="stat-numero">${seccionesRegistradas}/
								${secciones.size()}</div>
							<div class="text-muted small">Asistencias registradas</div>
						</div>
					</div>
				</div>
				<div class="col-12 col-md-4">
					<div class="card stat-card stat-alumnos shadow-sm h-100">
						<div class="card-body">
							<div class="stat-numero">${totalAlumnos}</div>
							<div class="text-muted small">Alumnos totales</div>
						</div>
					</div>
				</div>
			</div>

			<p class="text-muted small text-center mb-0">Seleccione una
				sección y presione "Abrir Asistencia" para iniciar el registro del
				día.</p>

		</c:if>

	</div>

	<script
		src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
	<script>
		if (window.location.search) {
			const urlLimpia = window.location.origin + window.location.pathname;
			window.history.replaceState({}, document.title, urlLimpia);
		}
	</script>
</body>
</html>