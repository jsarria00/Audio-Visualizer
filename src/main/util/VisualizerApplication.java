package util;

import gui.MediaQueuer;
import javafx.scene.media.AudioSpectrumListener;
import javafx.util.Duration;


import static java.lang.Thread.sleep;

/**
 * A class responsible for initiating the Application Thread of VisualizerMediaPlayerHolder
 * The Class is responsible as a communication interface for the MediaQueuer, VisualizerComponent with some of its children, and the VisualizerMediaPlayerHolder
 */
public class VisualizerApplication implements Runnable {
    private VisualizerMediaPlayerHolder player;
    private boolean isPlaying = false;
    private SongLog songLog;
    private boolean debug;
    private MediaQueuer queuer;
    private Thread queueThread;


    /**
     * Default constructor, which requires a boolean to check if it will start the VisualizerApplication in debug mode or not
     *
     * @param debug boolean value that determine if debug mode turned on.
     */
    public VisualizerApplication(boolean debug) {
        queuer = new MediaQueuer(this);
        this.debug = debug;
        songLog = new SongLog();
        player = new VisualizerMediaPlayerHolder(debug, songLog);
        queueThread = new Thread(queuer);
        queueThread.start();
    }

    /**
     * initiates a transition with a new .mp3 file
     * @param attempt directory string of the .mp3 file
     */
    public void transition(String attempt)
    {
        player.transition(attempt);
    }

    /**
     * Informs the VisualizerMediaPlayerHolder that the transition has ended
     */
    public void endTransition()
    {
        player.endTransition();
    }

    /**
     * requests the VisualizerMediaPlayerHolder to manage its transition volume
     */
    public void manageVol()
    {
        player.manageVol();
    }

    /**
     * gets the transition state of the VisualizerMediaPlayerHolder
     * @return boolean
     */
    public boolean getTransitionState()
    {
        return player.getTransitionState();
    }

    /**
     * Informs the MediaQueuer that the slider was clicked
     */
    public void sliderClicked(){
        queuer.sliderClicked();
    }

    /**
     * Informs the MediaQueuer that the slider was released
     */
    public void sliderReleased()
    {
        queuer.sliderReleased();
    }

    /**
     * requests the VisualizerMediaPlayerHolder to raise its volume
     */
    public void volUp()
    {
        player.volUp();
    }

    /**
     * requests the VisualizerMediaPlayerHolder to lower its volume
     */
    public void volDown()
    {
        player.volDown();
    }

    /**
     * gets the current of the VisualizerMediaPlayerHolder
     * @return int
     */
    public int getVolume()
    {
        return player.getVolume();
    }

    /**
     * gets the total duration of the current load player in VisualizerMediaPlayerHolder as a double
     * @return double
     */
    public double getEndTime()
    {
        return player.getEndTime();
    }

    /**
     * gets the current duration as a double of the VisualizerMediaPlayerHolder MediaPlayer
     * @return double
     */
    public double getCurrentTime()
    {
        return player.getCurrentTime();
    }

    /**
     * Transfers a requested Duration to the VisualizerMediaPlayerHolder
     * @param requestedTime Duration requested from a time interval
     */
    public void setSeek(Duration requestedTime)
    {
        player.setSeek(requestedTime);
    }

    /**
     * Prepares a Runnable class to function with the Java VisualizerApplication platform in threading
     * @param r Class that implements the Runnable interface
     */
    private static void startup(Runnable r)
    {
        com.sun.javafx.application.PlatformImpl.startup(r);
    }

    /**
     * Changes the play state of the MediaPlayerHolder to its opposite state, and then updates the VisualizerApplication with the MediaPlayer's play state.
     */
    public void togglePlayState()
    {
        if(isPlaying)
        {
            player.pause();
        }else
        {
            player.play();
        }//Only request once, so that it does not get hogged by the J.AWT thread
       isPlaying = player.isPlaying();
    }

    /**
     * Makes the JFrame turn off its queue
     */
    public void turnOffQueue()
    {
        queuer.turnOffQueue();
    }

    /**
     *Makes the MediaQueuer hide/show its JFrame
     */
    public void toggleVisibility()
    {
        queuer.toggleVisibility();
    }

    /**
     * Endlessly feeds the Application thread to get a new set of instructions via String input.
     */
    private void start()
    {
        while(true) {
            startup(player);

            //Wait for player to change to inUse
            try {
                sleep(200);
            } catch(InterruptedException e) {
                System.out.println("Main thread sleep was interrupted");
            }
            while(inUse() || isLoading()) {

                try {
                    sleep(200);
                    isPlaying = player.isPlaying();
                    //System.out.println("Waiting till free");
                } catch (InterruptedException e) {
                    System.out.println("Main thread sleep was interrupted");
                }
            }
        }
    }

    /**
     * checks if the MediaPlayerHolder is busy with a loading task
     * @return boolean
     */
    public boolean isLoading() {
        return player.isLoading();
    }

    /**
     * checks if the MediaPlayerHolder is busy with a task via terminal input( -debug argument)
     * @return boolean
     */
    public boolean inUse() {
        return player.inUse();
    }

    /**
     * Returns the playing status
     * @return Boolean which determines if the VisualizerMediaPlayerHolder is Playing
     */
    public boolean isPlaying()
    {
        return isPlaying;
    }

    /**
     * Requests the VisualizerMediaPlayerHolder to load a file
     * @param path string containing the Absolute directory and file name
     */
    public void load(String path) throws MediaAlreadyLoadedException
    {
        player.load(path);
        isPlaying = player.isPlaying();
    }

    /**
     * returns a reference to the Universal songLog Object in MediaPlayerHolder and VisualizerApplication.
     * @return a SongLog object
     */
    public SongLog getLog()
    {
        return songLog;
    }

    public void setAudioSpectrumListener(AudioSpectrumListener audioSpectrumListener)
    {
        player.setAudioSpectrumListener(audioSpectrumListener);
    }


    /**
     * Overridden method for Runnable interface via threading.
     * If in debug mode VisualizerApplication will request string input until quit.
     * Otherwise Java Application thread will be fed, and VisualizerApplication will consistently request and save the playing status of VisualizerMediaPlayerHolder
     */
    @Override
    public void run() {
        if(debug) {
            start();
        }
        else
        {
            startup(player);
            while (true) {
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("Main thread sleep was interrupted");
                }
                isPlaying = player.isPlaying();
            }
        }
    }
}
