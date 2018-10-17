package model;

import java.awt.*;

public abstract class VShape implements VShapeable {
    protected int[] currentColor;
    protected Color color;
    protected int[] visualizeData;
    protected boolean isVisualizeDataDefault;

    protected int[] rectangleHeight;
    protected int rectangleAverage;

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
        rectangleAverage = 0;

        rectangleHeight = new int[NUMBER_OF_AUDIO_RECTANGLES];
        for(int i = 0; i < NUMBER_OF_AUDIO_RECTANGLES; i++)
        {
            rectangleHeight[i] = AUDIO_RECTANGLE_MINIMUM_HEIGHT;
        }
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
        int recAvg = 0;
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
            //INTEGRATION OF A SIN FUNCTION TRANSLATED TO CODE(SIMULATES )
            recAvg += rectangleHeight[i]/2;
            if(rectangleAverage < recAvg) {
                rectangleAverage++;
            }
            else if(rectangleAverage > recAvg)
            {
                rectangleAverage--;
            }
        }
        rectangleAverage = recAvg/NUMBER_OF_AUDIO_RECTANGLES;
    }

    protected void resetAudioRectangles()
    {
        for(int i = 0; i < NUMBER_OF_AUDIO_RECTANGLES; i++)
        {
            if(rectangleHeight[i] - D_RECTANGLE_HEIGHT < AUDIO_RECTANGLE_MINIMUM_HEIGHT)
            {
                rectangleHeight[i] = AUDIO_RECTANGLE_MINIMUM_HEIGHT;
            }
            else if(rectangleHeight[i] > AUDIO_RECTANGLE_MINIMUM_HEIGHT)
            {
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
