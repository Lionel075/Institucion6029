package com.institucion6029.model;

import java.sql.Time;

public class HorarioClase {
	
	//Atributos privados
	private int idHorario;
	private int idSeccion;
	private int idCurso;
	private String idDocente;
	private String diaSemana;
	private Time horaInicio;
	private Time HoraFin;
	
	//Constructores
	public HorarioClase() {
		
	}

	public HorarioClase(int idHorario, int idSeccion, int idCurso, String idDocente, String diaSemana, Time horaInicio,
			Time horaFin) {
		super();
		this.idHorario = idHorario;
		this.idSeccion = idSeccion;
		this.idCurso = idCurso;
		this.idDocente = idDocente;
		this.diaSemana = diaSemana;
		this.horaInicio = horaInicio;
		HoraFin = horaFin;
	}
	
	//Métodos Getters y Setters
	public int getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(int idHorario) {
		this.idHorario = idHorario;
	}

	public int getIdSeccion() {
		return idSeccion;
	}

	public void setIdSeccion(int idSeccion) {
		this.idSeccion = idSeccion;
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

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Time getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Time getHoraFin() {
		return HoraFin;
	}

	public void setHoraFin(Time horaFin) {
		HoraFin = horaFin;
	}
	
}
