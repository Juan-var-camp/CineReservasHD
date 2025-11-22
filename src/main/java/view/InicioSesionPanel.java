package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import util.Validador;
import java.util.List;
import controladores.ControladorUsuario;
import model.Usuario;
import util.UIUtils;
import java.io.File;

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
        setBackground(new Color(240, 248, 255)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 255), 2, true),
                new EmptyBorder(30, 40, 30, 40)
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel labelLogo = new JLabel();
        labelLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String rutaImagen = "images/logo.jpg"; 
        File archivoImg = new File(rutaImagen);
        
        if (archivoImg.exists()) {
            ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            labelLogo.setIcon(new ImageIcon(imagenEscalada));
        } else {
            
            labelLogo.setText("[Logo Aquí]");
        }
        labelLogo.setBorder(new EmptyBorder(0, 0, 20, 0)); 
        
        panel.add(labelLogo);

        JLabel labelBienvenida = new JLabel("Bienvenido a CineReservasUltradHD100%realNoFake(CineReservasHD)");
        labelBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelBienvenida.setForeground(new Color(30, 60, 100));
        labelBienvenida.setBorder(new EmptyBorder(0, 0, 15, 0));

        fieldUsuario = new JTextField();
        UIUtils.estilizarCampo(fieldUsuario, "Usuario");

        fieldPassword = new JPasswordField();
        UIUtils.estilizarCampo(fieldPassword, "Contraseña");

        btnEntrar = new JButton("Entrar");
        UIUtils.estilizarBoton(btnEntrar, new Color(0, 120, 215), Color.WHITE);

        btnRegistrarse = new JButton("Registrarse");
        UIUtils.estilizarBoton(btnRegistrarse, new Color(230, 230, 230), Color.BLACK);

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

    private void iniciarSesion() {
        
        
        String usuario = fieldUsuario.getText();
        String password = new String(fieldPassword.getPassword());
        
        if (!Validador.validarCamposObligatorios(List.of(usuario, password), List.of("Usuario", "Contraseña"))) {
            return;
        }
        
        
        Usuario u = ControladorUsuario.getInstanciaControladorUsuario().iniciarSesion(usuario, password);
        
        if (u != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if ("Admin".equalsIgnoreCase(u.getTipo())) {
                MainFrame.getInstancia().cambiarVista("admin_panel");
            } else {
                MainFrame.getInstancia().cambiarVista("cartelera");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarse() {
        MainFrame.getInstancia().cambiarVista("registro");
    }
}