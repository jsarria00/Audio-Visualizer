package ui;


import javax.swing.JFrame;
//To play an audio file
//CustomClasses

import model.VisualizerComponent;
import util.VisualizerApplication;
import util.VisualizerTimer;
//import java.awt.image.BufferedImage;
//import java.awt.Point;




/**
 * A class that creates a JFrame, and a VisualizerMediaPlayerHolder and prepares them for use.
 * @author Javier Sarria Bastidas
 * @version W2
 */
public class Visualizer {
    private static Thread mediaThread;
    private static VisualizerComponent visualizerComponent;
    private static VisualizerTimer vTimer;
    private static VisualizerApplication player;

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

        visualizerComponent = new VisualizerComponent(player);
        vTimer = new VisualizerTimer(visualizerComponent);
        vTimer.start();
        System.out.println("Showing frame and adding the visualizer");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.add(visualizerComponent); //statement that adds our visualizer component to be set within the frame.
        frame.setVisible(true);
        /*Note for hiding and showing cursor in future development.
        frame.setCursor(frame.getToolkit().createCustomCursor(
                new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
                "null"));
                */
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
        player = new VisualizerApplication();
        mediaThread = new Thread(player);
        mediaThread.start();
        JFrame fr = createFrame();
        visualize(fr);


    }
}
