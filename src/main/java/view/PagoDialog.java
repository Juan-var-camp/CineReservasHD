package view;

import controladores.ControladorSeleccionSillas;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class PagoDialog extends JDialog {

    private final ControladorSeleccionSillas controladorReservas;
    private final Set<Point> coordenadasSeleccionadas;
    private final double totalAPagar;
    private boolean pagoExitoso = false;

    public PagoDialog(Frame owner, double totalAPagar, Set<Point> coordenadas, ControladorSeleccionSillas controlador) {
        super(owner, "Simulación de Pago", true);
        this.totalAPagar = totalAPagar;
        this.coordenadasSeleccionadas = coordenadas;
        this.controladorReservas = controlador;

        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Confirmar Pago", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTotal = new JLabel(String.format("Total a pagar: $%.2f", totalAPagar), SwingConstants.CENTER);
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Opciones de pago (simples por ahora) ---
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JRadioButton radioTarjeta = new JRadioButton("Tarjeta de Crédito", true);
        JRadioButton radioPSE = new JRadioButton("PSE");
        ButtonGroup grupoPago = new ButtonGroup();
        grupoPago.add(radioTarjeta);
        grupoPago.add(radioPSE);
        panelOpciones.add(radioTarjeta);
        panelOpciones.add(radioPSE);

        // --- Botones de acción ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnPagar = new JButton("Pagar");
        btnPagar.setBackground(new Color(40, 167, 69));
        btnPagar.setForeground(Color.WHITE);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnPagar);
        
        // --- Ensamblado ---
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPrincipal.add(lblTotal);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(panelOpciones);
        panelPrincipal.add(Box.createVerticalGlue());
        panelPrincipal.add(panelBotones);
        
        add(panelPrincipal, BorderLayout.CENTER);

        // --- Lógica de los botones ---
        btnPagar.addActionListener(e -> procesarPagoSimulado());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void procesarPagoSimulado() {
        System.out.println("Procesando pago simulado por un total de: $" + totalAPagar);

        boolean exito = controladorReservas.procesarReserva(coordenadasSeleccionadas);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Pago procesado y reserva registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.pagoExitoso = true;
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al procesar la reserva después del pago.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isPagoExitoso() {
        return pagoExitoso;
    }
}