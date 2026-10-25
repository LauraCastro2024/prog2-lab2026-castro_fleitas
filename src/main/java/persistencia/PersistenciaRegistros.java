package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.Material;
import modelo.ValidacionException;

public class PersistenciaRegistros {
    private final Path archivo;
    private final Gson gson;

    public PersistenciaRegistros(Path archivo) {
        this.archivo = archivo;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setStrictness(Strictness.STRICT)
                .create();
    }

    public List<Material> leer() throws IOException {
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            Material[] registros = gson.fromJson(lector, Material[].class);
            if (registros == null) {
                throw new IOException("El archivo debe contener un arreglo JSON, aunque este vacio.");
            }
            for (Material registro : registros) {
                if (registro == null || registro.getTitulo() == null || registro.getTipo() == null
        || registro.getEstado() == null) {
                    throw new IOException("El archivo contiene un registro incompleto.");
                }
                 try {
                    registro.validar();
                } catch (ValidacionException e) {
                    throw new IOException("El material " + registro.getIdentificador()
                            + " no es válido: " + e.getMessage());
                }   
            }
            return new ArrayList<>(Arrays.asList(registros));
        } catch (NoSuchFileException e) {
            return new ArrayList<>();
        } catch (JsonParseException e) {
            throw new IOException("El archivo no contiene registros JSON validos.", e);
        }
    }

    public void escribir(List<Material> registros) throws IOException {
        Path carpeta = archivo.toAbsolutePath().getParent();
        Files.createDirectories(carpeta);
        try (Writer escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            gson.toJson(registros, escritor);
        }
    }
}
