package util;

import model.ReporteGananciasPelicula;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GeneradorReporteGananciasTXT {
    public static void generar(List<ReporteGananciasPelicula> datos, File archivo, String fechaInicio, String fechaFin) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("===================================================");
            writer.println("    REPORTE DE GANANCIAS POR PELÍCULA");
            writer.println("===================================================");
            writer.println(" Periodo: " + fechaInicio + " al " + fechaFin);
            writer.println();

            String formatoFila = "%-40s | %-20s | %-20s%n";
            writer.printf(formatoFila, "Película", "Reservas Totales", "Ganancias");
            writer.println("-".repeat(90));

            for (ReporteGananciasPelicula r : datos) {
                writer.printf(formatoFila,
                        r.getPeliculaTitulo(),
                        r.getCantidadReservas(),
                        String.format("$%.2f", r.getTotalGanancias())
                );
            }
            
            writer.println();
            writer.println("--- FIN DEL REPORTE ---");
        }
    }
}