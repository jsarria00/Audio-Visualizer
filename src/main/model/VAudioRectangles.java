package model;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class VAudioRectangles extends VShape {
    int[] rectangleHeight;

    public VAudioRectangles()
    {
        super(AUDIO_RECTANGLE_SLEEPING_RGB);
        rectangleHeight = new int[NUMBER_OF_AUDIO_RECTANGLES];
        for(int i = 0; i < NUMBER_OF_AUDIO_RECTANGLES; i++)
        {
            rectangleHeight[i] = AUDIO_RECTANGLE_MINIMUM_HEIGHT;
        }
    }

    public void setAnimatedHeight()
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


    @Override
    public void awake()
    {
        setAnimatedHeight();
        if(currentColor[2] < AUDIO_RECTANGLE_PLAYING_RGB[2])
        {
            currentColor[2]++;
        }
    }

    @Override
    public void asleep()
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
        if(!isVisualizeDataDefault)
        {
            isVisualizeDataDefault = true;
            resetVisualizeData();
        }
        if(currentColor[2] > AUDIO_RECTANGLE_SLEEPING_RGB[2])
        {
            currentColor[2]--;
        }
    }


    @Override
    public void visualize(int[] magnitudes)
    {
        isVisualizeDataDefault = false;
        visualizeData = magnitudes;
    }


    @Override
    public void draw(Graphics2D g2, Rectangle enclosing){

        AffineTransform oldTransform = g2.getTransform();
        g2.setStroke(new BasicStroke(1));

        for(int i = 0; i < 2*NUMBER_OF_AUDIO_RECTANGLES; i++)
        {
            Rectangle audioRectangle;
            if(i < NUMBER_OF_AUDIO_RECTANGLES) {
                audioRectangle = new Rectangle((int) enclosing.getWidth() / 2, AUDIO_RECTANGLE_DISTANCE + (int) enclosing.getHeight() / 2, AUDIO_RECTANGLE_WIDTH, rectangleHeight[i]);
            }
            else
            {
                audioRectangle = new Rectangle((int) enclosing.getWidth() / 2, AUDIO_RECTANGLE_DISTANCE + (int) enclosing.getHeight() / 2, AUDIO_RECTANGLE_WIDTH, rectangleHeight[2*NUMBER_OF_AUDIO_RECTANGLES-i-1]);
            }

            g2.setColor(new Color(currentColor[1], currentColor[1], currentColor[2], 157));
            g2.fill(audioRectangle);
            g2.setColor(new Color(0, 0, 0, 157));
            g2.draw(audioRectangle);
            g2.rotate(Math.PI / NUMBER_OF_AUDIO_RECTANGLES, enclosing.getWidth() / 2, enclosing.getHeight() / 2);

        }
        g2.setTransform(oldTransform);
    }
}
