package util;

import sun.misc.Queue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

public class TransitionTimer implements ActionListener {

    private final int MANAGE_VOL_TIMES = 100;
    private int managedTimes = 0;
    QueueTransitionManager qTM;


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

