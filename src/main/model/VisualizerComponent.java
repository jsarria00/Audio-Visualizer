package model;

import gui.MediaOptions;
import gui.Selectable;
import gui.SongLogger;
import gui.WindowOptions;
import javafx.scene.media.AudioSpectrumListener;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VisualizerComponent extends JComponent implements Selectable {
    private VSquare vS;
    private VBackground vBg;
    private VAudioRectangles vAudioRectangles;
    private int screenX;
    private int screenY;
    private int waitTimeRemaining;
    private int mouseHideWaitTime;
    private boolean mouseHidden;
    private VisualizerApplication vApplication;
    private SongLogger sL;
    private MediaOptions mO;
    private WindowOptions wO;
    private Boolean canSelect;
    private JFrame heldBy;
    private AudioSpectrumListener audioSpectrumListener;
    private float[] magnitudes;
    private static final int NUM_AUDIO_RECTANGLES = 60;


    public VisualizerComponent(VisualizerApplication vApplication , JFrame heldBy)
    {
        this.vApplication = vApplication;
        this.heldBy = heldBy;
        audioSpectrumListener = new VisualizerAudioSpectrumListener(this);
        vApplication.setAudioSpectrumListener(audioSpectrumListener);
        vS = new VSquare();
        vBg = new VBackground();
        vAudioRectangles = new VAudioRectangles();
        sL = new SongLogger(vApplication);
        mO = new MediaOptions(vApplication);
        waitTimeRemaining = 0;
        mouseHideWaitTime = TIME_UNTILL_HIDDEN;
        mouseHidden = false;

    }

    private void unHideMouse()
    {
        mouseHideWaitTime = TIME_UNTILL_HIDDEN;
        if(mouseHidden)
        {
            mouseHidden = false;
            heldBy.setCursor(Cursor.getDefaultCursor());
        }
    }

    public void timedActions()
    {
        if(waitTimeRemaining > 0) {
            waitTimeRemaining--;
        }
        if(vApplication.isPlaying()) {
            vS.rotate();
            vS.awake();
            vBg.awake();
            vAudioRectangles.awake();
        }
        else
        {
            vS.asleep();
            vBg.asleep();
            vAudioRectangles.asleep();
        }

        if(mouseHideWaitTime > 0)
        {
            mouseHideWaitTime--;
        }else if(!mouseHidden)
        {
            mouseHidden = true;
            heldBy.setCursor(heldBy.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
        }
        sL.timedEvent();
        mO.timedEvent();
        repaint();
    }

    public void checkSelection(int x, int y)
    {
        if(waitTimeRemaining <= 0) {
            waitTimeRemaining = TIME_DELAY;
            canSelect = true;
            sL.clickedEvent(x, y);
            mO.clickedEvent(x,y);
        }
        unHideMouse();

    }

    public void selection(int x, int y)
    {
        if(canSelect) {
            canSelect = false;
            sL.releasedEvent(x, y);
            mO.releasedEvent(x,y);
        }
    }

    public void checkHover(int x, int y)
    {

        sL.hoverEvent(x, y);
        mO.hoverEvent(x,y);
        unHideMouse();

    }

    public void visualize(float[] magnitudes) {
        this.magnitudes = magnitudes;
        int[] integerMagnitudes = new int[NUM_AUDIO_RECTANGLES];
        for(int i = 0; i < NUM_AUDIO_RECTANGLES; i++)
        {
            integerMagnitudes[i] = 60+((int)(magnitudes[i]));
        }
        vAudioRectangles.visualize(integerMagnitudes);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        this.screenX = getWidth();
        this.screenY = getHeight();
        //SCREEN defined as a rectangle
        Rectangle enclosing = new Rectangle(0, 0, this.screenX, this.screenY);

        vBg.draw(g2, enclosing);
        vS.draw(g2, enclosing);
        vAudioRectangles.draw(g2, enclosing);

        //GEOMETRIC REPRESENTATIONS OF MEDIA OPTIONS
        Rectangle mediaOptionsEnclosing = new Rectangle(OPTION_SPACING, this.screenY - MEDIA_OPTION_SIZE - OPTION_SPACING, this.screenX - SONG_LOG_SIZE_X - 2*OPTION_SPACING, MEDIA_OPTION_SIZE );
        Rectangle windowOptionsEnclosing = new Rectangle(this.screenX - (2*WINDOW_OPTION_SIZE_X), 0, 2*WINDOW_OPTION_SIZE_X, WINDOW_OPTION_SIZE_Y);
        Rectangle historyLogEnclosing = new Rectangle(this.screenX - SONG_LOG_SIZE_X, WINDOW_OPTION_SIZE_Y+OPTION_SPACING, SONG_LOG_SIZE_X, this.screenY -WINDOW_OPTION_SIZE_Y - 2*OPTION_SPACING);

        sL.draw(g2, historyLogEnclosing);
        mO.draw(g2, mediaOptionsEnclosing);

        //DEBUG
//        int rectWidth = this.getWidth()/60;
//        int x = 0;
//        if(magnitudes !=null) {
//            for (int i = 0; i < 60; i++) {
//                g2.draw(new Rectangle(x, getHeight() / 2, rectWidth, 7*(60+((int)magnitudes[i]))));
//                x+=rectWidth;
//            }
//        }

        //for visual debugging
//        g2.draw(mediaOptionsEnclosing);
//        g2.draw(windowOptionsEnclosing);
//        g2.draw(historyLogEnclosing);

    }


}
