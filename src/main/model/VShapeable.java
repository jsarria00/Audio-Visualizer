package model;

public interface VShapeable {

    int[] BACKGROUND_SLEEPING_RGB = {80, 80 , 80};
    int NUMBER_OF_COLORS = 3;
    int BACKGROUND_COLOR_CHANGE_THRESHOLD = 600;
    int BACKGROUND_MAX_COLOR_BRIGHTNESS = 140; // change this for
    int BACKGROUND_COLOR_INCREMENT_THRESHOLD = 20;

    double D_RADIANS = (Math.PI) / 200;
    int D_GROWTH = 2;
    int SLEEPING_HALF_WIDTH = 150;
    int PLAYING_HALF_WIDTH = 200;
    int[] SQUARE_PLAYING_RGB = {135, 10, 10};
    int[] SQUARE_SLEEPING_RGB = {45, 10, 10};
}
