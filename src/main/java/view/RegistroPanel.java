package view;

import controladores.ControladorUsuario;
import model.Usuario;
import util.Validador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import util.UIUtils;

public class RegistroPanel extends JPanel {

    private JTextField fieldNombre;
    private JTextField fieldUsuario;
    private JPasswordField fieldPassword;
    private JPasswordField fieldConfirmar;
    private JButton btnRegistrar;
    private JButton btnVolver;

    public RegistroPanel() {
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

        JLabel labelTitulo = new JLabel("Registro de Usuario");
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitulo.setForeground(new Color(30, 60, 100));
        labelTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Campos
        fieldNombre = new JTextField();
        UIUtils.estilizarCampo(fieldNombre, "Nombre completo");

        fieldUsuario = new JTextField();
        UIUtils.estilizarCampo(fieldUsuario, "Nombre de usuario");

        fieldPassword = new JPasswordField();
        UIUtils.estilizarCampo(fieldPassword, "Contraseña");

        fieldConfirmar = new JPasswordField();
        UIUtils.estilizarCampo(fieldConfirmar, "Confirmar contraseña");

        // Botones
        btnRegistrar = new JButton("Registrar");
        UIUtils.estilizarBoton(btnRegistrar, new Color(0, 120, 215), Color.WHITE);

        btnVolver = new JButton("Volver");
        UIUtils.estilizarBoton(btnVolver, new Color(230, 230, 230), Color.BLACK);

        // Añadir componentes
        panel.add(labelTitulo);
        panel.add(fieldNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fieldUsuario);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fieldPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fieldConfirmar);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnVolver);
        panelBotones.add(btnRegistrar);

        panel.add(panelBotones);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);

        // Eventos
        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnVolver.addActionListener(e -> volverALogin());
    }

    private void registrarUsuario() {
        String nombre = fieldNombre.getText();
        String username = fieldUsuario.getText();
        String password = new String(fieldPassword.getPassword());
        String confirmar = new String(fieldConfirmar.getPassword());

        if (!Validador.validarCamposObligatorios(Arrays.asList(nombre, username, password, confirmar), Arrays.asList("Nombre completo","Nombre de usuario","Contraseña","Confirmar contraseña"))) {
            return;
        }

        if (!password.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre);
        nuevo.setUsername(username);
        nuevo.setPassword(password);

        ControladorUsuario controlador = ControladorUsuario.getInstanciaControladorUsuario();
        if (controlador.registrarUsuario(nuevo)) {
            JOptionPane.showMessageDialog(this, "Registro exitoso. ¡Bienvenido!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverALogin();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverALogin() {
        MainFrame.getInstancia().cambiarVista("login");
    }
}