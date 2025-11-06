package view;

import dao.PeliculaDAO;
import model.Pelicula;
import util.Validador;
import dao.ConexionDB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class PanelPeliculas extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtTitulo, txtGenero, txtDuracion;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar, btnSeleccionarImagen;
    private JLabel lblImagen;
    private PeliculaDAO peliculaDAO;
    private File imagenSeleccionada;
    private JPanel panelForm;

    public PanelPeliculas() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        
        

        try {
            peliculaDAO = new PeliculaDAO(ConexionDB.conectar());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ---- Panel de título ----
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 60, 100));
        panelTitulo.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("Gestión de Películas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);

        // ---- Panel de formulario ----
        panelForm = new JPanel();
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 255), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        panelForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        agregarCampoFormulario(panelForm, gbc, "Título:", txtTitulo = crearCampoEstilizado(), 0);
        agregarCampoFormulario(panelForm, gbc, "Género:", txtGenero = crearCampoEstilizado(), 1);
        agregarCampoFormulario(panelForm, gbc, "Duración (min):", txtDuracion = crearCampoEstilizado(), 2);

        // Campo de imagen
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panelForm.add(new JLabel("Imagen:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel panelImagen = new JPanel(new BorderLayout(10, 0));
        panelImagen.setBackground(Color.WHITE);
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        estilizarBoton(btnSeleccionarImagen, new Color(0, 120, 215), Color.WHITE);
        
        lblImagen = new JLabel("No se ha seleccionado ninguna imagen");
        lblImagen.setForeground(Color.GRAY);
        lblImagen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panelImagen.add(btnSeleccionarImagen, BorderLayout.WEST);
        panelImagen.add(lblImagen, BorderLayout.CENTER);
        panelForm.add(panelImagen, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(15, 5, 5, 5);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBackground(Color.WHITE);
        
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar Lista");

        estilizarBoton(btnAgregar, new Color(0, 120, 215), Color.WHITE);
        estilizarBoton(btnEditar, new Color(255, 165, 0), Color.WHITE);
        estilizarBoton(btnEliminar, new Color(220, 53, 69), Color.WHITE);
        estilizarBoton(btnActualizar, new Color(108, 117, 125), Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        
        panelForm.add(panelBotones, gbc);

        // ---- Tabla ----
        modelo = new DefaultTableModel(new Object[]{"ID", "Título", "Género", "Duración", "Imagen"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(30, 60, 100));
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        // Listener para cargar datos al seleccionar una fila
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosDesdeTabla();
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 255)));

        // ---- Diseño principal ----
        add(panelTitulo, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(new Color(240, 248, 255));
        panelCentral.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelCentral.add(panelForm, BorderLayout.NORTH);
        panelCentral.add(scroll, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);

        cargarPeliculas();

        // ---- Listeners ----
        btnAgregar.addActionListener(e -> agregarPelicula());
        btnEditar.addActionListener(e -> editarPelicula());
        btnEliminar.addActionListener(e -> eliminarPelicula());
        btnActualizar.addActionListener(e -> cargarPeliculas());
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
    }

    private void agregarCampoFormulario(JPanel panel, GridBagConstraints gbc, String label, JComponent campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(campo, gbc);
    }

    private JTextField crearCampoEstilizado() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 255), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        campo.setPreferredSize(new Dimension(200, 30));
        return campo;
    }

    private void estilizarBoton(JButton boton, Color fondo, Color texto) {
        boton.setFocusPainted(false);
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(fondo.darker());
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes", "jpg", "jpeg", "png", "gif"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            imagenSeleccionada = fileChooser.getSelectedFile();
            lblImagen.setText(imagenSeleccionada.getName());
            lblImagen.setForeground(Color.BLACK);
        }
    }

    private void cargarPeliculas() {
        try {
            modelo.setRowCount(0);
            List<Pelicula> lista = peliculaDAO.listar();
            for (Pelicula p : lista) {
                String nombreImagen = p.getImagenPath() != null ? 
                    new File(p.getImagenPath()).getName() : "Sin imagen";
                modelo.addRow(new Object[]{
                    p.getId(), 
                    p.getTitulo(), 
                    p.getGenero(), 
                    p.getDuracion(),
                    nombreImagen
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las películas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtTitulo.setText(modelo.getValueAt(fila, 1).toString());
            txtGenero.setText(modelo.getValueAt(fila, 2).toString());
            txtDuracion.setText(modelo.getValueAt(fila, 3).toString());
            
            // Para la imagen, necesitarías cargarla desde la base de datos
            // Por ahora, simplemente limpiamos la selección
            imagenSeleccionada = null;
            lblImagen.setText("Seleccione nueva imagen para actualizar");
            lblImagen.setForeground(Color.GRAY);
        }
    }

    private void agregarPelicula() {
        try {
            if (!validarCampos()) return;

            String titulo = txtTitulo.getText();
            String genero = txtGenero.getText();
            int duracion = Integer.parseInt(txtDuracion.getText());
            String imagenPath = imagenSeleccionada != null ? imagenSeleccionada.getAbsolutePath() : null;

            Pelicula pelicula = new Pelicula(titulo, genero, duracion, "", "", imagenPath);
            pelicula.setImagenPath(imagenPath);

            peliculaDAO.insertar(pelicula);
            cargarPeliculas();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Película agregada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos o error al agregar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPelicula() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una película para editar", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (!validarCampos()) return;

            int id = (int) modelo.getValueAt(fila, 0);
            String titulo = txtTitulo.getText();
            String genero = txtGenero.getText();
            int duracion = Integer.parseInt(txtDuracion.getText());
            String imagenPath = imagenSeleccionada != null ? imagenSeleccionada.getAbsolutePath() : null;

            Pelicula pelicula = new Pelicula(id, titulo, genero, duracion, "", "", imagenPath);
            pelicula.setImagenPath(imagenPath);

            peliculaDAO.actualizar(pelicula);
            cargarPeliculas();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Película actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar la película: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPelicula() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una película para eliminar", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar esta película?", 
                "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION);
                
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modelo.getValueAt(fila, 0);
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
            Arrays.asList(txtTitulo.getText(), txtGenero.getText(), txtDuracion.getText()),
            Arrays.asList("Título", "Género", "Duración")
        );
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtGenero.setText("");
        txtDuracion.setText("");
        imagenSeleccionada = null;
        lblImagen.setText("No se ha seleccionado ninguna imagen");
        lblImagen.setForeground(Color.GRAY);
        tabla.clearSelection();
    }
}