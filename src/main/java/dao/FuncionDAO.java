package dao;

import java.util.ArrayList;
import java.util.List;
import model.Funcion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionDAO {

    public boolean guardarFuncion(Funcion funcion) throws SQLException {
        String sql = "INSERT INTO funciones (id_pelicula, id_sala, fecha, hora, precio) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, funcion.getIdPelicula());
            ps.setInt(2, funcion.getIdSala());
            ps.setString(3, funcion.getFecha());
            ps.setString(4, funcion.getHora());
            ps.setDouble(5, funcion.getPrecio());

            return ps.executeUpdate() > 0;
        }
    }
    
    
    public boolean existeFuncion(int idSala, String fecha, String hora) throws SQLException {
        String sql = "SELECT COUNT(*) FROM funciones WHERE id_sala = ? AND fecha = ? AND hora = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSala);
            ps.setString(2, fecha);
            ps.setString(3, hora);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si el conteo es mayor que 0, significa que ya existe.
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public List<Funcion> listarPorPelicula(int idPelicula) throws SQLException {
        List<Funcion> funciones = new ArrayList<>();
        // Unimos con la tabla de salas para obtener el nombre de la sala directamente
        String sql = "SELECT f.*, s.nombre as nombre_sala FROM funciones f " +
                     "JOIN salas s ON f.id_sala = s.id " +
                     "WHERE f.id_pelicula = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idPelicula);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Funcion funcion = new Funcion(
                        rs.getInt("id"),
                        rs.getInt("id_pelicula"),
                        rs.getInt("id_sala"),
                        rs.getString("fecha"),
                        rs.getString("hora"),
                        rs.getDouble("precio")
                    );
                    // Guardamos el nombre de la sala en un campo transitorio del modelo
                    funcion.setNombreSala(rs.getString("nombre_sala"));
                    funciones.add(funcion);
                }
            }
        }
        return funciones;
    }
}