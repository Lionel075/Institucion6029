package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.institucion6029.dao.MatriculaDAO;
import com.institucion6029.model.ReservaMatricula;
import com.institucion6029.model.BitacoraCancelacion;
import com.institucion6029.utility.Conexion;
import com.institucion6029.model.Seccion;
import com.institucion6029.exception.ReservaDuplicadaException;
import com.institucion6029.exception.PeriodoMatriculaCerradoException;

public class MatriculaDAOImpl implements MatriculaDAO {

	@Override
	public Seccion registrarReservaConControlDeCupo(ReservaMatricula reserva, String grado) 
	        throws ReservaDuplicadaException, PeriodoMatriculaCerradoException {

	    String sqlPeriodo = "SELECT CASE "
	                       + "  WHEN CURDATE() BETWEEN fecha_inicio_preferencial AND fecha_fin_preferencial THEN 'Preferencial' "
	                       + "  WHEN CURDATE() BETWEEN fecha_inicio_general AND fecha_fin_general THEN 'General' "
	                       + "  ELSE NULL "
	                       + "END AS tipo_calculado "
	                       + "FROM cfg_anos_escolares WHERE id_ano = ? AND estado_ano = 'Activo'";

	    String sqlDuplicado = "SELECT id_reserva FROM mat_reservas_matricula "
	                         + "WHERE id_alumno = ? AND id_ano = ? AND estado_reserva IN ('Pendiente','Aprobada') "
	                         + "LIMIT 1";

	    String sqlSeccion = "SELECT id_seccion, id_ano, grado, seccion, id_docente_tutor, vacantes_disponibles "
	                       + "FROM sch_secciones WHERE grado = ? AND id_ano = ? AND vacantes_disponibles > 0 "
	                       + "ORDER BY seccion ASC LIMIT 1 FOR UPDATE";

	    String sqlInsertReserva = "INSERT INTO mat_reservas_matricula (id_alumno, id_seccion, id_ano, tipo_reserva, estado_reserva) "
	                             + "VALUES (?, ?, ?, ?, ?)";

	    String sqlDescuentoVacante = "UPDATE sch_secciones SET vacantes_disponibles = vacantes_disponibles - 1 "
	                                + "WHERE id_seccion = ? AND vacantes_disponibles > 0";

	    Connection con = null;
	    try {
	        con = Conexion.obtenerConexion();
	        con.setAutoCommit(false);

	        // 1. Determina si el periodo de matrícula está abierto y qué tipo aplica
	        String tipoCalculado = null;
	        try (PreparedStatement pstmtPeriodo = con.prepareStatement(sqlPeriodo)) {
	            pstmtPeriodo.setInt(1, reserva.getIdAnio());
	            try (ResultSet rsPeriodo = pstmtPeriodo.executeQuery()) {
	                if (rsPeriodo.next()) {
	                    tipoCalculado = rsPeriodo.getString("tipo_calculado");
	                }
	            }
	        }

	        if (tipoCalculado == null) {
	            con.rollback();
	            throw new PeriodoMatriculaCerradoException(
	                "El periodo de matrícula para id_ano=" + reserva.getIdAnio()
	                + " está cerrado, fuera de fechas, o el año no está 'Activo'.");
	        }
	        reserva.setTipoReserva(tipoCalculado);

	        // 2. Rechaza si el alumno ya tiene una reserva vigente
	        try (PreparedStatement pstmtDup = con.prepareStatement(sqlDuplicado)) {
	            pstmtDup.setInt(1, reserva.getIdAlumno());
	            pstmtDup.setInt(2, reserva.getIdAnio());

	            try (ResultSet rsDup = pstmtDup.executeQuery()) {
	                if (rsDup.next()) {
	                    con.rollback();
	                    throw new ReservaDuplicadaException(
	                        "El alumno con id=" + reserva.getIdAlumno()
	                        + " ya tiene una reserva Pendiente o Aprobada para el año " + reserva.getIdAnio());
	                }
	            }
	        }

	        // 3. Bloquea y asigna sección con cupo
	        Seccion seccionAsignada = null;

	        try (PreparedStatement pstmtSeccion = con.prepareStatement(sqlSeccion)) {
	            pstmtSeccion.setString(1, grado);
	            pstmtSeccion.setInt(2, reserva.getIdAnio());

	            try (ResultSet rs = pstmtSeccion.executeQuery()) {
	                if (rs.next()) {
	                    seccionAsignada = new Seccion();
	                    seccionAsignada.setIdSeccion(rs.getInt("id_seccion"));
	                    seccionAsignada.setIdAnio(rs.getInt("id_ano"));
	                    seccionAsignada.setGrado(rs.getString("grado"));
	                    seccionAsignada.setSeccion(rs.getString("seccion"));
	                    seccionAsignada.setIdDocenteTutor(rs.getString("id_docente_tutor"));
	                    seccionAsignada.setVacantesDisponibles(rs.getInt("vacantes_disponibles"));
	                }
	            }
	        }

	        if (seccionAsignada == null) {
	            con.rollback();
	            return null; // Sin cupo en A, B ni C
	        }

	        // 4. Inserta la reserva con el tipo ya calculado (no hardcodeado)
	        try (PreparedStatement pstmtInsert = con.prepareStatement(sqlInsertReserva)) {
	            pstmtInsert.setInt(1, reserva.getIdAlumno());
	            pstmtInsert.setInt(2, seccionAsignada.getIdSeccion());
	            pstmtInsert.setInt(3, reserva.getIdAnio());
	            pstmtInsert.setString(4, reserva.getTipoReserva());
	            pstmtInsert.setString(5, reserva.getEstadoReserva());
	            pstmtInsert.executeUpdate();
	        }

	        // 5. Descuenta la vacante
	        try (PreparedStatement pstmtUpdate = con.prepareStatement(sqlDescuentoVacante)) {
	            pstmtUpdate.setInt(1, seccionAsignada.getIdSeccion());
	            if (pstmtUpdate.executeUpdate() == 0) {
	                con.rollback();
	                return null;
	            }
	        }

	        con.commit();
	        return seccionAsignada;

	    } catch (ReservaDuplicadaException | PeriodoMatriculaCerradoException e) {
	        throw e; // se re-lanzan tal cual para que el Servlet distinga cada caso
	    } catch (SQLException e) {
	        System.err.println("[MatriculaDAOImpl] Error en transacción de reserva con control de cupo: " + e.getMessage());
	        if (con != null) {
	            try { con.rollback(); } catch (SQLException ex) {
	                System.err.println("[MatriculaDAOImpl] Error al hacer rollback: " + ex.getMessage());
	            }
	        }
	        return null;
	    } finally {
	        if (con != null) {
	            try {
	                con.setAutoCommit(true);
	                con.close();
	            } catch (SQLException e) {
	                System.err.println("[MatriculaDAOImpl] Error al cerrar conexión: " + e.getMessage());
	            }
	        }
	    }
	}
	
