package util;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SongLogTest {
    //These tests will erase existing data
    private Saveable saveLog;
    private Loadable loadLog;
    private static final String SER_FILE_NAME = "test.ser";
    private static final String FILE_NAME = "test";

    @Test
    public void testLoad()
    {
        SongLog log = new SongLog();
        saveLog = log;
        String songDir = saveLog.FILE_DIRECTORY + FILE_NAME;
        log.getSongList().add(new SongEntry(songDir));
        saveLog.save(SER_FILE_NAME);
        //File loads upon instantiation;
        loadLog = new SongLog();
        SongLog logLoaded = (SongLog)loadLog;
        SongEntry first = logLoaded.getSongList().get(0);
        //compare object
        assertEquals(first, log.getSongList().get(0)); // SongEntry
        assertEquals(first.getSongName(), log.getSongList().get(0).getSongName());//String
        assertEquals(first.getSongDirectory(), log.getSongList().get(0).getSongDirectory()); //String
    }
}
