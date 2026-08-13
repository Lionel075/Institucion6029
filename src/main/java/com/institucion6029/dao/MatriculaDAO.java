package com.institucion6029.dao;

import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.model.Seccion;
import com.institucion6029.exception.ReservaDuplicadaException;
import com.institucion6029.exception.PeriodoMatriculaCerradoException;
import com.institucion6029.exception.ErrorTransaccionException;
import java.util.List;

public interface MatriculaDAO {

    public Seccion registrarReservaConControlDeCupo(ReservaMatricula reserva, String grado) 
            throws ReservaDuplicadaException, PeriodoMatriculaCerradoException, ErrorTransaccionException;
    
    List<ReservaMatricula> listarReservasPorApoderado(String idPadre, int idAno);

    boolean cancelarReserva(int idReserva, String idPadreSolicitante) throws ErrorTransaccionException;

    int expirarReservasVencidas(int horasLimite) throws ErrorTransaccionException;
}
