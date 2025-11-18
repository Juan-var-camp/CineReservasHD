package dao;

import model.Reserva;
import model.HistorialReserva;
import model.ReporteReserva;
import model.ReporteGananciasPelicula;
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
    
    public List<HistorialReserva> listarReservasPorUsuario(int idUsuario) throws SQLException {
        List<HistorialReserva> historial = new ArrayList<>();
        String sql = """
            SELECT 
                p.titulo AS pelicula_titulo,
                s.nombre AS sala_nombre,
                f.fecha AS funcion_fecha,
                f.hora AS funcion_hora,
                r.cantidad AS cantidad,
                r.total AS total,
                r.fecha_reserva AS fecha_reserva
            FROM 
                reservas r
            JOIN 
                funciones f ON r.id_funcion = f.id
            JOIN 
                peliculas p ON f.id_pelicula = p.id
            JOIN 
                salas s ON f.id_sala = s.id
            WHERE 
                r.id_usuario = ?
            ORDER BY 
                r.fecha_reserva DESC
        """;

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historial.add(new HistorialReserva(
                        rs.getString("pelicula_titulo"),
                        rs.getString("sala_nombre"),
                        rs.getString("funcion_fecha"),
                        rs.getString("funcion_hora"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total"),
                        rs.getString("fecha_reserva")
                    ));
                }
            }
        }
        return historial;
    }
    
    public List<ReporteReserva> obtenerReservasParaReporte(String fechaInicio, String fechaFin) throws SQLException {
        List<ReporteReserva> reporte = new ArrayList<>();
        String sql = """
            SELECT 
                p.titulo AS pelicula_titulo,
                s.nombre AS sala_nombre,
                f.fecha AS funcion_fecha,
                f.hora AS funcion_hora,
                r.cantidad AS cantidad,
                r.total AS total,
                r.fecha_reserva AS fecha_reserva,
                u.nombre AS usuario_nombre,
                u.username AS usuario_username
            FROM 
                reservas r
            JOIN 
                usuarios u ON r.id_usuario = u.id
            JOIN 
                funciones f ON r.id_funcion = f.id
            JOIN 
                peliculas p ON f.id_pelicula = p.id
            JOIN 
                salas s ON f.id_sala = s.id
            WHERE 
                DATE(r.fecha_reserva) BETWEEN ? AND ?
            ORDER BY 
                r.fecha_reserva DESC
        """;

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reporte.add(new ReporteReserva(
                        rs.getString("pelicula_titulo"),
                        rs.getString("sala_nombre"),
                        rs.getString("funcion_fecha"),
                        rs.getString("funcion_hora"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total"),
                        rs.getString("fecha_reserva"),
                        rs.getString("usuario_nombre"),
                        rs.getString("usuario_username")
                    ));
                }
            }
        }
        return reporte;
    }
    
    public List<ReporteGananciasPelicula> obtenerGananciasPorPelicula(String fechaInicio, String fechaFin) throws SQLException {
        List<ReporteGananciasPelicula> reporte = new ArrayList<>();
        String sql = """
            SELECT 
                p.titulo AS pelicula_titulo,
                COUNT(r.id) AS cantidad_reservas,
                SUM(r.total) AS total_ganancias
            FROM 
                reservas r
            JOIN 
                funciones f ON r.id_funcion = f.id
            JOIN 
                peliculas p ON f.id_pelicula = p.id
            WHERE 
                DATE(r.fecha_reserva) BETWEEN ? AND ?
            GROUP BY 
                p.titulo
            ORDER BY 
                total_ganancias DESC
        """;
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reporte.add(new ReporteGananciasPelicula(
                        rs.getString("pelicula_titulo"),
                        rs.getInt("cantidad_reservas"),
                        rs.getDouble("total_ganancias")
                    ));
                }
            }
        }
        return reporte;
    }
}