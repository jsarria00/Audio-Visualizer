package util;

import java.awt.event.ActionListener;
import javax.swing.Timer;

public class QueueTransitionManager {

    VisualizerApplication vApplication;
    TransitionTimer tT;
    Timer t;
    private static int miliseconds;


    public QueueTransitionManager(VisualizerApplication vApplication, int transitionTime)
    {
        this.vApplication = vApplication;
        miliseconds = transitionTime/100;
    }

    public void volManage()
    {
        vApplication.manageVol();
    }

    public void endTransition()
    {
        t.stop();
        t.removeActionListener(tT);
        t = null;
        tT = null;
        vApplication.endTransition();
    }

    public void transition(String attempt)
    {
        vApplication.transition(attempt);
        tT = new TransitionTimer(this);
        t = new Timer(miliseconds, tT);
        t.start();

    }
}
