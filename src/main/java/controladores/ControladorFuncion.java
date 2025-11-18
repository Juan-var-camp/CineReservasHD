package controladores;

import dao.FuncionDAO;
import model.Funcion;
import java.sql.SQLException;
import java.util.Collections; 
import java.util.List;

public class ControladorFuncion {

    private static ControladorFuncion instancia;
    private final FuncionDAO funcionDAO;

    private ControladorFuncion() {
        this.funcionDAO = new FuncionDAO();
    }

    public static synchronized ControladorFuncion getInstancia() {
        if (instancia == null) {
            instancia = new ControladorFuncion();
        }
        return instancia;
    }
    
    public List<Funcion> listarFuncionesPorPelicula(int idPelicula) {
        try {
            return funcionDAO.listarPorPelicula(idPelicula);
        } catch (SQLException e) {
            System.err.println("Error al listar funciones por película: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // Devuelve una lista vacía segura
        }
    }

    public String crearFuncionConValidacion(int idPelicula, int idSala, String fecha, String hora, double precio) {
        try {
            if (funcionDAO.existeFuncion(idSala, fecha, hora)) {
                return "Ya existe una función programada para esa sala, en esa fecha y a esa hora.";
            }

            Funcion nuevaFuncion = new Funcion(idPelicula, idSala, fecha, hora, precio);
            if (funcionDAO.guardarFuncion(nuevaFuncion)) {
                return null; 
            } else {
                return "No se pudo guardar la función en la base de datos.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error de base de datos: " + e.getMessage();
        }
    }
}