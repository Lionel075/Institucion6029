package com.institucion6029.dao;

import com.institucion6029.model.Seccion;

public interface SeccionDAO {
    
    /**
     * Suma las vacantes de las secciones A, B y C de un mismo grado para la web pública.
     */
    public int obtenerVacantesDisponiblesPorGrado(String grado, int idAno);
    
    /**
     * Busca la primera sección disponible (A, B o C) de un grado que tenga cupos libres.
     */
    public Seccion buscarSeccionDisponibleParaAsignacion(String grado, int idAno);
    
    /**
     * Modifica el contador de vacantes en la base de datos (ej: -1 al reservar, +1 al cancelar).
     */
    public boolean modificarContadorVacantes(int idSeccion, int cambio);
}
