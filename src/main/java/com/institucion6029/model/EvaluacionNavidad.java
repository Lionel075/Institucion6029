package com.institucion6029.model;

import java.sql.Timestamp;

public class EvaluacionNavidad {
	
	//Atributos privados
	private int idEvaluacion;
	private int idAlumno;
	private int idCurso;
	private Timestamp fechaExamen;
	private Double notaRecuperacion;
	private String estadoEvaluacion;
	
	//Constructores
	public EvaluacionNavidad() {
		
	}

	public EvaluacionNavidad(int idEvaluacion, int idAlumno, int idCurso, Timestamp fechaExamen,
			Double notaRecuperacion, String estadoEvaluacion) {
		super();
		this.idEvaluacion = idEvaluacion;
		this.idAlumno = idAlumno;
		this.idCurso = idCurso;
		this.fechaExamen = fechaExamen;
		this.notaRecuperacion = notaRecuperacion;
		this.estadoEvaluacion = estadoEvaluacion;
	}
	
	//Métodos Getters y Setters
	public int getIdEvaluacion() {
		return idEvaluacion;
	}

	public void setIdEvaluacion(int idEvaluacion) {
		this.idEvaluacion = idEvaluacion;
	}

	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public Timestamp getFechaExamen() {
		return fechaExamen;
	}

	public void setFechaExamen(Timestamp fechaExamen) {
		this.fechaExamen = fechaExamen;
	}

	public Double getNotaRecuperacion() {
		return notaRecuperacion;
	}

	public void setNotaRecuperacion(Double notaRecuperacion) {
		this.notaRecuperacion = notaRecuperacion;
	}

	public String getEstadoEvaluacion() {
		return estadoEvaluacion;
	}

	public void setEstadoEvaluacion(String estadoEvaluacion) {
		this.estadoEvaluacion = estadoEvaluacion;
	}
	
}
