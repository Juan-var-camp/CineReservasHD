package view;

import controladores.ControladorSeleccionSillas;
import model.Funcion;
import model.Sala;
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SeleccionSillasPanel extends JPanel {
    
    private static final Color COLOR_DISPONIBLE = new Color(144, 238, 144); 
    private static final Color COLOR_SELECCIONADA = new Color(65, 105, 225); 
    private static final Color COLOR_OCUPADA = new Color(220, 20, 60);    
    
    private final ControladorSeleccionSillas controlador;
    private final Set<SeatButton> sillasSeleccionadas;
    private final Component[][] componentesSillas;
    private final int filas;
    private final int columnas;
    private final double precioPorSilla;

    private JLabel lblInfo;
    private Timer timer;
    private int tiempoRestante = 600; // 10 minutos en segundos
    private JLabel lblTiempo;
    
    public SeleccionSillasPanel(Funcion funcion) {
        this.controlador = new ControladorSeleccionSillas(funcion);
        this.precioPorSilla = funcion.getPrecio();
        
        Sala salaActual = controlador.getSalaActual();
        this.filas = salaActual.getFilas();
        this.columnas = salaActual.getColumnas();
        this.sillasSeleccionadas = new HashSet<>();
        this.componentesSillas = new Component[filas][columnas];

        initComponents(salaActual, funcion);
        iniciarTimer();
    }

    private void initComponents(Sala sala, Funcion funcion) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 248, 255));

        JPanel panelPantalla = new JPanel();
        panelPantalla.setBackground(new Color(25, 25, 112));  
        panelPantalla.setPreferredSize(new Dimension(0, 50));
        
        JLabel lblPantalla = new JLabel("PANTALLA", JLabel.CENTER);
        lblPantalla.setForeground(Color.WHITE);
        lblPantalla.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelPantalla.add(lblPantalla);

        JPanel panelSillas = new JPanel(new GridLayout(filas, columnas, 5, 5));
        panelSillas.setBackground(new Color(240, 248, 255));
        panelSillas.setBorder(BorderFactory.createTitledBorder(
            "Seleccione sus asientos - Sala: " + sala.getNombre()
        ));
        
        boolean[][] sillasOcupadasFuncion = funcion.getSillasOcupadas();

        char letraFila = 'A';
        for (int i = 0; i < filas; i++) {
            int contadorAsientosVisibles = 1;
            for (int j = 0; j < columnas; j++) {

                if (sala.isActiva(i, j)) {
                    String idVisible = letraFila + "" + contadorAsientosVisibles;
                    SeatButton btnSilla = new SeatButton(idVisible, i, j); 
                    
                    if (sillasOcupadasFuncion[i][j]) {
                        btnSilla.setOcupada(true);
                    } else {
                        btnSilla.setOcupada(false); 
                        btnSilla.addActionListener(e -> {
                            if (!btnSilla.isOcupada()) {
                            toggleSillaSeleccionada(btnSilla);
                            }
                        });
                    }
                    
                    componentesSillas[i][j] = btnSilla;
                    panelSillas.add(btnSilla);
                    contadorAsientosVisibles++; 

                } else {
                    
                    JPanel placeholder = new JPanel();
                    placeholder.setOpaque(false); 
                    componentesSillas[i][j] = placeholder;
                    panelSillas.add(placeholder);
                }
            }
            letraFila++;
        }

        JPanel panelInfo = crearPanelInfo();

        add(panelPantalla, BorderLayout.NORTH);
        add(panelSillas, BorderLayout.CENTER);
        add(panelInfo, BorderLayout.SOUTH);
    }
    
    private void iniciarTimer() {
        timer = new Timer(1000, e -> {
            tiempoRestante--;
            actualizarLabelTiempo();
            if (tiempoRestante <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this,
                        "Se ha agotado el tiempo para la selección de asientos.",
                        "Tiempo Agotado",
                        JOptionPane.WARNING_MESSAGE);
                MainFrame.getInstancia().cambiarVista("cartelera");
            }
        });
        timer.start();
    }

    private void actualizarLabelTiempo() {
        int minutos = tiempoRestante / 60;
        int segundos = tiempoRestante % 60;
        lblTiempo.setText(String.format("Tiempo Restante: %02d:%02d", minutos, segundos));
        lblTiempo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTiempo.setForeground(tiempoRestante < 60 ? Color.RED : Color.BLACK);
    }

    private void toggleSillaSeleccionada(SeatButton botonSilla) {
        if (botonSilla.isOcupada()) {
            return;
        }
        
        if (sillasSeleccionadas.contains(botonSilla)) {
            sillasSeleccionadas.remove(botonSilla);
            botonSilla.setBackground(COLOR_DISPONIBLE);
        } else {
            sillasSeleccionadas.add(botonSilla);
            botonSilla.setBackground(COLOR_SELECCIONADA);
        }
        actualizarInfo();
    }

    private JPanel crearPanelInfo() {
        JPanel panelInfo = new JPanel(new BorderLayout(10, 10));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Resumen de Selección"));
        panelInfo.setBackground(new Color(240, 248, 255));

        lblInfo = new JLabel("Seleccione los asientos haciendo clic en ellos", JLabel.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnConfirmar = new JButton("Confirmar Reserva");
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmar.setBackground(new Color(46, 139, 87)); 
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnCancelar = new JButton("Cancelar"); 
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnConfirmar.addActionListener(e -> confirmarSeleccion());
        
        btnCancelar.addActionListener(e -> { 
            String mensaje = "¿Estás seguro de que quieres cancelar y volver a la cartelera?";
            if (!sillasSeleccionadas.isEmpty()) {
                mensaje += "\nPerderás los asientos que has seleccionado.";
            }

            int confirm = JOptionPane.showConfirmDialog(
                this, 
                mensaje, 
                "Confirmar Cancelación", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                timer.stop();
                MainFrame.getInstancia().cambiarVista("cartelera");
            }
        });
        
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelAcciones.setOpaque(false);
        panelAcciones.add(btnCancelar); 
        panelAcciones.add(btnConfirmar); 

        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelLeyenda.setBackground(new Color(240, 248, 255));
        panelLeyenda.add(crearMuestra(COLOR_DISPONIBLE, "Disponible"));
        panelLeyenda.add(crearMuestra(COLOR_SELECCIONADA, "Seleccionado"));
        panelLeyenda.add(crearMuestra(COLOR_OCUPADA, "Ocupado"));

        JPanel panelNorteInfo = new JPanel(new BorderLayout());
        panelNorteInfo.setOpaque(false);
        
        lblTiempo = new JLabel();
        actualizarLabelTiempo();
        
        panelNorteInfo.add(lblInfo, BorderLayout.CENTER);
        panelNorteInfo.add(lblTiempo, BorderLayout.EAST);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.add(panelLeyenda, BorderLayout.NORTH);
        panelSur.add(panelAcciones, BorderLayout.SOUTH);

        panelInfo.add(panelNorteInfo, BorderLayout.CENTER);
        panelInfo.add(panelSur, BorderLayout.SOUTH);
        
        return panelInfo;
    }
    
    private JPanel crearMuestra(Color color, String texto) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        
        JLabel muestra = new JLabel();
        muestra.setOpaque(true);
        muestra.setPreferredSize(new Dimension(20, 20));
        muestra.setBackground(color);
        muestra.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
        panel.add(muestra);
        panel.add(new JLabel(texto));
        return panel;
    }

    private void actualizarInfo() {
        if (sillasSeleccionadas.isEmpty()) {
            lblInfo.setText("Seleccione los asientos haciendo clic en ellos");
        } else {
            String sillasTexto = sillasSeleccionadas.stream()
                .map(JButton::getText)
                .sorted()
                .collect(Collectors.joining(", "));
            
            double total = sillasSeleccionadas.size() * precioPorSilla;
            lblInfo.setText(String.format("<html><b>Seleccionado:</b> %s<br><b>Total:</b> $%.2f</html>", sillasTexto, total));
        }
    }
    
    private void confirmarSeleccion() {
        if (sillasSeleccionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un asiento.", "Selección vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Set<Point> coordenadasReales = new HashSet<>();
        for (SeatButton seatButton : sillasSeleccionadas) {
            coordenadasReales.add(new Point(seatButton.getGridRow(), seatButton.getGridCol()));
        }
        
        double total = sillasSeleccionadas.size() * precioPorSilla;
        
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        PagoDialog dialogoPago = new PagoDialog(mainFrame, total, coordenadasReales, controlador);
        dialogoPago.setVisible(true);
        
        if (dialogoPago.isPagoExitoso()) {
            timer.stop();
            actualizarSillasOcupadas();
            resetearSeleccion();
            JOptionPane.showMessageDialog(this, "¡Reserva completada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            MainFrame.getInstancia().cambiarVista("cartelera");
        } else {
            System.out.println("El pago fue cancelado por el usuario.");
        }
    }
    
    private void actualizarSillasOcupadas() {
        for (SeatButton btn : sillasSeleccionadas) {
            btn.setOcupada(true); 
        }
    }

    public void resetearSeleccion() {
        sillasSeleccionadas.clear();
        actualizarInfo();
    }
}