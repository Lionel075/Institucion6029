package com.institucion6029.dao;

import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.model.BitacoraCancelacion;
import com.institucion6029.model.Seccion;
import com.institucion6029.exception.ReservaDuplicadaException;
import com.institucion6029.exception.PeriodoMatriculaCerradoException;
import com.institucion6029.exception.ErrorTransaccionException;

public interface MatriculaDAO {
    
    public boolean registrarReservaMatricula(ReservaMatricula reserva);
    
    public boolean actualizarEstadoReserva(int idReserva, String nuevoEstado);
    
    public boolean registrarCancelacionPreferencial(BitacoraCancelacion bitacora);
    
    public Seccion registrarReservaConControlDeCupo(ReservaMatricula reserva, String grado) 
            throws ReservaDuplicadaException, PeriodoMatriculaCerradoException, ErrorTransaccionException;

    /**
     * Expira toda reserva 'Pendiente' cuya fecha_hora_reserva supere las 48 horas
     * (política ya anunciada en reserva.jsp pero nunca implementada) y libera de
     * forma atómica la vacante correspondiente en sch_secciones.
     * @return cantidad de reservas expiradas en esta ejecución.
     */
    public int expirarReservasVencidas();

    /**
     * Cancela (estado 'Expirada', ya que el ENUM de la BD no admite 'Cancelado')
     * toda reserva 'Preferencial' que siga 'Pendiente' una vez transcurridas las
     * 48 horas de gracia desde el cierre del periodo preferencial
     * (fecha_fin_preferencial en cfg_anos_escolares). Libera la vacante y deja
     * constancia en mat_bitacora_cancelaciones_preferencia con el motivo por defecto.
     * @return cantidad de reservas preferenciales expiradas en esta ejecución.
     */
    public int expirarReservasPreferencialesVencidas();
}