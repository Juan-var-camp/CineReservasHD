package view;

import controladores.ControladorSala;
import model.Sala;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ConfiguracionSalaPanel extends JPanel {

    private final int MAX_FILAS = 30;
    private final int MAX_COLUMNAS = 30;
    
    private static final Color COLOR_DISPONIBLE = new Color(220, 220, 220);//Gris
    private static final Color COLOR_OCUPADA = new Color(255, 90, 90);
    private static final Color COLOR_HOVER = new Color(180, 180, 255);

    private JSpinner spinnerFilas;
    private JSpinner spinnerColumnas;
    private JButton[][] botonesSillas;
    private JPanel panelSillas;
    private JButton btnGuardar, btnLimpiar, btnEliminar;
    private JTextField txtNombreSala;
    private JComboBox<String> comboSalas;

    private boolean arrastrando = false;
    private boolean activar = true;

    private final ControladorSala controladorSala = new ControladorSala();

    public ConfiguracionSalaPanel() {
        setLayout(new BorderLayout());
        inicializarComponentes();
        cargarSalasEnCombo();
    }

    private void inicializarComponentes() {
        // --- Panel superior ---
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));

        comboSalas = new JComboBox<>();
        comboSalas.setPreferredSize(new Dimension(150, 25));

        txtNombreSala = new JTextField(15);
        spinnerFilas = new JSpinner(new SpinnerNumberModel(10, 1, MAX_FILAS, 1));
        spinnerColumnas = new JSpinner(new SpinnerNumberModel(10, 1, MAX_COLUMNAS, 1));
        JButton btnGenerar = new JButton("Generar");

        panelTop.add(new JLabel("Salas:"));
        panelTop.add(comboSalas);
        panelTop.add(new JLabel("Nombre:"));
        panelTop.add(txtNombreSala);
        panelTop.add(new JLabel("Filas:"));
        panelTop.add(spinnerFilas);
        panelTop.add(new JLabel("Columnas:"));
        panelTop.add(spinnerColumnas);
        panelTop.add(btnGenerar);

        add(panelTop, BorderLayout.NORTH);

        // --- Panel central ---
        panelSillas = new JPanel();
        panelSillas.setLayout(new GridLayout(10, 10, 3, 3));
        botonesSillas = new JButton[MAX_FILAS][MAX_COLUMNAS];
        generarSillas(10, 10);

        JScrollPane scroll = new JScrollPane(panelSillas);
        add(scroll, BorderLayout.CENTER);

        // --- Panel inferior ---
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");
        btnEliminar = new JButton("Eliminar");

        panelBottom.add(btnEliminar);
        panelBottom.add(btnLimpiar);
        panelBottom.add(btnGuardar);
        add(panelBottom, BorderLayout.SOUTH);

        // --- Listeners ---
        btnGenerar.addActionListener(e -> {
            int filas = (int) spinnerFilas.getValue();
            int columnas = (int) spinnerColumnas.getValue();
            generarSillas(filas, columnas);
        });

        btnLimpiar.addActionListener(e -> limpiarSala());

        btnGuardar.addActionListener(e -> guardarSala());

        btnEliminar.addActionListener(e -> eliminarSala());

        comboSalas.addActionListener(e -> {
            String seleccion = (String) comboSalas.getSelectedItem();
            if (seleccion != null && !seleccion.equals("Nueva sala...")) {
                cargarSalaSeleccionada(seleccion);
            }
        });
    }

    // --- Genera los botones de sillas ---
    private void generarSillas(int filas, int columnas) {
        panelSillas.removeAll();
        panelSillas.setLayout(new GridLayout(filas, columnas, 3, 3));

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JButton btn = new JButton();
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setFocusPainted(false);
                btn.setToolTipText("Fila " + (i + 1) + ", Columna " + (j + 1));

                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        arrastrando = true;
                        activar = !btn.isEnabled();
                        cambiarEstado(btn, activar);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (btn.isEnabled()) btn.setBackground(COLOR_DISPONIBLE);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        arrastrando = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (arrastrando) cambiarEstado(btn, activar);
                        else if (btn.isEnabled()) btn.setBackground(COLOR_HOVER);
                    }
                });

                botonesSillas[i][j] = btn;
                panelSillas.add(btn);
            }
        }

        panelSillas.revalidate();
        panelSillas.repaint();
    }

    private void cambiarEstado(JButton btn, boolean activar) {
        btn.setEnabled(activar);
        btn.setBackground(activar ? COLOR_DISPONIBLE : COLOR_OCUPADA);
        btn.setText(activar ? "" : "X");
    }

    private void limpiarSala() {
        for (Component c : panelSillas.getComponents()) {
            if (c instanceof JButton btn) {
                btn.setEnabled(true);
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setText("");
            }
        }
    }

    private void guardarSala() {
        String nombre = txtNombreSala.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para la sala.");
            return;
        }
        
        int filas = (int) spinnerFilas.getValue();
        int columnas = (int) spinnerColumnas.getValue();

        Sala sala = Sala.desdeBotones(nombre, botonesSillas, filas, columnas);

        if (controladorSala.guardarSala(sala)) {
            JOptionPane.showMessageDialog(this, "Sala guardada correctamente.");
            cargarSalasEnCombo();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar la sala.");
        }
    }

    private void eliminarSala() {
        String nombre = (String) comboSalas.getSelectedItem();
        if (nombre == null || nombre.equals("Nueva sala...")) {
            JOptionPane.showMessageDialog(this, "Seleccione una sala existente para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea eliminar la sala \"" + nombre + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controladorSala.eliminarSala(nombre)) {
                JOptionPane.showMessageDialog(this, "Sala eliminada.");
                cargarSalasEnCombo();
                limpiarSala();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la sala.");
            }
        }
    }

    private void cargarSalasEnCombo() {
        comboSalas.removeAllItems();
        comboSalas.addItem("Nueva sala...");

        List<Sala> salas = controladorSala.listarSalas();
        for (Sala s : salas) {
            comboSalas.addItem(s.getNombre());
        }
    }

    private void cargarSalaSeleccionada(String nombre) {
        Sala sala = controladorSala.cargarSala(nombre);
        if (sala == null) return;

        txtNombreSala.setText(sala.getNombre());
        spinnerFilas.setValue(sala.getFilas());
        spinnerColumnas.setValue(sala.getColumnas());

        generarSillas(sala.getFilas(), sala.getColumnas());
        boolean[][] estado = sala.getEstadoSillas();

        for (int i = 0; i < sala.getFilas(); i++) {
            for (int j = 0; j < sala.getColumnas(); j++) {
                cambiarEstado(botonesSillas[i][j], estado[i][j]);
            }
        }
    }

    private boolean[][] getEstadoSillas(int filas, int columnas) {
        boolean[][] estado = new boolean[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (botonesSillas[i][j] != null) {
                    estado[i][j] = botonesSillas[i][j].isEnabled();
                }
            }
        }
        return estado;
    }
}
