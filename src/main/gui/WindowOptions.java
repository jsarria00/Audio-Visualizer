package gui;

import model.VisualizerComponent;
import util.VisualizerApplication;

import javax.swing.*;
import java.awt.*;

public class WindowOptions extends VisualizerOption implements Selectable{
    private Rectangle xButton;
    private Rectangle windowSizeButton;
    private JFrame heldBy;
    private boolean inFullscreen;
    int x_screen;
    int y_screen;


    public WindowOptions(VisualizerApplication visualizerApplication, JFrame heldBy)
    {
        super(visualizerApplication);
        this.heldBy = heldBy;
        inFullscreen = true;
    }

    @Override
    public void timedEvent() {

    }

    public void toggleFullScreen()
    {
        System.out.println("Toggling fullscreen.");
        if(inFullscreen)
        {
            inFullscreen = false;
            x_screen = heldBy.getX();
            y_screen = heldBy.getY();
            heldBy.dispose();
            heldBy.setSize(x_screen,y_screen); // User can move window and it will automatically resize to a smaller one.
            heldBy.setUndecorated(false);
            heldBy.revalidate();
            heldBy.setVisible(true);
        }
        else
        {
            inFullscreen = true;
            heldBy.dispose();
            heldBy.setExtendedState(JFrame.MAXIMIZED_BOTH);
            heldBy.setUndecorated(true);
            heldBy.revalidate();
            heldBy.setVisible(true);
        }
    }
    @Override
    public void releasedEvent(int x, int y) {
        boolean inHitBox = checkCollision(windowSizeButton, x, y);
        if(inHitBox)
        {
            toggleFullScreen();
        }
        inHitBox = checkCollision(xButton, x, y);
        if(inHitBox)
        {
            System.out.println("Closing Visualizer via GUI");
            System.exit(0);
        }
    }

    @Override
    public void draw(Graphics2D g2, Rectangle enclosing) {
        windowSizeButton = new Rectangle((int)enclosing.getX(), (int)enclosing.getY(), WINDOW_OPTION_SIZE_X, WINDOW_OPTION_SIZE_Y);
        xButton = new Rectangle((int)enclosing.getX()+WINDOW_OPTION_SIZE_X, (int)enclosing.getY(), WINDOW_OPTION_SIZE_X, WINDOW_OPTION_SIZE_Y);
        g2.draw(windowSizeButton);
        g2.draw(xButton);
    }
}
