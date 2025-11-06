package controladores;

import model.Sala;
import model.Reserva;
import view.SeleccionSillasPanel;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class ControladorSeleccionSillas {

    private final Sala sala;
    private final ControladorSala controladorSala;
    private final ControladorReserva controladorReserva;
    private final SeleccionSillasPanel vista;
    
    private static final double PRECIO_SILLA = 12.0;
    
    private final int idFuncion;
    private final int idUsuario;
    

    public ControladorSeleccionSillas(Sala sala, ControladorSala controladorSala, int idFuncion, int idUsuario) {
        
        this.sala = sala;
        this.controladorSala = controladorSala;
        this.controladorReserva = new ControladorReserva();
        this.idFuncion = idFuncion;
        this.idUsuario = idUsuario;
        this.vista = new SeleccionSillasPanel(sala, this);
    }
    
    public SeleccionSillasPanel getVista() {
        return vista;
    }
    
    public void confirmarSeleccion(Set<String> sillasSeleccionadas) {
        if (sillasSeleccionadas.isEmpty()) {
            vista.mostrarMensaje("Debe seleccionar al menos una silla.", "Selección vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = sillasSeleccionadas.size() * PRECIO_SILLA;

        // Mostrar resumen
        StringBuilder resumen = new StringBuilder("<html><div style='text-align:left;'>");
        resumen.append("<b>Sala:</b> ").append(sala.getNombre()).append("<br>");
        resumen.append("<b>Sillas seleccionadas:</b><br>");
        sillasSeleccionadas.stream().sorted().forEach(s -> resumen.append("&nbsp;&nbsp;• ").append(s).append("<br>"));
        resumen.append("<br><b>Total:</b> $").append(total).append("</div></html>");
        vista.mostrarMensaje(resumen.toString(), "Confirmación de Reserva", JOptionPane.INFORMATION_MESSAGE);

        // Actualizar estado de sillas
        actualizarEstadoSillas(sillasSeleccionadas);
        controladorSala.guardarSala(sala);

        // Guardar reserva en la base de datos
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Reserva reserva = new Reserva(idFuncion, idUsuario, sillasSeleccionadas.size(), total, fecha);
        controladorReserva.guardarReserva(reserva);

        vista.mostrarMensaje("Reserva registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        vista.resetearSeleccion();
    }
    
    private void actualizarEstadoSillas(Set<String> sillasSeleccionadas) {
        boolean[][] estado = sala.getEstadoSillas();
        for (String id : sillasSeleccionadas) {
            int fila = id.charAt(0) - 'A';
            int columna = Integer.parseInt(id.substring(1)) - 1;
            if (fila >= 0 && fila < estado.length && columna >= 0 && columna < estado[fila].length) {
                estado[fila][columna] = false;
            }
        }
    }
    
    
}
