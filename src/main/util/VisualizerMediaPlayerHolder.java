package util;

import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class that manages a MediaPlayer object, while this class is run through the Java.VisualizerApplication thread
 * @author Javier Sarria Bastidas
 * @version W2
 */
public class VisualizerMediaPlayerHolder implements Runnable
{
    //These will be implemented in an interface
    private static final String HELP = "help";
    private static final String LOAD = "load";
    private static final String PLAY = "play";
    private static final String PAUSE = "pause";
    private static final String QUIT = "quit";
    private static final String DIRECTORY = "src/main/media/";

    private static boolean debug;
    private static boolean isActive;
    private static boolean firstStart;
    private static boolean isPlaying;
    private static boolean isLoading;
    private static boolean isValidating;
    private static boolean hasInitialized;
    private static boolean firstLoad;
    private static MediaPlayer mediaPlayer;
    private static Media song;
    private String songName;
    private String songDir;
    private String inputStr;
    private Scanner input;
    private SongLog log;
    private AudioSpectrumListener audioSpectrumListener;

    /**
     * Constructor that sets up a scanner for use, initial bool values, and a non-null string.
     */
    public VisualizerMediaPlayerHolder(boolean debug, SongLog log) {
        this.debug = debug;
        this.log = log;
        songName = "";
        songDir = "";
        input = new Scanner(System.in);
        isActive = false;
        firstStart = true;
        isPlaying = false;
        isLoading = false;
        hasInitialized = false;
        isValidating = false;
        firstLoad = log.getSongList().size() > 0;
    }

    private void validateLog()
    {
        isValidating = true;
        ArrayList<SongEntry> songEntries = log.getSongList();
        for(int i = 0; i < songEntries.size(); i++)
        {
            try {
                Media attempt = new Media(new File(songEntries.get(i).getSongDirectory()).toURI().toString());
            }
            catch (MediaException e)
            {
                System.err.println("Song: " + songEntries.get(i).getSongName() + " was not found and removed from the song Log ");
                songEntries.remove(i);
                i--;
                log.validateSerialFile();

            }
        }
        isValidating = false;
    }

    /**
     * Trims all the spaces before and after the the first and last characters respectively, limits the spaces between characters to 1,
     * sets it to lower case to make commands non-case sensitive
     * @param a String command via user input
     * @return String with extra whitespace removed, and set to lower
     */
    private static String normalize(String a) {
        //Create a new string to hold a String without leading, extra() and trailing blanks
        String normalized = a.trim();
        normalized = normalized.replaceAll("( )+", " ");
        //with the final step of normalization, change to lower case and save to the string the class returns
        normalized = normalized.toLowerCase();
        return  normalized;
    }

    /**
     * pauses and discards the current MediaPlayer object in this class
     */
    private void deInitialize() {
        if(isPlaying&&hasInitialized){
            mediaPlayer.pause();
            isPlaying = false;
        }
        mediaPlayer = null;
        hasInitialized = false;
    }

    public double getEndTime()
    {
        if(mediaPlayer != null) {
            return mediaPlayer.getStopTime().toMillis();
        }
        return 0;
    }

    public double getCurrentTime()
    {
        if(mediaPlayer != null)
        {
            return mediaPlayer.getCurrentTime().toMillis();
        }
        return 0;
    }

    public void setSeek(Duration requestedTime)
    {
        if(mediaPlayer !=null)
        {
            mediaPlayer.seek(requestedTime);
        }
    }


    //Requires: File directory attempt not equivalent to currently loaded file
    //Modifies: This
    //Effects: MediaPlayer loads a unique media file
    private void checkLoaded(String attempt) throws MediaAlreadyLoadedException
    {
        int size = log.getSongList().size();
        if(size > 0)
        {
            if(attempt.equals(songDir))
            {
                throw new MediaAlreadyLoadedException();
            }
        }
    }
    /**
     * Will try to load the file directory indicated
     * @param attempt a file directory in a string
     */
    public void load(String attempt) throws MediaAlreadyLoadedException {
        if(!isLoading && !isValidating) {
            try {
                checkLoaded(attempt);
                // This local variable will throw an exception without interrupting the current playback if no file is found
                Media tempPointer = new Media(new File(attempt).toURI().toString());
                deInitialize();
                song = tempPointer;
                //System.out.println("TIME: " + song.getDuration());
                mediaPlayer = new MediaPlayer(song);
                hasInitialized = true;
                //Waiting done through a listener.
                isLoading = true;
                mediaPlayer.setOnReady(() -> {
                    songDir = attempt;
                    mediaPlayer.setAudioSpectrumNumBands(80);
                    SongEntry newestSong = new SongEntry(attempt);
                    songName = newestSong.getSongName();
                    log.addToLog(newestSong);
                    System.out.println("File loaded!");
                    isLoading = false;
                });
                //For now -Note scanner hogs the thread therefore
                mediaPlayer.setOnEndOfMedia(() -> {
                    //Pausing then setting the media back to 0 instead of using stop -> stop causes the Duration to be NULL until media starts playing again
                    mediaPlayer.seek(new Duration(0));
                    isPlaying = false;
                    mediaPlayer.pause();
                });
            } catch (MediaException e) {

                System.err.println("Media file \"" + new SongEntry(attempt).getSongName() + "\" does not exist");
            } finally {
                validateLog();
            }
        }
    }

