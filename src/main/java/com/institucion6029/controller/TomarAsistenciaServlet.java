package com.institucion6029.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.institucion6029.factory.DAOFactory;
import com.institucion6029.model.AlumnoAsistencia;
import com.institucion6029.model.AnioEscolar;
import com.institucion6029.model.SeccionDocente;
import com.institucion6029.model.Usuario;
import com.institucion6029.utility.ConfiguracionAcademica;
import com.institucion6029.exception.ErrorTransaccionException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/asistencia/tomar")
public class TomarAsistenciaServlet extends HttpServlet {

	private static final Logger LOG = LoggerFactory.getLogger(TomarAsistenciaServlet.class);
	private static final long serialVersionUID = 1L;

	private static final Locale ES = Locale.forLanguageTag("es-PE");
	private static final DateTimeFormatter FMT_LARGO = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", ES);

	private static final Set<String> ESTADOS_VALIDOS = Set.of("Presente", "Tardanza", "Ausente");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario docente = validarDocente(request, response);
		if (docente == null)
			return;

		AnioEscolar anio = obtenerAnio(request, response);
		if (anio == null)
			return;

		Integer idSeccion = leerIdSeccion(request);
		if (idSeccion == null) {
			response.sendRedirect(request.getContextPath() + "/asistencia/docente");
			return;
		}

		SeccionDocente seccion = DAOFactory.getAsistenciaDAO().obtenerSeccion(idSeccion, docente.getIdUsuario(),
				anio.getIdAnio());

		// La sección no existe o no es de este docente
		if (seccion == null) {
			response.sendRedirect(request.getContextPath() + "/asistencia/docente?error=SeccionNoAsignada");
			return;
		}

		List<AlumnoAsistencia> alumnos = DAOFactory.getAsistenciaDAO().listarAlumnos(idSeccion, anio.getIdAnio());

		String fecha = LocalDate.now().format(FMT_LARGO);
		fecha = fecha.substring(0, 1).toUpperCase(ES) + fecha.substring(1);

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		request.setAttribute("seccion", seccion);
		request.setAttribute("alumnos", alumnos);
		request.setAttribute("idSeccion", idSeccion);
		request.setAttribute("anioCalendario", anio.getAnioCalendario());
		request.setAttribute("fechaHoy", fecha);
		request.setAttribute("guardado", request.getParameter("ok") != null);

		request.getRequestDispatcher("/WEB-INF/views/toma_asistencia.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario docente = validarDocente(request, response);
		if (docente == null)
			return;

		AnioEscolar anio = obtenerAnio(request, response);
		if (anio == null)
			return;

		Integer idSeccion = leerIdSeccion(request);
		if (idSeccion == null) {
			response.sendRedirect(request.getContextPath() + "/asistencia/docente");
			return;
		}

		SeccionDocente seccion = DAOFactory.getAsistenciaDAO().obtenerSeccion(idSeccion, docente.getIdUsuario(),
				anio.getIdAnio());

		if (seccion == null) {
			response.sendRedirect(request.getContextPath() + "/asistencia/docente?error=SeccionNoAsignada");
			return;
		}

		List<AlumnoAsistencia> alumnos = DAOFactory.getAsistenciaDAO().listarAlumnos(idSeccion, anio.getIdAnio());

		if (alumnos.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/asistencia/tomar?idSeccion=" + idSeccion + "&ok=1");
			return;
		}

		Map<Integer, String> estados = new LinkedHashMap<>();
		boolean faltanAlumnos = false;

		for (AlumnoAsistencia alumno : alumnos) {
			String estado = request.getParameter("estado_" + alumno.getIdAlumno());
			if (estado == null || !ESTADOS_VALIDOS.contains(estado)) {
				faltanAlumnos = true;
				continue;
			}
			estados.put(alumno.getIdAlumno(), estado);
		}

		if (faltanAlumnos) {
			LOG.warn("Envío de asistencia incompleto. idSeccion={}, docente={}, marcados={}/{}",
					idSeccion, docente.getIdUsuario(), estados.size(), alumnos.size());
			response.sendRedirect(request.getContextPath() + "/asistencia/tomar?idSeccion=" + idSeccion
					+ "&error=Incompleto");
			return;
		}

		try {
		    DAOFactory.getAsistenciaDAO().guardarAsistencia(idSeccion, anio.getIdAnio(), docente.getIdUsuario(),
		            estados);
		} catch (ErrorTransaccionException e) {
		    LOG.error("Fallo al guardar asistencia. idSeccion={}, docente={}", idSeccion, docente.getIdUsuario(), e);
		    response.sendRedirect(request.getContextPath() + "/asistencia/tomar?idSeccion=" + idSeccion + "&error=NoGuardado");
		    return;
		}

		response.sendRedirect(request.getContextPath() + "/asistencia/tomar?idSeccion=" + idSeccion + "&ok=1");
	}

	// ---------------- helpers ----------------

	private Usuario validarDocente(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession sesion = request.getSession(false);
		if (sesion == null || sesion.getAttribute("usuario") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp?error=SesionExpirada");
			return null;
		}
		Usuario u = (Usuario) sesion.getAttribute("usuario");
		if (u.getIdRol() != 2) {
			response.sendRedirect(request.getContextPath() + "/dashboard?error=NoAutorizadoDocente");
			return null;
		}
		return u;
	}

	private AnioEscolar obtenerAnio(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			return ConfiguracionAcademica.obtenerAnioEscolarActivo();
		} catch (IllegalStateException e) {
			response.sendRedirect(request.getContextPath() + "/asistencia/docente?error=SinAnioActivo");
			return null;
		}
	}

	private Integer leerIdSeccion(HttpServletRequest request) {
		String id = request.getParameter("idSeccion");
		if (id == null || id.isBlank())
			return null;
		try {
			return Integer.valueOf(id.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}
}