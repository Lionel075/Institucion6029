package com.institucion6029.utility;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private PasswordUtil() {
    }

    /** Genera un hash BCrypt nuevo (usar al crear/resetear una contraseña). */
    public static String hash(String claveEnTextoPlano) {
        return BCrypt.hashpw(claveEnTextoPlano, BCrypt.gensalt(12));
    }

    /** true si el valor guardado tiene formato de hash BCrypt ($2a$, $2b$, $2y$). */
    public static boolean esHashBCrypt(String valorAlmacenado) {
        return valorAlmacenado != null
                && (valorAlmacenado.startsWith("$2a$")
                 || valorAlmacenado.startsWith("$2b$")
                 || valorAlmacenado.startsWith("$2y$"));
    }

    /** Verifica una clave en texto plano contra un hash BCrypt almacenado. */
    public static boolean verificar(String claveEnTextoPlano, String hashAlmacenado) {
        try {
            return BCrypt.checkpw(claveEnTextoPlano, hashAlmacenado);
        } catch (IllegalArgumentException e) {
            // Hash corrupto o formato inesperado: nunca autenticar por defecto
            System.err.println("[PasswordUtil] Hash BCrypt inválido en BD: " + e.getMessage());
            return false;
        }
    }
}
