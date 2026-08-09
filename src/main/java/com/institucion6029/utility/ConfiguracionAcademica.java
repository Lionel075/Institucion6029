package com.institucion6029.utility;

import com.institucion6029.factory.DAOFactory;
import com.institucion6029.model.AnioEscolar;

/**
 * Punto único de acceso al "año operativo activo". Reemplaza los hardcodeos
 * de ANIO_OPERATIVO = 2 que antes vivían repetidos en MatriculaServlet,
 * AsistenciaServlet y DashboardServlet.
 *
 * Se cachea en memoria con un TTL corto: evita una consulta a BD en cada
 * request, pero igual refleja un cambio de año activo (hecho por el director)
 * sin necesidad de reiniciar Tomcat.
 */
public class ConfiguracionAcademica {

    private static final long TTL_MILISEGUNDOS = 5 * 60 * 1000; // 5 minutos

    private static volatile AnioEscolar anioCacheado;
    private static volatile long ultimaCarga = 0L;
    private static final Object LOCK = new Object();

    private ConfiguracionAcademica() {
    }

    /**
     * @return el id_ano activo actual.
     * @throws IllegalStateException si no hay ningún año escolar activo
     *         configurado en cfg_anos_escolares. Esto es una condición de
     *         configuración, no un error transitorio: se propaga para que
     *         el llamador decida cómo informarlo (no debe asumirse un valor
     *         por defecto silencioso, que fue justamente el problema original).
     */
    public static int obtenerAnioOperativoActivo() {
        return obtenerAnioEscolarActivo().getIdAnio();
    }

    public static AnioEscolar obtenerAnioEscolarActivo() {
        long ahora = System.currentTimeMillis();

        AnioEscolar copiaLocal = anioCacheado;
        if (copiaLocal != null && (ahora - ultimaCarga) < TTL_MILISEGUNDOS) {
            return copiaLocal;
        }

        synchronized (LOCK) {
            // Doble chequeo: otro hilo pudo haber refrescado mientras esperábamos el lock
            if (anioCacheado != null && (System.currentTimeMillis() - ultimaCarga) < TTL_MILISEGUNDOS) {
                return anioCacheado;
            }

            AnioEscolar fresco = DAOFactory.getAnioEscolarDAO().obtenerAnioActivo();

            if (fresco == null) {
                throw new IllegalStateException(
                    "No hay ningún año escolar con estado_ano = 'Activo' en cfg_anos_escolares. "
                  + "Verifica la configuración antes de operar Matrícula o Asistencia.");
            }

            anioCacheado = fresco;
            ultimaCarga = System.currentTimeMillis();
            return fresco;
        }
    }

    /** Invalida la caché manualmente (útil si más adelante hay un panel de admin que cambia el año activo). */
    public static void invalidarCache() {
        synchronized (LOCK) {
            anioCacheado = null;
            ultimaCarga = 0L;
        }
    }
}
