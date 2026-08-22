package com.institucion6029.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.institucion6029.factory.DAOFactory;
import com.institucion6029.model.AnioEscolar;
import com.institucion6029.model.SeccionDocente;
import com.institucion6029.model.Usuario;
import com.institucion6029.utility.ConfiguracionAcademica;

@WebServlet("/asistencia/docente")
public class PanelDocenteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Locale ES = Locale.forLanguageTag("es-PE");
	private static final DateTimeFormatter FMT_LARGO = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", ES);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession sesion = request.getSession(false);
		if (sesion == null || sesion.getAttribute("usuario") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp?error=SesionExpirada");
			return;
		}

		Usuario docente = (Usuario) sesion.getAttribute("usuario");

		// Blindaje extra: SOLO usuarios DOC (rol 2)
		if (docente.getIdRol() != 2) {
			response.sendRedirect(request.getContextPath() + "/dashboard?error=NoAutorizadoDocente");
			return;
		}

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		AnioEscolar anio;
		try {
			anio = ConfiguracionAcademica.obtenerAnioEscolarActivo();
		} catch (IllegalStateException e) {
			request.setAttribute("sinAnioActivo", true);
			request.getRequestDispatcher("/WEB-INF/views/panel_docente.jsp").forward(request, response);
			return;
		}

		List<SeccionDocente> secciones = DAOFactory.getAsistenciaDAO().listarSecciones(docente.getIdUsuario(),
				anio.getIdAnio());

		int totalAlumnos = 0;
		int seccionesRegistradas = 0;
		for (SeccionDocente s : secciones) {
			totalAlumnos += s.getAlumnos();
			if (s.isRegistrada()) {
				seccionesRegistradas++;
			}
		}

		String fecha = LocalDate.now().format(FMT_LARGO);
		fecha = fecha.substring(0, 1).toUpperCase(ES) + fecha.substring(1);

		request.setAttribute("secciones", secciones);
		request.setAttribute("anioCalendario", anio.getAnioCalendario());
		request.setAttribute("totalAlumnos", totalAlumnos);
		request.setAttribute("seccionesRegistradas", seccionesRegistradas);
		request.setAttribute("fechaHoy", fecha);

		request.getRequestDispatcher("/WEB-INF/views/panel_docente.jsp").forward(request, response);
	}
}
