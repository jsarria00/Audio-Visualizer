package util;

import model.VisualizerComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualizerTimer implements ActionListener {
    private VisualizerComponent component;
    ActionListener listener;
    private final int MILISECONDS = 5;
    private Timer t;

    /**
     * Stores a reference of a visualizer component, and then initializes a timer to call the VisualizerTimer's actionsPreformed method every five miliseconds
     * @param component VisualizerComponent of the audio visualizer
     */
    public VisualizerTimer(VisualizerComponent component)
    {
        this.component = component;
        listener = this;
        t = new Timer(MILISECONDS, listener);
    }

    /**
     * starts the timer stored within the VisualizerTimer
     */
    public void start()
    {
        t.start();
    }


    /**
     * Calls a Visualizer component's timedActions method
     * @param e event from the timer Object
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        component.timedActions();
    }
}
