package model;

import util.VisualizerApplication;

import javax.swing.*;
import java.awt.*;

public class VisualizerComponent extends JComponent {
    private VSquare vS;
    private VBackground vBg;
    private int screenX = getWidth();
    private int screenY = getHeight();
    private VisualizerApplication vApplication;

    public VisualizerComponent(VisualizerApplication vApplication)
    {
        vS = new VSquare();
        vBg = new VBackground();
        this.vApplication = vApplication;
    }

    public void timedActions()
    {
        if(vApplication.isPlaying()) {
            vS.rotate();
            vS.awake();
            vBg.awake();
        }
        else
        {
            vS.asleep();
            vBg.asleep();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        this.screenX = getWidth();
        this.screenY = getHeight();
        Rectangle enclosing = new Rectangle(0, 0, this.screenX, this.screenY);
        //vS.rotate();
        vBg.draw(g2, enclosing);
        vS.draw(g2, enclosing);
        new Color(1,1,1);
    }
}
