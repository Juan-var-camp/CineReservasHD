package dao;

import model.Sala;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {
    
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
        int index = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = binario.charAt(index++) == '1';
            }
        }
        return matriz;
    }
    
    public void guardarSala(Sala sala) throws SQLException {
        String sql = "INSERT OR REPLACE INTO salas (nombre, filas, columnas, sillas) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sala.getNombre());
            ps.setInt(2, sala.getFilas());
            ps.setInt(3, sala.getColumnas());
            ps.setString(4, matrizABinario(sala.getEstadoSillas(), sala.getFilas(), sala.getColumnas()));

            ps.executeUpdate();
        }
    }
    
    public Sala cargarSala(String nombre) throws SQLException {
        String sql = "SELECT * FROM salas WHERE nombre = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int filas = rs.getInt("filas");
                int columnas = rs.getInt("columnas");
                String binario = rs.getString("sillas");

                boolean[][] sillas = binarioAMatriz(binario, filas, columnas);
                return new Sala(nombre, filas, columnas, sillas);
            }
        }
        return null;
    }
    
    public List<Sala> listarSalas() throws SQLException {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM salas";

        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int filas = rs.getInt("filas");
                int columnas = rs.getInt("columnas");
                String binario = rs.getString("sillas");
                boolean[][] sillas = binarioAMatriz(binario, filas, columnas);

                salas.add(new Sala(nombre, filas, columnas, sillas));
            }
        }
        return salas;
    }
    
    public void eliminarSala(String nombre) throws SQLException {
        String sql = "DELETE FROM salas WHERE nombre = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }
    
}
