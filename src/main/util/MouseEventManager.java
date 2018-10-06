package util;

import model.VisualizerComponent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEventManager implements MouseListener {
    private VisualizerComponent vC;
    private int xMouse;
    private int yMouse;
    // constructor
    public MouseEventManager(VisualizerComponent vC)
    {
        this.vC = vC;

    }
    @Override
    public void mousePressed(MouseEvent event)
    {
        if(event.getButton() == MouseEvent.BUTTON1)
        {
            xMouse = event.getX();
            yMouse = event.getY();
            vC.checkSelection(xMouse,yMouse);
        }

    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
        if(event.getButton() == MouseEvent.BUTTON1) {
            xMouse = event.getX();
            yMouse = event.getY();
            vC.selection(xMouse, yMouse);
        }
    }
    @Override
    public void mouseClicked(MouseEvent event) {}
    @Override
    public void mouseEntered(MouseEvent event) {}
    @Override
    public void mouseExited(MouseEvent event) {}

}
