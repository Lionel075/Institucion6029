package com.institucion6029.dao;

import java.util.List;
import java.util.Map;

import com.institucion6029.model.AlumnoAsistencia;
import com.institucion6029.model.SeccionDocente;

public interface AsistenciaDAO {

	List<SeccionDocente> listarSecciones(String idDocente, int idAno);

	SeccionDocente obtenerSeccion(int idSeccion, String idDocente, int idAno);

	List<AlumnoAsistencia> listarAlumnos(int idSeccion, int idAno);

	void guardarAsistencia(int idSeccion, int idAno, String idDocente, Map<Integer, String> estados);
}
