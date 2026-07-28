package com.institucion6029.model;

public class CuadroHonorAnual {
	
	//Atributos privados
	private int idCuadro;
	private int idAlumno;
	private int idAnio;
	private double promedioFinal;
	private int totalInasistenciasAnio;
	private int puntosLogrosAcumulados;
	private int puestoSeccion;
	private int puestoGrado;
	private String calificaCuadro;
	
	//Constructores
	public CuadroHonorAnual() {
		
	}

	public CuadroHonorAnual(int idCuadro, int idAlumno, int idAnio, double promedioFinal, int totalInasistenciasAnio,
			int puntosLogrosAcumulados, int puestoSeccion, int puestoGrado, String calificaCuadro) {
		super();
		this.idCuadro = idCuadro;
		this.idAlumno = idAlumno;
		this.idAnio = idAnio;
		this.promedioFinal = promedioFinal;
		this.totalInasistenciasAnio = totalInasistenciasAnio;
		this.puntosLogrosAcumulados = puntosLogrosAcumulados;
		this.puestoSeccion = puestoSeccion;
		this.puestoGrado = puestoGrado;
		this.calificaCuadro = calificaCuadro;
	}
	
	//Métodos Getters y Setters
	public int getIdCuadro() {
		return idCuadro;
	}

	public void setIdCuadro(int idCuadro) {
		this.idCuadro = idCuadro;
	}

	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

	public int getIdAnio() {
		return idAnio;
	}

	public void setIdAnio(int idAnio) {
		this.idAnio = idAnio;
	}

	public double getPromedioFinal() {
		return promedioFinal;
	}

	public void setPromedioFinal(double promedioFinal) {
		this.promedioFinal = promedioFinal;
	}

	public int getTotalInasistenciasAnio() {
		return totalInasistenciasAnio;
	}

	public void setTotalInasistenciasAnio(int totalInasistenciasAnio) {
		this.totalInasistenciasAnio = totalInasistenciasAnio;
	}

	public int getPuntosLogrosAcumulados() {
		return puntosLogrosAcumulados;
	}

	public void setPuntosLogrosAcumulados(int puntosLogrosAcumulados) {
		this.puntosLogrosAcumulados = puntosLogrosAcumulados;
	}

	public int getPuestoSeccion() {
		return puestoSeccion;
	}

	public void setPuestoSeccion(int puestoSeccion) {
		this.puestoSeccion = puestoSeccion;
	}

	public int getPuestoGrado() {
		return puestoGrado;
	}

	public void setPuestoGrado(int puestoGrado) {
		this.puestoGrado = puestoGrado;
	}

	public String getCalificaCuadro() {
		return calificaCuadro;
	}

	public void setCalificaCuadro(String calificaCuadro) {
		this.calificaCuadro = calificaCuadro;
	}
	
}
