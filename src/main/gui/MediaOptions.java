package gui;

import util.VisualizerApplication;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MediaOptions extends VisualizerOption{

    private boolean isPlaying;
    private Rectangle playButton;
    private Rectangle fileSelectButton;

    public MediaOptions(VisualizerApplication vApplication)
    {
        super(vApplication);
        isPlaying = false;
    }

    @Override
    public void timedEvent()
    {
        isPlaying = vApplication.isPlaying();
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
            JFileChooser selector = new JFileChooser();
            selector.setFileFilter(new FileNameExtensionFilter(".mp3", "mp3"));
            File f = new File(MEDIA_DIRECTORY);
            selector.setCurrentDirectory(f);
            int option = selector.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                String fileDir = selector.getSelectedFile().toString();
                System.out.println(fileDir + " was the selected path and file \nRequesting Visualizer Application to load file" );
                vApplication.load(fileDir);
                isPlaying = false;
            }
        }
    }

    @Override
    public void hoverEvent(int x, int y){
        x_y_position[0] = x;
        x_y_position[1] = y;
    }



    @Override
    public void draw(Graphics2D g2, Rectangle enclosure)
    {
        playButton = new Rectangle((int)(enclosure.getX()+enclosure.getWidth()/2), (int)enclosure.getY(), MEDIA_OPTION_SIZE, MEDIA_OPTION_SIZE);
        fileSelectButton = new Rectangle((int)(enclosure.getWidth()- MEDIA_OPTION_SIZE), (int)(enclosure.getY()), MEDIA_OPTION_SIZE, MEDIA_OPTION_SIZE);
        if(isPlaying)
        {
            if(clicked)
            {
                if(checkCollision(playButton, x_y_pressed[0], x_y_pressed[1]))
                {
                    drawPicture(g2, playButton , "src/main/images/pause_Selected.png");
                }
                else
                    drawPicture(g2, playButton , "src/main/images/pause_Default.png");

            } else if (checkCollision(playButton, x_y_position[0], x_y_position[1]))
            {
                drawPicture(g2, playButton , "src/main/images/pause_Hover.png");
            }
            else
                drawPicture(g2, playButton , "src/main/images/pause_Default.png");
        }
        else
        {
            if(clicked)
            {
                if(checkCollision(playButton, x_y_pressed[0], x_y_pressed[1]))
                {
                    drawPicture(g2, playButton , "src/main/images/play_Selected.png");
                }
                else
                {
                    drawPicture(g2, playButton , "src/main/images/play_Default.png");
                }

            } else if (checkCollision(playButton, x_y_position[0], x_y_position[1]))
            {
                drawPicture(g2, playButton , "src/main/images/play_Hover.png");
            }
            else
            {
                drawPicture(g2, playButton , "src/main/images/play_Default.png");
            }
        }


        if(clicked)
        {
            if(checkCollision(fileSelectButton, x_y_pressed[0], x_y_pressed[1]) ) {
                drawPicture(g2, fileSelectButton, "src/main/images/fileSelect_Selected.png");
            }
            else
                drawPicture(g2, fileSelectButton , "src/main/images/fileSelect_Default.png");

        }
        else if(checkCollision(fileSelectButton, x_y_position[0], x_y_position[1]))
        {
            drawPicture(g2, fileSelectButton, "src/main/images/fileSelect_Hover.png");
        }
        else
            drawPicture(g2, fileSelectButton , "src/main/images/fileSelect_Default.png");
    }

}
