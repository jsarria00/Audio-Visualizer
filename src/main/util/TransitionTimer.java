package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Requests for the volume to be managed through 100 ticks, before ending the transition.
 */
public class TransitionTimer implements ActionListener {

    private final int MANAGE_VOL_TIMES = 100;
    private int managedTimes = 0;
    QueueTransitionManager qTM;

    /**
     * stores a reference of the QueueTransitionManager that hold this object
     * @param qTM QueueTransitionManager
     */
    public TransitionTimer(QueueTransitionManager qTM)
    {
        this.qTM = qTM;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        qTM.volManage();
        managedTimes++;
        if(managedTimes > MANAGE_VOL_TIMES)
        {
            qTM.endTransition();
        }
    }
}

