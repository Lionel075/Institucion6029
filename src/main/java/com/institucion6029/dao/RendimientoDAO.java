package com.institucion6029.dao;

import java.util.List;
import com.institucion6029.model.CompetenciaLogro;
import com.institucion6029.model.CuadroHonorAnual;
import com.institucion6029.model.EvaluacionNavidad;

public interface RendimientoDAO {
    
    /**
     * Registra un puesto en el podio (1°, 2°, 3°). El Trigger de MySQL se encargará 
     * de calcular los puntos x1/x2 automáticamente en la base de datos.
     */
    public boolean registrarLogroCompetencia(CompetenciaLogro logro);
    
    /**
     * El Administrador agenda un examen especial pre-navideño para un curso con más de 10 faltas.
     */
    public boolean programarExamenNavidad(EvaluacionNavidad evaluacion);
    
    /**
     * El Tutor digita la nota final del examen de Navidad, modificando el estado a 'Rendido'.
     */
    public boolean registrarNotaExamenNavidad(int idEvaluacion, double nota);
    
    /**
     * Consolida el ranking aplicando tus filtros estrictos de desempate y exclusión por inasistencias.
     */
    public boolean generarCuadroHonorAnual(int idAno);
    
    /**
     * Extrae el Cuadro de Honor procesado de un aula específica para el panel privado del apoderado.
     */
    public List<CuadroHonorAnual> obtenerCuadroHonorPorSeccion(int idSeccion);
}
