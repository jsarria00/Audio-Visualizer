package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public interface Loadable{
    String FILE_DIRECTORY = "src/main/config/";

    ArrayList<SongEntry> load(String fileName);

}
