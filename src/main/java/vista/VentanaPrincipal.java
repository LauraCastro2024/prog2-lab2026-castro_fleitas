package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.ParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import modelo.EstadoConservacion;
import modelo.Material;

public class VentanaPrincipal extends JFrame {
    // Componentes de la base, adaptados a la biblioteca.
    private final JTextField campoIdentificador = new JTextField(10);
    private final JTextField campoTitulo = new JTextField(25);
    private final JTextField campoAutor = new JTextField(25);
    private final JComboBox<String> comboTipo = new JComboBox<>(Material.TIPOS);
    private final JCheckBox casillaSoloSala =
            new JCheckBox("Solo para consulta en sala", false);

    // Componente nuevo 1: JSpinner para las cantidades de ejemplares.
    private final JSpinner spinnerTotales = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JSpinner spinnerPrestados = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));

    // Componente nuevo 2: JRadioButton para el estado de conservación.
    private final JRadioButton radioBueno = new JRadioButton("Bueno", true);
    private final JRadioButton radioRegular = new JRadioButton("Regular");
    private final JRadioButton radioDeteriorado = new JRadioButton("Deteriorado");

    // Componente nuevo 3: JTextArea para las observaciones.
    private final JTextArea areaObservaciones = new JTextArea(6, 28);

    // Búsqueda.
    private final JTextField campoBusqueda = new JTextField(20);
    private final JComboBox<String> comboFiltroTipo = new JComboBox<>();

    private final JButton botonRegistrar = new JButton("Registrar");
    private final JButton botonModificar = new JButton("Modificar");
    private final JButton botonEliminar = new JButton("Eliminar");
    private final JButton botonBuscar = new JButton("Buscar");
    private final JButton botonLimpiar = new JButton("Limpiar");
    private final JButton botonGuardar = new JButton("Guardar");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[] {"Id", "Título", "Autor / editorial", "Tipo", "Ejemplares",
                "Prestados", "Disponibles", "Estado", "Préstamo"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tablaRegistros = new JTable(modeloTabla);

    public VentanaPrincipal() {
        super("Biblioteca del liceo - Catálogo de materiales");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // El identificador lo asigna el sistema: el usuario no lo escribe.
        campoIdentificador.setEditable(false);

        // El ButtonGroup hace que solo se pueda elegir un estado a la vez.
        ButtonGroup grupoEstado = new ButtonGroup();
        grupoEstado.add(radioBueno);
        grupoEstado.add(radioRegular);
        grupoEstado.add(radioDeteriorado);
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelEstado.add(radioBueno);
        panelEstado.add(radioRegular);
        panelEstado.add(radioDeteriorado);

        JPanel formulario = new JPanel(new GridLayout(8, 2, 5, 5));
        formulario.add(new JLabel("Identificador:"));
        formulario.add(campoIdentificador);
        formulario.add(new JLabel("Título (obligatorio):"));
        formulario.add(campoTitulo);
        formulario.add(new JLabel("Autor / editorial:"));
        formulario.add(campoAutor);
        formulario.add(new JLabel("Tipo de material:"));
        formulario.add(comboTipo);
        formulario.add(new JLabel("Ejemplares en la biblioteca:"));
        formulario.add(spinnerTotales);
        formulario.add(new JLabel("Ejemplares prestados:"));
        formulario.add(spinnerPrestados);
        formulario.add(new JLabel("Estado de conservación:"));
        formulario.add(panelEstado);
        formulario.add(new JLabel("Préstamo:"));
        formulario.add(casillaSoloSala);

        areaObservaciones.setLineWrap(true);
        areaObservaciones.setWrapStyleWord(true);
        JPanel panelObservaciones = new JPanel(new BorderLayout());
        panelObservaciones.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        panelObservaciones.add(new JScrollPane(areaObservaciones), BorderLayout.CENTER);

        JPanel panelDatos = new JPanel(new BorderLayout(10, 0));
        panelDatos.add(formulario, BorderLayout.CENTER);
        panelDatos.add(panelObservaciones, BorderLayout.EAST);

        JPanel acciones = new JPanel(new FlowLayout());
        acciones.add(botonRegistrar);
        acciones.add(botonModificar);
        acciones.add(botonEliminar);
        acciones.add(botonLimpiar);
        acciones.add(botonGuardar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        panelSuperior.add(panelDatos, BorderLayout.CENTER);
        panelSuperior.add(acciones, BorderLayout.SOUTH);

        comboFiltroTipo.addItem("Todos los tipos");
        for (String tipo : Material.TIPOS) {
            comboFiltroTipo.addItem(tipo);
        }
        JPanel busqueda = new JPanel(new FlowLayout());
        busqueda.add(new JLabel("Buscar por título o autor:"));
        busqueda.add(campoBusqueda);
        busqueda.add(new JLabel("Tipo:"));
        busqueda.add(comboFiltroTipo);
        busqueda.add(botonBuscar);

        tablaRegistros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tablaRegistros), BorderLayout.CENTER);
        add(busqueda, BorderLayout.SOUTH);
        setSize(1050, 680);
        setLocationRelativeTo(null);
    }

    public void mostrarRegistros(List<Material> registros) {
        modeloTabla.setRowCount(0);
        for (Material registro : registros) {
            modeloTabla.addRow(new Object[] {
                registro.getIdentificador(),
                registro.getTitulo(),
                registro.getAutor(),
                registro.getTipo(),
                registro.getEjemplaresTotales(),
                registro.getEjemplaresPrestados(),
                registro.getEjemplaresDisponibles(),
                registro.getEstado().getDescripcion(),
                registro.isSoloEnSala() ? "Solo en sala" : "A domicilio"
            });
        }
    }

    /** Arma un Material con lo que hay escrito en el formulario. No lo valida. */
    public Material obtenerDatosFormulario() {
        confirmarEdicion(spinnerTotales);
        confirmarEdicion(spinnerPrestados);
        return new Material(
                getIdentificadorFormulario(),
                campoTitulo.getText().trim(),
                campoAutor.getText().trim(),
                (String) comboTipo.getSelectedItem(),
                (Integer) spinnerTotales.getValue(),
                (Integer) spinnerPrestados.getValue(),
                getEstadoSeleccionado(),
                casillaSoloSala.isSelected(),
                areaObservaciones.getText().trim());
    }

    /** Muestra en el formulario los datos de un material. */
    public void mostrarMaterial(Material material) {
        campoIdentificador.setText(String.valueOf(material.getIdentificador()));
        campoTitulo.setText(material.getTitulo());
        campoAutor.setText(material.getAutor());
        comboTipo.setSelectedItem(material.getTipo());
        spinnerTotales.setValue(material.getEjemplaresTotales());
        spinnerPrestados.setValue(material.getEjemplaresPrestados());
        if (material.getEstado() == EstadoConservacion.REGULAR) {
            radioRegular.setSelected(true);
        } else if (material.getEstado() == EstadoConservacion.DETERIORADO) {
            radioDeteriorado.setSelected(true);
        } else {
            radioBueno.setSelected(true);
        }
        casillaSoloSala.setSelected(material.isSoloEnSala());
        areaObservaciones.setText(material.getObservaciones());
    }

    /** Deja el formulario vacío, listo para un material nuevo. */
    public void limpiarFormulario() {
        campoIdentificador.setText("");
        campoTitulo.setText("");
        campoAutor.setText("");
        comboTipo.setSelectedIndex(0);
        spinnerTotales.setValue(1);
        spinnerPrestados.setValue(0);
        radioBueno.setSelected(true);
        casillaSoloSala.setSelected(false);
        areaObservaciones.setText("");
        tablaRegistros.clearSelection();
    }

    /** Identificador del material cargado en el formulario, o -1 si no hay ninguno. */
    public int getIdentificadorFormulario() {
        String texto = campoIdentificador.getText().trim();
        if (texto.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(texto);
    }

    /** Identificador de la fila seleccionada en la tabla, o -1 si no hay ninguna. */
    public int getIdentificadorSeleccionado() {
        int fila = tablaRegistros.getSelectedRow();
        if (fila == -1) {
            return -1;
        }
        return (Integer) modeloTabla.getValueAt(fila, 0);
    }

    public String getTextoBusqueda() {
        return campoBusqueda.getText();
    }

    /** Tipo elegido para filtrar, o null si se eligió "Todos los tipos". */
    public String getTipoFiltro() {
        if (comboFiltroTipo.getSelectedIndex() == 0) {
            return null;
        }
        return (String) comboFiltroTipo.getSelectedItem();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Biblioteca", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Pregunta Sí / No. Devuelve true si el usuario elige Sí. */
    public boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    private EstadoConservacion getEstadoSeleccionado() {
        if (radioRegular.isSelected()) {
            return EstadoConservacion.REGULAR;
        }
        if (radioDeteriorado.isSelected()) {
            return EstadoConservacion.DETERIORADO;
        }
        return EstadoConservacion.BUENO;
    }

    /** Toma el número escrito a mano en un JSpinner aunque no se haya apretado Enter. */
    private void confirmarEdicion(JSpinner spinner) {
        try {
            spinner.commitEdit();
        } catch (ParseException e) {
            // Si lo escrito no es un número, el spinner conserva el último valor válido.
        }
    }

    public JTextField getCampoBusqueda() {
        return campoBusqueda;
    }

    public JComboBox<String> getComboFiltroTipo() {
        return comboFiltroTipo;
    }

    public JTable getTablaRegistros() {
        return tablaRegistros;
    }

    public JButton getBotonRegistrar() {
        return botonRegistrar;
    }

    public JButton getBotonModificar() {
        return botonModificar;
    }

    public JButton getBotonEliminar() {
        return botonEliminar;
    }

    public JButton getBotonBuscar() {
        return botonBuscar;
    }

    public JButton getBotonLimpiar() {
        return botonLimpiar;
    }

    public JButton getBotonGuardar() {
        return botonGuardar;
    }
}