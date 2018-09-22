package util;

import static java.lang.Thread.sleep;

public class VisualizerApplication implements Runnable {
    private VisualizerMediaPlayerHolder player;

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
                    //System.out.println("Waiting till free");
                } catch (InterruptedException e) {
                    System.out.println("Main thread sleep was interrupted");
                }
            }
        }
    }

    @Override
    public void run() {
        start();
    }
}
