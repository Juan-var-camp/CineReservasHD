package controladores;

import dao.SalaDAO;
import model.Sala;
import java.sql.SQLException;
import java.util.List;

public class ControladorSala {
    
    private final SalaDAO salaDAO;
    
    public ControladorSala() {
        salaDAO = new SalaDAO();
    }
    
    public boolean guardarSala(Sala sala) {
        try {
            salaDAO.guardarSala(sala);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Obtener una sala por nombre
    public Sala cargarSala(String nombre) {
        try {
            return salaDAO.cargarSala(nombre);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Listar todas las salas
    public List<Sala> listarSalas() {
        try {
            return salaDAO.listarSalas();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    // Eliminar una sala por nombre
    public boolean eliminarSala(String nombre) {
        try {
            salaDAO.eliminarSala(nombre);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
