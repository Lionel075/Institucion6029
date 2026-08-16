package com.institucion6029.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import jakarta.servlet.http.HttpSession;

public final class CsrfUtil {

    private static final String ATRIBUTO_SESION = "csrfToken";
    private static final int LONGITUD_BYTES = 32;
    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfUtil() {
    }

    public static String obtenerOGenerarToken(HttpSession session) {
        String token = (String) session.getAttribute(ATRIBUTO_SESION);
        if (token == null) {
            token = generarToken();
            session.setAttribute(ATRIBUTO_SESION, token);
        }
        return token;
    }

    public static String regenerarToken(HttpSession session) {
        String token = generarToken();
        session.setAttribute(ATRIBUTO_SESION, token);
        return token;
    }

    public static boolean validar(HttpSession session, String tokenRecibido) {
        if (session == null || tokenRecibido == null) {
            return false;
        }
        String tokenEsperado = (String) session.getAttribute(ATRIBUTO_SESION);
        if (tokenEsperado == null) {
            return false;
        }
        return MessageDigest.isEqual(
                tokenEsperado.getBytes(StandardCharsets.UTF_8),
                tokenRecibido.getBytes(StandardCharsets.UTF_8));
    }

    private static String generarToken() {
        byte[] bytes = new byte[LONGITUD_BYTES];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
