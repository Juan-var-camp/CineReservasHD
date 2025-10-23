package model;

public class Pago {
    private double monto;
    private String fecha;
    
    public Pago(double monto) {
        this.monto = monto;
        this.fecha = java.time.LocalDate.now().toString();
    }

    public double getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }
    
}
