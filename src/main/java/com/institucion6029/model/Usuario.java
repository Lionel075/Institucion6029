package com.institucion6029.model;

import java.sql.Timestamp;

public class Usuario {
	
	//Atributos privados
	private String idUsuario;
	private String correo;
	private String clave;
	private String nombreCompleto;
	private int idRol;
	private String estadoUsuario;
	private Timestamp fechaCreacion;
	
	//Constructores
	public Usuario() {
		
	}

	public Usuario(String idUsuario, String correo, String clave, String nombreCompleto, int idRol, String estadoUsuario,
			Timestamp fechaCreacion) {
		super();
		this.idUsuario = idUsuario;
		this.correo = correo;
		this.clave = clave;
		this.nombreCompleto = nombreCompleto;
		this.idRol = idRol;
		this.estadoUsuario = estadoUsuario;
		this.fechaCreacion = fechaCreacion;
	}
	
	//Métodos Getters y Setters
	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String contrasenia) {
		this.clave = contrasenia;
	}

	public String getNombreCompleto() {
	    return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
	    this.nombreCompleto = nombreCompleto;
	}
	
	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public String getEstadoUsuario() {
		return estadoUsuario;
	}

	public void setEstadoUsuario(String estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
}
