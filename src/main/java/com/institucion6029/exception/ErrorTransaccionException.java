package com.institucion6029.exception;

public class ErrorTransaccionException extends Exception {
    private static final long serialVersionUID = 1L;

    public ErrorTransaccionException(String mensaje) {
        super(mensaje);
    }

    public ErrorTransaccionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}