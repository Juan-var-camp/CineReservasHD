package model;

import javax.swing.JButton;

public class Sala {
    private int id;
    private String nombre;
    private int filas;
    private int columnas;
    private boolean[][] sillasActivas;

    public Sala(int id, String nombre, int filas, int columnas, boolean[][] sillasActivas) {
        this.id = id;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.sillasActivas = sillasActivas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean[][] getSillasActivas() {
        return sillasActivas;
    }

    public boolean isActiva(int fila, int columna) {
        return sillasActivas[fila][columna];
    }
    
    public static Sala desdeBotones(String nombre, JButton[][] botones, int filas, int columnas) {
        boolean[][] estadoDesactivadas = new boolean[filas][columnas];
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                estadoDesactivadas[i][j] = botones[i][j].isEnabled();
            }
        }
        return new Sala(0, nombre, filas, columnas, estadoDesactivadas);
    }
}