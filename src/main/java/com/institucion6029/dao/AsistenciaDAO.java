package com.institucion6029.dao;

import java.util.List;
import com.institucion6029.model.Alumno;
import com.institucion6029.model.AsistenciaDiaria;

public interface AsistenciaDAO {
    
    /**
     * Recupera la lista de alumnos pertenecientes a una sección específica 
     * para que el docente pueda tomar asistencia.
     */
    public List<Alumno> listarAlumnosPorSeccion(int idSeccion);
    
    /**
     * Registra de forma masiva o individual la asistencia binaria (1 o 0) 
     * de los alumnos para una fecha específica en el año operativo 2027.
     */
    public boolean registrarAsistencia(List<AsistenciaDiaria> listaAsistencia);
    public boolean justificarFalta(int idAsistencia, String motivoJustificado);
}
