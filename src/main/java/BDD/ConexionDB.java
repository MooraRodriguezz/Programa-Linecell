package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // --- ¡CONFIGURACIÓN IMPORTANTE! ---
    private static final String IP_SERVIDOR = "TU_IP_SERVIDOR"; // Reemplazar por la IP de la PC donde instalaste MariaDB
    private static final String PUERTO = "3306"; // Puerto por defecto de MariaDB
    private static final String NOMBRE_BASE_DATOS = "taller_db"; // El nombre que le pusiste
    private static final String USUARIO = "usuario_taller"; // El usuario que creaste
    private static final String CONTRASENA = "TU_CONTRASENA"; // La contraseña de usuario_taller
    // --- FIN CONFIGURACIÓN ---

    private static final String URL = "jdbc:mariadb://" + IP_SERVIDOR + ":" + PUERTO + "/" + NOMBRE_BASE_DATOS;

    public static Connection conectar() {
        Connection conn = null;
        try {
            // Asegúrate de que el driver esté cargado (aunque con Maven suele ser automático)
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