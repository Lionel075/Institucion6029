package com.institucion6029.model;

public class SeccionDocente {

	private int idSeccion;
	private String grado;
	private String seccion;
	private String turno;
	private String aula;
	private int alumnos;
	private int capacidad;
	private int registrados;
	private String horaRegistro;

	public int getIdSeccion() {
		return idSeccion;
	}

	public void setIdSeccion(int idSeccion) {
		this.idSeccion = idSeccion;
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

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public int getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(int alumnos) {
		this.alumnos = alumnos;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public int getRegistrados() {
		return registrados;
	}

	public void setRegistrados(int registrados) {
		this.registrados = registrados;
	}

	public String getHoraRegistro() {
		return horaRegistro;
	}

	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	/** true si la asistencia de hoy ya fue tomada */
	public boolean isRegistrada() {
		return registrados > 0;
	}

	/** Ej: 3° Primaria "A" */
	public String getEtiqueta() {
		return grado + " \"" + seccion + "\"";
	}
}