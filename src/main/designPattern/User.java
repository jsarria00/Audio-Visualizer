package designPattern;

import util.SongEntry;

import java.util.ArrayList;

public class User implements Observer {
    String name;
    public ArrayList<SongEntry> knownLoaded;

    public User(String name)
    {
        this.name = name;
        knownLoaded = new ArrayList<>();
    }

    public void update(SongEntry recent)
    {
        knownLoaded.add(recent);
        System.out.println(name + " knows that " + recent.getSongName() + " was loaded into the system");
    }
}
