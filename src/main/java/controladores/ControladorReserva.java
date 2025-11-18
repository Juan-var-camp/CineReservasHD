package controladores;

import dao.ReservaDAO;
import model.Reserva;
import model.HistorialReserva;
import java.util.Collections;
import java.sql.SQLException;
import java.util.List;

public class ControladorReserva {
    
    private static ControladorReserva instancia;
    private final ReservaDAO reservaDAO;
    
    private ControladorReserva() {
        reservaDAO = new ReservaDAO();
    }
    
    public static synchronized ControladorReserva getInstanciaControladorReserva() {
        if (instancia == null) {
            instancia = new ControladorReserva();
        }
        return instancia;
    }
    
    public boolean guardarReserva(Reserva reserva) {
        try {
            reservaDAO.guardarReserva(reserva);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Reserva> listarReservas() {
        try {
            return reservaDAO.listarReservas();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public List<HistorialReserva> obtenerHistorialPorUsuario(int idUsuario) {
        try {
            return reservaDAO.listarReservasPorUsuario(idUsuario);
        } catch (SQLException e) {
            System.err.println("Error al obtener el historial de reservas: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // Devuelve una lista vacía en caso de error
        }
    }
    
    public boolean eliminarReserva(int id) {
        try {
            reservaDAO.eliminarReserva(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }    
}