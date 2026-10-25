package modelo;

public enum EstadoConservacion {
    BUENO("Bueno"),
    REGULAR("Regular"),
    DETERIORADO("Deteriorado");

    private final String descripcion;

    EstadoConservacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}