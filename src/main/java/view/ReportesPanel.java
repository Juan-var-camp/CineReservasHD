package view;

import com.toedter.calendar.JDateChooser;
import controladores.ControladorReportes;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportesPanel extends JPanel {

    private JDateChooser dateChooserInicio, dateChooserFin;
    private JRadioButton radioPDF, radioTXT;
    private JRadioButton radioReservas, radioGanancias;
    private JButton btnGenerar;
    private final ControladorReportes controladorReportes;

    public ReportesPanel() {
        this.controladorReportes = ControladorReportes.getInstancia();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 40, 20, 40));
        setBackground(new Color(240, 248, 255));

        // Título
        JLabel lblTitulo = new JLabel("Generación de Reportes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de configuración
        JPanel panelConfig = new JPanel(new GridBagLayout());
        panelConfig.setBorder(BorderFactory.createTitledBorder("Configuración del Reporte"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; panelConfig.add(new JLabel("Tipo de Reporte:"), gbc);
        radioReservas = new JRadioButton("Reservas Detalladas", true);
        radioGanancias = new JRadioButton("Ganancias por Película");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(radioReservas);
        grupoTipo.add(radioGanancias);
        JPanel panelTipoReporte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipoReporte.add(radioReservas);
        panelTipoReporte.add(radioGanancias);
        gbc.gridx = 1; panelConfig.add(panelTipoReporte, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelConfig.add(new JLabel("Fecha de Inicio:"), gbc);
        gbc.gridx = 1; dateChooserInicio = new JDateChooser(); panelConfig.add(dateChooserInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelConfig.add(new JLabel("Fecha de Fin:"), gbc);
        gbc.gridx = 1; dateChooserFin = new JDateChooser(); panelConfig.add(dateChooserFin, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panelConfig.add(new JLabel("Formato de Archivo:"), gbc);
        radioPDF = new JRadioButton("PDF", true);
        radioTXT = new JRadioButton("TXT");
        ButtonGroup grupoFormato = new ButtonGroup();
        grupoFormato.add(radioPDF);
        grupoFormato.add(radioTXT);
        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRadios.add(radioPDF);
        panelRadios.add(radioTXT);
        gbc.gridx = 1; panelConfig.add(panelRadios, gbc);
        
        add(panelConfig, BorderLayout.CENTER);

        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnGenerar);
        add(panelBoton, BorderLayout.SOUTH);
        
        btnGenerar.addActionListener(e -> generarReporte());
    }
    
    private void generarReporte() {
        Date fechaInicio = dateChooserInicio.getDate();
        Date fechaFin = dateChooserFin.getDate();

        if (fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de inicio y fin.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String formato = radioPDF.isSelected() ? "pdf" : "txt";
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte");
        
        String nombreArchivoSugerido = radioReservas.isSelected() 
            ? "Reporte_Reservas." + formato 
            : "Reporte_Ganancias." + formato;
        
        fileChooser.setSelectedFile(new File(nombreArchivoSugerido));
        fileChooser.setFileFilter(new FileNameExtensionFilter(formato.toUpperCase() + " Files", formato));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaInicioStr = sdf.format(fechaInicio);
            String fechaFinStr = sdf.format(fechaFin);
            
            boolean exito;
            
            if (radioReservas.isSelected()) {
                exito = controladorReportes.generarReporteReservas(fechaInicioStr, fechaFinStr, formato, archivo);
            } else {
                exito = controladorReportes.generarReporteGanancias(fechaInicioStr, fechaFinStr, formato, archivo);
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, "Reporte generado exitosamente en:\n" + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al generar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}