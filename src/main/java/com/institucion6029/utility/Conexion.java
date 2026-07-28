package com.institucion6029.utility;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Conexion {

    private static DataSource dataSource;

    // Resuelve el recurso JNDI UNA sola vez, cuando la clase se carga
    static {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/institucion6029DB");
        } catch (NamingException e) {
            throw new RuntimeException(
                "No se pudo resolver el recurso JNDI 'jdbc/institucion6029DB'. "
              + "Verifica que el <Resource> esté declarado en el context.xml del servidor "
              + "(conf/Catalina/localhost/) y el <resource-ref> en web.xml.", e);
        }
    }

    /**
     * Toma una conexión prestada del pool de Tomcat.
     * @return Connection lista para usar
     * @throws SQLException Si el pool no puede entregar una conexión (agotado, BD caída, etc.)
     */
    public static Connection obtenerConexion() throws SQLException {
        return dataSource.getConnection();
    }

    // Clic derecho -> Run As -> Java Application (Prueba de Consola Local)
    // NOTA: este main() ya NO funciona fuera del contenedor de Tomcat, porque
    // depende del JNDI que solo existe dentro del servidor. Para probar la conexión
    // ahora hay que desplegar la app y revisar el log de arranque.
    public static void main(String[] args) {
        System.out.println("Esta prueba requiere ejecutarse dentro de Tomcat (JNDI no disponible standalone).");
    }
}