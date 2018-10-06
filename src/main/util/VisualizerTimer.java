package util;

import model.VisualizerComponent;
import ui.Visualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class VisualizerTimer implements ActionListener {
    private VisualizerComponent component;
    ActionListener listener;
    private final int MILISECONDS = 5;
    private Timer t;

    public VisualizerTimer(VisualizerComponent component)
    {
        this.component = component;
        listener = this;
        t = new Timer(MILISECONDS, listener);
        t.start();
    }

    public void start()
    {
        t.start();
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        component.timedActions();
    }
}
