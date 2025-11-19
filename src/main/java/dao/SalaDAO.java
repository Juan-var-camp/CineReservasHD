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
    
    public boolean guardarSala(Sala sala) throws SQLException {
        Sala salaExistente = buscarPorNombre(sala.getNombre());

        if (salaExistente != null) {
            String sqlUpdate = "UPDATE salas SET filas = ?, columnas = ?, sillas_activas = ?, sillas_ocupadas = ? WHERE id = ?";
            try (Connection conn = ConexionDB.conectar();
                 PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {

                ps.setInt(1, sala.getFilas());
                ps.setInt(2, sala.getColumnas());
                ps.setString(3, matrizABinario(sala.getSillasActivas(), sala.getFilas(), sala.getColumnas()));
                ps.setInt(4, salaExistente.getId()); 

                return ps.executeUpdate() > 0;
            }
        } else {
            String sqlInsert = "INSERT INTO salas (nombre, filas, columnas, sillas_activas) VALUES (?, ?, ?, ?)";
            try (Connection conn = ConexionDB.conectar();
                 PreparedStatement ps = conn.prepareStatement(sqlInsert)) {

                ps.setString(1, sala.getNombre());
                ps.setInt(2, sala.getFilas());
                ps.setInt(3, sala.getColumnas());
                ps.setString(4, matrizABinario(sala.getSillasActivas(), sala.getFilas(), sala.getColumnas()));

                return ps.executeUpdate() > 0;
            }
        }
    }

    public Sala cargarSala(int id) throws SQLException {
        String sql = "SELECT * FROM salas WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                int filas = rs.getInt("filas");
                int columnas = rs.getInt("columnas");
                String binarioActivas = rs.getString("sillas_activas");

                boolean[][] sillasActivas = binarioAMatriz(binarioActivas, filas, columnas);
                return new Sala(id, nombre, filas, columnas, sillasActivas);
            }
        }
        return null;
    }
    
    public Sala cargarSala(String nombre) throws SQLException {
        String sql = "SELECT * FROM salas WHERE nombre = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int filas = rs.getInt("filas");
                int columnas = rs.getInt("columnas");
                String binarioActivas = rs.getString("sillas_activas");

                boolean[][] sillasActivas = binarioAMatriz(binarioActivas, filas, columnas);
                return new Sala(id, nombre, filas, columnas, sillasActivas);
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
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int filas = rs.getInt("filas");
                int columnas = rs.getInt("columnas");
                String binarioActivas = rs.getString("sillas_activas");
                boolean[][] sillasActivas = binarioAMatriz(binarioActivas, filas, columnas);

                salas.add(new Sala(id, nombre, filas, columnas, sillasActivas));
            }
        }
        return salas;
    }
    
    public Sala buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM salas WHERE nombre = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int filas = rs.getInt("filas");
                    int columnas = rs.getInt("columnas");
                    String binarioActivas = rs.getString("sillas_activas");

                    boolean[][] sillasActivas = binarioAMatriz(binarioActivas, filas, columnas);
                    return new Sala(id, nombre, filas, columnas, sillasActivas);
                }
            }
        }
        return null; 
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