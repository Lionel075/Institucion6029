package com.institucion6029.model;

import java.sql.Date;

public class Alumno {
	
	//Atributos privados
	private int idAlumno;
	private String idPadre;
	private String dni;
	private String nombres;
	private String apellidos;
	private Date fechaNacimiento;
	private String estadoAcademico;
	
	//Constructores
	public Alumno() {
		
	}

	public Alumno(int idAlumno, String idPadre, String dni, String nombres, String apellidos, Date fechaNacimiento,
			String estadoAcademico) {
		super();
		this.idAlumno = idAlumno;
		this.idPadre = idPadre;
		this.dni = dni;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.estadoAcademico = estadoAcademico;
	}
	
	//Métodos Getters y Setters
	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

	public String getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEstadoAcademico() {
		return estadoAcademico;
	}

	public void setEstadoAcademico(String estadoAcademico) {
		this.estadoAcademico = estadoAcademico;
	}
	
}
