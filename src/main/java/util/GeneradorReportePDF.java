package util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.ReporteReserva;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneradorReportePDF {

    public static void generar(List<ReporteReserva> datos, File archivo, String fechaInicio, String fechaFin) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(archivo));

        document.open();
        
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font fontHeader = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font fontBody = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

        document.add(new Paragraph("Reporte de Reservas - CineReservasHD", fontTitulo));
        document.add(new Paragraph("Periodo: " + fechaInicio + " al " + fechaFin, fontBody));
        document.add(new Paragraph(" ")); // Salto de línea

        PdfPTable table = new PdfPTable(8); // 8 columnas
        table.setWidthPercentage(100);
        
        String[] headers = {"Usuario", "Película", "Sala", "Fecha Función", "Hora", "Asientos", "Total", "Fecha Reserva"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
            table.addCell(cell);
        }

        for (ReporteReserva r : datos) {
            table.addCell(new Phrase(r.getNombreUsuario(), fontBody));
            table.addCell(new Phrase(r.getPeliculaTitulo(), fontBody));
            table.addCell(new Phrase(r.getSalaNombre(), fontBody));
            table.addCell(new Phrase(r.getFuncionFecha(), fontBody));
            table.addCell(new Phrase(r.getFuncionHora(), fontBody));
            table.addCell(new Phrase(String.valueOf(r.getCantidadAsientos()), fontBody));
            table.addCell(new Phrase(String.format("$%.2f", r.getTotalPagado()), fontBody));
            table.addCell(new Phrase(r.getFechaDeReserva(), fontBody));
        }

        document.add(table);
        document.close();
    }
}