package com.institucion6029.utility;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hash(String claveEnTextoPlano) {
        return BCrypt.hashpw(claveEnTextoPlano, BCrypt.gensalt(12));
    }

    public static boolean esHashBCrypt(String valorAlmacenado) {
        return valorAlmacenado != null
                && (valorAlmacenado.startsWith("$2a$")
                 || valorAlmacenado.startsWith("$2b$")
                 || valorAlmacenado.startsWith("$2y$"));
    }

    public static boolean verificar(String claveEnTextoPlano, String hashAlmacenado) {
        try {
            return BCrypt.checkpw(claveEnTextoPlano, hashAlmacenado);
        } catch (IllegalArgumentException e) {
            System.err.println("[PasswordUtil] Hash BCrypt inválido en BD: " + e.getMessage());
            return false;
        }
    }
}
