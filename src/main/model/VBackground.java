package model;

import java.awt.*;
import java.util.Random;

public class VBackground {


    private static final int[] SLEEPING_RGB = {80, 80 , 80};
    private static final int NUMBER_OF_COLORS = 3;
    private static final int COLOR_CHANGE_THRESHOLD = 600;
    private static final int MAX_COLOR_BRIGHTNESS = 140; // change this for
    private static final int COLOR_INCREMENT_THRESHOLD = 20;

    private static Color vBackgroundColor;
    private static int dRed;
    private static int dGreen;
    private static int dBlue;
    private static int redDirection;
    private static int greenDirection;
    private static int blueDirection;
    private static int colorChangeCounter;
    private static int colorIncrementCounter;
    private int[] currentColor;




    public VBackground()
    {
        colorChangeCounter = 0;
        colorIncrementCounter = 0;
        currentColor = new int[NUMBER_OF_COLORS];
        for(int i = 0; i < 3; i++)
        {
            currentColor[i] = SLEEPING_RGB[i];
        }
        vBackgroundColor = new Color(currentColor[0], currentColor[1], currentColor[2]);
        Random r = new Random();
        dRed = r.nextInt()%7 + 1;
        dGreen = r.nextInt()%7 +1;
        dBlue = r.nextInt()%7 + 1;
        redDirection = 1;
        greenDirection = 1;
        blueDirection = 1;

    }


    public void randomizeDeltas()
    {
        Random r = new Random();
        dRed = r.nextInt()%7+1;
        dGreen = r.nextInt()%7+1;
        dBlue = r.nextInt()%7+1;
    }
    public void awake(){

        if(colorChangeCounter >= COLOR_CHANGE_THRESHOLD)
        {
            randomizeDeltas();
            colorChangeCounter = 0;
        }
        else{
            colorChangeCounter++;
        }
        if(colorIncrementCounter >= COLOR_INCREMENT_THRESHOLD){
            colorIncrementCounter = 0;
            //RED MODIFIER
            if ((currentColor[0] + dRed * redDirection > MAX_COLOR_BRIGHTNESS) || (currentColor[0] + dRed * redDirection < SLEEPING_RGB[0])) {
                redDirection *= -1;
            } else {
                currentColor[0] += dRed * redDirection;
            }
            //GREEN MODIFIER
            if ((currentColor[1] + dGreen * greenDirection > MAX_COLOR_BRIGHTNESS) || (currentColor[1] + dGreen * greenDirection < SLEEPING_RGB[1])) {
                greenDirection *= -1;
            } else {
                currentColor[1] += dGreen * greenDirection;
            }

            //BLUE MODIFIER
            if ((currentColor[2] + dBlue * blueDirection > MAX_COLOR_BRIGHTNESS) || (currentColor[2] + dBlue * blueDirection < SLEEPING_RGB[2])) {
                blueDirection *= -1;
            } else {
                currentColor[2] += dBlue * blueDirection;
            }
            vBackgroundColor = new Color(currentColor[0], currentColor[1], currentColor[2]);
        }
        else
        {
            colorIncrementCounter++;
        }
    }

    public void asleep()
    {
        if(currentColor[0] > SLEEPING_RGB[0])
        {
            currentColor[0]--;
        }
        if(currentColor[1] > SLEEPING_RGB[1])
        {
            currentColor[1]--;
        }
        if(currentColor[2] > SLEEPING_RGB[2])
        {
            currentColor[2]--;
        }
        vBackgroundColor = new Color(currentColor[0], currentColor[1], currentColor[2]);
    }


    public void draw(Graphics2D g2, Rectangle enclosing)
    {
        g2.setColor(vBackgroundColor);
        g2.fill(enclosing);
    }
}
