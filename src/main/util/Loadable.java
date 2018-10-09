package util;

import java.util.ArrayList;

public interface Loadable{
    String FILE_DIRECTORY = "src/main/config/";

    ArrayList<SongEntry> load(String fileName);

}
