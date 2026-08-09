package com.institucion6029.exception;

/**
 * Se lanza cuando no se puede calcular con certeza el correlativo de un
 * correo institucional (fallo de BD al consultar duplicados). Como `correo`
 * tiene restricción UNIQUE, dejar pasar un valor no confirmado arriesga que
 * el INSERT posterior falle con un error de restricción críptico en vez de
 * un mensaje claro en el punto real del problema.
 */
public class GeneracionCorreoException extends Exception {

    private static final long serialVersionUID = 1L;

    public GeneracionCorreoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}