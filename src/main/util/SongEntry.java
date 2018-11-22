package util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Stores a Directory and extracts the song name depending on the position of the last slash depending on the OS
 */
public class SongEntry implements Serializable {
    private String songName;
    private String songDirectory;
    private final String WINDOWS_PROGRAM_DIR = "src\\main\\media";
    private final String UNIX_PROGRAM_DIR = "src/main/media";

    /**
     * Default Constructor, takes file directory and saves the directory and extracts the fileName
     * @param songDirectory String with file directory
     */
    public SongEntry(String songDirectory)
    {
        this.songDirectory = songDirectory;
        if(this.songDirectory.contains(WINDOWS_PROGRAM_DIR) || this.songDirectory.contains(UNIX_PROGRAM_DIR))
        {
            programDirectoryTrim();
        }
        int sDLength = songDirectory.length();
        int counter = sDLength-1;
        while(counter >= 0)
        {
            //implement UNIX and Windows compatible directories
            if(songDirectory.charAt(counter) == '\\' || songDirectory.charAt(counter) == '/')
            {
                songName = songDirectory.substring(counter+1, sDLength); // Want to EXCLUDE slash
                break;
            }
            if(counter == 0)
            {
                songName = songDirectory;
            }
            counter--;
        }
    }

    /**
     * Trims the ParentDirectory into a ChildDirectory to allow the Jar application directory to move the directory that holds it without data loss.
     */
    public void programDirectoryTrim() {

        if(this.songDirectory.contains(WINDOWS_PROGRAM_DIR)) {
            this.songDirectory = songDirectory.substring(songDirectory.indexOf(WINDOWS_PROGRAM_DIR), songDirectory.length());
        }
        if(this.songDirectory.contains(UNIX_PROGRAM_DIR))
        {
            this.songDirectory = songDirectory.substring(songDirectory.indexOf(UNIX_PROGRAM_DIR), songDirectory.length());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongEntry songEntry = (SongEntry) o;
        return Objects.equals(songName, songEntry.songName);
    }


    @Override
    public int hashCode() {
        return Objects.hash(songName);
    }

    /**
     * Appends a string to a SongName
     * @param appending
     */
    public void appendSongName(String appending)
    {
        songName = songName + appending;
    }

    /**
     * Returns the name of the song with its extension
     * @return String with name of the file
     */
    public String getSongName()
    {
        return songName;
    }

    /**
     *Returns the absolutePath of the file
     * @return String with path to file
     */
    public String getSongDirectory()
    {
        return songDirectory;
    }

}
