package ui;


import javax.swing.JFrame;
//To play an audio file
//CustomClasses

import util.VisualizerApplication;


import static java.lang.Thread.sleep;

/**
 * A class that creates a JFrame, and a VisualizerMediaPlayerHolder and prepares them for use.
 * @author Javier Sarria Bastidas
 * @version W2
 */
public class Visualizer {
    private  static Thread mediaThread;
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

    /**
     * Prepares a Runnable class to function with the Java VisualizerApplication platform in threading
     * @param r Class that implements the Runnable interface
     */
    private static void startup(Runnable r)
    {
        com.sun.javafx.application.PlatformImpl.startup(r);
    }

    public static void main(String[] args)
    {
        VisualizerApplication player = new VisualizerApplication();
        mediaThread = new Thread(player);
        mediaThread.start();
        JFrame fr = createFrame();
        visualize(fr);
    }
}
