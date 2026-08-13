package com.institucion6029.model;

import java.sql.Timestamp;

public class ReservaMatricula {
	
	//Atributos privados
	private int idReserva;
	private int idAlumno;
	private int idSeccion;
	private int idAnio;
	private Timestamp fechaHoraReserva;
	private String tipoReserva;
	private String estadoReserva;
	
	private String nombreAlumno;
	private String apellidosAlumno;
	private String grado;
	private String seccion;
	
	//Constructor
	public ReservaMatricula() {
		
	}

	public ReservaMatricula(int idReserva, int idAlumno, int idSeccion, int idAnio, Timestamp fechaHoraReserva,
			String tipoReserva, String estadoReserva, String nombreAlumno, String apellidosAlumno, String grado,
			String seccion) {
		super();
		this.idReserva = idReserva;
		this.idAlumno = idAlumno;
		this.idSeccion = idSeccion;
		this.idAnio = idAnio;
		this.fechaHoraReserva = fechaHoraReserva;
		this.tipoReserva = tipoReserva;
		this.estadoReserva = estadoReserva;
		
		this.nombreAlumno = nombreAlumno;
		this.apellidosAlumno = apellidosAlumno;
		this.grado = grado;
		this.seccion = seccion;
	}
	
	//Métodos Getters y Setters
	public int getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}

	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

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

	public Timestamp getFechaHoraReserva() {
		return fechaHoraReserva;
	}

	public void setFechaHoraReserva(Timestamp fechaHoraReserva) {
		this.fechaHoraReserva = fechaHoraReserva;
	}

	public String getTipoReserva() {
		return tipoReserva;
	}

	public void setTipoReserva(String tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public String getEstadoReserva() {
		return estadoReserva;
	}

	public void setEstadoReserva(String estadoReserva) {
		this.estadoReserva = estadoReserva;
	}
	
	public String getNombreAlumno() { return nombreAlumno; }
	public void setNombreAlumno(String nombreAlumno) { this.nombreAlumno = nombreAlumno; }

	public String getApellidosAlumno() { return apellidosAlumno; }
	public void setApellidosAlumno(String apellidosAlumno) { this.apellidosAlumno = apellidosAlumno; }

	public String getGrado() { return grado; }
	public void setGrado(String grado) { this.grado = grado; }

	public String getSeccion() { return seccion; }
	public void setSeccion(String seccion) { this.seccion = seccion; }
	
}
