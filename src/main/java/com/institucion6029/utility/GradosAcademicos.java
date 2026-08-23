package com.institucion6029.utility;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class GradosAcademicos {

    public static final String[] TODOS = {
        "1° Primaria", "2° Primaria", "3° Primaria",
        "4° Primaria", "5° Primaria", "6° Primaria"
    };

    private static final Set<String> VALIDOS =
            Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(TODOS)));

    /**
     * Rango de edad permitido por grado: {edadMinima, edadMaxima}, ambas inclusive.
     * Criterio: edad cumplida al 31 de marzo del año escolar correspondiente
     * (norma MINEDU para el ingreso/permanencia en Educación Básica Regular - nivel Primaria).
     */
    private static final Map<String, int[]> RANGOS_EDAD;
    static {
        Map<String, int[]> rangos = new LinkedHashMap<>();
        rangos.put("1° Primaria", new int[]{6, 8});
        rangos.put("2° Primaria", new int[]{7, 9});
        rangos.put("3° Primaria", new int[]{8, 10});
        rangos.put("4° Primaria", new int[]{9, 11});
        rangos.put("5° Primaria", new int[]{10, 12});
        rangos.put("6° Primaria", new int[]{11, 13});
        RANGOS_EDAD = Collections.unmodifiableMap(rangos);
    }

    private GradosAcademicos() {
    }

    public static boolean esValido(String grado) {
        return grado != null && VALIDOS.contains(grado);
    }

    /**
     * Devuelve {edadMinima, edadMaxima} para el grado indicado, o null si el grado no es válido.
     * El arreglo retornado es una copia; modificarlo no afecta la configuración interna.
     */
    public static int[] rangoEdad(String grado) {
        int[] rango = RANGOS_EDAD.get(grado);
        return rango == null ? null : rango.clone();
    }

    /**
     * Calcula la edad cumplida a la fecha de corte escolar (31 de marzo del año calendario indicado)
     * y valida que corresponda al rango permitido para el grado solicitado.
     *
     * @param grado                  grado académico solicitado (ej. "1° Primaria")
     * @param fechaNacimiento        fecha de nacimiento del alumno
     * @param anioCalendarioEscolar  año calendario del año escolar activo (AnioEscolar.getAnioCalendario())
     * @return true si la edad del alumno al 31 de marzo de ese año está dentro del rango del grado
     */
    public static boolean edadCorrespondeAlGrado(String grado, Date fechaNacimiento, int anioCalendarioEscolar) {
        if (!esValido(grado) || fechaNacimiento == null) {
            return false;
        }
        int[] rango = RANGOS_EDAD.get(grado);
        int edad = calcularEdadAlCorteEscolar(fechaNacimiento, anioCalendarioEscolar);
        return edad >= rango[0] && edad <= rango[1];
    }

    /**
     * Edad cumplida al 31 de marzo del año calendario escolar indicado (fecha de corte MINEDU).
     */
    public static int calcularEdadAlCorteEscolar(Date fechaNacimiento, int anioCalendarioEscolar) {
        LocalDate nacimiento = fechaNacimiento.toLocalDate();
        LocalDate corte = LocalDate.of(anioCalendarioEscolar, 3, 31);
        return Period.between(nacimiento, corte).getYears();
    }
}