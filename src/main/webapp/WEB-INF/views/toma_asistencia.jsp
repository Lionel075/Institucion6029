<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Registro de Asistencia | Institución Educativa 6029</title>

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

.stat-card {
	border: none;
	border-top: 4px solid var(--azul-corporativo);
}

.stat-card .stat-numero {
	font-size: 2.1rem;
	font-weight: 700;
	color: var(--azul-corporativo);
}

.stat-card.stat-presente {
	border-top-color: #10b981;
}

.stat-card.stat-presente .stat-numero {
	color: #047857;
}

.stat-card.stat-tardanza {
	border-top-color: #f59e0b;
}

.stat-card.stat-tardanza .stat-numero {
	color: #b45309;
}

.stat-card.stat-ausente {
	border-top-color: #ef4444;
}

.stat-card.stat-ausente .stat-numero {
	color: #b91c1c;
}

.stat-card.stat-total {
	border-top-color: #64748b;
}

.stat-card.stat-total .stat-numero {
	color: #475569;
}

.campo-fijo {
	background-color: #eef2f7;
	border: 1px solid #dbe3ec;
	border-radius: .375rem;
	padding: .55rem .85rem;
}

.codigo-badge {
	font-size: .75rem;
	letter-spacing: .03em;
}

/* Filas resaltadas según excepción marcada */
tr.fila-tardanza {
	background-color: #fffbeb;
}

tr.fila-ausente {
	background-color: #fef2f2;
}

tr.fila-sin-marcar {
	outline: 2px solid #dc3545;
	outline-offset: -2px;
}

/* Radios con apariencia de casilla, como en el wireframe */
.form-check-input.op-asistencia {
	border-radius: .25rem;
}

.form-check-input.op-presente:checked {
	background-color: #198754;
	border-color: #198754;
}

.form-check-input.op-tardanza:checked {
	background-color: #fd7e14;
	border-color: #fd7e14;
}

.form-check-input.op-ausente:checked {
	background-color: #dc3545;
	border-color: #dc3545;
}

.form-check-input.op-asistencia:checked {
	background-image:
		url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 20'%3e%3cpath fill='none' stroke='%23fff' stroke-linecap='round' stroke-linejoin='round' stroke-width='3' d='m6 10 3 3 6-6'/%3e%3c/svg%3e");
}

.opcion-asistencia {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 8px 14px;
    margin-right: 14px;
    margin-bottom: 6px;
    border: 2px solid #6c757d;
    border-radius: 8px;
    background-color: #fff;
    cursor: pointer;
    font-weight: 500;
    min-width: 115px;
    justify-content: center;
}

.opcion-asistencia:hover {
    background-color: #f8f9fa;
}

/* Cuando el radio está seleccionado */
.opcion-asistencia:has(input:checked) {
    border-width: 3px;
}

/* Colores según estado */
.opcion-asistencia.presente:has(input:checked) {
    border-color: #198754;
    background-color: #e9f7ef;
}

.opcion-asistencia.tardanza:has(input:checked) {
    border-color: #ffc107;
    background-color: #fff8df;
}

