package ui;

import javax.swing.JFrame;



public class Visualizer
{
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

    public static void main(String[] args) {
        JFrame fr = createFrame();
        visualize(fr);

    }
}
