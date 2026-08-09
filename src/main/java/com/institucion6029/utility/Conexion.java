package com.institucion6029.utility;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Conexion {

    // volatile: garantiza que el resto de hilos vea la asignación completa
    // del DataSource una vez inicializado, sin necesidad de sincronizar
    // cada obtenerConexion().
    private static volatile DataSource dataSource;

    private Conexion() {
    }

    /**
     * Toma una conexión prestada del pool de Tomcat.
     * Resuelve el recurso JNDI de forma perezosa (en el primer uso real),
     * y reintenta en cada llamada si un intento anterior falló — a diferencia
     * de un bloque static{}, un fallo transitorio de arranque no deja la
     * clase en un estado roto permanente.
     *
     * @return Connection lista para usar
     * @throws SQLException Si el pool no puede entregar una conexión, o si
     *         el recurso JNDI no pudo resolverse.
     */
    public static Connection obtenerConexion() throws SQLException {
        DataSource ds = dataSource;
        if (ds == null) {
            synchronized (Conexion.class) {
                ds = dataSource;
                if (ds == null) {
                    ds = resolverDataSource();
                    dataSource = ds;
                }
            }
        }
        return ds.getConnection();
    }

    private static DataSource resolverDataSource() throws SQLException {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            return (DataSource) envContext.lookup("jdbc/institucion6029DB");
        } catch (NamingException e) {
            throw new SQLException(
                "No se pudo resolver el recurso JNDI 'jdbc/institucion6029DB'. "
              + "Verifica que el <Resource> esté declarado en el context.xml del servidor "
              + "(conf/Catalina/localhost/) y el <resource-ref> en web.xml.", e);
        }
    }

    // Clic derecho -> Run As -> Java Application (Prueba de Consola Local)
    // NOTA: este main() ya NO funciona fuera del contenedor de Tomcat, porque
    // depende del JNDI que solo existe dentro del servidor. Para probar la conexión
    // ahora hay que desplegar la app y revisar el log de arranque.
    public static void main(String[] args) {
        System.out.println("Esta prueba requiere ejecutarse dentro de Tomcat (JNDI no disponible standalone).");
    }
}