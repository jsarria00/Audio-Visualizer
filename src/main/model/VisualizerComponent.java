package model;

import gui.MediaOptions;
import gui.Selectable;
import gui.SongLogger;
import gui.WindowOptions;
import javafx.scene.media.AudioSpectrumListener;
import javafx.util.Duration;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class VisualizerComponent extends JComponent implements Selectable {
    private VSquare vS;
    private VBackground vBg;
    private VAudioRectangles vAudioRectangles;
    private int screenX;
    private int screenY;
    private int waitTimeRemaining;
    private int mouseHideWaitTime;
    private boolean wasPlaying;
    private boolean sliderClicked;
    private boolean mouseHidden;
    private VisualizerApplication vApplication;
    private SongLogger sL;
    private MediaOptions mO;
    private WindowOptions wO;
    private Boolean canSelect;
    private JFrame heldBy;
    private JSlider slider;
    private AudioSpectrumListener audioSpectrumListener;
    private static final int NUM_AUDIO_RECTANGLES = 60;
    private MouseMotionEventManager mouseMotionManager;
    private CanvasMouseEventManager canvasMouseManager;
    private String line;

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
        sliderClicked = false;
        wasPlaying = false;
        mouseHidden = false;
        mouseMotionManager = new MouseMotionEventManager();
        mouseMotionManager.setVisualizerComponent(this);
        canvasMouseManager = new CanvasMouseEventManager();
        canvasMouseManager.setVisualizerComponent(this);

        BufferedReader br = null;

        try {
            String theURL = "https://www.ugrad.cs.ubc.ca/~cs210/2018w1/welcomemsg.html"; //this can point to any URL
            URL url = new URL(theURL);
            br = new BufferedReader(new InputStreamReader(url.openStream()));


            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {

                sb.append(line);
                sb.append(System.lineSeparator());
            }
            line = sb.toString();

        }
        catch (IOException e)
        {

        }
        finally {
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch(IOException e)
            {

            }
        }
    }

    public void setCanvasMouseManager(CanvasMouseEventManager canvasMouseManager) {

        this.canvasMouseManager = canvasMouseManager;
        if(this.canvasMouseManager == null || !this.canvasMouseManager.equals(canvasMouseManager)) {
            if(this.canvasMouseManager != null) {
                this.canvasMouseManager.removeVisualizerComponent();
            }
            this.canvasMouseManager =  canvasMouseManager;
            canvasMouseManager.setVisualizerComponent(this);
        }
        this.addMouseListener(this.canvasMouseManager);
    }

    public void removeCanvasMouseManager()
    {
        if(canvasMouseManager != null)
        {
            this.removeMouseListener(canvasMouseManager);
            CanvasMouseEventManager temp = canvasMouseManager;
            canvasMouseManager = null;
            temp.removeVisualizerComponent();
        }
    }

    public void setMouseMotionManager(MouseMotionEventManager mouseMotionManager)
    {
        if(this.mouseMotionManager == null || !this.mouseMotionManager.equals(mouseMotionManager)) {
            if(this.mouseMotionManager != null)
            {
                this.mouseMotionManager.removeVisualizerComponent();
            }
            this.mouseMotionManager = mouseMotionManager;
            mouseMotionManager.setVisualizerComponent(this);
        }

        this.addMouseMotionListener(this.mouseMotionManager);
    }

    public void removeMouseMotionManager()
    {
        if(mouseMotionManager != null)
        {
            this.removeMouseMotionListener(mouseMotionManager);
            MouseMotionEventManager temp = mouseMotionManager;
            mouseMotionManager = null;
            temp.removeVisualizerComponent();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisualizerComponent that = (VisualizerComponent) o;
        return screenX == that.screenX &&
                screenY == that.screenY &&
                waitTimeRemaining == that.waitTimeRemaining &&
                mouseHideWaitTime == that.mouseHideWaitTime &&
                wasPlaying == that.wasPlaying &&
                sliderClicked == that.sliderClicked &&
                mouseHidden == that.mouseHidden &&
                Objects.equals(vS, that.vS) &&
                Objects.equals(vBg, that.vBg) &&
                Objects.equals(vAudioRectangles, that.vAudioRectangles) &&
                Objects.equals(vApplication, that.vApplication) &&
                Objects.equals(sL, that.sL) &&
                Objects.equals(mO, that.mO) &&
                Objects.equals(wO, that.wO) &&
                Objects.equals(canSelect, that.canSelect) &&
                Objects.equals(heldBy, that.heldBy) &&
                Objects.equals(slider, that.slider) &&
                Objects.equals(audioSpectrumListener, that.audioSpectrumListener) &&
                Objects.equals(mouseMotionManager, that.mouseMotionManager) &&
                Objects.equals(canvasMouseManager, that.canvasMouseManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vS, vBg, vAudioRectangles, screenX, screenY, waitTimeRemaining, mouseHideWaitTime, wasPlaying, sliderClicked, mouseHidden, vApplication, sL, mO, wO, canSelect, heldBy, slider, audioSpectrumListener, mouseMotionManager, canvasMouseManager);
    }

    public void clickedSlider()
    {
        sliderClicked = true;
        if(vApplication.isPlaying())
        {
            wasPlaying = true;
            vApplication.togglePlayState();
        }
    }

    public void releasedSlider()
    {
        if(wasPlaying)
        {
            wasPlaying = false;
            vApplication.togglePlayState();
        }
        Duration selectedTime = new Duration(slider.getValue());
        vApplication.setSeek(selectedTime);
        sliderClicked = false;
    }

    public void setSlider(JSlider slider)
    {
        this.slider = slider;
        slider.addMouseListener(new SliderMouseEventManager(this));
    }

    public void updateSlider()
    {
        int end = (int)vApplication.getEndTime();
        int current = (int)vApplication.getCurrentTime();
        if(end != 0)
        {
            slider.setMaximum(end);
            slider.setValue(current);
        }
        else
        {
          slider.setMaximum(1);
          slider.setValue(0);
        }
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

    public void modifyByState()
    {
        if(vApplication.isPlaying()) {
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
    }

    public void timedActions()
    {
        if(waitTimeRemaining > 0) {
            waitTimeRemaining--;
        }
        modifyByState();
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
        if(slider != null && !sliderClicked) // AND slider not currently clicked.
        {
            updateSlider();
        }
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
        int[] integerMagnitudes = new int[NUM_AUDIO_RECTANGLES];
        for(int i = 0; i < NUM_AUDIO_RECTANGLES; i++)
        {
            integerMagnitudes[i] = 60+((int)(magnitudes[i]));
        }
        vAudioRectangles.visualize(integerMagnitudes);
        vS.visualize(integerMagnitudes);
        vBg.visualize(integerMagnitudes);
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

        g2.drawString(line, this.screenX/2-220, this.screenY/2);
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