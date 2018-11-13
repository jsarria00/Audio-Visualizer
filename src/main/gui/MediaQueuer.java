package gui;

import util.SongEntry;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

public class MediaQueuer implements Selectable{
    private int queueCount;
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
    public MediaQueuer()
    {
        dataList = new ArrayList<>();
        queueCount = 0;
        visibility = true;
        queueWindow = new JFrame();
        queueWindow.setTitle("Song Queue");
        queueWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        queue = new JList(new DefaultListModel());
        queue.setFixedCellHeight(20);
        queue.setFixedCellWidth(400);

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

        panelHolder.add(queue);
        panelHolder.add(buttonHolder);

        queueWindow.add(new JScrollPane(panelHolder));
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
        if(!dataList.isEmpty()) {
            dataList.remove(i);
            queueCount--;
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
            queueCount++;
            updateJList();
        }
    }

    private void toggleQueueStatus()
    {
        queuePlaying = !queuePlaying;
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
}
