package util;

import gui.MediaQueuer;
import javafx.scene.media.AudioSpectrumListener;
import javafx.util.Duration;


import static java.lang.Thread.sleep;

public class VisualizerApplication implements Runnable {
    private VisualizerMediaPlayerHolder player;
    private boolean isPlaying = false;
    private SongLog songLog;
    private boolean debug;
    private MediaQueuer queuer;
    private Thread queueThread;


    /**
     * Default constructor, which requires a boolean to check if it will start the VisualizerApplication in debug mode or not
     * @param debug boolean value that determine if debug mode turned on.
     */
    public VisualizerApplication(boolean debug)
    {
        queuer = new MediaQueuer(this);
        this.debug = debug;
        songLog = new SongLog();
        player = new VisualizerMediaPlayerHolder(debug, songLog);
        queueThread = new Thread(queuer);
        queueThread.start();
    }

    public double getEndTime()
    {
        return player.getEndTime();
    }

    public double getCurrentTime()
    {
        return player.getCurrentTime();
    }

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

    public void turnOffQueue()
    {
        queuer.turnOffQueue();
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

    public boolean isLoading() {
        return VisualizerMediaPlayerHolder.isLoading();
    }

    public boolean inUse() {
        return VisualizerMediaPlayerHolder.inUse();
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
