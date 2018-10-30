package model;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class VAudioRectangles extends VShape implements Rotatable{

    private double radians;

    public VAudioRectangles()
    {
        super(AUDIO_RECTANGLE_SLEEPING_RGB);
        radians = 0;
    }

    @Override
    public void rotate()
    {
        if(radians - D_RADIANS/SLOWER_BY <= 0)
        {
            radians = 2*Math.PI;
        }
        else
        {
            radians -= D_RADIANS/SLOWER_BY;
        }
    }


    @Override
    public void awake()
    {
        setAnimatedHeight();
        rotate();
        if(currentColor[2] < AUDIO_RECTANGLE_PLAYING_RGB[2])
        {
            currentColor[2]++;
        }
    }



    @Override
    public void asleep()
    {
        resetAudioRectangles();
        if(!isVisualizeDataDefault)
        {
            isVisualizeDataDefault = true;
            resetVisualizeData();
        }
        if(currentColor[2] > AUDIO_RECTANGLE_SLEEPING_RGB[2])
        {
            rotate();
            currentColor[2]--;
        }
    }




    @Override
    public void draw(Graphics2D g2, Rectangle enclosing){
        AffineTransform oldTransform = g2.getTransform();
        int enclosingW =(int)enclosing.getWidth();
        int enclosingH = (int)enclosing.getHeight();
        g2.rotate(radians, enclosingW/2.0, enclosingH/2.0);
        g2.setStroke(new BasicStroke(1));

        for(int i = 0; i < 2*NUMBER_OF_AUDIO_RECTANGLES; i++)
        {
            Rectangle smallAudioRectangle;
            Rectangle largeAudioRectangle;
            if(i < NUMBER_OF_AUDIO_RECTANGLES) {
                smallAudioRectangle = new Rectangle((int) enclosing.getWidth() / 2, AUDIO_RECTANGLE_DISTANCE + (int) enclosing.getHeight() / 2 + rectangleAverage, AUDIO_RECTANGLE_WIDTH, rectangleHeight[i]*4/7);
                largeAudioRectangle = new Rectangle((int) enclosing.getWidth() / 2, AUDIO_RECTANGLE_DISTANCE + (int) enclosing.getHeight() / 2 + rectangleAverage, AUDIO_RECTANGLE_WIDTH, rectangleHeight[i]);
            }
            else
            {
                smallAudioRectangle = new Rectangle((int) enclosing.getWidth() / 2, AUDIO_RECTANGLE_DISTANCE + (int) enclosing.getHeight() / 2 + rectangleAverage, AUDIO_RECTANGLE_WIDTH, rectangleHeight[i-NUMBER_OF_AUDIO_RECTANGLES]*4/7);
                largeAudioRectangle = new Rectangle((int) enclosing.getWidth() / 2, AUDIO_RECTANGLE_DISTANCE + (int) enclosing.getHeight() / 2 + rectangleAverage, AUDIO_RECTANGLE_WIDTH, rectangleHeight[i-NUMBER_OF_AUDIO_RECTANGLES]);//2*NUMBER_OF_AUDIO_RECTANGLES-i-1
            }
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fill(largeAudioRectangle);
            g2.setColor(new Color(currentColor[1], currentColor[1], currentColor[2], 157));
            g2.fill(smallAudioRectangle);
            g2.setColor(new Color(0, 0, 0, 157));
            g2.draw(smallAudioRectangle);
            g2.rotate(Math.PI / NUMBER_OF_AUDIO_RECTANGLES, enclosing.getWidth() / 2, enclosing.getHeight() / 2);

        }
        g2.setTransform(oldTransform);
    }
}
