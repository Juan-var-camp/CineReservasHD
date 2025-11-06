package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import dao.ConexionDB;
import model.Sala;

public class MainFrame extends JFrame {

    private static MainFrame instancia; 
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private Map<String, JPanel> vistas; // Se guardan las vistas por nombre

    private MainFrame() {
        inicializarVentana();
        inicializarVistas();
    }

    public static MainFrame getInstancia() {
        if (instancia == null) {
            instancia = new MainFrame();
        }
        return instancia;
    }

    private void inicializarVentana() {
        setTitle("CineReservas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        vistas = new HashMap<>();

        setContentPane(panelContenedor);
    }

    private void inicializarVistas() {
        // Crea e inserta las vistas que quieras usar
        ConexionDB.inicializarDatabase();
        InicioSesionPanel login = new InicioSesionPanel();
        RegistroPanel registro = new RegistroPanel();
        ConfiguracionSalaPanel configSala = new ConfiguracionSalaPanel();
        MenuTestPanel menuTest = new MenuTestPanel();
        PanelPeliculas panelPeliculas = new PanelPeliculas();

        // Añadirlas al contenedor con un nombre
        vistas.put("login", login);
        vistas.put("registro", registro);
        vistas.put("config_sala", configSala);
        vistas.put("menu_test", menuTest);
        vistas.put("panel_peliculas", panelPeliculas);

        panelContenedor.add(login, "login");
        panelContenedor.add(registro, "registro");
        panelContenedor.add(configSala, "config_sala");
        panelContenedor.add(menuTest, "menu_test");
        panelContenedor.add(panelPeliculas, "panel_peliculas");

        mostrarVista("menu_test");
    }

    public void mostrarVista(String nombreVista) {
        JPanel vista = vistas.get(nombreVista);
        if (vista != null) {
            cardLayout.show(panelContenedor, nombreVista);
        } else {
            System.err.println("Vista no encontrada: " + nombreVista);
        }
    }

    public static void cambiarVista(String nombreVista) {
        getInstancia().mostrarVista(nombreVista);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = MainFrame.getInstancia();
            frame.setVisible(true);
        });
    }
}
