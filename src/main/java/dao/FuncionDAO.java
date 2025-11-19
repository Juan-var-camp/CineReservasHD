package dao;

import java.util.ArrayList;
import java.util.List;
import model.Funcion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionDAO {
    
    private String matrizABinario(boolean[][] matriz, int filas, int columnas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                sb.append(matriz[i][j] ? '1' : '0');
            }
        }
        return sb.toString();
    }
    
    private boolean[][] binarioAMatriz(String binario, int filas, int columnas) {
        boolean[][] matriz = new boolean[filas][columnas];
        if(binario == null || binario.length() != filas * columnas) {
            return matriz; // Retorna matriz de false por defecto
        }
        int index = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = binario.charAt(index++) == '1';
            }
        }
        return matriz;
    }

    public boolean guardarFuncion(Funcion funcion) throws SQLException {
        String sqlSala = "SELECT filas, columnas FROM salas WHERE id = ?";
        int filas = 0, columnas = 0;
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement psSala = conn.prepareStatement(sqlSala)) {
            psSala.setInt(1, funcion.getIdSala());
            try(ResultSet rs = psSala.executeQuery()){
                if(rs.next()){
                    filas = rs.getInt("filas");
                    columnas = rs.getInt("columnas");
                } else {
                    return false; // Sala no encontrada
                }
            }
        }

        String sillasOcupadasInicial = "0".repeat(filas * columnas);

        String sql = "INSERT INTO funciones (id_pelicula, id_sala, fecha, hora, precio, sillas_ocupadas) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, funcion.getIdPelicula());
            ps.setInt(2, funcion.getIdSala());
            ps.setString(3, funcion.getFecha());
            ps.setString(4, funcion.getHora());
            ps.setDouble(5, funcion.getPrecio());
            ps.setString(6, sillasOcupadasInicial);

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
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public List<Funcion> listarPorPelicula(int idPelicula) throws SQLException {
        List<Funcion> funciones = new ArrayList<>();
        String sql = "SELECT f.*, s.nombre as nombre_sala, s.filas, s.columnas FROM funciones f " +
                     "JOIN salas s ON f.id_sala = s.id " +
                     "WHERE f.id_pelicula = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idPelicula);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int filas = rs.getInt("filas");
                    int columnas = rs.getInt("columnas");
                    Funcion funcion = new Funcion(
                        rs.getInt("id"),
                        rs.getInt("id_pelicula"),
                        rs.getInt("id_sala"),
                        rs.getString("fecha"),
                        rs.getString("hora"),
                        rs.getDouble("precio")
                    );
                    funcion.setNombreSala(rs.getString("nombre_sala"));
                    funcion.setFilasSala(filas);
                    funcion.setColumnasSala(columnas);
                    funcion.setSillasOcupadas(binarioAMatriz(rs.getString("sillas_ocupadas"), filas, columnas));
                    funciones.add(funcion);
                }
            }
        }
        return funciones;
    }
    
    public boolean actualizarSillasOcupadas(Funcion funcion) throws SQLException {
        String sql = "UPDATE funciones SET sillas_ocupadas = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String sillasBinario = matrizABinario(funcion.getSillasOcupadas(), funcion.getFilasSala(), funcion.getColumnasSala());
            ps.setString(1, sillasBinario);
            ps.setInt(2, funcion.getId());

            return ps.executeUpdate() > 0;
        }
    }
}