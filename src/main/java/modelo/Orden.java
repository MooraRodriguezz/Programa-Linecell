package modelo;

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

    private double presupuesto;
    private double importeFinal;

    private String fechaIngreso;
    private String estadoActual;

    public Orden() {
    }

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

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public double getImporteFinal() {
        return importeFinal;
    }

    public void setImporteFinal(double importeFinal) {
        this.importeFinal = importeFinal;
    }

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