package controlador;

import fachada.FachadaRegistros;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import modelo.Material;
import modelo.ValidacionException;
import vista.VentanaPrincipal;

public class ControladorRegistros {
    private final VentanaPrincipal vista;
    private final FachadaRegistros fachada;

    public ControladorRegistros(VentanaPrincipal vista, FachadaRegistros fachada) {
        this.vista = vista;
        this.fachada = fachada;
        // Cada botón queda conectado con el método que hace su acción.
        vista.getBotonRegistrar().addActionListener(evento -> registrar());
        vista.getBotonModificar().addActionListener(evento -> modificar());
        vista.getBotonEliminar().addActionListener(evento -> eliminar());
        vista.getBotonBuscar().addActionListener(evento -> actualizarListado());
        vista.getCampoBusqueda().addActionListener(evento -> actualizarListado());
        vista.getComboFiltroTipo().addActionListener(evento -> actualizarListado());
        vista.getBotonLimpiar().addActionListener(evento -> vista.limpiarFormulario());
        vista.getBotonGuardar().addActionListener(evento -> guardar());
        // Al elegir una fila de la tabla, se cargan sus datos en el formulario.
        vista.getTablaRegistros().getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
        // Al cerrar la ventana, se avisa si quedó algo sin guardar.
        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {
                salir();
            }
        });
    }

    public void iniciar() {
        vista.setVisible(true);
        try {
            fachada.cargarDatos();
            actualizarListado();
        } catch (IOException e) {
            vista.mostrarError("No se pudieron cargar los datos: " + e.getMessage()
                    + "\nEl archivo no fue modificado.");
        }
    }

    private void registrar() {
        try {
            int identificador = fachada.registrar(vista.obtenerDatosFormulario());
            actualizarListado();
            vista.limpiarFormulario();
            vista.mostrarMensaje("Material registrado con el identificador " + identificador + ".");
        } catch (ValidacionException e) {
            vista.mostrarError(e.getMessage());
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar: " + e.getMessage());
        }
    }

    private void modificar() {
        if (vista.getIdentificadorFormulario() == -1) {
            vista.mostrarError("Seleccione en la tabla el material que desea modificar.");
            return;
        }
        try {
            Material datos = vista.obtenerDatosFormulario();
            fachada.modificar(datos);
            actualizarListado();
            vista.mostrarMensaje("Material " + datos.getIdentificador() + " modificado.");
        } catch (ValidacionException e) {
            vista.mostrarError(e.getMessage());
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar: " + e.getMessage());
        }
    }

    private void eliminar() {
        Material material = fachada.buscarPorIdentificador(vista.getIdentificadorFormulario());
        if (material == null) {
            vista.mostrarError("Seleccione en la tabla el material que desea eliminar.");
            return;
        }
        if (!vista.confirmar("¿Eliminar del catálogo \"" + material.getTitulo() + "\"?")) {
            vista.mostrarMensaje("Eliminación cancelada. El material se conserva.");
            return;
        }
        try {
            fachada.eliminar(material.getIdentificador());
            actualizarListado();
            vista.limpiarFormulario();
            vista.mostrarMensaje("Material eliminado.");
        } catch (ValidacionException e) {
            vista.mostrarError(e.getMessage());
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar: " + e.getMessage());
        }
    }

    private void guardar() {
        try {
            fachada.guardarDatos();
            vista.mostrarMensaje("Catálogo guardado.");
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar: " + e.getMessage());
        }
    }

    /** Muestra en la tabla los materiales que cumplen la búsqueda actual. */
    private void actualizarListado() {
        vista.mostrarRegistros(fachada.buscar(vista.getTextoBusqueda(), vista.getTipoFiltro()));
    }

    private void cargarSeleccion() {
        int identificador = vista.getIdentificadorSeleccionado();
        if (identificador == -1) {
            return;
        }
        Material material = fachada.buscarPorIdentificador(identificador);
        if (material != null) {
            vista.mostrarMaterial(material);
        }
    }

    private void salir() {
        if (fachada.hayCambiosSinGuardar()
                && !vista.confirmar("Hay cambios que no se pudieron guardar. ¿Salir de todos modos?")) {
            return;
        }
        vista.dispose();
        System.exit(0);
    }
}