package dao;

import model.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    
    public void guardarReserva(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (id_funcion, id_usuario, cantidad, total, fecha_reserva) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getIdFuncion());
            stmt.setInt(2, reserva.getIdUsuario());
            stmt.setInt(3, reserva.getCantidad());
            stmt.setDouble(4, reserva.getTotal());
            stmt.setString(5, reserva.getFechaReserva());
            stmt.executeUpdate();
        }
    }
    
    public List<Reserva> listarReservas() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("id"));
                r.setIdFuncion(rs.getInt("id_funcion"));
                r.setIdUsuario(rs.getInt("id_usuario"));
                r.setCantidad(rs.getInt("cantidad"));
                r.setTotal(rs.getDouble("total"));
                r.setFechaReserva(rs.getString("fecha_reserva"));
                lista.add(r);
            }
        }
        return lista;
    }
    
    public void eliminarReserva(int id) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
}
