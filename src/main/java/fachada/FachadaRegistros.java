package fachada;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Material;
import modelo.ValidacionException;
import persistencia.PersistenciaRegistros;

public class FachadaRegistros {
    private final PersistenciaRegistros persistencia;
    private List<Material> registros;
    private boolean cargaCorrecta;
    private boolean cambiosSinGuardar;

    public FachadaRegistros(PersistenciaRegistros persistencia) {
        this.persistencia = persistencia;
        registros = new ArrayList<>();
    }

    public void cargarDatos() throws IOException {
        cargaCorrecta = false;
        registros = persistencia.leer();
        cargaCorrecta = true;
        cambiosSinGuardar = false;
    }

    public List<Material> listarRegistros() {
        return new ArrayList<>(registros);
    }

    /** Devuelve una copia del material con ese identificador, o null si no existe. */
    public Material buscarPorIdentificador(int identificador) {
        int posicion = posicionDe(identificador);
        if (posicion == -1) {
            return null;
        }
        return new Material(identificador, registros.get(posicion));
    }

    /** Valida y agrega un material nuevo. Devuelve el identificador asignado. */
    public int registrar(Material datos) throws ValidacionException, IOException {
        verificarCarga();
        datos.validar();
        int nuevoIdentificador = siguienteIdentificador();
        registros.add(new Material(nuevoIdentificador, datos));
        guardarTrasCambio();
        return nuevoIdentificador;
    }

    /** Reemplaza el material que tiene el mismo identificador, sin duplicarlo. */
    public void modificar(Material datos) throws ValidacionException, IOException {
        verificarCarga();
        int posicion = posicionDe(datos.getIdentificador());
        if (posicion == -1) {
            throw new ValidacionException("Seleccione en la tabla el material que desea modificar.");
        }
        datos.validar();
        registros.set(posicion, new Material(datos.getIdentificador(), datos));
        guardarTrasCambio();
    }

    public void eliminar(int identificador) throws ValidacionException, IOException {
        verificarCarga();
        int posicion = posicionDe(identificador);
        if (posicion == -1) {
            throw new ValidacionException("Seleccione en la tabla el material que desea eliminar.");
        }
        registros.get(posicion).verificarQueSePuedeEliminar();
        registros.remove(posicion);
        guardarTrasCambio();
    }

    /** Devuelve los materiales que contienen el texto en el título o el autor y son del tipo pedido. */
    public List<Material> buscar(String texto, String tipo) {
        String textoBuscado = texto == null ? "" : texto.trim().toLowerCase();
        List<Material> resultado = new ArrayList<>();
        for (Material material : registros) {
            boolean coincideTexto = textoBuscado.isEmpty()
                    || material.getTitulo().toLowerCase().contains(textoBuscado)
                    || material.getAutor().toLowerCase().contains(textoBuscado);
            boolean coincideTipo = tipo == null || tipo.equals(material.getTipo());
            if (coincideTexto && coincideTipo) {
                resultado.add(material);
            }
        }
        return resultado;
    }

    public void guardarDatos() throws IOException {
        // No reemplazar un archivo que no se pudo recuperar.
        verificarCarga();
        persistencia.escribir(registros);
        cambiosSinGuardar = false;
    }

    public boolean hayCambiosSinGuardar() {
        return cambiosSinGuardar;
    }

    private void guardarTrasCambio() throws IOException {
        // Si la escritura falla, el cambio queda en memoria y se puede reintentar con Guardar.
        cambiosSinGuardar = true;
        guardarDatos();
    }

    private void verificarCarga() throws IOException {
        if (!cargaCorrecta) {
            throw new IOException("Los datos no se cargaron correctamente, por eso no se permiten cambios.");
        }
    }

    private int siguienteIdentificador() {
        int mayor = 0;
        for (Material material : registros) {
            if (material.getIdentificador() > mayor) {
                mayor = material.getIdentificador();
            }
        }
        return mayor + 1;
    }

    private int posicionDe(int identificador) {
        for (int i = 0; i < registros.size(); i++) {
            if (registros.get(i).getIdentificador() == identificador) {
                return i;
            }
        }
        return -1;
    }
}
