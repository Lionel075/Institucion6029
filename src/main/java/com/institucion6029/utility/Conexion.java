package com.institucion6029.utility;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Conexion {

    private static volatile DataSource dataSource;

    private Conexion() {
    }

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

    public static void main(String[] args) {
        System.out.println("Esta prueba requiere ejecutarse dentro de Tomcat (JNDI no disponible standalone).");
    }
}