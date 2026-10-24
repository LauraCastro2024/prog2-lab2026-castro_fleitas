package controlador;

import fachada.FachadaRegistros;
import java.io.IOException;
import vista.VentanaPrincipal;

public class ControladorRegistros {
    private final VentanaPrincipal vista;
    private final FachadaRegistros fachada;

    public ControladorRegistros(VentanaPrincipal vista, FachadaRegistros fachada) {
        this.vista = vista;
        this.fachada = fachada;
        vista.getBotonRegistrar().addActionListener(evento -> avisarPendiente("Registrar"));
        vista.getBotonModificar().addActionListener(evento -> avisarPendiente("Modificar"));
        vista.getBotonEliminar().addActionListener(evento -> avisarPendiente("Eliminar"));
        vista.getBotonBuscar().addActionListener(evento -> avisarPendiente("Buscar"));
        vista.getBotonLimpiar().addActionListener(evento -> avisarPendiente("Limpiar"));
        vista.getBotonGuardar().addActionListener(evento -> avisarPendiente("Guardar"));
    }

    public void iniciar() {
        vista.setVisible(true);
        try {
            fachada.cargarDatos();
            vista.mostrarRegistros(fachada.listarRegistros());
        } catch (IOException e) {
            vista.mostrarError("No se pudieron cargar los datos: " + e.getMessage()
                    + "\nEl archivo no fue modificado.");
        }
    }

    private void avisarPendiente(String accion) {
        vista.mostrarMensaje(accion + ": funcionalidad pendiente de implementar por el equipo.");
    }
}
