package org.example.util;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        GradientPaint gp = new GradientPaint(
                0, 0, new Color(20, 50, 80),
                0, getHeight(), new Color(220, 240, 245)
        );

        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
