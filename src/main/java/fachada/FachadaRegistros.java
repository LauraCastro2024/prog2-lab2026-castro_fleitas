package fachada;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Registro;
import persistencia.PersistenciaRegistros;

public class FachadaRegistros {
    private final PersistenciaRegistros persistencia;
    private List<Registro> registros;
    private boolean cargaCorrecta;

    public FachadaRegistros(PersistenciaRegistros persistencia) {
        this.persistencia = persistencia;
        registros = new ArrayList<>();
    }

    public void cargarDatos() throws IOException {
        cargaCorrecta = false;
        registros = persistencia.leer();
        cargaCorrecta = true;
    }

    public List<Registro> listarRegistros() {
        return new ArrayList<>(registros);
    }

    public void guardarDatos() throws IOException {
        // No reemplazar un archivo que no se pudo recuperar.
        if (!cargaCorrecta) {
            throw new IOException("No se puede guardar sin haber cargado los datos correctamente.");
        }
        persistencia.escribir(registros);
    }
}
