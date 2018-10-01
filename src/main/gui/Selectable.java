package gui;

public interface Selectable {
    //
    String MEDIA_DIRECTORY = "src/main/media";
    //MouseEvents
    int TIME_UNTILL_HIDDEN = 300;
    int TIME_DELAY = 10; //50milliseconds
    int NUMBER_OF_DIMENSIONS = 2;
    //Spacing between buttons
    int OPTION_SPACING = 10;
    //Window options
    int WINDOW_OPTION_SIZE_X = 50;
    int WINDOW_OPTION_SIZE_Y = 25;
    //Square shape
    int MEDIA_OPTION_SIZE = 50;
    //Song LOG
    int SONG_LOG_SIZE_X = 600;
    int SONG_LOG_TOGGLE_SIZE = 25;
    int SONG_LOG_ITEM_X = SONG_LOG_SIZE_X - OPTION_SPACING*2;
    int SONG_LOG_ITEM_Y = 50;
}
