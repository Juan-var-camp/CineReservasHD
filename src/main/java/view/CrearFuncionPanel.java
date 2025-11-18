package view;

import com.toedter.calendar.JDateChooser;
import controladores.ControladorFuncion;
import controladores.ControladorPelicula;
import controladores.ControladorSala;
import model.Pelicula;
import model.Sala;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CrearFuncionPanel extends JPanel {

    // Componentes de la UI
    private JComboBox<PeliculaItem> comboPeliculas;
    private JComboBox<SalaItem> comboSalas;
    private JDateChooser dateChooser;
    private JSpinner spinnerHora, spinnerMinuto;
    private JTextField txtPrecio;
    private JButton btnGuardar, btnLimpiar;

    // Controladores
    private final ControladorFuncion controladorFuncion;
    private final ControladorPelicula controladorPelicula;
    private final ControladorSala controladorSala;

    public CrearFuncionPanel() {
        this.controladorFuncion = ControladorFuncion.getInstancia();
        this.controladorPelicula = ControladorPelicula.getInstancia();
        this.controladorSala = ControladorSala.getInstanciaControladorSala();
        
        initComponents();
        cargarDatosIniciales();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 40, 20, 40));
        setBackground(new Color(240, 248, 255));

        // Panel de Título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 60, 100));
        JLabel lblTitulo = new JLabel("Gestión de Funciones de Cine");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);

        // Panel de Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Crear Nueva Función"),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Película:"), gbc);
        gbc.gridx = 1; comboPeliculas = new JComboBox<>(); panelForm.add(comboPeliculas, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Sala:"), gbc);
        gbc.gridx = 1; comboSalas = new JComboBox<>(); panelForm.add(comboSalas, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1; dateChooser = new JDateChooser(); dateChooser.setDateFormatString("yyyy-MM-dd"); panelForm.add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Hora (HH:MM):"), gbc);
        gbc.gridx = 1;
        spinnerHora = new JSpinner(new SpinnerNumberModel(12, 0, 23, 1));
        spinnerMinuto = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        spinnerHora.setEditor(new JSpinner.NumberEditor(spinnerHora, "00"));
        spinnerMinuto.setEditor(new JSpinner.NumberEditor(spinnerMinuto, "00"));
        JPanel panelHora = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelHora.setBackground(Color.WHITE);
        panelHora.add(spinnerHora);
        panelHora.add(new JLabel(" : "));
        panelHora.add(spinnerMinuto);
        panelForm.add(panelHora, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panelForm.add(new JLabel("Precio (COP):"), gbc);
        gbc.gridx = 1; txtPrecio = new JTextField(15); panelForm.add(txtPrecio, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(getBackground());
        btnGuardar = new JButton("Guardar Función");
        btnLimpiar = new JButton("Limpiar Formulario");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        
        btnGuardar.addActionListener(e -> guardarFuncion());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        add(panelTitulo, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardarFuncion() {
        // 1. Obtener los objetos seleccionados de los ComboBox
        PeliculaItem peliculaSeleccionada = (PeliculaItem) comboPeliculas.getSelectedItem();
        SalaItem salaSeleccionada = (SalaItem) comboSalas.getSelectedItem();
        
        if (peliculaSeleccionada == null || salaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una película y una sala.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validar la fecha
        Date fechaSeleccionada = dateChooser.getDate();
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validar el precio
        double precio;
        try {
            precio = Double.parseDouble(txtPrecio.getText());
            if (precio <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número positivo válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Formatear datos y llamar al controlador
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFormateada = sdf.format(fechaSeleccionada);
        String horaFormateada = String.format("%02d:%02d", spinnerHora.getValue(), spinnerMinuto.getValue());
        int idSala = salaSeleccionada.getId();
        int idPelicula = peliculaSeleccionada.getId();
        
        String resultado = controladorFuncion.crearFuncionConValidacion(idPelicula, idSala, fechaFormateada, horaFormateada, precio);
        
        if (resultado == null) { // Éxito
            JOptionPane.showMessageDialog(this, "Función creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else { // Hubo un error
            JOptionPane.showMessageDialog(this, resultado, "Error al Crear Función", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        if (comboPeliculas.getItemCount() > 0) comboPeliculas.setSelectedIndex(0);
        if (comboSalas.getItemCount() > 0) comboSalas.setSelectedIndex(0);
        dateChooser.setDate(null);
        spinnerHora.setValue(12);
        spinnerMinuto.setValue(0);
        txtPrecio.setText("");
    }

    public void cargarDatosIniciales() {
        // Cargar películas en el ComboBox
        comboPeliculas.removeAllItems();
        List<Pelicula> peliculas = controladorPelicula.listarPeliculas();
        for (Pelicula p : peliculas) {
            comboPeliculas.addItem(new PeliculaItem(p.getId(), p.getTitulo()));
        }

        // Cargar salas en el ComboBox
        comboSalas.removeAllItems();
        List<Sala> salas = controladorSala.listarSalas();
        for (Sala s : salas) {
            comboSalas.addItem(new SalaItem(s.getId(), s.getNombre())); 
        }
    }

    // Clases internas para manejar objetos en los JComboBox
    private static class PeliculaItem {
        private final int id;
        private final String titulo;
        public PeliculaItem(int id, String titulo) { this.id = id; this.titulo = titulo; }
        public int getId() { return id; }
        @Override public String toString() { return titulo; }
    }
    
    private static class SalaItem {
        private final int id;
        private final String nombre;
        public SalaItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
        public int getId() { return id; }
        @Override public String toString() { return nombre; }
    }
}