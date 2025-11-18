package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;

public class SeatButton extends JButton {

    private final int gridRow;
    private final int gridCol;
    private boolean ocupada;
    
    private static final Color COLOR_OCUPADA = new Color(220, 20, 60);
    private static final Color COLOR_DISPONIBLE = new Color(144, 238, 144);

    public SeatButton(String text, int gridRow, int gridCol) {
        super(text);
        this.gridRow = gridRow;
        this.gridCol = gridCol;
        
        this.ocupada = false;
        setOpaque(true);
        setBorderPainted(true);
        setFocusPainted(false);
        setContentAreaFilled(true);
        setFont(new Font("Arial", Font.BOLD, 12));
        setMargin(new Insets(2, 2, 2, 2));
        setForeground(Color.BLACK);
        setFocusPainted(false);
    }
    
    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
        if (ocupada) {
            setBackground(COLOR_OCUPADA);
            setToolTipText("Asiento ocupado");
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            setBackground(COLOR_DISPONIBLE);
            setToolTipText("Asiento disponible");
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
    
    public boolean isOcupada() {
        return ocupada;
    }

    public int getGridRow() {
        return gridRow;
    }

    public int getGridCol() {
        return gridCol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatButton that = (SeatButton) o;
        return gridRow == that.gridRow && gridCol == that.gridCol;
    }

    @Override
    public int hashCode() {
        return 31 * gridRow + gridCol;
    }
}