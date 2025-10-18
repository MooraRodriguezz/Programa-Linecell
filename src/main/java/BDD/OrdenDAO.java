package BDD;

import modelo.Orden;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void agregarOrden(Orden orden) {
        String sql = "INSERT INTO Ordenes(nombreCliente, apellidoCliente, telefono, equipo, fallaReportada, presupuesto, fechaIngreso, estado) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, orden.getNombreCliente());
            pstmt.setString(2, orden.getApellidoCliente());
            pstmt.setString(3, orden.getTelefono());
            pstmt.setString(4, orden.getEquipo());
            pstmt.setString(5, orden.getFallaReportada());
            pstmt.setDouble(6, orden.getPresupuesto());
            pstmt.setString(7, orden.getFechaIngreso());
            pstmt.setString(8, orden.getEstado());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al agregar orden: " + e.getMessage());
        }
    }

    public List<Orden> getTodasLasOrdenes() {
        List<Orden> ordenes = new ArrayList<>();
        String sql = "SELECT * FROM Ordenes ORDER BY id DESC";

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Orden orden = new Orden();
                orden.setId(rs.getInt("id"));
                orden.setNombreCliente(rs.getString("nombreCliente"));
                orden.setApellidoCliente(rs.getString("apellidoCliente"));
                orden.setTelefono(rs.getString("telefono"));
                orden.setEquipo(rs.getString("equipo"));
                orden.setFallaReportada(rs.getString("fallaReportada"));
                orden.setPresupuesto(rs.getDouble("presupuesto"));
                orden.setFechaIngreso(rs.getString("fechaIngreso"));
                orden.setEstado(rs.getString("estado"));

                ordenes.add(orden);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener órdenes: " + e.getMessage());
        }
        return ordenes;
    }

    public void actualizarOrden(Orden orden) {
        String sql = "UPDATE Ordenes SET nombreCliente = ?, apellidoCliente = ?, telefono = ?, equipo = ?, fallaReportada = ?, presupuesto = ?, fechaIngreso = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, orden.getNombreCliente());
            pstmt.setString(2, orden.getApellidoCliente());
            pstmt.setString(3, orden.getTelefono());
            pstmt.setString(4, orden.getEquipo());
            pstmt.setString(5, orden.getFallaReportada());
            pstmt.setDouble(6, orden.getPresupuesto());
            pstmt.setString(7, orden.getFechaIngreso());
            pstmt.setString(8, orden.getEstado());
            pstmt.setInt(9, orden.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar orden: " + e.getMessage());
        }
    }

    public void eliminarOrden(int id) {
        String sql = "DELETE FROM Ordenes WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar orden: " + e.getMessage());
        }
    }
}