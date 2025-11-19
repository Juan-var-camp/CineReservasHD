package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:src/cinereservas.db";
    private static Connection connection = null;

    public static Connection conectar() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return connection;
    }
    
    public static void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public static void inicializarDatabase() {
        try (Statement stmt = conectar().createStatement()) {

            String sqlPelicula = """
                CREATE TABLE IF NOT EXISTS peliculas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    genero TEXT,
                    duracion INTEGER,
                    clasificacion TEXT,
                    sipnosis TEXT,
                    imagen_path TEXT,
                    puntaje REAL              
                );
            """;

            String sqlSala = """
                CREATE TABLE IF NOT EXISTS salas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL UNIQUE,
                    filas INTEGER NOT NULL,
                    columnas INTEGER NOT NULL,
                    sillas_activas TEXT      
                );
            """;

            String sqlFuncion = """
                CREATE TABLE IF NOT EXISTS funciones (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_pelicula INTEGER,
                    id_sala INTEGER,
                    fecha TEXT,
                    hora TEXT,
                    precio REAL,
                    sillas_ocupadas TEXT,
                    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id),
                    FOREIGN KEY (id_sala) REFERENCES salas(id)
                );
            """;

            String sqlUsuario = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,            
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,           
                    tipo TEXT
                );
            """;

            String sqlReserva = """
                CREATE TABLE IF NOT EXISTS reservas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_funcion INTEGER,
                    id_usuario INTEGER,
                    cantidad INTEGER,
                    total REAL,
                    fecha_reserva TEXT,
                    FOREIGN KEY (id_funcion) REFERENCES funciones(id),
                    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
                );
            """;

            stmt.execute(sqlPelicula);
            stmt.execute(sqlSala);
            stmt.execute(sqlFuncion);
            stmt.execute(sqlUsuario);
            stmt.execute(sqlReserva);

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}