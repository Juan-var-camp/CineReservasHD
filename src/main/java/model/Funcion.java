package model;

public class Funcion {
    private int id;
    private int idPelicula;
    private int idSala;
    private String fecha;
    private String hora;
    private double precio;
    private String nombreSala;

    public Funcion(int idPelicula, int idSala, String fecha, String hora, double precio) {
        this.idPelicula = idPelicula;
        this.idSala = idSala;
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
    }

    public Funcion(int id, int idPelicula, int idSala, String fecha, String hora, double precio) {
        this.id = id;
        this.idPelicula = idPelicula;
        this.idSala = idSala;
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }
}