package controladores;

import dao.UsuarioDAO;
import model.Usuario;

public class ControladorUsuario {


    private static ControladorUsuario instancia;
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioActual;
    
    private ControladorUsuario() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static synchronized ControladorUsuario getInstanciaControladorUsuario() {
        if (instancia == null) {
            instancia = new ControladorUsuario();
        }
        return instancia;
    }
    
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
    
    public boolean registrarUsuario(Usuario usuario) {
        return usuarioDAO.registrar(usuario);
    }

    public Usuario iniciarSesion(String username, String password) {
        usuarioActual = usuarioDAO.login(username, password);
        return usuarioActual;
    }
}