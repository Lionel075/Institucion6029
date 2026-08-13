package com.institucion6029.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.institucion6029.exception.ErrorTransaccionException;
import com.institucion6029.factory.DAOFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExpiracionReservasScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(ExpiracionReservasScheduler.class);

    public static final int HORAS_LIMITE_PAGO = 48;

    private static final long INTERVALO_MINUTOS = 15;

    private static ScheduledExecutorService executor;

    private ExpiracionReservasScheduler() {
    }

    public static synchronized void iniciar() {
        if (executor != null && !executor.isShutdown()) {
            LOG.warn("El scheduler de expiración de reservas ya estaba iniciado.");
            return;
        }

        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "expiracion-reservas");
            t.setDaemon(true); // no debe impedir el apagado de Tomcat
            return t;
        });

        executor.scheduleWithFixedDelay(
                ExpiracionReservasScheduler::ejecutarBarrido,
                INTERVALO_MINUTOS,
                INTERVALO_MINUTOS,
                TimeUnit.MINUTES);

        LOG.info("Scheduler de expiración de reservas iniciado (cada {} min, límite {} h).",
                INTERVALO_MINUTOS, HORAS_LIMITE_PAGO);
    }

    public static synchronized void detener() {
        if (executor == null) {
            return;
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        LOG.info("Scheduler de expiración de reservas detenido.");
    }

    private static void ejecutarBarrido() {
        try {
            int liberadas = DAOFactory.getMatriculaDAO().expirarReservasVencidas(HORAS_LIMITE_PAGO);
            if (liberadas > 0) {
                LOG.info("Barrido de expiración: {} reserva(s) liberada(s).", liberadas);
            }
        } catch (ErrorTransaccionException e) {
            LOG.error("Fallo al ejecutar el barrido de expiración de reservas", e);
        } catch (Throwable t) {
            LOG.error("Error inesperado en el barrido de expiración de reservas", t);
        }
    }
}
