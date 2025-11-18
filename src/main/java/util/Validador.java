package util;

import javax.swing.JOptionPane;
import java.util.List;

public class Validador {
    
    public static boolean validarCamposObligatorios(List<String> campos, List<String> placeholders, String mensaje) {
        for (int i = 0; i < campos.size(); i++) {
            String campo = campos.get(i);
            String placeholder = placeholders.get(i);
            if (campo == null || campo.trim().isEmpty() || campo.equals(placeholder)) {
                JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
    
    public static boolean validarCamposObligatorios(List<String> campos,List<String> placeholders){
        return validarCamposObligatorios(campos, placeholders, "Todos los campos son obligatorios");
    }
}