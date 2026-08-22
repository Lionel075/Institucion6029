package com.institucion6029.dao;

import java.util.List;
import com.institucion6029.model.Seccion;

public interface SeccionDAO {
    
    public int obtenerVacantesDisponiblesPorGrado(String grado, int idAno);
    
    public List<Seccion> listarTodasLasSecciones(int idAno);
}
