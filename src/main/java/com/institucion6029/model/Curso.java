package com.institucion6029.model;

public class Curso {
	
	//Atributos privados
	private int idCurso;
	private String nombreCurso;
	private String descripcion;
	
	//Constructores
	public Curso() {
		
	}

	public Curso(int idCurso, String nombreCurso, String descripcion) {
		super();
		this.idCurso = idCurso;
		this.nombreCurso = nombreCurso;
		this.descripcion = descripcion;
	}
	
	//Métodos Getters y Setters
	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public String getNombreCurso() {
		return nombreCurso;
	}

	public void setNombreCurso(String nombreCurso) {
		this.nombreCurso = nombreCurso;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
