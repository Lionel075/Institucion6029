package com.institucion6029.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.institucion6029.factory.DAOFactory;
import com.institucion6029.model.Usuario;
import com.institucion6029.model.Alumno;
import com.institucion6029.model.AsistenciaDiaria;
import com.institucion6029.model.Docente;
import com.institucion6029.model.Seccion;
import com.institucion6029.utility.ConfiguracionAcademica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/asistencia/docente")
public class AsistenciaServlet extends HttpServlet {
    
	private static final Logger LOG = LoggerFactory.getLogger(AsistenciaServlet.class);
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        
        LOG.info("Verificando acceso para el ID: {} con ID de Rol: {}", user.getIdUsuario(), user.getIdRol());
        
        //Solo los docentes (rol 2) pueden ver este panel
        if (user.getIdRol() != 2) { // 2 = DOCENTE
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=RolDocenteInvalido");
            return;
        }

        String idUsuarioDocente = user.getIdUsuario(); // Recupera el código alfanumérico (ej: DOC-00010)
        
        //Resuelve el año escolar activo
        int anoOperativo;
        try {
            anoOperativo = ConfiguracionAcademica.obtenerAnioOperativoActivo();
        } catch (IllegalStateException e) {
            System.err.println("[AsistenciaServlet] " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=SinAnioActivo");
            return;
        }
        
        try {
        	//Busca la ficha del docente asociada a este usuario
            Docente docenteInfo = DAOFactory.getDocenteDAO().obtenerPorIdUsuario(idUsuarioDocente);
            
            if (docenteInfo != null) {
                request.setAttribute("docente", docenteInfo);
                
                //Busca la sección de la que este docente es tutor, para el año activo
                Seccion seccionTutorada = DAOFactory.getDocenteDAO().obtenerSeccionTutorada(idUsuarioDocente, anoOperativo);
                
                if (seccionTutorada != null) {
                    request.setAttribute("seccion", seccionTutorada);
                    
                    //Regla corporativa de Turnos Académicos basados en el grado de tu objeto Seccion
                    // Determina el turno (Mañana/Tarde) leyendo el texto del grado de la sección
                    String grado = seccionTutorada.getGrado(); // Ej: "1° Primaria"
                    String turno = "Mañana";
                    if (grado.contains("4°") || grado.contains("5°") || grado.contains("6°")) {
                        turno = "Tarde";
                    }
                    request.setAttribute("turnoAcademico", turno);

                    //Cargar alumnos de esa sección específica
                    List<Alumno> alumnosSeccion = DAOFactory.getAsistenciaDAO().listarAlumnosPorSeccion(seccionTutorada.getIdSeccion());
                    request.setAttribute("listaAlumnos", alumnosSeccion);
                    
                    request.setAttribute("fechaHoy", new Date());
                    request.getRequestDispatcher("/WEB-INF/views/asistencia_docente.jsp").forward(request, response);
                    return;
                } else {
                	response.sendRedirect(request.getContextPath() + "/login.jsp?error=SinSeccionAsignada");
                    return;
                }
            } else {
            	response.sendRedirect(request.getContextPath() + "/login.jsp?error=FichaDocenteNoEncontrada");
            }
        } catch (Exception e) {
        	LOG.error("Error al cargar panel de asistencia. idUsuarioDocente={}", idUsuarioDocente, e);
            response.sendRedirect(request.getContextPath() + "/dashboard?error=ErrorInterno");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        if (user.getIdRol() != 2) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=NoAutorizado");
            return;
        }

        try {
            int idSeccion = Integer.parseInt(request.getParameter("idSeccion"));
            int idCursoBase = 1; // Asignación temporal de curso base de tutoría (Matemática/Comunicación)
            String idDocente = user.getIdUsuario();
            
            String[] arrIdAlumnos = request.getParameterValues("idAlumno");
            
            if (arrIdAlumnos != null && arrIdAlumnos.length > 0) {
                List<AsistenciaDiaria> loteAsistencia = new ArrayList<>();
                java.sql.Date fechaRegistro = new java.sql.Date(System.currentTimeMillis());

                for (String idAluStr : arrIdAlumnos) {
                    int idAlu = Integer.parseInt(idAluStr);
                    String valorMarcado = request.getParameter("estado_" + idAlu); // Recibe "Presente" o "Falta"
                    
                    AsistenciaDiaria ast = new AsistenciaDiaria();
                    ast.setIdAlumno(idAlu);
                    ast.setIdCurso(idCursoBase);
                    ast.setIdSeccion(idSeccion);
                    ast.setIdDocente(idDocente);
                    ast.setFecha(fechaRegistro);
                    ast.setEstadoInicial(valorMarcado); 
                    ast.setEstadoFinal(valorMarcado);   
                    
                    loteAsistencia.add(ast);
                }

                boolean exitoGuardado = DAOFactory.getAsistenciaDAO().registrarAsistencia(loteAsistencia);
                
                if (exitoGuardado) {
                    response.sendRedirect(request.getContextPath() + "/dashboard?msg=AsistenciaExitosa");
                } else {
                    response.sendRedirect(request.getContextPath() + "/docente/asistencia?error=ErrorPersistencia");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/docente/asistencia?error=SinAlumnos");
            }
        } catch (Exception e) {
        	LOG.error("Error procesando lote de asistencia", e);
            response.sendRedirect(request.getContextPath() + "/docente/asistencia?error=DatosInvalidos");
        }
    }
}
