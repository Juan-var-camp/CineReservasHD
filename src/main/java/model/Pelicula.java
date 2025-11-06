package model;

public class Pelicula {
    private int id;
    private String titulo;
    private String genero;
    private int duracion; // en minutos
    private String clasificacion;
    private String sipnosis;
    private String imagenPath; // Nueva propiedad para la imagen

    // Constructor completo
    public Pelicula(int id, String titulo, String genero, int duracion, String clasificacion, String sipnosis, String imagenPath) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
        this.sipnosis = sipnosis;
        this.imagenPath = imagenPath;
    }

    // Constructor sin ID
    public Pelicula(String titulo, String genero, int duracion, String clasificacion, String sipnosis, String imagenPath) {
        this(0, titulo, genero, duracion, clasificacion, sipnosis, imagenPath);
    }

    // Constructor simplificado para compatibilidad (puedes eliminarlo luego)
    public Pelicula(int id, String titulo, String genero, int duracion) {
        this(id, titulo, genero, duracion, "", "", null);
    }

    public Pelicula(String titulo, String genero, int duracion) {
        this(0, titulo, genero, duracion, "", "", null);
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getGenero() { return genero; }
    public int getDuracion() { return duracion; }
    public String getClasificacion() { return clasificacion; }
    public String getSipnosis() { return sipnosis; }
    public String getImagenPath() { return imagenPath; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
    public void setSipnosis(String sipnosis) { this.sipnosis = sipnosis; }
    public void setImagenPath(String imagenPath) { this.imagenPath = imagenPath; }

    @Override
    public String toString() {
        return String.format("%d - %s (%s, %d min)", id, titulo, genero, duracion);
    }
}