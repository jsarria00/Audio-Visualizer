package util;

import designPattern.AbstractSongLog;
import designPattern.Observer;
import designPattern.User;

import java.io.*;
import java.util.ArrayList;

public class SongLog extends AbstractSongLog implements Saveable, Loadable{
    private static final int MAX_CAPACITY = 10;
    private static final String FILE_NAME = "SongLog.ser";
    private static ArrayList<SongEntry> songList;

    /**
     * Default Constructor.
     * It will attempt to instantiate the ArrayList via Load, if loading fails, a new ArrayList will be instantiated
     */
    public SongLog() {
        songList = load(FILE_NAME);
        if(songList == null) {
            songList = new ArrayList<>();
        }
    }

    public void notifyUsers(SongEntry a)
    {
        System.out.println("Informing all observers that " + a.getSongName() + " was loaded into the system");
        for(Observer user: users)
        {
            user.update(a);
        }
    }

    //REQUIRES: Existing FILE_NAME in src/main/config directory
    //MODIFIES: this
    //EFFECTS: Recovers a previously existing ArrayList of SongEntries and returns, else returns null
    /**
     *Attempts to recover a previous SongEntry ArrayList and return it, if not recoverable will return null.
     * @param fileName String that contains the Serialized filename with .ser at the end
     * @return a Recovered or null SongEntry ArrayList
     */
    public ArrayList<SongEntry> load(String fileName)
    {
        ArrayList songList;
        try {
            String path = Loadable.FILE_DIRECTORY + fileName;
            FileInputStream input = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(input);
            songList = (ArrayList) objectIn.readObject();
            objectIn.close();
            return songList;
        } catch(IOException e){
            System.err.println("SongLog file not found! SongLog will create upon first file load!");
        } catch(ClassNotFoundException e)
        {
            System.err.println("Program failed to input ArrayList<SongEntry>");
        }
        return null;
    }

    //Requires: String with a non-specific fileName name with the .ser extension
    //MODIFIES: nothing
    //EFFECTS: Creates a file with fileName in the src/main/config module
    /**
     * Saves a serialized object file of the arrayList stored in the SongLog in the src/main/config directory in project root
     * @param fileName String ending with ".ser"
     */
    public void save(String fileName)
    {
        try {
            String path = Saveable.FILE_DIRECTORY + fileName;
            FileOutputStream output = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(output);
            objectOut.writeObject(songList);
            objectOut.close();

        } catch(IOException e)
        {
            System.err.println("Unable to output SongLog to a file");
        }
    }

    public void validateSerialFile()
    {
        save(FILE_NAME);
    }

    /**
     * Adds to the currently held songLog ArrayList, then saves the ArrayList
     * @param s
     */
    public void addToLog(SongEntry s) {
        if(s != null) {
            if(songList.contains(s))//removes the old entry, and bumps it back to the top
            {
                songList.remove(s);
            }
            if(songList.size() >= MAX_CAPACITY)
            {
                int index_to_remove = 0;
                songList.remove(index_to_remove);
            }
            songList.add(s);
            save(FILE_NAME);
            notifyUsers(s);
        }
    }

    /**
     * Returns the existing songList ArrayList
     * @return songList ArrayList
     */
    public ArrayList<SongEntry> getSongList()
    {
        return songList;
    }
}
