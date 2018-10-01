package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public interface Saveable {
    String FILE_DIRECTORY = "src/main/config/";

    //Requires:nothing
    //Modifies:nothing
    //Effects:Saves a Serializable object or set into a b
    void save(String fileName);

}
