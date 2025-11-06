package model;

import java.time.LocalDateTime; // Clase recomendada para fechas y horas

public class Funcion {
    private Pelicula pelicula;
    private SalaDeprecated sala;
    private LocalDateTime horario; 

    public Funcion(Pelicula pelicula, SalaDeprecated sala, LocalDateTime horario) {
        this.pelicula = pelicula;
        this.sala = sala;
        this.horario = horario;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public SalaDeprecated getSala() {
        return sala;
    }

    public LocalDateTime getHorario() {
        return horario;
    }
    
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public void setSala(SalaDeprecated sala) {
        this.sala = sala;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return String.format("Película: %s, Sala: %s, Hora: %s", 
                             pelicula.getTitulo(), 
                             sala.getNombre(), 
                             horario.toLocalTime().toString());
    }
}