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