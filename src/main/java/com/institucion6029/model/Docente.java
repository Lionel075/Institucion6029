package com.institucion6029.model;

public class Docente {
	
	//Atributos privado
	private int idDocente;
	private String idUsuario;
	private String dni;
	private String nombres;
	private String apellidos;
	private String telefono;
	private String correoPersonal;
	
	//Constructores
	public Docente() {
		
	}
	
	public Docente(int idDocente, String idUsuario, String dni, String nombres, String apellidos, String telefono,
			String correoPersonal) {
		super();
		this.idDocente = idDocente;
		this.idUsuario = idUsuario;
		this.dni = dni;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.correoPersonal = correoPersonal;
	}
	
	//Métodos Getters y Setters
	public int getIdDocente() {
		return idDocente;
	}

	public void setIdDocente(int idDocente) {
		this.idDocente = idDocente;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreoPersonal() {
		return correoPersonal;
	}

	public void setCorreoPersonal(String correoPersonal) {
		this.correoPersonal = correoPersonal;
	}
	
}
