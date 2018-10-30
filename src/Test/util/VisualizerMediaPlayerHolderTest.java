package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class VisualizerMediaPlayerHolderTest {
    public VisualizerMediaPlayerHolder vMPH;
    private static final String LIGHT = "src/main/media/San Holo - Light.mp3";
    private static final String SHELTER = "src/main/media/Shelter.mp3";
    private String songDir;

    @BeforeEach
    public void runBefore()
    {
        vMPH = new VisualizerMediaPlayerHolder(false, new SongLog());
        com.sun.javafx.application.PlatformImpl.startup(vMPH);

    }

    public void testWait()
    {
        try{ wait(2000);}
        catch (InterruptedException e)
        {
            System.out.println("Waiting for thread to finish loading file");
        }
        catch (IllegalMonitorStateException e)
        {

        }
    }


    @Test
    public void testLoad()
    {
        testWait();
        try {
            vMPH.load(LIGHT);
        } catch (MediaAlreadyLoadedException e)
        {
            fail("Should not catch MediaAlreadyLoadedException");
        }
        testWait();
        songDir = vMPH.getSongDir();
        vMPH.play();
        try {
            vMPH.load(songDir);
            fail("Should catch MediaAlreadyLoadedException as it is already loaded!");
        } catch (MediaAlreadyLoadedException e)
        {

        }
        assertTrue(vMPH.isPlaying());
        try {
            vMPH.load(SHELTER);
        } catch (MediaAlreadyLoadedException e)
        {
            fail("Should not catch MediaAlreadyLoadedException");
        }
        assertFalse(vMPH.isPlaying());

    }
}
