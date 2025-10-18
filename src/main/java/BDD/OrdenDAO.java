package BDD;

import modelo.HistorialEstado;
import modelo.Orden;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenDAO {

    private final String SQL_CREAR_TABLA_ORDENES = """
        CREATE TABLE IF NOT EXISTS Ordenes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            numeroOrden TEXT,
            nombreCliente TEXT NOT NULL,
            apellidoCliente TEXT,
            telefono TEXT NOT NULL,
            tipoEquipo TEXT,
            marca TEXT,
            modelo TEXT,
            numeroSerie TEXT,
            fallaReportada TEXT,
            observacionesPublicas TEXT,
            observacionesPrivadas TEXT,
            presupuesto REAL DEFAULT 0,
            importeFinal REAL DEFAULT 0,
            fechaIngreso TEXT,
            estadoActual TEXT
        );
    """;

    private final String SQL_CREAR_TABLA_HISTORIAL = """
        CREATE TABLE IF NOT EXISTS HistorialEstados (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            orden_id INTEGER,
            estado TEXT,
            fecha TEXT,
            FOREIGN KEY (orden_id) REFERENCES Ordenes (id) ON DELETE CASCADE
        );
    """;

    public void inicializarBaseDeDatos() {
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(SQL_CREAR_TABLA_ORDENES);
            stmt.execute(SQL_CREAR_TABLA_HISTORIAL);
        } catch (SQLException e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }

    public void agregarOrden(Orden orden) {
        String sqlOrden = "INSERT INTO Ordenes(numeroOrden, nombreCliente, apellidoCliente, telefono, tipoEquipo, marca, modelo, numeroSerie, fallaReportada, observacionesPublicas, observacionesPrivadas, presupuesto, importeFinal, fechaIngreso, estadoActual) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlEstado = "INSERT INTO HistorialEstados(orden_id, estado, fecha) VALUES(?,?,?)";

        Connection conn = null;
        PreparedStatement pstmtOrden = null;
        PreparedStatement pstmtEstado = null;
        ResultSet generatedKeys = null;

        try {
            conn = ConexionDB.conectar();
            conn.setAutoCommit(false);

            pstmtOrden = conn.prepareStatement(sqlOrden, Statement.RETURN_GENERATED_KEYS);
            pstmtOrden.setString(1, orden.getNumeroOrden());
            pstmtOrden.setString(2, orden.getNombreCliente());
            pstmtOrden.setString(3, orden.getApellidoCliente());
            pstmtOrden.setString(4, orden.getTelefono());
            pstmtOrden.setString(5, orden.getTipoEquipo());
            pstmtOrden.setString(6, orden.getMarca());
            pstmtOrden.setString(7, orden.getModelo());
            pstmtOrden.setString(8, orden.getNumeroSerie());
            pstmtOrden.setString(9, orden.getFallaReportada());
            pstmtOrden.setString(10, orden.getObservacionesPublicas());
            pstmtOrden.setString(11, orden.getObservacionesPrivadas());
            pstmtOrden.setDouble(12, orden.getPresupuesto());
            pstmtOrden.setDouble(13, orden.getImporteFinal());
            pstmtOrden.setString(14, orden.getFechaIngreso());
            pstmtOrden.setString(15, orden.getEstadoActual());
            pstmtOrden.executeUpdate();

            generatedKeys = pstmtOrden.getGeneratedKeys();
            if (generatedKeys.next()) {
                int ordenId = generatedKeys.getInt(1);
                pstmtEstado = conn.prepareStatement(sqlEstado);
                pstmtEstado.setInt(1, ordenId);
                pstmtEstado.setString(2, orden.getEstadoActual());
                pstmtEstado.setString(3, orden.getFechaIngreso());
                pstmtEstado.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            System.out.println("Error al agregar orden: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmtOrden != null) pstmtOrden.close();
                if (pstmtEstado != null) pstmtEstado.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void actualizarOrden(Orden orden) {
        String sql = "UPDATE Ordenes SET numeroOrden = ?, nombreCliente = ?, apellidoCliente = ?, telefono = ?, tipoEquipo = ?, marca = ?, modelo = ?, numeroSerie = ?, fallaReportada = ?, observacionesPublicas = ?, observacionesPrivadas = ?, presupuesto = ?, importeFinal = ?, fechaIngreso = ?, estadoActual = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, orden.getNumeroOrden());
            pstmt.setString(2, orden.getNombreCliente());
            pstmt.setString(3, orden.getApellidoCliente());
            pstmt.setString(4, orden.getTelefono());
            pstmt.setString(5, orden.getTipoEquipo());
            pstmt.setString(6, orden.getMarca());
            pstmt.setString(7, orden.getModelo());
            pstmt.setString(8, orden.getNumeroSerie());
            pstmt.setString(9, orden.getFallaReportada());
            pstmt.setString(10, orden.getObservacionesPublicas());
            pstmt.setString(11, orden.getObservacionesPrivadas());
            pstmt.setDouble(12, orden.getPresupuesto());
            pstmt.setDouble(13, orden.getImporteFinal());
            pstmt.setString(14, orden.getFechaIngreso());
            pstmt.setString(15, orden.getEstadoActual());
            pstmt.setInt(16, orden.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar orden: " + e.getMessage());
        }
    }

    public void agregarEstadoHistorial(int ordenId, String estado, String fecha) {
        String sql = "INSERT INTO HistorialEstados(orden_id, estado, fecha) VALUES(?,?,?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ordenId);
            pstmt.setString(2, estado);
            pstmt.setString(3, fecha);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al agregar estado: " + e.getMessage());
        }
    }

    public List<HistorialEstado> getHistorialDeOrden(int ordenId) {
        List<HistorialEstado> historial = new ArrayList<>();
        String sql = "SELECT * FROM HistorialEstados WHERE orden_id = ? ORDER BY fecha ASC";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ordenId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                historial.add(new HistorialEstado(rs.getString("estado"), rs.getString("fecha")));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener historial: " + e.getMessage());
        }
        return historial;
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
                orden.setNumeroOrden(rs.getString("numeroOrden"));
                orden.setNombreCliente(rs.getString("nombreCliente"));
                orden.setApellidoCliente(rs.getString("apellidoCliente"));
                orden.setTelefono(rs.getString("telefono"));
                orden.setTipoEquipo(rs.getString("tipoEquipo"));
                orden.setMarca(rs.getString("marca"));
                orden.setModelo(rs.getString("modelo"));
                orden.setNumeroSerie(rs.getString("numeroSerie"));
                orden.setFallaReportada(rs.getString("fallaReportada"));
                orden.setObservacionesPublicas(rs.getString("observacionesPublicas"));
                orden.setObservacionesPrivadas(rs.getString("observacionesPrivadas"));
                orden.setPresupuesto(rs.getDouble("presupuesto"));
                orden.setImporteFinal(rs.getDouble("importeFinal"));
                orden.setFechaIngreso(rs.getString("fechaIngreso"));
                orden.setEstadoActual(rs.getString("estadoActual"));
                ordenes.add(orden);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener órdenes: " + e.getMessage());
        }
        return ordenes;
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