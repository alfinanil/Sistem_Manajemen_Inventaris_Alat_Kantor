package org.example.util;

import javax.swing.border.Border;
import java.awt.*;

public class RoundedBorder implements Border {

    private final int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(10, 14, 10, 14);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g,
                            int x, int y, int width, int height) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(200, 200, 200));

        g2.drawRoundRect(
                x + 1,
                y + 1,
                width - 3,
                height - 3,
                radius * 2,
                radius * 2
        );

        g2.dispose();
    }
}
