package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UIUtils {
    
    private UIUtils() {}
    
    public static void estilizarCampo(JTextField campo, String placeholder) {
        campo.setMaximumSize(new Dimension(250, 35));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(Color.GRAY);
        campo.setText(placeholder);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 255), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
    }
    
    public static void estilizarBoton(JButton boton, Color fondo, Color texto) {
        boton.setFocusPainted(false);
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(fondo.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
    }
}