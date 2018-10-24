package util;

import model.VisualizerComponent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SliderMouseEventManager implements MouseListener {
    private VisualizerComponent vC;

    public SliderMouseEventManager(VisualizerComponent vC)
    {
        this.vC = vC;

    }
    @Override
    public void mousePressed(MouseEvent event)
    {
        if(event.getButton() == MouseEvent.BUTTON1)
        {
            vC.clickedSlider();
        }

    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
        if(event.getButton() == MouseEvent.BUTTON1) {
            vC.releasedSlider();
        }
    }
    @Override
    public void mouseClicked(MouseEvent event) {}
    @Override
    public void mouseEntered(MouseEvent event) {}
    @Override
    public void mouseExited(MouseEvent event) {}
}
