package util;

import model.VisualizerComponent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class CanvasMouseEventManager implements MouseListener {
    private VisualizerComponent vC;
    private int xMouse;
    private int yMouse;
    // constructor
    public CanvasMouseEventManager()
    {
    }

    public void setVisualizerComponent(VisualizerComponent vC)
    {
        if(this.vC == null || !this.vC.equals(vC))
        {
            this.vC = vC;
            this.vC.setCanvasMouseManager(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CanvasMouseEventManager that = (CanvasMouseEventManager) o;
        return xMouse == that.xMouse &&
                yMouse == that.yMouse &&
                Objects.equals(vC, that.vC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vC, xMouse, yMouse);
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
