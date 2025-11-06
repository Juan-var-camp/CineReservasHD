package view;

import controladores.ControladorSeleccionSillas;
import model.Sala;
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SeleccionSillasPanel extends JPanel {

    private JPanel panelPrincipal;
    private JPanel panelPantalla;
    private JPanel panelSillas;
    private JPanel panelInfo;
    private JLabel lblPantalla;
    private JLabel lblInfo;
    private JButton btnConfirmar;
    private Set<String> sillasSeleccionadas;
    private JButton[][] botonesSillas;
    private int filas;
    private int columnas;
    private ControladorSeleccionSillas controlador;

    public SeleccionSillasPanel(Sala sala, ControladorSeleccionSillas controlador) {
        
        this.filas = sala.getFilas();
        this.columnas = sala.getColumnas();
        this.controlador = controlador;
        this.sillasSeleccionadas = new HashSet<>();

        inicializarComponentes(sala);
        configurarPanel();
    }

    private void inicializarComponentes(Sala sala) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // --- Panel superior: Pantalla ---
        panelPantalla = new JPanel();
        panelPantalla.setBackground(new Color(0, 0, 139));
        panelPantalla.setPreferredSize(new Dimension(500, 60));
        panelPantalla.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        lblPantalla = new JLabel("Sala: " + sala.getNombre(), JLabel.CENTER);
        lblPantalla.setForeground(Color.WHITE);
        lblPantalla.setFont(new Font("Arial", Font.BOLD, 18));
        panelPantalla.add(lblPantalla);

        // --- Panel central: Sillas ---
        JPanel panelSillasCompleto = new JPanel(new BorderLayout());
        panelSillasCompleto.setBackground(new Color(240, 240, 240));
        panelSillasCompleto.setBorder(BorderFactory.createTitledBorder("Seleccione sus sillas"));

        panelSillas = new JPanel(new GridLayout(filas, columnas, 5, 5));
        panelSillas.setBackground(new Color(240, 240, 240));
        botonesSillas = new JButton[filas][columnas];

        boolean[][] estado = sala.getEstadoSillas();
        char letraFila = 'A';

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String idSilla = letraFila + "" + (j + 1);
                JButton btnSilla = createSeatButton(idSilla);

                // Si la silla está deshabilitada en la sala, no se puede seleccionar
                if (!estado[i][j]) {
                    btnSilla.setEnabled(false);
                    btnSilla.setBackground(Color.DARK_GRAY);
                    btnSilla.setForeground(Color.WHITE);
                    btnSilla.setToolTipText("Silla inactiva");
                } else {
                    btnSilla.addActionListener(e -> toggleSillaSeleccionada((JButton) e.getSource()));
                }

                botonesSillas[i][j] = btnSilla;
                panelSillas.add(btnSilla);
            }
            letraFila++;
        }

        panelSillasCompleto.add(panelSillas, BorderLayout.CENTER);

        // --- Panel inferior: Información y confirmación ---
        crearPanelInfo();

        // --- Ensamblar todo ---
        panelPrincipal.add(panelPantalla, BorderLayout.NORTH);
        panelPrincipal.add(panelSillasCompleto, BorderLayout.CENTER);
        panelPrincipal.add(panelInfo, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JButton createSeatButton(String idSilla) {
        JButton btnSilla = new JButton(idSilla);
        btnSilla.setPreferredSize(new Dimension(55, 55));
        btnSilla.setBackground(new Color(144, 238, 144)); // Verde claro
        btnSilla.setForeground(Color.BLACK);
        btnSilla.setFont(new Font("Arial", Font.BOLD, 12));
        btnSilla.setOpaque(true);
        btnSilla.setBorderPainted(true);
        btnSilla.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        btnSilla.setFocusPainted(false);
        btnSilla.setToolTipText("Silla " + idSilla + " - Click para seleccionar");
        return btnSilla;
    }

    private void toggleSillaSeleccionada(JButton botonSilla) {
        String idSilla = botonSilla.getText();

        if (sillasSeleccionadas.contains(idSilla)) {
            // Deseleccionar silla
            sillasSeleccionadas.remove(idSilla);
            botonSilla.setBackground(new Color(144, 238, 144)); // Verde claro
            botonSilla.setForeground(Color.BLACK);
            botonSilla.setToolTipText("Silla " + idSilla + " - Click para seleccionar");
        } else {
            // Seleccionar silla
            sillasSeleccionadas.add(idSilla);
            botonSilla.setBackground(new Color(220, 20, 60)); // Rojo
            botonSilla.setForeground(Color.WHITE);
            botonSilla.setToolTipText("Silla " + idSilla + " - Seleccionada (Click para deseleccionar)");
        }

        actualizarInfo();
    }

    private void crearPanelInfo() {
        panelInfo = new JPanel(new BorderLayout(10, 10));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Resumen de Selección"));
        panelInfo.setBackground(new Color(240, 240, 240));

        lblInfo = new JLabel("<html><div style='text-align: center;'>Seleccione las sillas haciendo clic en ellas</div></html>", JLabel.CENTER);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));

        btnConfirmar = new JButton("Confirmar Selección");
        btnConfirmar.setBackground(new Color(34, 139, 34));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.addActionListener(e -> controlador.confirmarSeleccion(sillasSeleccionadas));

        // Leyenda de colores
        JPanel panelLeyenda = new JPanel(new FlowLayout());
        panelLeyenda.setBackground(new Color(240, 240, 240));

        JPanel muestraDisponible = crearMuestra(Color.GREEN.brighter(), "Disponible");
        JPanel muestraSeleccionada = crearMuestra(Color.RED, "Seleccionada");
        JPanel muestraInactiva = crearMuestra(Color.DARK_GRAY, "Inactiva");

        panelLeyenda.add(muestraDisponible);
        panelLeyenda.add(new JLabel("Disponible"));
        panelLeyenda.add(Box.createHorizontalStrut(20));
        panelLeyenda.add(muestraSeleccionada);
        panelLeyenda.add(new JLabel("Seleccionada"));
        panelLeyenda.add(Box.createHorizontalStrut(20));
        panelLeyenda.add(muestraInactiva);
        panelLeyenda.add(new JLabel("Inactiva"));

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(panelLeyenda, BorderLayout.NORTH);
        panelSur.add(btnConfirmar, BorderLayout.SOUTH);

        panelInfo.add(lblInfo, BorderLayout.CENTER);
        panelInfo.add(panelSur, BorderLayout.SOUTH);
    }

    private JPanel crearMuestra(Color color, String texto) {
        JPanel muestra = new JPanel();
        muestra.setPreferredSize(new Dimension(20, 20));
        muestra.setBackground(color);
        muestra.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return muestra;
    }

    private void actualizarInfo() {
        if (sillasSeleccionadas.isEmpty()) {
            lblInfo.setText("<html><div style='text-align: center;'>Seleccione las sillas haciendo clic en ellas</div></html>");
        } else {
            String sillasTexto = String.join(", ", sillasSeleccionadas);
            lblInfo.setText("<html><div style='text-align: center;'>"
                    + "<b>Sillas seleccionadas:</b><br>"
                    + sillasTexto + "<br>"
                    + "Total: " + sillasSeleccionadas.size() + " silla(s)</div></html>");
        }
    }
    
    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
    
    public int preguntar(String pregunta, String titulo) {
        return JOptionPane.showConfirmDialog(this, pregunta, titulo, JOptionPane.YES_NO_OPTION);
    }

    public void resetearSeleccion() {
        sillasSeleccionadas.clear();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JButton btn = botonesSillas[i][j];
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(144, 238, 144));
                    btn.setForeground(Color.BLACK);
                    btn.setToolTipText("Silla " + btn.getText() + " - Click para seleccionar");
                }
            }
        }
        actualizarInfo();
    }

    private void configurarPanel() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
