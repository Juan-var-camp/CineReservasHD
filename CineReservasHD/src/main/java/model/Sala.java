package model;
import java.util.HashMap;
import java.util.Map;

public class Sala {
    private int id;
    private String nombre;
    private int nfilas;
    private int ncolumnas;
    private Asiento[][] asientos;
    private Map<Integer, Character> numerosALetras = new HashMap<>();

    public Sala(int id, String nombre, int nfilas, int ncolumnas) {
        this.id = id;
        this.nombre = nombre;
        this.nfilas = nfilas;
        this.ncolumnas = ncolumnas;
        this.asientos = new Asiento[nfilas][ncolumnas];
        
        for (int i = 0; i < 26; i++) {
            numerosALetras.put(i, (char) ('A' + i));
        }
        
        generarAsientos();
        
    }
    
    private void generarAsientos() {
        for (int i = 0; i < nfilas; i++) {
            char letraFila = numerosALetras.get(i);
            for (int j = 0; j < ncolumnas; j++) {
                String codigo = letraFila + String.valueOf(j + 1);
                asientos[i][j] = new Asiento(i, j, codigo);
            }
        }
    }
    
    public void desactivarAsiento(int fila, int columna){
        if (fila < 0 || fila >= nfilas || columna < 0 || columna >= ncolumnas) {
            System.out.println("⚠️ Posición fuera de rango.");
            return;
        }
        
        asientos[fila][columna].desactivar();
        
        char letraFila = numerosALetras.get(fila);
        int contador = 1;
        for (int j = 0; j < ncolumnas; j++) {
            if (!asientos[fila][j].isDesactivado()) {
                asientos[fila][j].setCodigo(letraFila + String.valueOf(contador));
                contador++;
            }
        }
        
    }
    
    // Imprimir toda la matriz
    public void imprimirSala() {
        for (int i = 0; i < nfilas; i++) {
            for (int j = 0; j < ncolumnas; j++) {
                System.out.print(asientos[i][j].getCodigo() + "\t");
            }
            System.out.println();
        }
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Asiento[][] getAsientos() {
        return asientos;
    }

    public void setAsientos(Asiento[][] asientos) {
        this.asientos = asientos;
    }
}
