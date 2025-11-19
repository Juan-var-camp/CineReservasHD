// --- START OF FILE model/HistorialReserva.java ---
package model;

public class HistorialReserva {
    private final String peliculaTitulo;
    private final String salaNombre;
    private final String funcionFecha;
    private final String funcionHora;
    private final int cantidadAsientos;
    private final double totalPagado;
    private final String fechaDeReserva;

    public HistorialReserva(String peliculaTitulo, String salaNombre, String funcionFecha, 
                            String funcionHora, int cantidadAsientos, double totalPagado, 
                            String fechaDeReserva) {
        this.peliculaTitulo = peliculaTitulo;
        this.salaNombre = salaNombre;
        this.funcionFecha = funcionFecha;
        this.funcionHora = funcionHora;
        this.cantidadAsientos = cantidadAsientos;
        this.totalPagado = totalPagado;
        this.fechaDeReserva = fechaDeReserva;
    }

    public String getPeliculaTitulo() { return peliculaTitulo; }
    public String getSalaNombre() { return salaNombre; }
    public String getFuncionFecha() { return funcionFecha; }
    public String getFuncionHora() { return funcionHora; }
    public int getCantidadAsientos() { return cantidadAsientos; }
    public double getTotalPagado() { return totalPagado; }
    public String getFechaDeReserva() { return fechaDeReserva; }
}