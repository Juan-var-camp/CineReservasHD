package model;

import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private Cliente cliente;
    private Funcion funcion;
    private List<Asiento> asientos;
    private Pago pago;

    public Reserva(Cliente cliente, Funcion funcion, List<Asiento> asientos, Pago pago) {
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = asientos;
    }
    
    public void realizarPago(Pago pago){
        if (true) {
            this.pago = pago;
            System.out.println("Reserva confirmada con pago exitoso.");
        } else {
            System.out.println("Error al procesar el pago.");
        }
    }
}
