package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageManager {

    private static final String IMAGE_DIRECTORY = "images";

    
    public static String saveImage(File sourceFile) {
        if (sourceFile == null) {
            return null;
        }
        
        try {
            File destDir = new File(IMAGE_DIRECTORY);
            if (!destDir.exists()) {
                destDir.mkdirs(); 
            }

            String originalFileName = sourceFile.getName();
            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i); 
            }
            String uniqueFileName = "pelicula_" + System.currentTimeMillis() + fileExtension;

            Path sourcePath = sourceFile.toPath();
            Path destinationPath = Paths.get(IMAGE_DIRECTORY, uniqueFileName);

            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            return destinationPath.toString();

        } catch (IOException e) {
            System.err.println("Error al guardar la imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    
    public static void deleteImage(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            return;
        }
        
        try {
            File fileToDelete = new File(relativePath);
            if (fileToDelete.exists()) {
                if (fileToDelete.delete()) {
                    System.out.println("Imagen eliminada: " + relativePath);
                } else {
                    System.err.println("No se pudo eliminar la imagen: " + relativePath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar la imagen: " + e.getMessage());
        }
    }
}