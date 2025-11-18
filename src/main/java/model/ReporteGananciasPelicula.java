package model;

public class ReporteGananciasPelicula {
    private final String peliculaTitulo;
    private final int cantidadReservas;
    private final double totalGanancias;

    public ReporteGananciasPelicula(String peliculaTitulo, int cantidadReservas, double totalGanancias) {
        this.peliculaTitulo = peliculaTitulo;
        this.cantidadReservas = cantidadReservas;
        this.totalGanancias = totalGanancias;
    }

    public String getPeliculaTitulo() {
        return peliculaTitulo;
    }

    public int getCantidadReservas() {
        return cantidadReservas;
    }

    public double getTotalGanancias() {
        return totalGanancias;
    }
}