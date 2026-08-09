package com.institucion6029.utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Lista única de los grados académicos válidos para Matrícula.
 * Antes vivía duplicada como arreglo local en DashboardServlet y no existía
 * en absoluto en MatriculaServlet (que no validaba el "grado" recibido).
 */
public final class GradosAcademicos {

    public static final String[] TODOS = {
        "1° Primaria", "2° Primaria", "3° Primaria",
        "4° Primaria", "5° Primaria", "6° Primaria"
    };

    private static final Set<String> VALIDOS =
            Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(TODOS)));

    private GradosAcademicos() {
    }

    public static boolean esValido(String grado) {
        return grado != null && VALIDOS.contains(grado);
    }
}
