package com.institucion6029.exception;

public class ReservaDuplicadaException extends Exception {
    private static final long serialVersionUID = 1L;

    public ReservaDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
