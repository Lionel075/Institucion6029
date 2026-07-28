package com.institucion6029.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;

public class GeneradorCorreo {

    private static final String DOMINIO = "@6029.edu.pe";

    /**
     * Genera un correo institucional automático y único consultando la base de datos.
     * @param conexion Objeto Connection activo de tu clase Conexion
     * @param primerNombre Ejemplo: "Pedro"
     * @param primerApellido Ejemplo: "Martinez"
     * @return Correo formateado y único (Ej: pmartinez01@6029.edu.pe)
     */
    public static String generarCorreoInstitucional(Connection conexion, String primerNombre, String primerApellido) {
        if (primerNombre == null || primerApellido == null || primerNombre.trim().isEmpty() || primerApellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre y apellido no pueden estar vacíos.");
        }

        // 1. Obtener la inicial del nombre y el apellido limpio
        String inicialNombre = primerNombre.trim().substring(0, 1);
        String apellidoLimpio = primerApellido.trim().split(" ")[0]; // Toma solo el primer apellido si envían dos

        // 2. Unir y normalizar (quitar tildes, eñes y pasar a minúsculas)
        String raizCorreo = limpiarTexto(inicialNombre + apellidoLimpio);

        // 3. Consultar a la base de datos cuántos usuarios ya tienen esa misma raíz
        int correlativo = obtenerSiguienteCorrelativo(conexion, "%" + raizCorreo + "%");

        // 4. Formatear el correlativo con dos dígitos (01, 02, etc.) y retornar
        String sufijoNumero = String.format("%02d", correlativo);
        
        return raizCorreo + sufijoNumero + DOMINIO;
    }

    /**
     * Elimina tildes, eñes, diéresis y caracteres especiales del texto.
     */
    private static String limpiarTexto(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // Expresión regular para quitar los acentos combinados y dejar caracteres limpios
        String textoSinAcentos = textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // Reemplazar la eñe si persistiera y pasar a minúsculas limpiando espacios residuales
        return textoSinAcentos.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    /**
     * Ejecuta la consulta SQL para contar las coincidencias y calcular el número del correo.
     */
    private static int obtenerSiguienteCorrelativo(Connection conexion, String patronBusqueda) {
        String sql = "SELECT COUNT(*) FROM acc_usuarios WHERE correo LIKE ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, patronBusqueda);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Si el conteo es 0, el método retorna 1 (para generar el 01)
                    // Si el conteo es 1, retorna 2 (para generar el 02)
                    return rs.getInt(1) + 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular el correlativo del correo: " + e.getMessage());
            // En caso de fallo en la base de datos, retorna 1 por defecto para no romper el flujo
            return 1; 
        }
        return 1;
    }
}

