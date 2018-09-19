package util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Scanner;

public class VisualizerMediaPlayerHolder implements Runnable
{
    //These Will be implemented in an interface
    private static final String HELP = "help";
    private static final String LOAD = "load";
    private static final String PLAY = "play";
    private static final String PAUSE = "pause";
    private static final String QUIT = "quit";
    private static final String DIRECTORY = "src/media/";

    private static boolean isActive;
    private static boolean firstStart;
    private static boolean isPlaying;
    private static boolean hasInitialized;
    private static MediaPlayer mediaPlayer;
    private static Media song;
    private String songStr;
    private String inputStr;
    private Scanner input;


    public VisualizerMediaPlayerHolder() {
        songStr = "";
        input = new Scanner(System.in);

        isActive = false;
        firstStart = true;
        isPlaying = false;
        hasInitialized = false;
    }

    private static String normalize(String a) {
        //Create a new string to hold a String without leading, extra() and trailing blanks
        String normalized = a.trim();
        normalized = normalized.replaceAll("( )+", " ");
        //with the final step of normalization, change to lower case and save to the string the class returns
        normalized = normalized.toLowerCase();
        return  normalized;
    }

    private void deInitialize() {
        if(isPlaying&&hasInitialized){
            mediaPlayer.pause();
            isPlaying = false;
        }
        mediaPlayer = null;
        hasInitialized = false;
    }

    private void load() {
        //future specification, custom exception to prevent unloading the current song if it is not found or is the same song.
        System.out.println("Type the name of the song file with it's extension. Note this is case sensitive!");
        songStr = input.nextLine();
        try {
            //To prevent operator overload from recognizing the string as a Path.
            String filePath = DIRECTORY + songStr;

            // This local variable will throw an exception without interrupting the current playback if no file is found
            Media tempPointer = new Media(new File(filePath).toURI().toString());
            deInitialize();
            song = tempPointer;
            //System.out.println("TIME: " + song.getDuration());
            mediaPlayer = new MediaPlayer(song);
            hasInitialized = true;
            //Waiting done through a listener.
            mediaPlayer.setOnReady(() -> System.out.println("File loaded!"));
            //For now
            mediaPlayer.setOnEndOfMedia(() -> deInitialize());
        }
        catch (MediaException e) {
            System.err.println("Media file \"" + songStr + "\" does not exist");
        }
    }

    private void play() {
        if (!hasInitialized) {
            System.err.println("There is no song loaded; please load a song before playing."); //Exception throw idea
        } else if (isPlaying) {
            System.out.println("Song is already playing");
        } else {
            mediaPlayer.play();
            isPlaying = true;
            System.out.println("\"" + songStr + "\" is now playing.");
        }
    }

    private void pause() {
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

    private void start()
    {
        if(firstStart) {
            System.out.println("Type \"help\" for a list of commands to get started");
            firstStart = false;
        }
            inputStr = input.nextLine();

            if(normalize(inputStr).equals(HELP)) {
                System.out.println("help - Prints a list of commands. \n" +
                        "load - prepares the program to load a song, then requests file name located in the media folder.\n" +
                        "play - starts playing the audio of loaded media.\n" +
                        "pause - pauses the audio of the loaded media.\n" +
                        "quit - De-initializes the program, and exits." );
            }
            else if(normalize(inputStr).equals(LOAD)) {
                load();
            }
            else if(normalize(inputStr).equals(PLAY)) {
                play();
            }
            else if(normalize(inputStr).equals(PAUSE)) {
                pause();
            }
            else if(normalize(inputStr).equals(QUIT)) {
                deInitialize();
                System.exit(0);
            }
            else {
                System.out.println("Unrecognized command \"" + inputStr +"\". Type \"help\" for a list of commands");
            }
    }
    public static boolean inUse()
    {
        return isActive;
    }


    @Override
    public void run() {
        isActive = true;
        //System.out.println("First");
        start();
        //System.out.println("Second");
        isActive = false;
    }

}