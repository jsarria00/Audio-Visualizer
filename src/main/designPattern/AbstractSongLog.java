package designPattern;

import util.SongEntry;

import java.util.ArrayList;

public abstract class AbstractSongLog {

    protected ArrayList<Observer> users;

    public AbstractSongLog()
    {
        users = new ArrayList<>();
    }

    public void addObserver(Observer a)
    {
        if(!users.contains(a))
        {
            users.add(a);
        }
    }

    public void removeObserver(Observer a)
    {
        if(users.contains(a))
        {
            users.remove(a);
        }
    }

    public abstract void notifyUsers(SongEntry a);
}
