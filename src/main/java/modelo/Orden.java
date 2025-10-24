package modelo;

import java.math.BigDecimal; // <-- IMPORTANTE

public class Orden {

    private int id;
    private String numeroOrden;
    private String nombreCliente;
    private String apellidoCliente;
    private String telefono;

    private String tipoEquipo;
    private String marca;
    private String modelo;
    private String numeroSerie;

    private String fallaReportada;
    private String observacionesPublicas;
    private String observacionesPrivadas;

    // CAMBIADO de double a BigDecimal
    private BigDecimal presupuesto;
    private BigDecimal importeFinal;

    private String fechaIngreso;
    private String estadoActual;

    public Orden() {
        // Inicializamos los valores para evitar errores
        this.presupuesto = BigDecimal.ZERO;
        this.importeFinal = BigDecimal.ZERO;
    }

    // --- GETTERS Y SETTERS ---
    // (El resto es igual)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getFallaReportada() {
        return fallaReportada;
    }

    public void setFallaReportada(String fallaReportada) {
        this.fallaReportada = fallaReportada;
    }

    public String getObservacionesPublicas() {
        return observacionesPublicas;
    }

    public void setObservacionesPublicas(String observacionesPublicas) {
        this.observacionesPublicas = observacionesPublicas;
    }

    public String getObservacionesPrivadas() {
        return observacionesPrivadas;
    }

    public void setObservacionesPrivadas(String observacionesPrivadas) {
        this.observacionesPrivadas = observacionesPrivadas;
    }

    // --- GETTERS Y SETTERS CAMBIADOS ---

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public BigDecimal getImporteFinal() {
        return importeFinal;
    }

    public void setImporteFinal(BigDecimal importeFinal) {
        this.importeFinal = importeFinal;
    }

    // --- FIN DE GETTERS Y SETTERS CAMBIADOS ---

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }
}