package view;

import dao.ConexionDB;
import model.Funcion;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private static MainFrame instancia;
    private final CardLayout cardLayout;
    private final JPanel panelContenedor;
    private final Map<String, JPanel> vistas;
    private HistorialReservasPanel historialPanel;

    private MainFrame() {
        setTitle("CineReservasHD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        vistas = new HashMap<>();
        setContentPane(panelContenedor);

        inicializarVistas();
        cambiarVista("login"); 
    }

    public static synchronized MainFrame getInstancia() {
        if (instancia == null) {
            instancia = new MainFrame();
        }
        return instancia;
    }

    private void inicializarVistas() {
        ConexionDB.inicializarDatabase();

        // Paneles que se cargarán una vez
        InicioSesionPanel loginPanel = new InicioSesionPanel();
        RegistroPanel registroPanel = new RegistroPanel();
        CarteleraPanel carteleraPanel = new CarteleraPanel();
        historialPanel = new HistorialReservasPanel();
        
        AdminPanel adminPanel = new AdminPanel();
        
        
        
        // Añadir paneles al contenedor
        panelContenedor.add(historialPanel, "historial");
        panelContenedor.add(loginPanel, "login");
        panelContenedor.add(registroPanel, "registro"); 
        panelContenedor.add(carteleraPanel, "cartelera");
        
        panelContenedor.add(adminPanel, "admin_panel");
        
        // Guardar referencia en el mapa
        vistas.put("historial", historialPanel);
        vistas.put("login", loginPanel);
        vistas.put("registro", registroPanel);
        vistas.put("cartelera", carteleraPanel);
        
        vistas.put("admin_panel", adminPanel);
    }

    public void mostrarSeleccionSillas(Funcion funcion) {
        if (vistas.containsKey("seleccion")) {
            panelContenedor.remove(vistas.get("seleccion"));
        }
        
        SeleccionSillasPanel seleccionSillas = new SeleccionSillasPanel(
            funcion.getNombreSala(),
            funcion.getId(),
            funcion.getPrecio()
        );
        
        vistas.put("seleccion", seleccionSillas);
        panelContenedor.add(seleccionSillas, "seleccion");
        
        cambiarVista("seleccion");
    }

    public void cambiarVista(String nombreVista) {
        if ("cartelera".equals(nombreVista)) {
            // Asegurarse de que la vista exista y sea del tipo correcto
            Component vista = vistas.get(nombreVista);
            if (vista instanceof CarteleraPanel carteleraPanel) {
                carteleraPanel.cargarPeliculas();
            }
        }
        
        if ("admin_panel".equals(nombreVista)) {
            Component vista = vistas.get(nombreVista);
            if (vista instanceof CrearFuncionPanel) {
                ((CrearFuncionPanel) vista).cargarDatosIniciales();
            }    
        }
        cardLayout.show(panelContenedor, nombreVista);
    }
    
    public void mostrarHistorial() {
        historialPanel.cargarHistorial();
        cambiarVista("historial");
    }

    public static void main(String[] args) {
        try {
             UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            System.err.println("Nimbus Look and Feel no encontrado.");
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame.getInstancia().setVisible(true);
        });
    }
}