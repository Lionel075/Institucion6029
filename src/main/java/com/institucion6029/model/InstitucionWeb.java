package com.institucion6029.model;

public class InstitucionWeb {
	
	//Atributos privados
	private int idInstitucion;
	private String codigoModular;
	private String nombreInstitucion;
	private String descripcion;
	private String direccion;
	private String nivelEducativo;
	private int maxEstudiantesAula;
	private double pensionPreferencial;
	private String modalidad;
	
	//Constructores
	public InstitucionWeb() {
		
	}

	public InstitucionWeb(int idInstitucion, String codigoModular, String nombreInstitucion, String descripcion,
			String direccion, String nivelEducativo, int maxEstudiantesAula, double pensionPreferencial,
			String modalidad) {
		super();
		this.idInstitucion = idInstitucion;
		this.codigoModular = codigoModular;
		this.nombreInstitucion = nombreInstitucion;
		this.descripcion = descripcion;
		this.direccion = direccion;
		this.nivelEducativo = nivelEducativo;
		this.maxEstudiantesAula = maxEstudiantesAula;
		this.pensionPreferencial = pensionPreferencial;
		this.modalidad = modalidad;
	}
	
	//Métodos Getters y Setters
	public int getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(int idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getCodigoModular() {
		return codigoModular;
	}

	public void setCodigoModular(String codigoModular) {
		this.codigoModular = codigoModular;
	}

	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNivelEducativo() {
		return nivelEducativo;
	}

	public void setNivelEducativo(String nivelEducativo) {
		this.nivelEducativo = nivelEducativo;
	}

	public int getMaxEstudiantesAula() {
		return maxEstudiantesAula;
	}

	public void setMaxEstudiantesAula(int maxEstudiantesAula) {
		this.maxEstudiantesAula = maxEstudiantesAula;
	}

	public double getPensionPreferencial() {
		return pensionPreferencial;
	}

	public void setPensionPreferencial(double pensionPreferencial) {
		this.pensionPreferencial = pensionPreferencial;
	}

	public String getModalidad() {
		return modalidad;
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	
}
