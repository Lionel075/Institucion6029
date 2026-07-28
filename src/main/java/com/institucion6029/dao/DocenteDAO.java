package com.institucion6029.dao;

import com.institucion6029.model.Docente;
import com.institucion6029.model.Seccion;

public interface DocenteDAO {
    
    /**
     * Recupera la ficha técnica completa del profesor usando su código de login (Ej: 'DOC-00010').
     */
    public Docente obtenerPorIdUsuario(String idUsuario);
    
    /**
     * Identifica qué aula física tiene bajo su cargo de tutoría oficial este docente para el año activo.
     */
    public Seccion obtenerSeccionTutorada(String idUsuario, int idAno);
}

