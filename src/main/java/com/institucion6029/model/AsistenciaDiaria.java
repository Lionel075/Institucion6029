package com.institucion6029.model;

import java.sql.Date;
import java.sql.Timestamp;

public class AsistenciaDiaria {
	
	//Atributos privados
	private int idAsistencia;
	private int idAlumno;
	private int idCurso;
	private int idSeccion;
	private String idDocente;
	private Date fecha;
	private String estadoInicial;
	private String estadoFinal;
	private Timestamp fechaModificacion;
	
	//Constructores
	public AsistenciaDiaria() {
		
	}

	public AsistenciaDiaria(int idAsistencia, int idAlumno, int idCurso, int idSeccion, String idDocente, Date fecha,
			String estadoInicial, String estadoFinal, Timestamp fechaModificacion) {
		super();
		this.idAsistencia = idAsistencia;
		this.idAlumno = idAlumno;
		this.idCurso = idCurso;
		this.setIdSeccion(idSeccion);
		this.idDocente = idDocente;
		this.fecha = fecha;
		this.estadoInicial = estadoInicial;
		this.estadoFinal = estadoFinal;
		this.fechaModificacion = fechaModificacion;
	}
	
	//Métodos Getters y Setters
	public int getIdAsistencia() {
		return idAsistencia;
	}

	public void setIdAsistencia(int idAsistencia) {
		this.idAsistencia = idAsistencia;
	}

	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public String getIdDocente() {
		return idDocente;
	}

	public void setIdDocente(String idDocente) {
		this.idDocente = idDocente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public String getEstadoFinal() {
		return estadoFinal;
	}

	public void setEstadoFinal(String estadoFinal) {
		this.estadoFinal = estadoFinal;
	}

	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public int getIdSeccion() {
		return idSeccion;
	}

	public void setIdSeccion(int idSeccion) {
		this.idSeccion = idSeccion;
	}
	
}
