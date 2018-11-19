package gui;

import model.VisualizerComponent;
import util.VisualizerApplication;
import static gui.UIKeys.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
        try {
            //ImageIO.read(this.getClass().getResource("/resources/play_Default.png"))
            uiImages.put(toWindowedDefault, ImageIO.read(this.getClass().getResource("/resources/toWindowed_Default.png")));
            uiImages.put(toWindowedHover, ImageIO.read(this.getClass().getResource("/resources/toWindowed_Hover.png")));
            uiImages.put(toWindowedSelected, ImageIO.read(this.getClass().getResource("/resources/toWindowed_Selected.png")));
            uiImages.put(toFullDefault, ImageIO.read(this.getClass().getResource("/resources/toFull_Default.png")));
            uiImages.put(toFullHover, ImageIO.read(this.getClass().getResource("/resources/toFull_Hover.png")));
            uiImages.put(toFullSelected, ImageIO.read(this.getClass().getResource("/resources/toFull_Selected.png")));
            uiImages.put(xDefault, ImageIO.read(this.getClass().getResource("/resources/xDefault.png")));
            uiImages.put(xHover, ImageIO.read(this.getClass().getResource("/resources/xHover.png")));
            uiImages.put(xSelected, ImageIO.read(this.getClass().getResource("/resources/xSelected.png")));

        }
        catch(IOException e)
        {
            System.err.println("Failed to load one or more resources");
        }
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
        x_y_position[0] = x;
        x_y_position[1] = y;
        clicked = false;
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

    private void drawToWindowedOption(Graphics2D g2)
    {
        if(clicked)
        {
            if(checkCollision(windowSizeButton, x_y_pressed[0], x_y_pressed[1]))
            {
                drawPicture(g2, windowSizeButton , uiImages.get(toWindowedSelected));
            }
            else
                drawPicture(g2, windowSizeButton , uiImages.get(toWindowedDefault));

        } else if (checkCollision(windowSizeButton, x_y_position[0], x_y_position[1]))
        {
            drawPicture(g2, windowSizeButton , uiImages.get(toWindowedHover));
        }
        else
            drawPicture(g2, windowSizeButton , uiImages.get(toWindowedDefault));
    }

    private void drawToFullOption(Graphics2D g2)
    {
        if(clicked)
        {
            if(checkCollision(windowSizeButton, x_y_pressed[0], x_y_pressed[1]))
            {
                drawPicture(g2, windowSizeButton , uiImages.get(toFullSelected));
            }
            else
                drawPicture(g2, windowSizeButton , uiImages.get(toFullDefault));

        } else if (checkCollision(windowSizeButton, x_y_position[0], x_y_position[1]))
        {
            drawPicture(g2, windowSizeButton , uiImages.get(toFullHover));
        }
        else
            drawPicture(g2, windowSizeButton , uiImages.get(toFullDefault));
    }


    private void drawXButton(Graphics2D g2)
    {
        if(clicked)
        {
            if(checkCollision(xButton, x_y_pressed[0], x_y_pressed[1]))
            {
                drawPicture(g2, xButton , uiImages.get(xSelected));
            }
            else
                drawPicture(g2, xButton , uiImages.get(xDefault));

        } else if (checkCollision(xButton, x_y_position[0], x_y_position[1]))
        {
            drawPicture(g2, xButton , uiImages.get(xHover));
        }
        else
            drawPicture(g2, xButton , uiImages.get(xDefault));
    }

    @Override
    public void draw(Graphics2D g2, Rectangle enclosing) {
        windowSizeButton = new Rectangle((int)enclosing.getX(), (int)enclosing.getY(), WINDOW_OPTION_SIZE_X, WINDOW_OPTION_SIZE_Y);
        xButton = new Rectangle((int)enclosing.getX()+WINDOW_OPTION_SIZE_X, (int)enclosing.getY(), WINDOW_OPTION_SIZE_X, WINDOW_OPTION_SIZE_Y);
        if(inFullscreen) {
            drawToWindowedOption(g2);
        }
        else
        {
            drawToFullOption(g2);
        }
        drawXButton(g2);
        g2.draw(windowSizeButton);
        g2.draw(xButton);
    }
}
