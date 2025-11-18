package model;

import java.util.List;

public class Cliente extends Usuario{
    public List<Reserva> historialReservas;

    public Cliente(int id, String username, String password, String nombre) {
        super(id, username, password, nombre);
    }
}