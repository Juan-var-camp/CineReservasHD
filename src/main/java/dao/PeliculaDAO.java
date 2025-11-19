package dao;

import model.Pelicula;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {
    
    public PeliculaDAO() {    }

    public void crearTabla() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS peliculas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                genero TEXT NOT NULL,
                duracion INTEGER NOT NULL,
                clasificacion TEXT,
                sipnosis TEXT,
                imagen_path TEXT,
                puntaje REAL     
            );
        """;
        
        try (Connection conn = ConexionDB.conectar();
                Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        }
    }

    public void insertar(Pelicula p) throws SQLException {
        String sql = "INSERT INTO peliculas (titulo, genero, duracion, clasificacion, sipnosis, imagen_path, puntaje) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getGenero());
            ps.setInt(3, p.getDuracion());
            ps.setString(4, p.getClasificacion());
            ps.setString(5, p.getSipnosis());
            ps.setString(6, p.getImagenPath());
            ps.setDouble(7, p.getPuntaje());
            ps.executeUpdate();
        }
    }

    public List<Pelicula> listar() throws SQLException {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM peliculas";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pelicula(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("duracion"),
                    rs.getString("clasificacion"),
                    rs.getString("sipnosis"),
                    rs.getString("imagen_path"),
                    rs.getDouble("puntaje")
                ));
            }
        }
        return lista;
    }

    public void actualizar(Pelicula p) throws SQLException {
        String sql = "UPDATE peliculas SET titulo=?, genero=?, duracion=?, clasificacion=?, sipnosis=?, imagen_path=?, puntaje=? WHERE id=?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getGenero());
            ps.setInt(3, p.getDuracion());
            ps.setString(4, p.getClasificacion());
            ps.setString(5, p.getSipnosis());
            ps.setString(6, p.getImagenPath());
            ps.setDouble(7, p.getPuntaje()); 
            ps.setInt(8, p.getId());         
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM peliculas WHERE id=?";
        try (Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Pelicula buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM peliculas WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Pelicula(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("duracion"),
                    rs.getString("clasificacion"),
                    rs.getString("sipnosis"),
                    rs.getString("imagen_path"),
                    rs.getDouble("puntaje")
                );
            }
        }
        return null;
    }
}