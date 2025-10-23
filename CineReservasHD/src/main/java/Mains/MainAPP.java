package Mains;

import dao.ConexionDB;
import dao.PeliculaDAO;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import model.Pelicula;

public class MainAPP {
    public static void main(String[] args) {
        System.out.println("Hola");
        
        try (Connection conn = ConexionDB.conectar()) {
            PeliculaDAO dao = new PeliculaDAO(conn);
            
            Scanner sc = new Scanner(System.in);
            int opcion;

            do {
                System.out.println("\n--- GESTIÓN DE PELÍCULAS ---");
                System.out.println("1. Agregar");
                System.out.println("2. Listar");
                System.out.println("3. Editar");
                System.out.println("4. Eliminar");
                System.out.println("0. Salir");
                System.out.print("Opción: ");
                opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Título: ");
                        String t = sc.nextLine();
                        System.out.print("Género: ");
                        String g = sc.nextLine();
                        System.out.print("Duración (min): ");
                        int d = sc.nextInt();
                        System.out.println("Clasificacion: ");
                        String c = sc.nextLine();
                        System.out.println("Sipnosis: ");
                        String s = sc.nextLine();
                        dao.insertar(new Pelicula(t, g, d, c, s));
                    }
                    case 2 -> {
                        List<Pelicula> lista = dao.listar();
                        lista.forEach(System.out::println);
                    }
                    case 3 -> {
                        System.out.print("ID de la película a editar: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Nuevo título: ");
                        String nt = sc.nextLine();
                        System.out.print("Nuevo género: ");
                        String ng = sc.nextLine();
                        System.out.print("Nueva duración: ");
                        int nd = sc.nextInt();
                        System.out.print("Nuevo género: ");
                        String nc = sc.nextLine();
                        System.out.print("Nuevo género: ");
                        String ns = sc.nextLine();
                        dao.actualizar(new Pelicula(id, nt, ng, nd, nc, ns));
                    }
                    case 4 -> {
                        System.out.print("ID a eliminar: ");
                        int id = sc.nextInt();
                        dao.eliminar(id);
                    }
                }
            } while (opcion != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
