package com.institucion6029.model;

import java.sql.Timestamp;

public class UsuarioGoogleExterno {
	
	//Atributos privados
	private int idGoogleUser;
	private String googleId;
	private String correoGoogle;
	private String nombreCompleto;
	private Timestamp fechaAutenticacion;
	
	//Constructor
	public UsuarioGoogleExterno() {
		
	}

	public UsuarioGoogleExterno(int idGoogleUser, String googleId, String correoGoogle, String nombreCompleto,
			Timestamp fechaAutenticacion) {
		super();
		this.idGoogleUser = idGoogleUser;
		this.googleId = googleId;
		this.correoGoogle = correoGoogle;
		this.nombreCompleto = nombreCompleto;
		this.fechaAutenticacion = fechaAutenticacion;
	}
	
	//Métodos Getters y Setters
	public int getIdGoogleUser() {
		return idGoogleUser;
	}

	public void setIdGoogleUser(int idGoogleUser) {
		this.idGoogleUser = idGoogleUser;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getCorreoGoogle() {
		return correoGoogle;
	}

	public void setCorreoGoogle(String correoGoogle) {
		this.correoGoogle = correoGoogle;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public Timestamp getFechaAutenticacion() {
		return fechaAutenticacion;
	}

	public void setFechaAutenticacion(Timestamp fechaAutenticacion) {
		this.fechaAutenticacion = fechaAutenticacion;
	}
	
}
