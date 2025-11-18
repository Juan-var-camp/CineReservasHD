package view;

import dao.PeliculaDAO;
import model.Pelicula;
import util.Validador;
import util.ImageManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class PanelPeliculas extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    // --- CAMPOS DEL FORMULARIO ---
    private JTextField txtTitulo, txtGenero, txtDuracion, txtClasificacion, txtSipnosis, txtPuntaje;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar, btnSeleccionarImagen;
    private JLabel lblImagen;
    private final PeliculaDAO peliculaDAO;
    private String imagenPathSeleccionada;

    public PanelPeliculas() {
        this.peliculaDAO = new PeliculaDAO();
        initComponents();
        cargarPeliculas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Panel de título ----
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 60, 100));
        JLabel lblTitulo = new JLabel("Gestión de Películas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);

        // ---- Panel de formulario ----
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos de la Película"),
                new EmptyBorder(10, 15, 10, 15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- Fila 0 ---
        agregarCampoFormulario(panelForm, gbc, "Título:", txtTitulo = new JTextField(), 0, 0);
        agregarCampoFormulario(panelForm, gbc, "Género:", txtGenero = new JTextField(), 0, 2);

        // --- Fila 1 ---
        agregarCampoFormulario(panelForm, gbc, "Duración (min):", txtDuracion = new JTextField(), 1, 0);
        agregarCampoFormulario(panelForm, gbc, "Clasificación:", txtClasificacion = new JTextField(), 1, 2);
        
        // --- Fila 2 ---
        agregarCampoFormulario(panelForm, gbc, "Puntaje (0.0):", txtPuntaje = new JTextField(), 2, 0);
        
        // --- Fila 3: Sipnosis (ocupa más espacio) ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST;
        panelForm.add(new JLabel("Sipnosis:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; gbc.ipady = 40; // Mayor altura
        txtSipnosis = new JTextField();
        panelForm.add(txtSipnosis, gbc);
        gbc.ipady = 0; gbc.gridwidth = 1; // Reset

        // --- Fila 4: Imagen ---
        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(new JLabel("Imagen:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        JPanel panelImagen = new JPanel(new BorderLayout(10, 0));
        panelImagen.setBackground(Color.WHITE);
        btnSeleccionarImagen = new JButton("Seleccionar...");
        lblImagen = new JLabel("No se ha seleccionado ninguna imagen");
        panelImagen.add(btnSeleccionarImagen, BorderLayout.WEST);
        panelImagen.add(lblImagen, BorderLayout.CENTER);
        panelForm.add(panelImagen, gbc);

        // ---- Panel de botones ----
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBackground(Color.WHITE);
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Guardar Cambios");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Limpiar / Cancelar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);

        modelo = new DefaultTableModel(new Object[]{"ID", "Título", "Género", "Duración", "Puntaje"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);

        // ---- Ensamblado Final ----
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.add(panelForm, BorderLayout.NORTH);
        panelCentral.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panelCentral.add(panelBotones, BorderLayout.SOUTH);

        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);

        // ---- Listeners ----
        btnAgregar.addActionListener(e -> agregarPelicula());
        btnEditar.addActionListener(e -> editarPelicula());
        btnEliminar.addActionListener(e -> eliminarPelicula());
        btnActualizar.addActionListener(e -> limpiarCampos());
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDatosDesdeTabla();
        });
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes", "jpg", "jpeg", "png", "gif"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoOriginal = fileChooser.getSelectedFile();
            
            // CAMBIO: Copiamos la imagen a nuestro directorio y obtenemos la nueva ruta.
            String nuevaRuta = ImageManager.saveImage(archivoOriginal);
            
            if (nuevaRuta != null) {
                this.imagenPathSeleccionada = nuevaRuta;
                // Mostramos solo el nombre del archivo para que no sea una ruta larga.
                lblImagen.setText(new File(nuevaRuta).getName()); 
                lblImagen.setForeground(Color.BLACK);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la imagen.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void cargarPeliculas() {
        try {
            modelo.setRowCount(0); 
            List<Pelicula> lista = peliculaDAO.listar();
            for (Pelicula p : lista) {
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getTitulo(),
                    p.getGenero(),
                    p.getDuracion(),
                    String.format("%.1f", p.getPuntaje()) // Añadido puntaje
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las películas: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return; 

        try {
            int id = (int) modelo.getValueAt(fila, 0);
            Pelicula p = peliculaDAO.buscarPorId(id); 
            if (p == null) return;

            txtTitulo.setText(p.getTitulo());
            txtGenero.setText(p.getGenero());
            txtDuracion.setText(String.valueOf(p.getDuracion()));
            txtClasificacion.setText(p.getClasificacion());
            txtSipnosis.setText(p.getSipnosis());
            txtPuntaje.setText(String.valueOf(p.getPuntaje()));
            
            this.imagenPathSeleccionada = p.getImagenPath();
            if (imagenPathSeleccionada != null && !imagenPathSeleccionada.isEmpty()) {
                lblImagen.setText(new File(imagenPathSeleccionada).getName());
            } else {
                lblImagen.setText("No se ha seleccionado ninguna imagen");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos de la película.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarPelicula() {
        if (!validarCampos()) return;

        try {
            
            Pelicula pelicula = new Pelicula(
                txtTitulo.getText(),
                txtGenero.getText(),
                Integer.parseInt(txtDuracion.getText()),
                txtClasificacion.getText(),
                txtSipnosis.getText(),
                imagenPathSeleccionada,
                Double.parseDouble(txtPuntaje.getText())
            );

            peliculaDAO.insertar(pelicula);
            cargarPeliculas();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Película agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La duración y el puntaje deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar la película: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPelicula() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una película de la tabla para editar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        try {
            int id = (int) modelo.getValueAt(fila, 0);
            
            Pelicula pelicula = new Pelicula(
                id,
                txtTitulo.getText(),
                txtGenero.getText(),
                Integer.parseInt(txtDuracion.getText()),
                txtClasificacion.getText(),
                txtSipnosis.getText(),
                imagenPathSeleccionada,
                Double.parseDouble(txtPuntaje.getText())
            );

            peliculaDAO.actualizar(pelicula);
            cargarPeliculas();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Película actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La duración y el puntaje deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al editar la película: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos() {
        txtTitulo.setText("");
        txtGenero.setText("");
        txtDuracion.setText("");
        txtClasificacion.setText("");
        txtSipnosis.setText("");
        txtPuntaje.setText("");
        imagenPathSeleccionada = null;
        lblImagen.setText("No se ha seleccionado ninguna imagen");
        lblImagen.setForeground(Color.GRAY);
        tabla.clearSelection();
    }

    private void eliminarPelicula() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una película para eliminar", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar esta película? Esta acción es irreversible.", 
                "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                
                int id = (int) modelo.getValueAt(fila, 0);
                Pelicula peliculaAEliminar = peliculaDAO.buscarPorId(id);
                if (peliculaAEliminar != null) {
                    ImageManager.deleteImage(peliculaAEliminar.getImagenPath());
                }
                peliculaDAO.eliminar(id);
                cargarPeliculas();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Película eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCampos() {
        return Validador.validarCamposObligatorios(
            Arrays.asList(txtTitulo.getText(), txtGenero.getText(), txtDuracion.getText(), txtPuntaje.getText()),
            Arrays.asList("Título", "Género", "Duración", "Puntaje")
        );
    }

    private void agregarCampoFormulario(JPanel panel, GridBagConstraints gbc, String label, JComponent campo, int fila, int columna) {
        gbc.gridx = columna;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = columna + 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campo, gbc);
    }
}