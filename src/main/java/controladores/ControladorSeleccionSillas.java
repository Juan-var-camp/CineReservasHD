package controladores;

import model.Reserva;
import java.awt.Point;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import model.Funcion;
import model.Sala;

public class ControladorSeleccionSillas {

    private final ControladorSala controladorSala;
    private final ControladorReserva controladorReserva;
    private final ControladorUsuario controladorUsuario;
    private final ControladorFuncion controladorFuncion;
        
    private final Funcion funcionActual;
    private final Sala salaActual;

    public ControladorSeleccionSillas(Funcion funcion) {
        
        this.controladorSala = ControladorSala.getInstanciaControladorSala();
        this.controladorReserva = ControladorReserva.getInstanciaControladorReserva();
        this.controladorUsuario = ControladorUsuario.getInstanciaControladorUsuario();
        this.controladorFuncion = ControladorFuncion.getInstancia();
        
        this.funcionActual = funcion;
        this.salaActual = controladorSala.cargarSala(funcion.getIdSala());
    }
    
    public boolean procesarReserva(Set<Point> coordenadasSeleccionadas) {
        if (coordenadasSeleccionadas == null || coordenadasSeleccionadas.isEmpty()) {
            return false; 
        }

        try {
            double total = coordenadasSeleccionadas.size() * funcionActual.getPrecio();
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int idUsuario = controladorUsuario.getUsuarioActual().getId();
            Reserva nuevaReserva = new Reserva(funcionActual.getId(), idUsuario, coordenadasSeleccionadas.size(), total, fecha);

            if (!controladorReserva.guardarReserva(nuevaReserva)) {
                return false; 
            }

            actualizarEstadoSillasOcupadas(coordenadasSeleccionadas);
            controladorFuncion.guardarEstadoSillas(funcionActual);
            
            return true; 

        } catch (Exception e) {
            System.err.println("Error procesando la reserva: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void actualizarEstadoSillasOcupadas(Set<Point> coordenadas) {
        boolean[][] ocupadas = funcionActual.getSillasOcupadas();
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

    public Sala getSalaActual(){
        return this.salaActual;
    }

    public int getIdFuncion() {
        return funcionActual.getId();
    }

    public ControladorUsuario getControladorUsuario() {
        return controladorUsuario;
    }

    public ControladorReserva getControladorReserva() {
        return controladorReserva;
    }
}