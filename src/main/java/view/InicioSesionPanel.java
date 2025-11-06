package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import util.Validador;
import java.util.List;
import controladores.ControladorUsuario;
import model.Usuario;


public class InicioSesionPanel extends JPanel {

    private JTextField fieldUsuario;
    private JPasswordField fieldPassword;
    private JButton btnEntrar;
    private JButton btnRegistrarse;

    public InicioSesionPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // Azul claro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // --- Panel principal ---
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 255), 2, true),
                new EmptyBorder(30, 40, 30, 40)
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- Etiqueta de bienvenida ---
        JLabel labelBienvenida = new JLabel("Bienvenido a CineReservasHD");
        labelBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelBienvenida.setForeground(new Color(30, 60, 100));
        labelBienvenida.setBorder(new EmptyBorder(0, 0, 15, 0));

        fieldUsuario = new JTextField();
        estilizarCampo(fieldUsuario, "Usuario");

        fieldPassword = new JPasswordField();
        estilizarCampo(fieldPassword, "Contraseña");

        btnEntrar = new JButton("Entrar");
        estilizarBoton(btnEntrar, new Color(0, 120, 215), Color.WHITE);

        btnRegistrarse = new JButton("Registrarse");
        estilizarBoton(btnRegistrarse, new Color(230, 230, 230), Color.BLACK);

        panel.add(labelBienvenida);
        panel.add(fieldUsuario);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fieldPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnRegistrarse);
        panelBotones.add(btnEntrar);

        panel.add(panelBotones);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);

        btnEntrar.addActionListener(e -> iniciarSesion());
        btnRegistrarse.addActionListener(e -> registrarse());
    }

    private void estilizarCampo(JTextField campo, String placeholder) {
        campo.setMaximumSize(new Dimension(250, 35));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(Color.GRAY);
        campo.setText(placeholder);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 255), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Placeholder funcional
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void estilizarBoton(JButton boton, Color fondo, Color texto) {
        boton.setFocusPainted(false);
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efecto
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(fondo.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
    }

    private void iniciarSesion() {
        
        
        String usuario = fieldUsuario.getText();
        String password = new String(fieldPassword.getPassword());
        
        if (!Validador.validarCamposObligatorios(List.of(usuario, password), List.of("Usuario", "Contraseña"))) {
            return;
        }
        
        ControladorUsuario controlador = new ControladorUsuario();
        Usuario u = controlador.iniciarSesion(usuario, password);
        
        if (u != null) {
        JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        // Aquí llamar el menu principal
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }


        
    }

    private void registrarse() {
        MainFrame.cambiarVista("registro");
    }
}

