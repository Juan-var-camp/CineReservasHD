package view;

import controladores.ControladorPelicula;
import controladores.ControladorUsuario;
import model.Pelicula;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CarteleraPanel extends JPanel {
    private final JPanel panelPeliculas;

    public CarteleraPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(25, 25, 112)); 
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel labelTitulo = new JLabel("Cartelera de CineReservasHD");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        labelTitulo.setForeground(Color.WHITE);
        
        JButton btnHistorial = new JButton("Mi Historial");
        btnHistorial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHistorial.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorial.addActionListener(e -> {
            MainFrame.getInstancia().mostrarHistorial();
        });
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBackground(new Color(220, 53, 69)); 
        btnCerrarSesion.setForeground(Color.WHITE);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setOpaque(false); // Hacerlo transparente
        panelBotones.add(btnHistorial);
        panelBotones.add(btnCerrarSesion);
        
        // 3. Añadir funcionalidad a los botones
        btnHistorial.addActionListener(e -> {
            MainFrame.getInstancia().mostrarHistorial();
        });
        
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "¿Estás seguro de que quieres cerrar la sesión?", 
                "Confirmar Cierre de Sesión", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                ControladorUsuario.getInstanciaControladorUsuario().cerrarSesion();
                MainFrame.getInstancia().cambiarVista("login");
            }
        });
        
        panelHeader.add(labelTitulo, BorderLayout.CENTER);
        panelHeader.add(panelBotones, BorderLayout.EAST); 
        add(panelHeader, BorderLayout.NORTH);
        
        panelPeliculas = new JPanel();
        panelPeliculas.setLayout(new BoxLayout(panelPeliculas, BoxLayout.Y_AXIS));
        panelPeliculas.setBackground(Color.WHITE);
        panelPeliculas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(panelPeliculas);
        scrollPane.setBorder(null); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        add(scrollPane, BorderLayout.CENTER);
    }

    public void cargarPeliculas() {
        panelPeliculas.removeAll();
        
        List<Pelicula> peliculas = ControladorPelicula.getInstancia().listarPeliculas();

        if (peliculas.isEmpty()) {
            JLabel mensaje = new JLabel("No hay películas disponibles en este momento.");
            mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelPeliculas.add(mensaje);
        } else {
            for (Pelicula pelicula : peliculas) {
                PeliculaCardPanel card = new PeliculaCardPanel(pelicula);
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        DetallesDialog dialogo = new DetallesDialog(
                            (JFrame) SwingUtilities.getWindowAncestor(CarteleraPanel.this),
                            pelicula
                        );
                        dialogo.setVisible(true);
                    }
                });
                panelPeliculas.add(card);
                
            }
        }
        panelPeliculas.revalidate();
        panelPeliculas.repaint();
    }
}