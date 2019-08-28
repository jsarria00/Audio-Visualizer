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
 * @author Greyfire
 * @version W2
 */
public class VisualizerMediaPlayerHolder implements Runnable
{
    //These will be implemented in an interface
    private final String HELP = "help";
    private final String LOAD = "load";
    private final String PLAY = "play";
    private final String PAUSE = "pause";
    private final String QUIT = "quit";
    private final String DIRECTORY = "src/main/media/";

    private boolean debug;
    private boolean isActive;
    private boolean firstStart;
    private boolean isPlaying;
    private boolean isLoading;
    private boolean isValidating;
    private boolean hasInitialized;
    private boolean firstLoad;
    private MediaPlayer mediaPlayer;
    private Media song;

    private int volume;
    private String songName;
    private String songDir;
    private String inputStr;
    private Scanner input;
    private SongLog log;
    private AudioSpectrumListener audioSpectrumListener;

    //Transition vars
    private MediaPlayer transitionPlayer;
    private boolean transitioning;
    private int transitionVol;
    private int currentTransitionVol;

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
        volume = 100;
    }

    /**
     * validates the the SongLog to ensure only files that exist in the same directory are in the log.
     */
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
     * Toggles the volume up as long as it's less than 100
     */
    public void volUp()
    {
        if(!transitioning) {
            if (volume < 100) {
                volume++;
            }
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volume / 100.0);
            }
        }
    }

    /**
     * toggles the volume down as long as it's greater than 0
     */
    public void volDown()
    {
        if(!transitioning) {
            if (volume > 0) {
                volume--;
            }
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volume / 100.0);
            }
        }
    }

    /**
     * Loads a transitionPlayer with a .mp3 file
     * @param attempt directory string containing .mp3 file
     */
    private void loadTransition(String attempt)
    {
        try {
            // This local variable will throw an exception without interrupting the current playback if no file is found
            Media tempPointer = new Media(new File(attempt).toURI().toString());
            song = tempPointer;
            //System.out.println("TIME: " + song.getDuration());
            transitionPlayer = new MediaPlayer(song);
            hasInitialized = true;
            //Waiting done through a listener.
            isLoading = true;
            transitionPlayer.setOnReady(() -> {
                mediaPlayer.setAudioSpectrumListener(null);
                transitionPlayer.setVolume(0);
                songDir = attempt;
                transitionPlayer.setAudioSpectrumNumBands(80);
                SongEntry newestSong = new SongEntry(attempt);
                songName = newestSong.getSongName();
                log.addToLog(newestSong);
                System.out.println("File loaded!");
                isLoading = false;
                transitionPlayer.play();
                transitionPlayer.setAudioSpectrumListener(audioSpectrumListener);

            });
            //For now -Note scanner hogs the thread therefore
            transitionPlayer.setOnEndOfMedia(() -> {
                //Pausing then setting the media back to 0 instead of using stop -> stop causes the Duration to be NULL until media starts playing again
                if(transitionPlayer != null) {
                    transitionPlayer.seek(new Duration(0));
                    isPlaying = false;
                    transitionPlayer.pause();
                } else{
                    mediaPlayer.seek(new Duration(0));//FOR THE TRANSITION PLAYER, RIGHT AT THE MOMENT THE TRANSITION ENDS IT WILL BE REKNOWN AS THE NEW MAIN MEDIA PLAYER
                    isPlaying = false;
                    mediaPlayer.pause();
                }
            });
        } catch (MediaException e) {

            System.err.println("Media file \"" + new SongEntry(attempt).getSongName() + "\" does not exist");
        } finally {
            validateLog();
        }
    }

    /**
     * Increments the current transition volume by one, while the old MediaPlayer will be set to volume-transitionVolume, and the transitioning player will be set to transitionVolume.
     */
    public void manageVol()
    {
        if(transitionPlayer != null)
        {
            if(currentTransitionVol < transitionVol) {
                currentTransitionVol++;
                mediaPlayer.setVolume((volume - currentTransitionVol)/100.0);
                transitionPlayer.setVolume((currentTransitionVol)/100.0);
            }
        }
    }

    /**
     * Turns on the transition state, and removes the previous Lambda expression from the main MediaPlayer, and loads a Transition MediaPlayer with a directory requested via Queue
     * @param attempt directory of .mp3 file
     */
    public void transition(String attempt)
    {
        transitioning = true;
        transitionVol = volume;
        currentTransitionVol = 0;
        loadTransition(attempt);
        mediaPlayer.setOnEndOfMedia(()->{});

    }

    /**
     * Replaces and the old MediaPlayer with the Transition MediaPlayer, and removes the transition state
     */
    public void endTransition()
    {
        System.out.println("Ending Transition");
        if(transitionPlayer !=null)
        {
            mediaPlayer.pause();
            mediaPlayer = null;
            mediaPlayer = transitionPlayer;
            transitionPlayer = null;
        }
        transitioning = false;
    }

    /**
     * Returns the integer value of the MediaPlayerHolder's volume distribution for all MediaPlayers
     * @return integer
     */
    public int getVolume()
    {
        return volume;
    }

    public boolean getTransitionState()
    {
        return transitioning;
    }
    /**
     * Trims all the spaces before and after the the first and last characters respectively, limits the spaces between characters to 1,
     * sets it to lower case to make commands non-case sensitive
     * @param a String command via user input
     * @return String with extra whitespace removed, and set to lower
     */
    private String normalize(String a) {
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

    /**
     *
     * @return MediaPlayer's currentTime double value of the file loaded.
     */
    public double getEndTime()
    {
        if(mediaPlayer != null) {
            return mediaPlayer.getStopTime().toMillis();
        }
        return 0;
    }

    /**
     * Returns the existing media player's current position of loaded .mp3 file
     * @return MediaPlayer's currentTime double value of the file loaded.
     */
    public double getCurrentTime()
    {
        if(mediaPlayer != null)
        {
            return mediaPlayer.getCurrentTime().toMillis();
        }
        return 0;
    }

    /**
     * Sets the the position of the the duration of the current file
     * @param requestedTime duration requested by the user
     */
    public void setSeek(Duration requestedTime)
    {
        if(!transitioning) {
            if (mediaPlayer != null) {
                mediaPlayer.seek(requestedTime);
            }
        }
    }


    //Requires: File directory attempt not equivalent to currently loaded file
    //Modifies: This
    //Effects: MediaPlayer loads a unique media file
    /**
     *Throws an exception if the same file is attempted to be reloaded
     * @param attempt directory string containing the .mp3 file
     * @throws MediaAlreadyLoadedException if the directory attempt is identical to the songDir stored in this class
     */
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
        if(!transitioning) {
            if (!isLoading && !isValidating) {
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
                        mediaPlayer.setAudioSpectrumListener(audioSpectrumListener);
                        mediaPlayer.setVolume(volume / 100.0);
                        songDir = attempt;
                        mediaPlayer.setAudioSpectrumNumBands(80);
                        SongEntry newestSong = new SongEntry(attempt);
                        songName = newestSong.getSongName();
                        log.addToLog(newestSong);
                        System.out.println("File loaded!");
                        isLoading = false;
                        play();

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
                mediaPlayer.setAudioSpectrumListener(audioSpectrumListener);
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
        if(!transitioning) {
            if (!hasInitialized) {
                System.err.println("There is no song loaded; please load a song before playing."); //Exception throw idea
            } else if (isPlaying) {
                System.out.println("Song is already playing");
            } else {
                mediaPlayer.play();
                isPlaying = true;
                System.out.println("\"" + songName + "\" is now playing.");
            }
        }
    }

    /**
     * Sets the media player class to a PAUSED state
     */
    public void pause() {
        if(!transitioning) {
            if (!hasInitialized) {
                System.err.println("There is no song loaded; please load a song before attempting to pause"); //Exception throw idea
            } else if (!isPlaying) {
                System.out.println("Song is already paused");
            } else {
                mediaPlayer.pause();
                isPlaying = false;
                System.out.println("Song paused.");
            }
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
    public boolean inUse()
    {
        return isActive;
    }

    public  boolean isLoading()
    {
        return isLoading;
    }

    public boolean isPlaying(){ return isPlaying; }

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
