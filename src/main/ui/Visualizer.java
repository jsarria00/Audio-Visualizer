package ui;


import javax.swing.*;
//To play an audio file
//CustomClasses

import model.VisualizerComponent;
import util.*;

import java.awt.*;
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
        //System.out.println("Creating a frame with XxY size.");//for debug
        JFrame frame = new JFrame();
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setSize(1920,1080);
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
        JSlider slider = new JSlider(0,100,0);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        visualizerComponent = new VisualizerComponent(player, frame, panel);
        visualizerComponent.setPreferredSize(new Dimension(visualizerComponent.getWidth(), (visualizerComponent.getHeight()) - 75));
        panel.add(visualizerComponent, BorderLayout.PAGE_START);
        panel.add(slider, BorderLayout.PAGE_END);
        vTimer = new VisualizerTimer(visualizerComponent);
        vTimer.start();
        visualizerComponent.setSlider(slider);
        //System.out.println("Showing frame and adding the visualizer");//for Debug
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.add(panel); //statement that adds our visualizer component to be set within the frame.
        frame.setVisible(true);
    }


    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.opengl", "true");
        boolean debug = false;
        //lineArguments bool setter
        for(int i=0; i<args.length; i++)
        {
            debug = args[i].equals("-debug");
        }
        player = new VisualizerApplication(debug);
        mediaThread = new Thread(player);
        mediaThread.start();
        JFrame fr = createFrame();
        visualize(fr);


    }
}
