package model;

public class Asiento {
    private int fila;
    private int columna;
    private boolean disponible; //Libre, Ocupado
    private String codigo;
    private boolean desactivado;
    
    public Asiento(int fila, int columna, String codigo){
        this.fila = fila;
        this.columna = columna;
        this.codigo = codigo;
        this.disponible = true;
        this.desactivado = false;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public void ocupar(){
        this.disponible = false;
    }
    
    public void liberar(){
        this.disponible = true;
    }
    
    public void desactivar(){
        this.codigo = "DESACTIVADO";
        this.desactivado = true;
    }
    
    public boolean isDesactivado(){
        return this.desactivado;
    }
}
