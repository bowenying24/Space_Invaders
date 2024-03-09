package org.cis1200;

import java.awt.*;
import javax.swing.JPanel;

public class MenuPaintComponent extends JPanel {

    /**
     * To draw in the panel
     * @param g, graphic component
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        g2.drawLine(20, 600, 610, 600);
    }
}