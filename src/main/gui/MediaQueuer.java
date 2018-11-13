package gui;

import javafx.util.Duration;
import util.MediaAlreadyLoadedException;
import util.SongEntry;
import util.VisualizerApplication;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MediaQueuer implements Runnable, Selectable{
    private int queueCount;
    private VisualizerApplication vApplication;
    private JFrame queueWindow;
    private ArrayList<SongEntry> dataList;
    private JList queue;
    private JSplitPane panelHolder;
    private JPanel buttonHolder;
    private JButton addToQueue;
    private JButton removeFromQueue;
    private JButton clearQueue;
    private JButton toggleQueue;
    boolean visibility;
    boolean queuePlaying;
    private final String NOT_PLAYING = "Not playing";
    private final String PLAYING = "Playing queue";

    public MediaQueuer(VisualizerApplication vApplication)
    {
        this.vApplication = vApplication;
        dataList = new ArrayList<>();
        queueCount = 0;
        visibility = true;
        queueWindow = new JFrame();
        queueWindow.setTitle("Song Queue");
        queueWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        queue = new JList(new DefaultListModel());
        queue.setFixedCellHeight(20);
        queue.setFixedCellWidth(350);

        //SplitPane
        panelHolder = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        panelHolder.setDividerLocation(320);
        panelHolder.setEnabled(false);
        buttonHolder = new JPanel();
        //Buttons

        addToQueue = new JButton("Add");
        addToQueue.addActionListener(e->{addToQueue();});

        removeFromQueue = new JButton("Remove");
        removeFromQueue.addActionListener(e->{removeFromQueue(queue.getSelectedIndex());});


        clearQueue = new JButton("Clear");
        clearQueue.addActionListener(e->{clearQueue();});

        toggleQueue = new JButton(NOT_PLAYING);
        toggleQueue.addActionListener(e->{toggleQueueStatus();});
        queuePlaying = false;

        //ButtonHolder
        buttonHolder.add(addToQueue);
        buttonHolder.add(removeFromQueue);
        buttonHolder.add(clearQueue);
        buttonHolder.add(toggleQueue);

        panelHolder.add(new JScrollPane(queue));
        panelHolder.add(buttonHolder);

        queueWindow.add(panelHolder);
        queueWindow.setSize(new Dimension(800,400));
        queueWindow.setResizable(false);
        queueWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                toggleVisibility();
            }
        });
        queueWindow.setVisible(visibility);
    }


    public void updateJList()
    {
        String[] data = new String[queueCount];
        int count = 0;
        for(SongEntry s: dataList)
        {
            data[count] = s.getSongName();
            count++;
        }
        queue.setListData(data);
    }

    private void removeFromQueue(int i)
    {

        if(!dataList.isEmpty() && i != -1) {
            dataList.remove(i);
            queueCount = dataList.size();
        }
        updateJList();
    }

    private void clearQueue()
    {
        queueCount = 0;
        dataList = new ArrayList<>();
        updateJList();
    }

    private void addToQueue()
    {
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(new FileNameExtensionFilter(".mp3", "mp3"));
        File f = new File(MEDIA_DIRECTORY);
        selector.setCurrentDirectory(f);
        int option = selector.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            String fileDir = selector.getSelectedFile().toString();
            dataList.add(new SongEntry(fileDir));
            queueCount = dataList.size();
            updateJList();
        }
    }

    private void toggleQueueStatus()
    {
        queuePlaying = !queuePlaying;
        changeQueueButtonText();
    }

    private void changeQueueButtonText() {
        if(queuePlaying)
        {
            toggleQueue.setText(PLAYING);
        }
        else
        {
            toggleQueue.setText(NOT_PLAYING);
        }
    }

    public void toggleVisibility()
    {
        visibility = !visibility;
        queueWindow.setVisible(visibility);
    }

    public void turnOffQueue() {
        queuePlaying = false;
        changeQueueButtonText();
    }

    @Override
    public void run()
    {
        while(true)
        {
            try{
                sleep(200);
            }catch (InterruptedException e)
            {
                System.err.println("Media Queue wait time was interrupted.");
            }
            catch (IllegalMonitorStateException e)
            {
                System.err.println("Queue thread Entered illegal state.");
            }
            if(dataList.isEmpty())
            {
                turnOffQueue();
            }
            else if(queuePlaying && !vApplication.isLoading() && !vApplication.inUse() && !vApplication.isPlaying())
            {
                SongEntry temp = dataList.get(0);
                dataList.remove(0);
                queueCount = dataList.size();
                updateJList();
                try{
                    vApplication.load(temp.getSongDirectory());
                } catch(MediaAlreadyLoadedException e){
                    vApplication.setSeek(new Duration(0));
                    vApplication.togglePlayState();
                }
            }
        }
    }


}
