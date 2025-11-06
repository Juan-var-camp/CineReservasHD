package model;

public class Reserva {
    private int id;
    private int idFuncion;
    private int idUsuario;
    private int cantidad;
    private double total;
    private String fechaReserva;

    public Reserva() {}

    public Reserva(int idFuncion, int idUsuario, int cantidad, double total, String fechaReserva) {
        this.idFuncion = idFuncion;
        this.idUsuario = idUsuario;
        this.cantidad = cantidad;
        this.total = total;
        this.fechaReserva = fechaReserva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
}
