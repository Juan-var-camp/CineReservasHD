package controladores;

import dao.ReservaDAO;
import model.Reserva;
import java.sql.SQLException;
import java.util.List;

public class ControladorReserva {
    
    private final ReservaDAO reservaDAO;
    
    public ControladorReserva() {
        reservaDAO = new ReservaDAO();
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
