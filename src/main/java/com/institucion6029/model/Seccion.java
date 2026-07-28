package com.institucion6029.model;

public class Seccion {
	
	//Atributos privados
	private int idSeccion;
	private int idAnio;
	private String grado;
	private String seccion;
	private String idDocenteTutor;
	private int vacantesDisponibles;
	
	//Constructores
	public Seccion() {
		
	}

	public Seccion(int idSeccion, int idAnio, String grado, String seccion, String idDocenteTutor,
			int vacantesDisponibles) {
		super();
		this.idSeccion = idSeccion;
		this.idAnio = idAnio;
		this.grado = grado;
		this.seccion = seccion;
		this.idDocenteTutor = idDocenteTutor;
		this.vacantesDisponibles = vacantesDisponibles;
	}
	
	//Métodos Setters y Getters
	public int getIdSeccion() {
		return idSeccion;
	}

	public void setIdSeccion(int idSeccion) {
		this.idSeccion = idSeccion;
	}

	public int getIdAnio() {
		return idAnio;
	}

	public void setIdAnio(int idAnio) {
		this.idAnio = idAnio;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getSeccion() {
		return seccion;
	}

	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	public String getIdDocenteTutor() {
		return idDocenteTutor;
	}

	public void setIdDocenteTutor(String idDocenteTutor) {
		this.idDocenteTutor = idDocenteTutor;
	}

	public int getVacantesDisponibles() {
		return vacantesDisponibles;
	}

	public void setVacantesDisponibles(int vacantesDisponibles) {
		this.vacantesDisponibles = vacantesDisponibles;
	}
	
}
