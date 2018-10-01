package gui;


import util.SongEntry;
import util.SongLog;
import util.VisualizerApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SongLogger extends VisualizerOption {
    private boolean opened;
    private int xPosition;
    private ArrayList<SongEntry> log;
    private Rectangle openButton;
    private ArrayList<Rectangle> songsLogged;


    public SongLogger(VisualizerApplication vApplication)
    {
        super(vApplication);
        this.vApplication = vApplication;
        log = this.vApplication.getLog().getSongList();
        xPosition = SONG_LOG_SIZE_X;
        opened = false;
        songsLogged = new ArrayList<>();
    }

    private void toggleState()
    {
        opened = !opened;
    }

    @Override
    public void timedEvent()
    {
        if(opened) {
            if(xPosition > 0)
            {
                xPosition -= SONG_LOG_SIZE_X/20;
            }
        }
        else
        {
            if(xPosition < SONG_LOG_SIZE_X)
            {
                xPosition += SONG_LOG_SIZE_X/20;
            }
        }
    }


    @Override
    public void clickedEvent(int x, int y)
    {
        clicked = true;
        x_y_pressed[0] = x;
        x_y_pressed[1] = y;

    }

    @Override
    public void releasedEvent(int x, int y)
    {
        //since Hover can't be heard while mouse is clicked - This corrects things being highlighted that are not being hovered over after release
        x_y_position[0] = x;
        x_y_position[1] = y;


        clicked = false;
        //for Readability
        boolean inHitBox = checkSelection(openButton, x_y_pressed[0], x_y_pressed[1], x, y);
        if(inHitBox)
        {
            toggleState();
        }
        int songIndex = log.size() - 1;
        for(Rectangle r: songsLogged)
        {
            inHitBox = checkSelection(r, x_y_pressed[0], x_y_pressed[1], x, y);
            if(inHitBox)
            {
                vApplication.load(log.get(songIndex).getSongDirectory());
            }
            songIndex--;
        }

    }

    @Override
    public void hoverEvent(int x, int y)
    {
        x_y_position[0] = x;
        x_y_position[1] = y;
    }


    @Override
    public void draw(Graphics2D g2, Rectangle enclosure)
    {
        Rectangle panelRectangle = new Rectangle((int)enclosure.getX()+xPosition, (int)enclosure.getY(), (int)enclosure.getWidth(), (int)enclosure.getHeight());
        openButton = new Rectangle((int)panelRectangle.getX()-SONG_LOG_TOGGLE_SIZE, (int)panelRectangle.getY(), SONG_LOG_TOGGLE_SIZE, SONG_LOG_TOGGLE_SIZE);
        //DRAW ENCLOSURES
        g2.setColor(new Color(0));
        drawPicture(g2, openButton , "src/main/images/historyButton_Default.png");
        if(clicked)
        {
            if(checkCollision(openButton, x_y_pressed[0], x_y_pressed[1]))
            {
                drawPicture(g2, openButton , "src/main/images/historyButton_Selected.png");
            }

        } else if (checkCollision(openButton, x_y_position[0], x_y_position[1]))
        {
            drawPicture(g2, openButton , "src/main/images/historyButton_Hover.png");
        }
        g2.draw(openButton);
        //
        songsLogged = new ArrayList<>();
        int spacing = OPTION_SPACING;

        //Since list is backwards we need to decrement a counter
        int size = log.size();
        //Draw enclosure depending on mouse condition
        g2.setColor(new Color(0,0,0));
        g2.setFont(new Font("TimesRoman", Font.BOLD, 20));

        g2.setColor(new Color(195, 195, 195));
        g2.fill(panelRectangle);
        g2.setColor(new Color(0));

        //Hide entities that are not in frame performance increase
        if(xPosition<SONG_LOG_SIZE_X)
        {
            g2.draw(panelRectangle);
            for(int i = size -1; i >=0 ; i--)
            {
                SongEntry sE = log.get(i);
                Rectangle songButton = new Rectangle((int)panelRectangle.getX() + OPTION_SPACING, (int)enclosure.getY()+spacing, SONG_LOG_ITEM_X, SONG_LOG_ITEM_Y);
                songsLogged.add(songButton);
                spacing += OPTION_SPACING + SONG_LOG_ITEM_Y;
                drawPicture(g2, songButton, "src/main/images/logItem_Default.png");
                g2.drawString(sE.getSongName(), (int)(songButton.getX()+OPTION_SPACING*2), (int)(songButton.getY()+songButton.getHeight()/2 + OPTION_SPACING));


                if(clicked) {
                    if(checkCollision(songButton, x_y_pressed[0], x_y_pressed[1])) {
                        drawPicture(g2, songButton, "src/main/images/logItem_Selected.png");
                        g2.drawString(sE.getSongName(), (int)(songButton.getX()+OPTION_SPACING*2), (int)(songButton.getY()+songButton.getHeight()/2 + OPTION_SPACING));
                    }
                } else // hover
                if(checkCollision(songButton, x_y_position[0], x_y_position[1])) {
                    drawPicture(g2, songButton, "src/main/images/logItem_Hover.png");
                    g2.drawString(sE.getSongName(), (int)(songButton.getX()+OPTION_SPACING*2), (int)(songButton.getY()+songButton.getHeight()/2 + OPTION_SPACING));
                }

            }
        }
        //for condition to check debounce -

        //for condition to draw rectangles

        //g2.fill(panelRectangle);

    }


}
