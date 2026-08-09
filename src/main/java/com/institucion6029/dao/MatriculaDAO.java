package com.institucion6029.dao;

import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.model.Seccion;
import com.institucion6029.exception.ReservaDuplicadaException;
import com.institucion6029.exception.PeriodoMatriculaCerradoException;
import com.institucion6029.exception.ErrorTransaccionException;

public interface MatriculaDAO {

    public Seccion registrarReservaConControlDeCupo(ReservaMatricula reserva, String grado) 
            throws ReservaDuplicadaException, PeriodoMatriculaCerradoException, ErrorTransaccionException;
}

