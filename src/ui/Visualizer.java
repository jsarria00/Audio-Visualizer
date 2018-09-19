package ui;


import javax.swing.JFrame;
//To play an audio file
//CustomClasses

import util.VisualizerMediaPlayerHolder;


import static java.lang.Thread.sleep;


public class Visualizer
{

    private static Thread mediaThread;
    /**
     * constructs a new JFrame and sets up additional settings
     * @return Returns a JFrame with a set title "Audio Visualizer" with a starting size of 640x400 pixels.
     */
    private static JFrame createFrame()
    {
        System.out.println("Creating a frame with 640x400 size.");
        JFrame frame = new JFrame();
        frame.setSize(640,400);
        frame.setTitle("Audio Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    /**
     * sets a frame to visible and also adds the visualizer component to the frame. This allows the user to start interacting with the UI
     * @param frame a pre-created frame that will be modified to be visible and hold the visualizer component
     */
    private static void visualize(JFrame frame)
    {
        System.out.println("Showing frame and adding the visualizer");
        frame.setVisible(true);
        //frame.add() statement that adds our visualizer component to be set within the frame.
    }

    public static void startup(Runnable r)
    {
        com.sun.javafx.application.PlatformImpl.startup(r);
    }

    public static void main(String[] args)
    {
        VisualizerMediaPlayerHolder player = new VisualizerMediaPlayerHolder();
        JFrame fr = createFrame();
        visualize(fr);


        while(true) {


            startup(player);
            mediaThread = new Thread(player);

            //Wait for player to change to inUse
            try {
                sleep(200);
            } catch(InterruptedException e) {
                System.out.println("Main thread sleep was interrupted");
            }

            while(player.inUse()) {

                try {
                    sleep(1000);
                    //System.out.println("Waiting till free");
                } catch (InterruptedException e) {
                    System.out.println("Main thread sleep was interrupted");
                }
            }

        }
    }
}
