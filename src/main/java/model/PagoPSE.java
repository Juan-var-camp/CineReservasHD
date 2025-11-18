package model;

public class PagoPSE extends Pago {
    private String banco;

    public PagoPSE(String banco, double monto) {
        super(monto);
        this.banco = banco;
    }
    
    public boolean procesarPago(){
        //Simular pago con PSE
        System.out.println("Procesando pago por PSE con el banco: " + this.banco);
        return true;
    }
}