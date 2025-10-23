package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:src/cinereservas.db";
    private static Connection connection;

    public static Connection conectar() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("Conexión establecida con SQLite");
            return conn;
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
    }
    
    public static void inicializarDatabase() {
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {

            String sqlPelicula = """
                CREATE TABLE IF NOT EXISTS peliculas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    genero TEXT,
                    duracion INTEGER,
                    clasificacion TEXT,
                    sipnosis TEXT
                );
            """;

            String sqlSala = """
                CREATE TABLE IF NOT EXISTS salas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    capacidad INTEGER NOT NULL
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
                    FOREIGN KEY (id_pelicula) REFERENCES pelicula(id),
                    FOREIGN KEY (id_sala) REFERENCES sala(id)
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
                    FOREIGN KEY (id_funcion) REFERENCES funcion(id),
                    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
                );
            """;

            stmt.execute(sqlPelicula);
            stmt.execute(sqlSala);
            stmt.execute(sqlFuncion);
            stmt.execute(sqlUsuario);
            stmt.execute(sqlReserva);

            System.out.println("Base de datos inicializada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }    
}
