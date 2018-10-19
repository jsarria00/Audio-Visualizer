package util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class MediaCleaner implements Runnable{
    ArrayList<SongEntry> log;
    SongEntry fileToCheck;

    public MediaCleaner(ArrayList<SongEntry> log, SongEntry fileToCheck)
    {
        this.log = log;
        this.fileToCheck = fileToCheck;
    }

    @Override
    public void run()
    {
        try {
            Media attempt = new Media(new File(fileToCheck.getSongDirectory()).toURI().toString());
            MediaPlayer fakePlayer = new MediaPlayer(attempt);
        }
        catch (MediaException e)
        {
            System.err.println("Song: " + fileToCheck.getSongName() + " was not found and removed from the song Log ");
            log.remove(log.indexOf(fileToCheck));

        }
    }
}
