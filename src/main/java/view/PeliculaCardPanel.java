package view;

import model.Pelicula; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class PeliculaCardPanel extends JPanel {
    private static final Color COLOR_FONDO = new Color(245, 245, 245);
    private static final Color COLOR_HOVER = new Color(230, 230, 230);

    public PeliculaCardPanel(Pelicula pelicula) {
        configurarPanel();
        crearComponentes(pelicula);
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(15, 10));
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(COLOR_FONDO);
            }
        });
    }

    private void crearComponentes(Pelicula pelicula) {
        // Panel para la imagen del póster
        JLabel labelImagen = new JLabel();
        labelImagen.setPreferredSize(new Dimension(180, 250));
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            File archivoImagen = new File(pelicula.getImagenPath());
            if (archivoImagen.exists()) {
                ImageIcon icon = new ImageIcon(pelicula.getImagenPath());
                Image img = icon.getImage().getScaledInstance(180, 250, Image.SCALE_SMOOTH);
                labelImagen.setIcon(new ImageIcon(img));
            } else {
                labelImagen.setText("Imagen no encontrada");
                labelImagen.setOpaque(true);
                labelImagen.setBackground(Color.DARK_GRAY);
                labelImagen.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            labelImagen.setText("Error al cargar imagen");
        }
        add(labelImagen, BorderLayout.WEST);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);

        JLabel labelTitulo = new JLabel(pelicula.getTitulo());
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        labelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea areaDescripcion = new JTextArea(pelicula.getSipnosis());
        areaDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setEditable(false);
        areaDescripcion.setOpaque(false);
        areaDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaDescripcion.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panelInfo.add(labelTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInfo.add(crearPanelBadges(pelicula));
        panelInfo.add(areaDescripcion);
        panelInfo.add(Box.createVerticalGlue()); 

        JPanel panelPuntaje = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelPuntaje.setOpaque(false);
        panelPuntaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel estrella = new JLabel("★"); // Símbolo de estrella
        estrella.setForeground(new Color(255, 193, 7));
        estrella.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JLabel labelPuntaje = new JLabel(String.format("%.1f / 10.0", pelicula.getPuntaje()));
        labelPuntaje.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelPuntaje.add(estrella);
        panelPuntaje.add(labelPuntaje);
        panelInfo.add(panelPuntaje);

        add(panelInfo, BorderLayout.CENTER);
    }

    private JPanel crearPanelBadges(Pelicula pelicula) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(crearBadge(pelicula.getGenero(), new Color(0, 123, 255), Color.WHITE));
        panel.add(crearBadge(pelicula.getClasificacion(), new Color(40, 167, 69), Color.WHITE));
        panel.add(crearBadge(pelicula.getDuracion() + " min", Color.GRAY, Color.WHITE));

        return panel;
    }

    private JLabel crearBadge(String texto, Color fondo, Color colorTexto) {
        JLabel badge = new JLabel(texto);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setBackground(fondo);
        badge.setForeground(colorTexto);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        return badge;
    }
}