    /**
     * Will run the first time the Java Application thread is fed and load a song into the mediaPlayer
     */
    private void firstTimeLoad()
    {
        validateLog();
        //Almost identical to load, but looks at the most recent song loaded does not re-log that song.
        ArrayList<SongEntry> songList = log.getSongList();
        int size = songList.size();
        try {
            // This local variable will throw an exception without interrupting the current playback if no file is found
            Media tempPointer = new Media(new File(songName = songList.get(size-1).getSongDirectory()).toURI().toString());
            deInitialize();
            song = tempPointer;
            //System.out.println("TIME: " + song.getDuration());

            mediaPlayer = new MediaPlayer(song);
            hasInitialized = true;
            //Waiting done through a listener.
            isLoading = true;
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.setAudioSpectrumNumBands(80);
                songDir = songList.get(size-1).getSongDirectory();
                songName = songList.get(size-1).getSongName();
                System.out.println("Previous session file successfully loaded!");
                isLoading = false;
            });
            //For now -Note scanner hogs the thread therefore
            mediaPlayer.setOnEndOfMedia(()->{
                //Pausing then setting the media back to 0 instead of using stop -> stop causes the Duration to be NULL until media starts playing again
                mediaPlayer.seek(new Duration(0));
                isPlaying = false;
                mediaPlayer.pause();
            });
        }catch (MediaException e) {

            System.err.println("Media file \"" + songList.get(size-1).getSongName() + "\" does not exist anymore");
        }

    }

    /**
     * Requests sting input from the user in Debug mode before attempting file Load
     */
    private void loadCMD()
    {
        //future specification, custom exception to prevent unloading the current song if it is not found or is the same song.
        System.out.println("Type the name of the song file with it's extension. Note this is case sensitive!");
        String songStr = input.nextLine();
        songStr = songStr.trim();
        try {
            load(DIRECTORY + songStr);
        } catch (MediaAlreadyLoadedException e)
        {
            System.err.println("Media was already loaded, aborting request.");
        }
    }

    /**
     * sets the MediaPlayer class to PLAYING state.
     */
    public void play() {
        if (!hasInitialized) {
            System.err.println("There is no song loaded; please load a song before playing."); //Exception throw idea
        } else if (isPlaying) {
            System.out.println("Song is already playing");
        } else {
            mediaPlayer.setAudioSpectrumListener(audioSpectrumListener);
            mediaPlayer.play();
            isPlaying = true;
            System.out.println("\"" + songName + "\" is now playing.");
        }
    }

    /**
     * Sets the media player class to a PAUSED state
     */
    public void pause() {
        if(!hasInitialized) {
            System.err.println("There is no song loaded; please load a song before attempting to pause"); //Exception throw idea
        }
        else if(!isPlaying) {
            System.out.println("Song is already paused");
        }
        else {
            mediaPlayer.pause();
            isPlaying = false;
            System.out.println("Song paused.");
        }
    }

    /**
     * a method that calls a set of operations depending on user input. This input is not case sensitive
     */
    private void start()
    {
        if(firstStart) {
            System.out.println("Type \"help\" for a list of commands to get started");
            firstStart = false;
        }
            inputStr = input.nextLine();

        switch (normalize(inputStr)) {
            case HELP:
                System.out.println("help - Prints a list of commands. \n" +
                        "load - prepares the program to load a song, then requests file name located in the media folder.\n" +
                        "play - starts playing the audio of loaded media.\n" +
                        "pause - pauses the audio of the loaded media.\n" +
                        "quit - De-initializes the program, and exits.");
                break;
            case LOAD:
                loadCMD();
                break;
            case PLAY:
                play();
                break;
            case PAUSE:
                pause();
                break;
            case QUIT:
                deInitialize();
                System.exit(0);
            default:
                System.out.println("Unrecognized command \"" + inputStr + "\". Type \"help\" for a list of commands");
                break;
        }
    }

    //Mainly for testing, due to absolute directories
    public String getSongDir()
    {
        return songDir;
    }

    /**
     * Shows if the VisualizerMediaPlayerHolder
     * @return a Boolean which shows if the class is currently being threaded.
     */
    public static boolean inUse()
    {
        return isActive;
    }

    public static boolean isLoading()
    {
        return isLoading;
    }

    public static boolean isPlaying(){ return isPlaying; }

    public void setAudioSpectrumListener(AudioSpectrumListener audioSpectrumListener)
    {
        this.audioSpectrumListener = audioSpectrumListener;
    }

    /**
     * The start point of VisualizerMediaPlayerHolder Runnable when threaded.
     */
    @Override
    public void run() {
        isActive = true;
        if (firstLoad) {
            firstTimeLoad();
            firstLoad = false;
        }
        if(debug) {
            //System.out.println("First");
            start();
            //System.out.println("Second");
        }
        isActive = false;
    }

}