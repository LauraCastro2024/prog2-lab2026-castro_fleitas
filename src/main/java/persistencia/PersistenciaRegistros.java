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
import modelo.Registro;

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

    public List<Registro> leer() throws IOException {
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            Registro[] registros = gson.fromJson(lector, Registro[].class);
            if (registros == null) {
                throw new IOException("El archivo debe contener un arreglo JSON, aunque este vacio.");
            }
            for (Registro registro : registros) {
                if (registro == null || registro.getNombre() == null || registro.getCategoria() == null) {
                    throw new IOException("El archivo contiene un registro incompleto.");
                }
            }
            return new ArrayList<>(Arrays.asList(registros));
        } catch (NoSuchFileException e) {
            return new ArrayList<>();
        } catch (JsonParseException e) {
            throw new IOException("El archivo no contiene registros JSON validos.", e);
        }
    }

    public void escribir(List<Registro> registros) throws IOException {
        Path carpeta = archivo.toAbsolutePath().getParent();
        Files.createDirectories(carpeta);
        try (Writer escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            gson.toJson(registros, escritor);
        }
    }
}
