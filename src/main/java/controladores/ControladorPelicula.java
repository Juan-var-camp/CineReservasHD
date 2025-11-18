package controladores;

import dao.PeliculaDAO;
import model.Pelicula;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ControladorPelicula {
    
    private static ControladorPelicula instancia;
    private final PeliculaDAO peliculaDAO;
    
    private ControladorPelicula() {
        this.peliculaDAO = new PeliculaDAO();
    }
    
    public static synchronized ControladorPelicula getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPelicula();
        }
        return instancia;
    }
    
    public List<Pelicula> listarPeliculas() {
        try {
            return peliculaDAO.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar películas: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); 
        }
    }
}