.opcion-asistencia.ausente:has(input:checked) {
    border-color: #dc3545;
    background-color: #fdebec;
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
			</span> <a href="${pageContext.request.contextPath}/asistencia/docente"
				class="btn btn-outline-secondary btn-sm"> &laquo; Volver al
				Panel </a>
		</div>

		<!-- Título -->
		<div class="mb-4">
			<h1 class="h3 text-dark fw-bold mb-1">Registro de Asistencia
				Diaria</h1>
			<p class="text-secondary small mb-0">Todos los alumnos inician
				como presentes. Marque solo las excepciones.</p>
		</div>

		<!-- Alertas -->
		<c:if test="${guardado}">
			<div class="alert alert-success alert-dismissible fade show"
				role="alert">
				<strong>¡Listo!</strong> La asistencia fue confirmada correctamente.
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
		</c:if>
		
		<c:if test="${param.error eq 'NoGuardado'}">
            <div class="alert alert-danger">
                No se pudo guardar la asistencia. Intenta nuevamente; si el problema persiste, avisa a Dirección.
            </div>
        </c:if>

	<c:if test="${param.error eq 'Incompleto'}">
            <div class="alert alert-warning">
                <strong>Faltan alumnos por marcar.</strong> Debes seleccionar Presente, Tardanza o Ausente para
                todos los alumnos de la nómina antes de confirmar.
            </div>
        </c:if>
		
		<!-- Política de registro -->
		<div class="alert alert-primary" role="alert">
			<strong>Política de Registro (Periodo ${anioCalendario}):</strong>
			<ul class="small mb-0 mt-2">
				<li>La asistencia solo puede registrarse una vez dentro de la
					fecha vigente.</li>
				<li>Una vez confirmada, la edición posterior queda marcada como
					"modificada" para auditoría.</li>
			</ul>
		</div>

		<!-- Sección y fecha -->
		<div class="card border-0 shadow-sm mb-4">
			<div class="card-body">
				<div class="row g-4">
					<div class="col-12 col-md-6">
						<label
							class="form-label text-muted small fw-semibold text-uppercase">Sección</label>
						<div class="campo-fijo">
							<c:out value="${seccion.grado}" />
							"
							<c:out value="${seccion.seccion}" />
							" &middot; Turno
							<c:out value="${seccion.turno}" />
							<span class="text-muted small">(Aula <c:out
									value="${seccion.aula}" />)
							</span>
						</div>
					</div>
					<div class="col-12 col-md-6">
						<label
							class="form-label text-muted small fw-semibold text-uppercase">Fecha</label>
						<div class="campo-fijo">${fechaHoy}</div>
					</div>
				</div>
			</div>
		</div>

		<!-- Contadores en vivo -->
		<div class="row g-4 mb-4">
			<div class="col-6 col-lg-3">
				<div class="card stat-card stat-presente shadow-sm h-100">
					<div class="card-body">
						<div class="stat-numero" id="nPresentes">0</div>
						<div class="text-muted small">Presentes</div>
					</div>
				</div>
			</div>
			<div class="col-6 col-lg-3">
				<div class="card stat-card stat-tardanza shadow-sm h-100">
					<div class="card-body">
						<div class="stat-numero" id="nTardanzas">0</div>
						<div class="text-muted small">Tardanzas</div>
					</div>
				</div>
			</div>
			<div class="col-6 col-lg-3">
				<div class="card stat-card stat-ausente shadow-sm h-100">
					<div class="card-body">
						<div class="stat-numero" id="nFaltas">0</div>
						<div class="text-muted small">Faltas</div>
					</div>
				</div>
			</div>
			<div class="col-6 col-lg-3">
				<div class="card stat-card stat-total shadow-sm h-100">
					<div class="card-body">
						<div class="stat-numero">${alumnos.size()}</div>
						<div class="text-muted small">Total alumnos</div>
					</div>
				</div>
			</div>
		</div>

		<!-- Formulario de registro -->
		<form method="post"
			action="${pageContext.request.contextPath}/asistencia/tomar"
			id="formAsistencia">
			<input type="hidden" name="idSeccion" value="${idSeccion}"> <input
				type="hidden" name="csrfToken" value="${csrfToken}">

			<div class="mb-3 d-flex align-items-center justify-content-between">
				<h2 class="h5 text-dark mb-0">Nómina de Alumnos</h2>
				<button type="button" class="btn btn-outline-secondary btn-sm"
					id="btnLimpiar">Limpiar marcas</button>
			</div>

			<div class="card border-0 shadow-sm mb-4">
				<div class="card-body">
					<c:choose>
						<c:when test="${empty alumnos}">
							<p class="text-muted small text-center py-4 mb-0">Esta
								sección no tiene alumnos matriculados en el periodo
								${anioCalendario}.</p>
						</c:when>
						<c:otherwise>
							<p class="text-muted small">Seleccione el estado correspondiente para cada alumno</p>
							<div class="table-responsive">
								<table class="table align-middle">
									<thead>
										<tr>
											<th style="width: 60px;">#</th>
											<th>Alumno</th>
											<th style="width: 140px;">Estado</th>
											<th style="width: 38%;">Registro</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="a" items="${alumnos}" varStatus="i">
											<tr class="fila-alumno">
												<td class="text-muted"><c:if test="${i.count lt 10}">0</c:if>${i.count}
												</td>
												<td>
													<div class="fw-semibold text-dark">
														<c:out value="${a.apellidos}" />
														,
														<c:out value="${a.nombres}" />
													</div> <span
													class="badge bg-light text-secondary border codigo-badge">
														<c:out value="${a.codigo}" />
												</span>
												</td>
												<td><span class="badge etiqueta-estado">${a.estado}</span>
												</td>
												<td>
													<div class="d-flex flex-wrap gap-3">
														<div class="form-check">
															<input class="form-check-input op-asistencia op-presente"
																type="radio" name="estado_${a.idAlumno}"
																id="p_${a.idAlumno}" value="Presente">
																<label for="p_${a.idAlumno}">Presente</label>
														</div>
														<div class="form-check">
															<input class="form-check-input op-asistencia op-tardanza"
																type="radio" name="estado_${a.idAlumno}"
																id="t_${a.idAlumno}" value="Tardanza">
																<label for="t_${a.idAlumno}">Tardanza</label>
														</div>
														<div class="form-check">
															<input class="form-check-input op-asistencia op-ausente"
																type="radio" name="estado_${a.idAlumno}"
																id="a_${a.idAlumno}" value="Ausente">
																<label for="a_${a.idAlumno}">Ausente</label>
														</div>
													</div>
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

			<div class="d-flex justify-content-between align-items-center">
				<a href="${pageContext.request.contextPath}/asistencia/docente"
					class="btn btn-outline-secondary"> &laquo; Volver al Panel </a>
				<c:if test="${not empty alumnos}">
					<button type="submit" class="btn btn-success btn-lg shadow-sm">Confirmar
						Asistencia</button>
				</c:if>
			</div>
		</form>

	</div>

	<script
		src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
	<script>
	(function() {
	    var filas = document.querySelectorAll('tr.fila-alumno');

	    function pintarFila(fila) {
	        var marcado = fila.querySelector('input[type="radio"]:checked');
	        var estado = marcado ? marcado.value : '';
	        var badge = fila.querySelector('.etiqueta-estado');

	        fila.classList.remove('fila-tardanza', 'fila-ausente');
	        badge.className = 'badge etiqueta-estado';

	        if (estado === 'Tardanza') {
	            fila.classList.add('fila-tardanza');
	            badge.classList.add('bg-warning', 'text-dark');
	            badge.textContent = 'Tardanza';
	        } else if (estado === 'Ausente') {
	            fila.classList.add('fila-ausente');
	            badge.classList.add('bg-danger');
	            badge.textContent = 'Ausente';
	        } else if (estado === 'Presente') {
	            badge.classList.add('bg-success');
	            badge.textContent = 'Presente';
	        } else {
	            badge.textContent = '';
	        }

	        return estado;
	    }

	    function refrescar() {
	        var p = 0, t = 0, f = 0;

	        filas.forEach(function(fila) {
	            var estado = pintarFila(fila);

	            if (estado === 'Presente') {
	                p++;
	            } else if (estado === 'Tardanza') {
	                t++;
	            } else if (estado === 'Ausente') {
	                f++;
	            }
	        });

	        document.getElementById('nPresentes').textContent = p;
	        document.getElementById('nTardanzas').textContent = t;
	        document.getElementById('nFaltas').textContent = f;
	    }

	    document.querySelectorAll('input.op-asistencia').forEach(function(radio) {
	        radio.addEventListener('change', refrescar);
	    });

	    var limpiar = document.getElementById('btnLimpiar');

	    if (limpiar) {
	        limpiar.addEventListener('click', function() {
	            document.querySelectorAll('input.op-asistencia').forEach(function(radio) {
	                radio.checked = false;
	            });

	            refrescar();
	        });
	    }

	    var formulario = document.getElementById('formAsistencia');
	    if (formulario) {
	        formulario.addEventListener('submit', function(evento) {
	            var incompletas = [];

	            filas.forEach(function(fila) {
	                var marcado = fila.querySelector('input[type="radio"]:checked');
	                fila.classList.toggle('fila-sin-marcar', !marcado);
	                if (!marcado) {
	                    incompletas.push(fila);
	                }
	            });

	            if (incompletas.length > 0) {
	                evento.preventDefault();
	                incompletas[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
	                alert('Faltan ' + incompletas.length + ' alumno(s) por marcar. '
	                    + 'Selecciona Presente, Tardanza o Ausente para cada uno antes de confirmar.');
	            }
	        });
	    }

	    refrescar();
	})();
	</script>
</body>
</html>