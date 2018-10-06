package util;

import model.VisualizerComponent;
import ui.Visualizer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionEventManager implements MouseMotionListener {

    private int xMouse;
    private int yMouse;
    private VisualizerComponent vC;


    public MouseMotionEventManager(VisualizerComponent vC)
    {
        this.vC = vC;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        xMouse = e.getX();
        yMouse = e.getY();
        vC.checkHover(xMouse, yMouse);
    }
}
