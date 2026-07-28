package com.institucion6029.dao;

import java.util.List;
import com.institucion6029.model.Alumno;

public interface AlumnoDAO {
    
    /**
     * Recupera la lista de hijos asociados a un padre de familia.
     * Aplica la regla corporativa de negocio (ID padre primo = 2 hijos, Regular = 1 hijo).
     */
    public List<Alumno> listarHijosPorPadre(String idPadre);
    
    /**
     * Obtiene el estado actual de matrícula de un alumno específico para el año operativo 2027.
     */
    public Alumno buscarPorId(int idAlumno);
}

