package com.institucion6029.model;

public class PadreApoderado {
	
	//Atributos privados
	private String idUsuario;
	private String dni;
	private String nombres;
	private String apellidos;
	private String telefono;
	private String direccion;
	
	//Constructores
	public PadreApoderado() {
		
	}

	public PadreApoderado(String idUsuario, String dni, String nombres, String apellidos, String telefono,
			String direccion) {
		super();
		this.idUsuario = idUsuario;
		this.dni = dni;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.direccion = direccion;
	}
	
	//Métodos Getters y Setters
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
}
