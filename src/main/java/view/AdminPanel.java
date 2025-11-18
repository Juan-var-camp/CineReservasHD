package view;

import controladores.ControladorUsuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminPanel extends JPanel {

    private final JTabbedPane tabbedPane;
    private final PanelPeliculas panelPeliculas;
    private final CrearFuncionPanel crearFuncionPanel;
    private final ConfiguracionSalaPanel configuracionSalaPanel;
    private final ReportesPanel reportesPanel;

    public AdminPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Panel de Administración", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(lblTitulo, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelPeliculas = new PanelPeliculas();
        crearFuncionPanel = new CrearFuncionPanel();
        configuracionSalaPanel = new ConfiguracionSalaPanel();
        reportesPanel = new ReportesPanel(); 

        tabbedPane.addTab("   Películas   ", panelPeliculas);
        tabbedPane.addTab("   Funciones   ", crearFuncionPanel);
        tabbedPane.addTab("   Salas   ", configuracionSalaPanel);
        tabbedPane.addTab("   Reportes   ", reportesPanel);
        

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            recargarPanelSeleccionado(selectedIndex);
        });

        add(tabbedPane, BorderLayout.CENTER);
        
        add(tabbedPane, BorderLayout.CENTER);

        // --- Panel Inferior con Botón de Cerrar Sesión ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(220, 53, 69)); // Rojo
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "¿Está seguro de que desea cerrar la sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                ControladorUsuario.getInstanciaControladorUsuario().cerrarSesion();
                MainFrame.getInstancia().cambiarVista("login");
            }
        });
        bottomPanel.add(btnCerrarSesion);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void cargarDatos() {
        panelPeliculas.cargarPeliculas();
        crearFuncionPanel.cargarDatosIniciales();
        configuracionSalaPanel.cargarSalasEnCombo();
    }
    
    private void recargarPanelSeleccionado(int index) {
        switch (index) {
            case 0 -> // Películas
                panelPeliculas.cargarPeliculas();
            case 1 -> // Funciones
                crearFuncionPanel.cargarDatosIniciales();
            case 2 -> // Salas
                configuracionSalaPanel.cargarSalasEnCombo();
        }
    }
}