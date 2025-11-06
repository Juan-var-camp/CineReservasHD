package dao;

import model.Usuario;
import java.sql.*;
import util.HashUtil;

public class UsuarioDAO {

    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO usuarios(nombre, username, password, tipo) VALUES (?, ?, ?, ?)"; // Faltaba un parámetro
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConexionDB.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsername());
            ps.setString(3, HashUtil.hashPasword(u.getPassword()));
            ps.setString(4, "Cliente");
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error registrando: " + e.getMessage());
            return false;
        } finally {
            // Cerrar recursos manualmente
            try {
                if (ps != null) ps.close();
                // No cerramos la conexión aquí para reutilizarla
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public Usuario login(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ?"; // Cambié "usuario" por "username"
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                String hashGuardado = rs.getString("password");
                String hashIngresado = HashUtil.hashPasword(password);
                
                if (hashGuardado.equals(hashIngresado)) {
                    Usuario u = new Usuario();
                    u.setNombre(rs.getString("nombre"));
                    u.setUsername(rs.getString("username"));
                    u.setTipo(rs.getString("tipo"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en login: " + e.getMessage());
        } finally {
            // Cerrar recursos manualmente
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                // No cerramos la conexión aquí para reutilizarla
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
        return null;
    }
}