	@Override
	public boolean registrarReservaMatricula(ReservaMatricula reserva) {
	    // Se omite fecha_hora_reserva en la inserción para que MySQL use su DEFAULT CURRENT_TIMESTAMP
	    String sql = "INSERT INTO mat_reservas_matricula (id_alumno, id_seccion, id_ano, tipo_reserva, estado_reserva) VALUES (?, ?, ?, ?, ?)";

	    try (Connection con = Conexion.obtenerConexion();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, reserva.getIdAlumno());
	        pstmt.setInt(2, reserva.getIdSeccion());
	        pstmt.setInt(3, reserva.getIdAnio());
	        pstmt.setString(4, reserva.getTipoReserva());
	        pstmt.setString(5, reserva.getEstadoReserva()); // Inicia como 'Pendiente'
	        
	        return pstmt.executeUpdate() > 0;
	        
	    } catch (SQLException e) {
	        System.err.println("[MatriculaDAOImpl] Error al registrar reserva de matrícula: " + e.getMessage());
	        return false;
	    }
	}

    @Override
    public boolean actualizarEstadoReserva(int idReserva, String nuevoEstado) {
        String sql = "UPDATE mat_reservas_matricula SET estado_reserva = ? WHERE id_reserva = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idReserva);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[MatriculaDAOImpl] Error al actualizar estado de la reserva: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean registrarCancelacionPreferencial(BitacoraCancelacion bitacora) {
        // Se omite fecha_cancelacion para que use el TIMESTAMP por defecto del sistema
        String sql = "INSERT INTO mat_bitacora_cancelaciones_preferencia (id_alumno, id_ano, motivo) VALUES (?, ?, ?)";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, bitacora.getIdAlumno());
            pstmt.setInt(2, bitacora.getIdAnio());
            pstmt.setString(3, bitacora.getMotivo());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[MatriculaDAOImpl] Error al registrar auditoría en bitácora: " + e.getMessage());
            return false;
        }
    }
}

