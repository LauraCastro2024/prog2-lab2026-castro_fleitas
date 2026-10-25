package modelo;

public class Material {
    public static final String[] TIPOS = {
        "Libro", "Revista", "Diccionario", "Enciclopedia", "Mapa", "Material audiovisual"
    };

    private int identificador;
    private String titulo;
    private String autor;
    private String tipo;
    private int ejemplaresTotales;
    private int ejemplaresPrestados;
    private EstadoConservacion estado;
    private boolean soloEnSala;
    private String observaciones;

    public Material(int identificador, String titulo, String autor, String tipo,
            int ejemplaresTotales, int ejemplaresPrestados, EstadoConservacion estado,
            boolean soloEnSala, String observaciones) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.autor = autor;
        this.tipo = tipo;
        this.ejemplaresTotales = ejemplaresTotales;
        this.ejemplaresPrestados = ejemplaresPrestados;
        this.estado = estado;
        this.soloEnSala = soloEnSala;
        this.observaciones = observaciones;
    }
        public void validar() throws ValidacionException {
        // Validación 1: el título es obligatorio.
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidacionException("El título es obligatorio.");
        }
        if (tipo == null || !esTipoValido(tipo)) {
            throw new ValidacionException("Debe seleccionar un tipo de material válido.");
        }
        if (estado == null) {
            throw new ValidacionException("Debe indicar el estado de conservación.");
        }

        // Validación 2: las cantidades de ejemplares deben ser coherentes.
        if (ejemplaresTotales < 1) {
            throw new ValidacionException("La biblioteca debe tener al menos un ejemplar.");
        }
        if (ejemplaresPrestados < 0) {
            throw new ValidacionException("Los ejemplares prestados no pueden ser negativos.");
        }
        if (ejemplaresPrestados > ejemplaresTotales) {
            throw new ValidacionException("No puede haber más ejemplares prestados ("
                    + ejemplaresPrestados + ") que ejemplares en la biblioteca ("
                    + ejemplaresTotales + ").");
        }

        // Validación 3, propia del problema: un material deteriorado no se presta.
        if (estado == EstadoConservacion.DETERIORADO && !soloEnSala) {
            throw new ValidacionException("Un material deteriorado no se puede prestar a domicilio.\n"
                    + "Marque la casilla \"Solo para consulta en sala\".");
        }
        if (soloEnSala && ejemplaresPrestados > 0) {
            throw new ValidacionException("Un material solo para consulta en sala "
                    + "no puede tener ejemplares prestados.");
        }
    }

    public void verificarQueSePuedeEliminar() throws ValidacionException {
        if (ejemplaresPrestados > 0) {
            throw new ValidacionException("No se puede eliminar \"" + titulo + "\" porque tiene "
                    + ejemplaresPrestados + " ejemplar(es) prestado(s).");
        }
    }

    public static boolean esTipoValido(String tipo) {
        for (String tipoValido : TIPOS) {
            if (tipoValido.equals(tipo)) {
                return true;
            }
        }
        return false;
    }

    public int getEjemplaresDisponibles() {
        return ejemplaresTotales - ejemplaresPrestados;
    }

    public int getIdentificador() { return identificador; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor == null ? "" : autor; }
    public String getTipo() { return tipo; }
    public int getEjemplaresTotales() { return ejemplaresTotales; }
    public int getEjemplaresPrestados() { return ejemplaresPrestados; }
    public EstadoConservacion getEstado() { return estado; }
    public boolean isSoloEnSala() { return soloEnSala; }
    public String getObservaciones() { return observaciones == null ? "" : observaciones; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setEjemplaresTotales(int cantidad) { this.ejemplaresTotales = cantidad; }
    public void setEjemplaresPrestados(int cantidad) { this.ejemplaresPrestados = cantidad; }
    public void setEstado(EstadoConservacion estado) { this.estado = estado; }
    public void setSoloEnSala(boolean soloEnSala) { this.soloEnSala = soloEnSala; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}