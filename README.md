## Configuración de la Base de Datos (JNDI)

Esta intranet utiliza **JNDI** para gestionar el pool de conexiones a través de Tomcat. El archivo `db.properties` no tiene efecto en tiempo de ejecución.

### Pasos para configurar el entorno local:
1. Copia el archivo `src/main/webapp/META-INF/context.xml.example`.
2. Renómbralo a `context.xml` en la misma carpeta (este archivo está ignorado en Git).
3. Reemplaza `tu_usuario`, `tu_password` y el nombre de la base de datos con tus credenciales locales.
4. Asegúrate de que el contenedor busque el recurso bajo el nombre mapeado en `Conexion.java`: `java:comp/env/jdbc/institucion6029DB`.
