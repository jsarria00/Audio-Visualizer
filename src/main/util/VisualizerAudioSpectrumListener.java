package util;

import javafx.scene.media.AudioSpectrumListener;
import model.VisualizerComponent;

/**
 * Holds a Reference to a VisualizerComponent and is passed into newly instantiated and loaded MediaPlayer Objects
 */
public class VisualizerAudioSpectrumListener implements AudioSpectrumListener {
    private VisualizerComponent vC;

    /**
     * Creates a audio spectrum Listener that references a VisualizerComponent.
     * @param vC
     */
    public VisualizerAudioSpectrumListener(VisualizerComponent vC)
    {
        this.vC = vC;
    }


    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        vC.visualize(magnitudes);
    }

}
