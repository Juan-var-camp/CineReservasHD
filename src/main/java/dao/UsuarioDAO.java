package dao;

import model.Usuario;
import java.sql.*;
import util.HashUtil;

public class UsuarioDAO {

    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO usuarios(nombre, username, password, tipo) VALUES (?, ?, ?, ?)"; 
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsername());
            ps.setString(3, HashUtil.hashPasword(u.getPassword()));
            ps.setString(4, "Cliente");
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error registrando: " + e.getMessage());
            return false;
        }
    }

    public Usuario login(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ?"; 
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("password");
                    String hashIngresado = HashUtil.hashPasword(password);
                    
                    if (hashGuardado.equals(hashIngresado)) {
                        Usuario u = new Usuario();
                        u.setId(rs.getInt("id"));
                        u.setNombre(rs.getString("nombre"));
                        u.setUsername(rs.getString("username"));
                        u.setTipo(rs.getString("tipo"));
                        return u;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en login: " + e.getMessage());
        }
        return null;
    }
}