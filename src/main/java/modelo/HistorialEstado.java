package modelo;

public class HistorialEstado {

    private String estado;
    private String fecha;

    public HistorialEstado(String estado, String fecha) {
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return fecha + ": " + estado;
    }
}