package com.institucion6029.dao;

import java.util.List;
import com.institucion6029.model.Alumno;

public interface AlumnoDAO {
    
    public List<Alumno> listarHijosPorPadre(String idPadre);

    public Alumno buscarPorId(int idAlumno);
}

