package model;

import javax.swing.JButton;


public class Sala {
    private String nombre;
    private int filas;
    private int columnas;
    private boolean[][] estadoSillas;

    public Sala(String nombre, int filas, int columnas, boolean[][] estadoSillas) {
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.estadoSillas = estadoSillas;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public boolean[][] getEstadoSillas() {
        return estadoSillas;
    }

    public void setEstadoSillas(boolean[][] estadoSillas) {
        this.estadoSillas = estadoSillas;
    }
    
    public static Sala desdeBotones(String nombre, JButton[][] botones, int filas, int columnas) {
        boolean[][] estado = new boolean[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                estado[i][j] = botones[i][j].isEnabled();
            }
        }
        return new Sala(nombre, filas, columnas, estado);
    }
}
