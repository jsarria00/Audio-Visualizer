package util;


import javax.swing.Timer;

public class QueueTransitionManager {

    private VisualizerApplication vApplication;
    private TransitionTimer tT;
    private Timer t;
    private int miliseconds;

    /**
     *
     * @param vApplication
     * @param transitionTime
     */
    public QueueTransitionManager(VisualizerApplication vApplication, int transitionTime)
    {
        this.vApplication = vApplication;
        miliseconds = transitionTime/100;
    }


    /**
     * Requests the VisualizerApplication to Manage volume
     */
    public void volManage()
    {
        vApplication.manageVol();
    }

    /**
     * Ends the transition, and discards the timer and transition timer to the garbage collector.
     */
    public void endTransition()
    {
        t.stop();
        t.removeActionListener(tT);
        t = null;
        tT = null;
        vApplication.endTransition();
    }

    /**
     * Informs the VisualizerApplication that a transition has started and starts the Timer to tick per 1/100 the declared transition time of  for a TransitionTimer
     * @param attempt
     */
    public void transition(String attempt)
    {
        vApplication.transition(attempt);
        tT = new TransitionTimer(this);
        t = new Timer(miliseconds, tT);
        t.start();
    }
}
