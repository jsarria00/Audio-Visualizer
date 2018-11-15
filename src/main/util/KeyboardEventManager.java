package util;

import model.VisualizerComponent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardEventManager implements KeyListener {
    private VisualizerComponent vC;

    public KeyboardEventManager(VisualizerComponent vC)
    {
        this.vC = vC;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            vC.volUp();
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            vC.volDown();
        }
        if(e.getKeyChar() ==  'h' || e.getKeyChar() == 'H')
        {

            vC.toggleUI();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
