package util;

import model.VisualizerComponent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

public class MouseMotionEventManager implements MouseMotionListener {

    private int xMouse;
    private int yMouse;
    private VisualizerComponent vC;


    public MouseMotionEventManager()
    {
    }

    public void setVisualizerComponent(VisualizerComponent vC)
    {
        if(this.vC == null || !this.vC.equals(vC))
        {
            this.vC = vC;
            this.vC.setMouseMotionManager(this);
            System.out.println("xddd");
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MouseMotionEventManager that = (MouseMotionEventManager) o;
        return xMouse == that.xMouse &&
                yMouse == that.yMouse &&
                Objects.equals(vC, that.vC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xMouse, yMouse, vC);
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
