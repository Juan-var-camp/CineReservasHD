package controladores;

import dao.ReservaDAO;
import model.ReporteReserva;
import model.ReporteGananciasPelicula;
import util.GeneradorReportePDF;
import util.GeneradorReporteTXT;
import util.GeneradorReporteGananciasPDF; 
import util.GeneradorReporteGananciasTXT;
import java.io.File;
import java.util.List;

public class ControladorReportes {
    
    private static ControladorReportes instancia;
    private final ReservaDAO reservaDAO;

    private ControladorReportes() {
        this.reservaDAO = new ReservaDAO();
    }
    
    public static synchronized ControladorReportes getInstancia() {
        if (instancia == null) {
            instancia = new ControladorReportes();
        }
        return instancia;
    }

    public boolean generarReporteReservas(String fechaInicio, String fechaFin, String formato, File archivo) {
        try {
            List<ReporteReserva> datos = reservaDAO.obtenerReservasParaReporte(fechaInicio, fechaFin);
            
            if ("pdf".equalsIgnoreCase(formato)) {
                GeneradorReportePDF.generar(datos, archivo, fechaInicio, fechaFin);
            } else if ("txt".equalsIgnoreCase(formato)) {
                GeneradorReporteTXT.generar(datos, archivo, fechaInicio, fechaFin);
            } else {
                return false; // Formato no soportado
            }
            return true;

        } catch (Exception e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
     public boolean generarReporteGanancias(String fechaInicio, String fechaFin, String formato, File archivo) {
        try {
            List<ReporteGananciasPelicula> datos = reservaDAO.obtenerGananciasPorPelicula(fechaInicio, fechaFin);

            if ("pdf".equalsIgnoreCase(formato)) {
                GeneradorReporteGananciasPDF.generar(datos, archivo, fechaInicio, fechaFin);
            } else if ("txt".equalsIgnoreCase(formato)) {
                GeneradorReporteGananciasTXT.generar(datos, archivo, fechaInicio, fechaFin);
            } else {
                return false; // Formato no soportado
            }
            return true;

        } catch (Exception e) {
            System.err.println("Error al generar el reporte de ganancias: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}