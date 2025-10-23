package model;

import java.util.List;

public class Cliente extends Usuario{
    public List<Reserva> historialReservas;

    public Cliente(String username, String password, String nombre) {
        super(username, password, nombre);
    }
    
}
