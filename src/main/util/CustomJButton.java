package util;

import javax.swing.*;
import java.awt.*;


public class CustomJButton  extends JButton {
    public CustomJButton(String text)
    {
        super(text);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g.create();
        Point start = new Point(0,0); // Initial gradient colour
        Point end = new Point(0, getHeight()); // Colour to shade too
        Color first = new Color(79, 81, 83);// Initial gradient point
        Color second = new Color(22, 25, 27); // End gradient
        this.setForeground(Color.WHITE);

        g2.setPaint(new GradientPaint(start, first, end, second));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();//Renews button appearance to new settings
        super.paintComponent(g);
    }
}
