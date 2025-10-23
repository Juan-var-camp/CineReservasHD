package model;

public class Pelicula {
    private int id;
    private String titulo;
    private String genero;
    private int duracion; // en minutos
    private String clasificacion;
    private String sipnosis;

    public Pelicula(int id, String titulo, String genero, int duracion, String clasificacion, String sipnosis) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
        this.sipnosis = sipnosis;
    }

    public Pelicula(String titulo, String genero, int duracion, String clasificacion, String sipnosis) {
        this(0, titulo, genero, duracion, clasificacion, sipnosis);
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getGenero() { return genero; }
    public int getDuracion() { return duracion; }
    public String getClasificacion() { return clasificacion; }

    public String getSipnosis() { return sipnosis; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setClasificacion(String clasificacion) {this.clasificacion = clasificacion;}
    public void setSipnosis(String sipnosis) {this.sipnosis = sipnosis;}

    @Override
    public String toString() {
        return String.format("%d - %s (%s, %d min)", id, titulo, genero, duracion);
    }
}
