package BDD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class OrdenDAO {

    private final String SQL_CREAR_TABLA = """
        CREATE TABLE IF NOT EXISTS Ordenes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombreCliente TEXT NOT NULL,
            apellidoCliente TEXT,
            telefono TEXT NOT NULL,
            equipo TEXT,
            fallaReportada TEXT,
            presupuesto REAL,
            fechaIngreso TEXT,
            estado TEXT
        );
    """;

    public void inicializarBaseDeDatos() {

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(SQL_CREAR_TABLA);

        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }
}