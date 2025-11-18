package util;

import model.ReporteReserva;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GeneradorReporteTXT {

    public static void generar(List<ReporteReserva> datos, File archivo, String fechaInicio, String fechaFin) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("===================================================");
            writer.println("      REPORTE DE RESERVAS - CINERESERVASHD");
            writer.println("===================================================");
            writer.println(" Periodo: " + fechaInicio + " al " + fechaFin);
            writer.println();

            String formatoFila = "%-20s | %-25s | %-15s | %-12s | %-10s | %-10s%n";
            writer.printf(formatoFila, "Usuario", "Película", "Sala", "Total", "Asientos", "Fecha Reserva");
            writer.println("-".repeat(110));

            for (ReporteReserva r : datos) {
                writer.printf(formatoFila,
                        r.getNombreUsuario(),
                        r.getPeliculaTitulo(),
                        r.getSalaNombre(),
                        String.format("$%.2f", r.getTotalPagado()),
                        r.getCantidadAsientos(),
                        r.getFechaDeReserva()
                );
            }
            
            writer.println();
            writer.println("--- FIN DEL REPORTE ---");
        }
    }
}