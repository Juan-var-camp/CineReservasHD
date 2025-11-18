package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.ReporteGananciasPelicula;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneradorReporteGananciasPDF {
    public static void generar(List<ReporteGananciasPelicula> datos, File archivo, String fechaInicio, String fechaFin) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(archivo));

        document.open();
        
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font fontHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font fontBody = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        document.add(new Paragraph("Reporte de Ganancias por Película", fontTitulo));
        document.add(new Paragraph("Periodo: " + fechaInicio + " al " + fechaFin, fontBody));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        
        String[] headers = {"Película", "Cantidad de Reservas", "Ganancias Totales"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
            table.addCell(cell);
        }

        for (ReporteGananciasPelicula r : datos) {
            table.addCell(new Phrase(r.getPeliculaTitulo(), fontBody));
            table.addCell(new Phrase(String.valueOf(r.getCantidadReservas()), fontBody));
            table.addCell(new Phrase(String.format("$%.2f", r.getTotalGanancias()), fontBody));
        }

        document.add(table);
        document.close();
    }
}