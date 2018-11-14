package gui;

import javafx.util.Duration;
import util.MediaAlreadyLoadedException;
import util.VisualizerApplication;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static gui.UIKeys.*;

public class MediaOptions extends VisualizerOption{

    private boolean isPlaying;
    private Rectangle playButton;
    private Rectangle fileSelectButton;
    private Rectangle queueWindowButton;
    private int errorMessageTime;


    public MediaOptions(VisualizerApplication vApplication)
    {
        super(vApplication);
        isPlaying = false;
        errorMessageTime = 0;
        try {
            //ImageIO.read(this.getClass().getResource("/resources/play_Default.png"))
            uiImages.put(playDefault, ImageIO.read(this.getClass().getResource("/resources/play_Default.png")));
            uiImages.put(playSelected, ImageIO.read(this.getClass().getResource("/resources/play_Selected.png")));
            uiImages.put(playHover, ImageIO.read(this.getClass().getResource("/resources/play_Hover.png")));
            uiImages.put(pauseDefault, ImageIO.read(this.getClass().getResource("/resources/pause_Default.png")));
            uiImages.put(pauseSelected, ImageIO.read(this.getClass().getResource("/resources/pause_Selected.png")));
            uiImages.put(pauseHover, ImageIO.read(this.getClass().getResource("/resources/pause_Hover.png")));
            uiImages.put(fileSelectDefault, ImageIO.read(this.getClass().getResource("/resources/fileSelect_Default.png")));
            uiImages.put(fileSelectSelected, ImageIO.read(this.getClass().getResource("/resources/fileSelect_Selected.png")));
            uiImages.put(fileSelectHover, ImageIO.read(this.getClass().getResource("/resources/fileSelect_Hover.png")));
        }
        catch(IOException e)
        {
            System.err.println("Failed to load one or more resources");
        }
    }

    @Override
    public void timedEvent()
    {
        if(errorMessageTime > 0)
        {
            errorMessageTime--;
        }
        isPlaying = vApplication.isPlaying();
    }

    @Override
    public void releasedEvent(int x, int y)
    {
        x_y_position[0] = x;
        x_y_position[1] = y;



        clicked = false;

        boolean inHitBox = checkSelection(playButton, x_y_pressed[0], x_y_pressed[1], x, y);
        if(inHitBox)
        {
            vApplication.togglePlayState();
        }
        inHitBox = checkSelection(fileSelectButton, x_y_pressed[0], x_y_pressed[1], x, y);
        if(inHitBox)
        {
            loadFile();
        }
        inHitBox = checkSelection(queueWindowButton, x_y_pressed[0], x_y_pressed[1], x, y);
        if(inHitBox)
        {
            vApplication.toggleVisibility();
        }
    }

    private void loadFile() {
        vApplication.turnOffQueue();
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(new FileNameExtensionFilter(".mp3", "mp3"));
        File f = new File(MEDIA_DIRECTORY);
        selector.setCurrentDirectory(f);
        int option = selector.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            String fileDir = selector.getSelectedFile().toString();
            System.out.println(fileDir + " was the selected path and file \nRequesting Visualizer Application to load file" );
            try {
                    vApplication.load(fileDir);
                    isPlaying = false;
            }
            catch(MediaAlreadyLoadedException e)
            {
                errorMessageTime = 1000;
                System.err.println("Media was already loaded, aborting request.");
            }
        }
    }



    private void drawPauseButton(Graphics2D g2)
    {
        if(clicked)
        {
            if(checkCollision(playButton, x_y_pressed[0], x_y_pressed[1]))
            {
                drawPicture(g2, playButton , uiImages.get(pauseSelected));
            }
            else
                drawPicture(g2, playButton , uiImages.get(pauseDefault));

        } else if (checkCollision(playButton, x_y_position[0], x_y_position[1]))
        {
            drawPicture(g2, playButton , uiImages.get(pauseHover));
        }
        else
            drawPicture(g2, playButton , uiImages.get(pauseDefault));
    }

    private void drawPlayButton(Graphics2D g2)
    {
        if(clicked)
        {
            if(checkCollision(playButton, x_y_pressed[0], x_y_pressed[1]))
            {
                drawPicture(g2, playButton , uiImages.get(playSelected));
            }
            else
            {
                drawPicture(g2, playButton , uiImages.get(playDefault));
            }

        } else if (checkCollision(playButton, x_y_position[0], x_y_position[1]))
        {
            drawPicture(g2, playButton , uiImages.get(playHover));
        }
        else
        {
            drawPicture(g2, playButton , uiImages.get(playDefault));
        }
    }

    private void drawFileSelectButton(Graphics2D g2)
    {
        if(clicked)
        {
            if(checkCollision(fileSelectButton, x_y_pressed[0], x_y_pressed[1]) ) {
                drawPicture(g2, fileSelectButton, uiImages.get(fileSelectSelected));
            }
            else
                drawPicture(g2, fileSelectButton , uiImages.get(fileSelectDefault));
        }
        else if(checkCollision(fileSelectButton, x_y_position[0], x_y_position[1]))
        {
            drawPicture(g2, fileSelectButton, uiImages.get(fileSelectHover));
        }
        else
            drawPicture(g2, fileSelectButton , uiImages.get(fileSelectDefault));
    }


    @Override
    public void draw(Graphics2D g2, Rectangle enclosure)
    {
        if(errorMessageTime > 0)
        {
            g2.setColor(new Color(100,0,0));
            g2.drawString("Media is AlreadyLoaded, Aborting request", OPTION_SPACING ,2*OPTION_SPACING);
        }
        playButton = new Rectangle((int)(enclosure.getX()+enclosure.getWidth()/2), (int)enclosure.getY(), MEDIA_OPTION_SIZE, MEDIA_OPTION_SIZE);
        fileSelectButton = new Rectangle((int)(enclosure.getWidth()- MEDIA_OPTION_SIZE), (int)(enclosure.getY()), MEDIA_OPTION_SIZE, MEDIA_OPTION_SIZE);
        queueWindowButton = new Rectangle((int)(fileSelectButton.getX() - MEDIA_OPTION_SIZE-OPTION_SPACING), (int)(enclosure.getY()), MEDIA_OPTION_SIZE, MEDIA_OPTION_SIZE);
        if(isPlaying)
        {
            drawPauseButton(g2);
        }
        else
        {
            drawPlayButton(g2);
        }
        drawFileSelectButton(g2);
        g2.draw(queueWindowButton);
    }
}
