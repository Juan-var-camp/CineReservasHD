package controladores;

import dao.SalaDAO;
import model.Sala;
import java.sql.SQLException;
import java.util.List;

public class ControladorSala {
    
    private static ControladorSala instancia;
    private final SalaDAO salaDAO;
    private  Sala salaActual;
    
    private ControladorSala() {
        salaDAO = new SalaDAO();
        
    }
    
    public static synchronized ControladorSala getInstanciaControladorSala() {
        if (instancia == null) {
            instancia = new ControladorSala();
        }
        return instancia;
    }
    
    
    

    public Sala getSalaActual() {
        return salaActual;
    }
    
    
    
    public boolean guardarSala(Sala sala) {
        try {
            // El DAO ahora devuelve un boolean que indica el éxito real de la operación.
            return salaDAO.guardarSala(sala);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Obtener una sala por nombre
    public Sala cargarSala(String nombre) {
        try {
            this.salaActual = salaDAO.cargarSala(nombre);
            return salaActual;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Sala cargarSala(int id) {
        try {
            this.salaActual = salaDAO.cargarSala(id);
            return salaActual;
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