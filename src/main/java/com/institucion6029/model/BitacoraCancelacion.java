package com.institucion6029.model;

import java.sql.Timestamp;

public class BitacoraCancelacion {
	
	//Atributos privados
	private int idBitacora;
	private int idAlumno;
	private int idAnio;
	private Timestamp fechaCancelacion;
	private String motivo;
	
	//Constructor
	public BitacoraCancelacion() {
		
	}

	public BitacoraCancelacion(int idBitacora, int idAlumno, int idAnio, Timestamp fechaCancelacion, String motivo) {
		super();
		this.idBitacora = idBitacora;
		this.idAlumno = idAlumno;
		this.idAnio = idAnio;
		this.fechaCancelacion = fechaCancelacion;
		this.motivo = motivo;
	}
	
	//Métodos Getters y Setters
	public int getIdBitacora() {
		return idBitacora;
	}

	public void setIdBitacora(int idBitacora) {
		this.idBitacora = idBitacora;
	}

	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

	public int getIdAnio() {
		return idAnio;
	}

	public void setIdAnio(int idAnio) {
		this.idAnio = idAnio;
	}

	public Timestamp getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Timestamp fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}
