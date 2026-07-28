package com.institucion6029.dao;

import java.util.List;
import com.institucion6029.model.HorarioClase;

public interface HorarioDAO {
    
    /**
     * Extrae el listado cronológico de bloques de clase asignados a un aula física específica.
     * @param idSeccion ID numérico de la sección (1 al 18).
     * @return Colección de objetos HorarioClase ordenados por día y hora de inicio.
     */
    public List<HorarioClase> obtenerHorarioPorSeccion(int idSeccion);
}

