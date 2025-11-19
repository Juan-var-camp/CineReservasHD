package model;

public class PagoTarjeta extends Pago{
    private String titular;
    private String numeroTarjeta;

    public PagoTarjeta(String titular, String numeroTarjeta, double monto) {
        super(monto);
        this.titular = titular;
        this.numeroTarjeta = numeroTarjeta;
    }
    
    public boolean procesarPago(){
        System.out.println("Procesando pago con tarjeta de " + titular);
        return true;
    }
}