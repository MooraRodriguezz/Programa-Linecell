package BDD;

import modelo.HistorialEstado;
import modelo.Orden;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // <-- IMPORTANTE

public class OrdenDAO {

    public void inicializarBaseDeDatos() {
        // (Este método se mantiene igual, no hace falta cambiarlo)
        try (Connection conn = ConexionDB.conectar()) {
            if (conn != null) {
                System.out.println("Conexión a la base de datos verificada.");
            } else {
                System.err.println("Fallo al verificar la conexión a la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
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
            if (conn == null) throw new SQLException("No se pudo conectar a la base de datos.");
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

            // CAMBIADO: Se pasa BigDecimal directamente
            pstmtOrden.setBigDecimal(12, orden.getPresupuesto());
            pstmtOrden.setBigDecimal(13, orden.getImporteFinal());

            // Convertir fecha String a java.sql.Date
            pstmtOrden.setDate(14, Date.valueOf(LocalDate.parse(orden.getFechaIngreso())));
            pstmtOrden.setString(15, orden.getEstadoActual());

            int affectedRows = pstmtOrden.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La inserción de la orden falló, ninguna fila afectada.");
            }

            generatedKeys = pstmtOrden.getGeneratedKeys();
            if (generatedKeys.next()) {
                int ordenId = generatedKeys.getInt(1);
                orden.setId(ordenId);

                pstmtEstado = conn.prepareStatement(sqlEstado);
                pstmtEstado.setInt(1, ordenId);
                pstmtEstado.setString(2, orden.getEstadoActual());
                pstmtEstado.setDate(3, Date.valueOf(LocalDate.parse(orden.getFechaIngreso())));
                pstmtEstado.executeUpdate();
            } else {
                throw new SQLException("La inserción de la orden falló, no se obtuvo ID.");
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Error al agregar orden: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
        } finally {
            try { if (generatedKeys != null) generatedKeys.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmtEstado != null) pstmtEstado.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmtOrden != null) pstmtOrden.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void actualizarOrden(Orden orden) {
        String sql = "UPDATE Ordenes SET numeroOrden = ?, nombreCliente = ?, apellidoCliente = ?, telefono = ?, tipoEquipo = ?, marca = ?, modelo = ?, numeroSerie = ?, fallaReportada = ?, observacionesPublicas = ?, observacionesPrivadas = ?, presupuesto = ?, importeFinal = ?, fechaIngreso = ?, estadoActual = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("No se pudo conectar a la base de datos.");

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

            // CAMBIADO: Se pasa BigDecimal directamente
            pstmt.setBigDecimal(12, orden.getPresupuesto());
            pstmt.setBigDecimal(13, orden.getImporteFinal());

            pstmt.setDate(14, Date.valueOf(LocalDate.parse(orden.getFechaIngreso())));
            pstmt.setString(15, orden.getEstadoActual());
            pstmt.setInt(16, orden.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar orden: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void agregarEstadoHistorial(int ordenId, String estado, String fecha) {
        String sql = "INSERT INTO HistorialEstados(orden_id, estado, fecha) VALUES(?,?,?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) throw new SQLException("No se pudo conectar a la base de datos.");
            pstmt.setInt(1, ordenId);
            pstmt.setString(2, estado);
            pstmt.setDate(3, Date.valueOf(LocalDate.parse(fecha)));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar estado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<HistorialEstado> getHistorialDeOrden(int ordenId) {
        List<HistorialEstado> historial = new ArrayList<>();
        String sql = "SELECT * FROM HistorialEstados WHERE orden_id = ? ORDER BY fecha ASC, id ASC";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) throw new SQLException("No se pudo conectar a la base de datos.");
            pstmt.setInt(1, ordenId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Date dbDate = rs.getDate("fecha");
                String fechaFormateada = (dbDate != null) ? dbDate.toLocalDate().toString() : "Fecha inválida";
                historial.add(new HistorialEstado(rs.getString("estado"), fechaFormateada));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
            e.printStackTrace();
        }
        return historial;
    }

    public List<Orden> getTodasLasOrdenes() {
        List<Orden> ordenes = new ArrayList<>();
        String sql = "SELECT * FROM Ordenes ORDER BY id DESC";

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) throw new SQLException("No se pudo conectar a la base de datos.");

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

                // CAMBIADO: Se lee BigDecimal directamente
                orden.setPresupuesto(rs.getBigDecimal("presupuesto"));
                orden.setImporteFinal(rs.getBigDecimal("importeFinal"));

                Date dbDate = rs.getDate("fechaIngreso");
                orden.setFechaIngreso((dbDate != null) ? dbDate.toLocalDate().toString() : "");
                orden.setEstadoActual(rs.getString("estadoActual"));
                ordenes.add(orden);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener órdenes: " + e.getMessage());
            e.printStackTrace();
        }
        return ordenes;
    }

    public void eliminarOrden(int id) {
        String sql = "DELETE FROM Ordenes WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) throw new SQLException("No se pudo conectar a la base de datos.");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar orden: " + e.getMessage());
            e.printStackTrace();
        }
    }
}