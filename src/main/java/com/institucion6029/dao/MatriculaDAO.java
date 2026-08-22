package com.institucion6029.dao;

import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.model.Seccion;
import com.institucion6029.exception.ReservaDuplicadaException;
import com.institucion6029.exception.PeriodoMatriculaCerradoException;
import com.institucion6029.exception.ErrorTransaccionException;
import java.util.List;
import java.util.Map;

public interface MatriculaDAO {

    public Seccion registrarReservaConControlDeCupo(ReservaMatricula reserva, String grado) 
            throws ReservaDuplicadaException, PeriodoMatriculaCerradoException, ErrorTransaccionException;
    
    List<ReservaMatricula> listarReservasPorApoderado(String idPadre, int idAno);

    boolean cancelarReserva(int idReserva, String idPadreSolicitante) throws ErrorTransaccionException;

    int expirarReservasVencidas(int horasLimite) throws ErrorTransaccionException;
    
    List<ReservaMatricula> listarReservasPorEstado(int idAno, String estadoFiltro);

    Map<String, Integer> contarReservasPorEstado(int idAno);

    boolean aprobarReserva(int idReserva, String idUsuarioDireccion) throws ErrorTransaccionException;

    boolean rechazarReserva(int idReserva, String idUsuarioDireccion) throws ErrorTransaccionException;
}
