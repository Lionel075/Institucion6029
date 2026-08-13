package com.institucion6029.utility;

import com.institucion6029.factory.DAOFactory;
import com.institucion6029.model.AnioEscolar;

public class ConfiguracionAcademica {

    private static final long TTL_MILISEGUNDOS = 5 * 60 * 1000; // 5 minutos

    private static volatile AnioEscolar anioCacheado;
    private static volatile long ultimaCarga = 0L;
    private static final Object LOCK = new Object();

    private ConfiguracionAcademica() {
    }

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

    public static void invalidarCache() {
        synchronized (LOCK) {
            anioCacheado = null;
            ultimaCarga = 0L;
        }
    }
}
