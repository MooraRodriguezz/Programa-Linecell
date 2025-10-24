package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // --- ¡CONFIGURACIÓN IMPORTANTE! ---
    // IP_SERVIDOR: "localhost" si está en la misma PC
    private static final String IP_SERVIDOR = "localhost";

    // PUERTO: El que usaste al instalar. ¡OJO! Probablemente sea "3307" si "3306" estaba ocupado
    private static final String PUERTO = "3307";

    // NOMBRE_BASE_DATOS: El que creamos
    private static final String NOMBRE_BASE_DATOS = "taller_celulares";

    // USUARIO: El que creamos para la app
    private static final String USUARIO = "app_java";

    // CONTRASENA: La que le pusimos a ese usuario
    private static final String CONTRASENA = "pass123";
    // --- FIN CONFIGURACIÓN ---

    private static final String URL = "jdbc:mariadb://" + IP_SERVIDOR + ":" + PUERTO + "/" + NOMBRE_BASE_DATOS;

    public static Connection conectar() {
        Connection conn = null;
        try {
            // Esto ya está en tu pom.xml, así que va a funcionar
            Class.forName("org.mariadb.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            // System.out.println("Conexión a MariaDB establecida."); // Opcional para debug
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            // Aquí podrías mostrar un Alert al usuario indicando el error de conexión
            e.printStackTrace(); // Imprime más detalles del error en consola
        }
        return conn;
    }

    // Método para obtener la IP del servidor (solo para información)
    public static String getIpServidor() {
        return IP_SERVIDOR;
    }
}