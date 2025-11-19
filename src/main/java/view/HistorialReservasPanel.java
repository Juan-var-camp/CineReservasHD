package view;

import controladores.ControladorReserva;
import controladores.ControladorUsuario;
import model.HistorialReserva;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialReservasPanel extends JPanel {

    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private final ControladorReserva controladorReserva;

    public HistorialReservasPanel() {
        this.controladorReserva = ControladorReserva.getInstanciaControladorReserva();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setBackground(new Color(240, 248, 255));

        JLabel lblTitulo = new JLabel("Mi Historial de Reservas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"Película", "Fecha Función", "Hora", "Sala", "Asientos", "Total Pagado", "Fecha de Reserva"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaHistorial.setRowHeight(25);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver a la Cartelera");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.addActionListener(e -> MainFrame.getInstancia().cambiarVista("cartelera"));
        
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBackground(getBackground());
        panelSur.add(btnVolver);
        add(panelSur, BorderLayout.SOUTH);
    }

    public void cargarHistorial() {
        modeloTabla.setRowCount(0);

        Usuario usuarioActual = ControladorUsuario.getInstanciaControladorUsuario().getUsuarioActual();
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this, "No se ha podido identificar al usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<HistorialReserva> historial = controladorReserva.obtenerHistorialPorUsuario(usuarioActual.getId());

        if (historial.isEmpty()) {
             modeloTabla.addRow(new Object[]{"No tienes reservas aún.", "", "", "", "", "", ""});
        } else {
            for (HistorialReserva r : historial) {
                modeloTabla.addRow(new Object[]{
                    r.getPeliculaTitulo(),
                    r.getFuncionFecha(),
                    r.getFuncionHora(),
                    r.getSalaNombre(),
                    r.getCantidadAsientos(),
                    String.format("$%.2f", r.getTotalPagado()),
                    r.getFechaDeReserva()
                });
            }
        }
    }
}