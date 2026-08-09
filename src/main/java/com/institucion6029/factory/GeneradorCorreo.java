package com.institucion6029.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.institucion6029.exception.GeneracionCorreoException;

public class GeneradorCorreo {

    private static final Logger LOG = LoggerFactory.getLogger(GeneradorCorreo.class);
    private static final String DOMINIO = "@6029.edu.pe";

    /**
     * Genera un correo institucional automático y único consultando la base de datos.
     * @throws GeneracionCorreoException si no se pudo determinar con certeza el
     *         correlativo (fallo de BD). No se debe asumir un correo por defecto:
     *         al haber UNIQUE en la columna `correo`, un valor adivinado puede
     *         colisionar con uno ya existente.
     */
    public static String generarCorreoInstitucional(Connection conexion, String primerNombre, String primerApellido) 
            throws GeneracionCorreoException {
        if (primerNombre == null || primerApellido == null || primerNombre.trim().isEmpty() || primerApellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre y apellido no pueden estar vacíos.");
        }

        String inicialNombre = primerNombre.trim().substring(0, 1);
        String apellidoLimpio = primerApellido.trim().split(" ")[0];

        String raizCorreo = limpiarTexto(inicialNombre + apellidoLimpio);

        int correlativo = obtenerSiguienteCorrelativo(conexion, "%" + raizCorreo + "%");

        String sufijoNumero = String.format("%02d", correlativo);
        
        return raizCorreo + sufijoNumero + DOMINIO;
    }

    private static String limpiarTexto(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        String textoSinAcentos = textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return textoSinAcentos.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    /**
     * Ejecuta la consulta SQL para contar las coincidencias y calcular el número del correo.
     * Ya NO retorna 1 por defecto ante un fallo de BD: eso generaba un correo "adivinado"
     * que, al existir la restricción UNIQUE en `correo`, podía colisionar con uno real
     * y romper el flujo más adelante con un error de integridad menos claro.
     */
    private static int obtenerSiguienteCorrelativo(Connection conexion, String patronBusqueda) 
            throws GeneracionCorreoException {
        String sql = "SELECT COUNT(*) FROM acc_usuarios WHERE correo LIKE ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, patronBusqueda);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al calcular el correlativo del correo para el patrón={}", patronBusqueda, e);
            throw new GeneracionCorreoException(
                "No se pudo verificar la disponibilidad del correo institucional debido a un error de base de datos.", e);
        }
        return 1; // COUNT(*) siempre retorna una fila (incluso con 0 coincidencias), así que este
                  // camino es inalcanzable en la práctica — se mantiene solo por seguridad del compilador.
    }
}