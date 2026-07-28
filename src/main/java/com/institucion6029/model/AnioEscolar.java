package com.institucion6029.model;

import java.sql.Date;

public class AnioEscolar {
	
	//Atributos privados
	private int idAnio;
	private int anioCalendario;
	private Date fechaInicioPreferencial;
	private Date fechaFinPreferencial;
	private Date fechaInicioGeneral;
	private Date fechaFinGeneral;
	private String estadoAnio;
	
	//Constructores
	public AnioEscolar() {
		
	}
	
	
	public AnioEscolar(int idAnio, int anioCalendario, Date fechaInicioPreferencial, Date fechaFinPreferencial,
			Date fechaInicioGeneral, Date fechaFinGeneral, String estadoAnio) {
		super();
		this.idAnio = idAnio;
		this.anioCalendario = anioCalendario;
		this.fechaInicioPreferencial = fechaInicioPreferencial;
		this.fechaFinPreferencial = fechaFinPreferencial;
		this.fechaInicioGeneral = fechaInicioGeneral;
		this.fechaFinGeneral = fechaFinGeneral;
		this.estadoAnio = estadoAnio;
	}
	
	//Métodos Getters y Setters
	public int getIdAnio() {
		return idAnio;
	}


	public void setIdAnio(int idAnio) {
		this.idAnio = idAnio;
	}


	public int getAnioCalendario() {
		return anioCalendario;
	}


	public void setAnioCalendario(int anioCalendario) {
		this.anioCalendario = anioCalendario;
	}


	public Date getFechaInicioPreferencial() {
		return fechaInicioPreferencial;
	}


	public void setFechaInicioPreferencial(Date fechaInicioPreferencial) {
		this.fechaInicioPreferencial = fechaInicioPreferencial;
	}


	public Date getFechaFinPreferencial() {
		return fechaFinPreferencial;
	}


	public void setFechaFinPreferencial(Date fechaFinPreferencial) {
		this.fechaFinPreferencial = fechaFinPreferencial;
	}


	public Date getFechaInicioGeneral() {
		return fechaInicioGeneral;
	}


	public void setFechaInicioGeneral(Date fechaInicioGeneral) {
		this.fechaInicioGeneral = fechaInicioGeneral;
	}


	public Date getFechaFinGeneral() {
		return fechaFinGeneral;
	}


	public void setFechaFinGeneral(Date fechaFinGeneral) {
		this.fechaFinGeneral = fechaFinGeneral;
	}


	public String getEstadoAnio() {
		return estadoAnio;
	}


	public void setEstadoAnio(String estadoAnio) {
		this.estadoAnio = estadoAnio;
	}
	
}
