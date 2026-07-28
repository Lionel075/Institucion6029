package com.institucion6029.model;

public class Rol {
	
	//Atributos privados
	private int idRol;
	private String nombreRol;
	
	//Constructores
	public Rol() {
		
	}

	public Rol(int idRol, String nombreRol) {
		super();
		this.idRol = idRol;
		this.nombreRol = nombreRol;
	}
	
	//Métodos Getters y Setters
	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}
	
}
