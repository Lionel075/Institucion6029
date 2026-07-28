package com.institucion6029.model;

import java.sql.Date;

public class CompetenciaLogro {
	
	//Atributos privados
	private int idLogro;
	private int idAlumno;
	private String descripcionEvento;
	private String tipoCompetencia;
	private String puestoObtenido;
	private int puntajeCalculado;
	private Date fechaLogro;
	
	//Constructores
	public CompetenciaLogro() {
		
	}

	public CompetenciaLogro(int idLogro, int idAlumno, String descripcionEvento, String tipoCompetencia,
			String puestoObtenido, int puntajeCalculado, Date fechaLogro) {
		super();
		this.idLogro = idLogro;
		this.idAlumno = idAlumno;
		this.descripcionEvento = descripcionEvento;
		this.tipoCompetencia = tipoCompetencia;
		this.puestoObtenido = puestoObtenido;
		this.puntajeCalculado = puntajeCalculado;
		this.fechaLogro = fechaLogro;
	}
	
	//Métodos Getters y Setters
	public int getIdLogro() {
		return idLogro;
	}

	public void setIdLogro(int idLogro) {
		this.idLogro = idLogro;
	}

	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

	public String getDescripcionEvento() {
		return descripcionEvento;
	}

	public void setDescripcionEvento(String descripcionEvento) {
		this.descripcionEvento = descripcionEvento;
	}

	public String getTipoCompetencia() {
		return tipoCompetencia;
	}

	public void setTipoCompetencia(String tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
	}

	public String getPuestoObtenido() {
		return puestoObtenido;
	}

	public void setPuestoObtenido(String puestoObtenido) {
		this.puestoObtenido = puestoObtenido;
	}

	public int getPuntajeCalculado() {
		return puntajeCalculado;
	}

	public void setPuntajeCalculado(int puntajeCalculado) {
		this.puntajeCalculado = puntajeCalculado;
	}

	public Date getFechaLogro() {
		return fechaLogro;
	}

	public void setFechaLogro(Date fechaLogro) {
		this.fechaLogro = fechaLogro;
	}
	
}
