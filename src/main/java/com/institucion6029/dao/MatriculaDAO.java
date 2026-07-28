package com.institucion6029.dao;

import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.model.BitacoraCancelacion;
import com.institucion6029.model.Seccion;
import com.institucion6029.exception.ReservaDuplicadaException;
import com.institucion6029.exception.PeriodoMatriculaCerradoException;

public interface MatriculaDAO {
    
    // Debe llamarse exactamente como tu @Override de abajo
    public boolean registrarReservaMatricula(ReservaMatricula reserva);
    
    public boolean actualizarEstadoReserva(int idReserva, String nuevoEstado);
    
    public boolean registrarCancelacionPreferencial(BitacoraCancelacion bitacora);
    
    public Seccion registrarReservaConControlDeCupo(ReservaMatricula reserva, String grado) 
            throws ReservaDuplicadaException, PeriodoMatriculaCerradoException;
}
