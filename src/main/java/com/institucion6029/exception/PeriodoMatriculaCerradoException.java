package com.institucion6029.exception;

public class PeriodoMatriculaCerradoException extends Exception {
    private static final long serialVersionUID = 1L;

    public PeriodoMatriculaCerradoException(String mensaje) {
        super(mensaje);
    }
}