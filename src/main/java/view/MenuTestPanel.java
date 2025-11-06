package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuTestPanel extends JPanel {

    public MenuTestPanel() {
        setLayout(new GridLayout(4, 2, 20, 20)); // 8 botones (4 filas x 2 columnas)
        setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        // Crear botones
        String[] nombresVistas = {
            "login",
            "registro",
            "config_sala",
            "panel_peliculas",
            "vista5",
            "vista6",
            "vista7",
            "vista8"
        };

        for (String nombre : nombresVistas) {
            JButton btn = new JButton("Ir a " + nombre);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.addActionListener(new CambiarVistaListener(nombre));
            add(btn);
        }
    }

    private static class CambiarVistaListener implements ActionListener {
        private final String nombreVista;

        public CambiarVistaListener(String nombreVista) {
            this.nombreVista = nombreVista;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame.cambiarVista(nombreVista);
        }
    }
}
