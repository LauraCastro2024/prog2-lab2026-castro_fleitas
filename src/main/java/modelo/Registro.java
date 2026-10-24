package modelo;

public class Registro {
    private int identificador;
    private String nombre;
    private String categoria;
    private boolean activo;

    public Registro(int identificador, String nombre, String categoria, boolean activo) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.categoria = categoria;
        this.activo = activo;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
