package util;

import java.io.Serializable;

public class SongEntry implements Serializable {
    private String songName;
    private String songDirectory;

    /**
     * Default Constructor, takes file directory and saves the directory and extracts the fileName
     * @param songDirectory String with file directory
     */
    public SongEntry(String songDirectory)
    {
        this.songDirectory = songDirectory;
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
