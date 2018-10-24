package gui;

import util.VisualizerApplication;

import java.awt.*;
import java.awt.image.BufferedImage;
public abstract class VisualizerOption implements Selectable {


    protected static VisualizerApplication vApplication;
    protected boolean clicked;
    protected int[] x_y_pressed;
    protected int[] x_y_position;


    public VisualizerOption(VisualizerApplication vApplication) {
        this.vApplication = vApplication;
        clicked = false;
        x_y_pressed = new int[NUMBER_OF_DIMENSIONS];
        x_y_pressed[0] = 0;
        x_y_pressed[1] = 0;
        x_y_position = new int[NUMBER_OF_DIMENSIONS];
        x_y_position[0] = 0;
        x_y_position[1] = 0;
    }

    protected boolean checkCollision (Rectangle container, int x, int y)
    {
        return (((x > container.getX()))
                && (x < container.getX()+ container.getWidth())
                && (y > container.getY())
                && (y < container.getY()+ container.getHeight()));
    }

    protected boolean checkSelection (Rectangle container, int x, int y, int xRel, int yRel)
    {
        return (((xRel > container.getX())&&(x > container.getX()))
                && ((xRel < container.getX() + container.getWidth())&&(x < container.getX()+ container.getWidth()))
                && ((yRel > container.getY())&&(y > container.getY()))
                && ((yRel < container.getY() + container.getHeight())&&(y < container.getY()+ container.getHeight()))

        );
    }

    protected void drawPicture(Graphics2D g2, Rectangle container, BufferedImage bI)
    {
        g2.drawImage(bI,  (int)container.getX(), (int)container.getY(), null);

    }

    public abstract void timedEvent();

    public abstract void clickedEvent(int x, int y);

    public abstract void releasedEvent(int x, int y);

    public abstract void hoverEvent(int x, int y);


    public abstract void draw(Graphics2D g2, Rectangle enclosing);

}
