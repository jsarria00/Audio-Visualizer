package gui;

import model.VisualizerComponent;
import util.VisualizerApplication;

import javax.swing.*;
import java.awt.*;

public class WindowOptions extends VisualizerOption implements Selectable{
    Rectangle xButton;
    Rectangle windowSizeButton;

    public WindowOptions(VisualizerApplication visualizerApplication)
    {
        super(visualizerApplication);
    }

    @Override
    public void timedEvent() {

    }

    @Override
    public void releasedEvent(int x, int y) {

    }

    @Override
    public void draw(Graphics2D g2, Rectangle enclosing) {
        windowSizeButton = new Rectangle((int)enclosing.getX(), (int)enclosing.getY(), WINDOW_OPTION_SIZE_X, WINDOW_OPTION_SIZE_Y);
        xButton = new Rectangle((int)enclosing.getX()+WINDOW_OPTION_SIZE_X, (int)enclosing.getY(), WINDOW_OPTION_SIZE_X, WINDOW_OPTION_SIZE_Y);
        g2.draw(windowSizeButton);
        g2.draw(xButton);
    }
}
