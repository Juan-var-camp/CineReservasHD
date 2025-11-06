package controladores;

import dao.UsuarioDAO;
import model.Usuario;

public class ControladorUsuario {
    
    private UsuarioDAO usuarioDAO;
    
    public ControladorUsuario() {
        usuarioDAO = new UsuarioDAO();
    }
    
    public boolean registrarUsuario(Usuario usuario) {
        return usuarioDAO.registrar(usuario);
    }

    public Usuario iniciarSesion(String username, String password) {
        
        return usuarioDAO.login(username, password);
    }
}
