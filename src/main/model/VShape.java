package model;

import java.awt.*;

public abstract class VShape implements VShapeable {
    protected int[] currentColor;
    protected Color color;
    protected int[] visualizeData;
    protected boolean isVisualizeDataDefault;

    int[] rectangleHeight;

    public  VShape(int[] defaultColor)
    {
        currentColor = new int[NUMBER_OF_COLORS];
        for(int i = 0; i < NUMBER_OF_COLORS; i++) {
            currentColor[0] = defaultColor[i];
        }
        this.color = new Color(currentColor[0], currentColor[1], currentColor[2]);

        visualizeData = new int[NUMBER_OF_AUDIO_RECTANGLES];
        for(int i = 0; i < NUMBER_OF_AUDIO_RECTANGLES; i++)
        {
            visualizeData[i] = 0;
        }
        isVisualizeDataDefault = true;
    }

    protected void resetVisualizeData()
    {
        for(int i = 0; i < NUMBER_OF_AUDIO_RECTANGLES; i++)
        {
            visualizeData[i] = 0;
        }
    }

    protected void setAnimatedHeight()
    {
        for(int i = 0; i < NUMBER_OF_AUDIO_RECTANGLES; i++) {
            if((rectangleHeight[i] < AUDIO_RECTANGLE_MINIMUM_HEIGHT + D_RECTANGLE_HEIGHT * visualizeData[i]) && (rectangleHeight[i] + D_RECTANGLE_HEIGHT >  AUDIO_RECTANGLE_MINIMUM_HEIGHT + D_RECTANGLE_HEIGHT * visualizeData[i]))
            {
                rectangleHeight[i] = AUDIO_RECTANGLE_MINIMUM_HEIGHT + D_RECTANGLE_HEIGHT * visualizeData[i];
            }
            else if (rectangleHeight[i] < AUDIO_RECTANGLE_MINIMUM_HEIGHT + D_RECTANGLE_HEIGHT * visualizeData[i]) {
                rectangleHeight[i] += D_RECTANGLE_HEIGHT;
            }
            else if((rectangleHeight[i] > AUDIO_RECTANGLE_MINIMUM_HEIGHT + D_RECTANGLE_HEIGHT * visualizeData[i]) && (rectangleHeight[i] - D_RECTANGLE_HEIGHT < AUDIO_RECTANGLE_MINIMUM_HEIGHT + D_RECTANGLE_HEIGHT * visualizeData[i]))
            {
                rectangleHeight[i] = AUDIO_RECTANGLE_MINIMUM_HEIGHT + D_RECTANGLE_HEIGHT * visualizeData[i];
            }
            else if (rectangleHeight[i] > AUDIO_RECTANGLE_MINIMUM_HEIGHT + D_RECTANGLE_HEIGHT * visualizeData[i]) {
                rectangleHeight[i] -= D_RECTANGLE_HEIGHT;
            }
        }
    }


    public abstract void awake();

    public abstract void asleep();


    public void visualize(int[] magnitudes)
    {
        isVisualizeDataDefault = false;
        visualizeData = magnitudes;
    }

    public abstract void draw(Graphics2D g2, Rectangle enclosing);






}
