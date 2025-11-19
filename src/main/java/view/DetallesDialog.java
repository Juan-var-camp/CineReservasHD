package view;

import controladores.ControladorFuncion;
import model.Funcion;
import model.Pelicula;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DetallesDialog extends JDialog {
    private static final Color COLOR_FONDO = new Color(248, 248, 248);

    public DetallesDialog(JFrame parent, Pelicula pelicula) {
        super(parent, pelicula.getTitulo(), true);
        configurarDialogo();
        crearContenido(pelicula);
    }

    private void configurarDialogo() {
        setSize(900, 650);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
    }

    private void crearContenido(Pelicula pelicula) {
        JPanel panelContenido = new JPanel(new BorderLayout(30, 0));
        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel labelImagen = new JLabel();
        labelImagen.setPreferredSize(new Dimension(280, 400));
        try {
            ImageIcon icon = new ImageIcon(new ImageIcon(pelicula.getImagenPath()).getImage().getScaledInstance(280, 400, Image.SCALE_SMOOTH));
            labelImagen.setIcon(icon);
        } catch (Exception e) {
            labelImagen.setText("Imagen no disponible");
        }
        panelContenido.add(labelImagen, BorderLayout.WEST);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setOpaque(false);

        JLabel labelHorariosTitulo = new JLabel("Funciones Disponibles");
        labelHorariosTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelHorariosTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelDerecho.add(labelHorariosTitulo);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 15)));
        panelDerecho.add(crearPanelHorarios(pelicula));

        panelContenido.add(panelDerecho, BorderLayout.CENTER);
        add(new JScrollPane(panelContenido), BorderLayout.CENTER);
    }

    private JPanel crearPanelHorarios(Pelicula pelicula) {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setOpaque(false);
        panelPrincipal.setAlignmentX(Component.LEFT_ALIGNMENT);

        List<Funcion> funciones = ControladorFuncion.getInstancia().listarFuncionesPorPelicula(pelicula.getId());
        
        if (funciones.isEmpty()) {
            panelPrincipal.add(new JLabel("No hay funciones programadas para esta película."));
            return panelPrincipal;
        }

        Map<String, List<Funcion>> funcionesPorFecha = funciones.stream()
            .collect(Collectors.groupingBy(Funcion::getFecha));

        for (Map.Entry<String, List<Funcion>> entry : funcionesPorFecha.entrySet()) {
            panelPrincipal.add(crearItemHorario(entry.getKey(), entry.getValue()));
            panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        return panelPrincipal;
    }

    private JPanel crearItemHorario(String fecha, List<Funcion> funciones) {
        JPanel panelItem = new JPanel();
        panelItem.setLayout(new BoxLayout(panelItem, BoxLayout.Y_AXIS));
        panelItem.setOpaque(false);
        panelItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelFecha = new JLabel(fecha);
        labelFecha.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelItem.add(labelFecha);
        panelItem.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Funcion funcion : funciones) {
            JButton btnHorario = new JButton(funcion.getHora() + " (" + funcion.getNombreSala() + ")");
            btnHorario.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnHorario.addActionListener(e -> {
                this.dispose(); 
                MainFrame.getInstancia().mostrarSeleccionSillas(funcion);
            });
            panelBotones.add(btnHorario);
        }
        
        panelItem.add(panelBotones);
        return panelItem;
    }
}