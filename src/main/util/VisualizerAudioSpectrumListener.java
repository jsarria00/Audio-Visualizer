package util;

import javafx.scene.media.AudioSpectrumListener;
import model.VisualizerComponent;

public class VisualizerAudioSpectrumListener implements AudioSpectrumListener {
    private VisualizerComponent vC;

    public VisualizerAudioSpectrumListener(VisualizerComponent vC)
    {
        this.vC = vC;
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        vC.visualize(magnitudes);
    }

}
