package controladores;

import model.Reserva;
import java.awt.Point; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class ControladorSeleccionSillas {

    private final ControladorSala controladorSala;
    private final ControladorReserva controladorReserva;
    private ControladorUsuario controladorUsuario;
        
    private final int idFuncion;  //Esto puede quitarse despues de agregar el 
    private final double precioFuncion;

    public ControladorSeleccionSillas(String nombreSala, int idFuncion, double precioFuncion) {
        this.controladorSala = ControladorSala.getInstanciaControladorSala();
        this.controladorReserva = ControladorReserva.getInstanciaControladorReserva();
        this.controladorUsuario = ControladorUsuario.getInstanciaControladorUsuario();
        this.idFuncion = idFuncion;
        this.precioFuncion = precioFuncion;
        
        controladorSala.cargarSala(nombreSala);
    }
    
    public boolean procesarReserva(Set<Point> coordenadasSeleccionadas) {
        if (coordenadasSeleccionadas == null || coordenadasSeleccionadas.isEmpty()) {
            return false; 
        }

        try {
            double total = coordenadasSeleccionadas.size() * this.precioFuncion;
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int idUsuario = controladorUsuario.getUsuarioActual().getId();
            Reserva nuevaReserva = new Reserva(idFuncion, idUsuario, coordenadasSeleccionadas.size(), total, fecha);

            if (!controladorReserva.guardarReserva(nuevaReserva)) {
                return false; 
            }

            // CAMBIO: El método de actualización también recibe las coordenadas
            actualizarEstadoSillasOcupadas(coordenadasSeleccionadas);
            controladorSala.guardarSala(controladorSala.getSalaActual());
            
            return true; // Éxito

        } catch (Exception e) {
            System.err.println("Error procesando la reserva: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void actualizarEstadoSillasOcupadas(Set<Point> coordenadas) {
        boolean[][] ocupadas = controladorSala.getSalaActual().getSillasOcupadas();
        for (Point p : coordenadas) {
            int fila = p.x; // Asumiendo que guardamos (fila, columna)
            int columna = p.y;

            // Verificación de límites para seguridad
            if (fila >= 0 && fila < ocupadas.length && columna >= 0 && columna < ocupadas[fila].length) {
                ocupadas[fila][columna] = true;
            }
        }
    }
   
    public ControladorSala getControladorSala(){
        return controladorSala;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public ControladorUsuario getControladorUsuario() {
        return controladorUsuario;
    }
    
    public ControladorReserva getControladorReserva() {
        return controladorReserva;
    }
}