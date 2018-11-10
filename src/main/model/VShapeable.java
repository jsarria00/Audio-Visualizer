package model;

import java.awt.*;

public interface VShapeable {

    int[] BACKGROUND_SLEEPING_RGB = {80, 80 , 80};
    int NUMBER_OF_COLORS = 3;
    int BACKGROUND_COLOR_CHANGE_THRESHOLD = 600;
    int BACKGROUND_MAX_COLOR_BRIGHTNESS = 140; // change this for
    int BACKGROUND_COLOR_INCREMENT_THRESHOLD = 10;

    int D_GROWTH = 2;
    int SLEEPING_HALF_WIDTH = 150;
    int PLAYING_HALF_WIDTH = 200;
    int[] SQUARE_PLAYING_RGB = {135, 10, 10};
    int[] SQUARE_SLEEPING_RGB = {45, 10, 10};
    int[] AUDIO_RECTANGLE_PLAYING_RGB = {0, 0, 105}; // Old B = 105
    int[] AUDIO_RECTANGLE_SLEEPING_RGB = {0, 0, 0};
    int NUMBER_OF_AUDIO_RECTANGLES = 60;
    int AUDIO_RECTANGLE_MINIMUM_HEIGHT = 10;
    int AUDIO_RECTANGLE_WIDTH = 15;
    int AUDIO_RECTANGLE_DISTANCE = (int)(PLAYING_HALF_WIDTH/(Math.PI/5));
    int D_RECTANGLE_HEIGHT = 4;
    int FLASH_SENSITIVITY = 70;
}
