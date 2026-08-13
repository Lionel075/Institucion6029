package com.institucion6029.dao;

public interface SeccionDAO {
    
    /**
     * Suma las vacantes de las secciones A, B y C de un mismo grado para la web pública.
     */
    public int obtenerVacantesDisponiblesPorGrado(String grado, int idAno);
}
