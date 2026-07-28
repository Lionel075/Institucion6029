package com.institucion6029.model;

public class BonoDescuento {
	
	//Atributos privados
	private int idBono;
	private int idAlumno;
	private int idAnioAplicacion;
	private String porcentajeDescuento;
	private String estadoBono;
	
	//Constructores
	public BonoDescuento() {
		
	}

	public BonoDescuento(int idBono, int idAlumno, int idAnioAplicacion, String porcentajeDescuento,
			String estadoBono) {
		super();
		this.idBono = idBono;
		this.idAlumno = idAlumno;
		this.idAnioAplicacion = idAnioAplicacion;
		this.porcentajeDescuento = porcentajeDescuento;
		this.estadoBono = estadoBono;
	}
	
	//Métodos Getters y Setters
	public int getIdBono() {
		return idBono;
	}

	public void setIdBono(int idBono) {
		this.idBono = idBono;
	}

	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

	public int getIdAnioAplicacion() {
		return idAnioAplicacion;
	}

	public void setIdAnioAplicacion(int idAnioAplicacion) {
		this.idAnioAplicacion = idAnioAplicacion;
	}

	public String getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(String porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	public String getEstadoBono() {
		return estadoBono;
	}

	public void setEstadoBono(String estadoBono) {
		this.estadoBono = estadoBono;
	}
	
}
