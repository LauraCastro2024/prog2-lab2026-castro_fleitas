package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Material;

public class VentanaPrincipal extends JFrame {
    private final JTextField campoIdentificador = new JTextField(10);
    private final JTextField campoNombre = new JTextField(20);
    private final JComboBox<String> comboCategoria =
            new JComboBox<>(new String[] {"General", "Materiales", "Servicios"});
    private final JCheckBox casillaActivo = new JCheckBox("Activo", true);
    private final JTextField campoBusqueda = new JTextField(20);
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
        super("Gestor de registros - Prototipo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));
        formulario.add(new JLabel("Identificador:"));
        formulario.add(campoIdentificador);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Categoria:"));
        formulario.add(comboCategoria);
        formulario.add(new JLabel("Estado:"));
        formulario.add(casillaActivo);

        JPanel acciones = new JPanel(new FlowLayout());
        acciones.add(botonRegistrar);
        acciones.add(botonModificar);
        acciones.add(botonEliminar);
        acciones.add(botonLimpiar);
        acciones.add(botonGuardar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(formulario, BorderLayout.CENTER);
        panelSuperior.add(acciones, BorderLayout.SOUTH);

        JPanel busqueda = new JPanel(new FlowLayout());
        busqueda.add(new JLabel("Buscar:"));
        busqueda.add(campoBusqueda);
        busqueda.add(botonBuscar);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tablaRegistros), BorderLayout.CENTER);
        add(busqueda, BorderLayout.SOUTH);
        setSize(800, 450);
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

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Prototipo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JTextField getCampoIdentificador() {
        return campoIdentificador;
    }

    public JTextField getCampoNombre() {
        return campoNombre;
    }

    public JComboBox<String> getComboCategoria() {
        return comboCategoria;
    }

    public JCheckBox getCasillaActivo() {
        return casillaActivo;
    }

    public JTextField getCampoBusqueda() {
        return campoBusqueda;
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
