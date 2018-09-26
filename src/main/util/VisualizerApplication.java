package util;

import static java.lang.Thread.sleep;

public class VisualizerApplication implements Runnable {
    private VisualizerMediaPlayerHolder player;
    private boolean isPlaying = false;

    public VisualizerApplication()
    {
        player = new VisualizerMediaPlayerHolder();
    }

    private static void startup(Runnable r)
    {
        com.sun.javafx.application.PlatformImpl.startup(r);
    }

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
            while(VisualizerMediaPlayerHolder.inUse() || VisualizerMediaPlayerHolder.isLoading()) {

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

    public boolean isPlaying()
    {
        return isPlaying;
    }

    @Override
    public void run() {
        start();
    }
}
