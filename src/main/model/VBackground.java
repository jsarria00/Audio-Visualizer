package model;



import java.awt.*;
import java.util.Random;

public class VBackground extends VShape implements VShapeable {


    private static int dRed;
    private static int dGreen;
    private static int dBlue;
    private static int redDirection;
    private static int greenDirection;
    private static int blueDirection;
    private static int colorChangeCounter;
    private static int colorIncrementCounter;
    private int max;
    private int decreaseMaxCounter;
    private int flashCooldownRemaining;
    private int flashVisibility;
    private final int COOLDOWN = 100;//one-half second




    public VBackground()
    {
        super(BACKGROUND_SLEEPING_RGB);
        colorChangeCounter = 0;
        colorIncrementCounter = 0;
        currentColor = new int[NUMBER_OF_COLORS];
        for(int i = 0; i < 3; i++)
        {
            currentColor[i] = BACKGROUND_SLEEPING_RGB[i];
        }
        color = new Color(currentColor[0], currentColor[1], currentColor[2]);
        Random r = new Random();
        dRed = r.nextInt()%7 + 1;
        dGreen = r.nextInt()%7 +1;
        dBlue = r.nextInt()%7 + 1;
        redDirection = 1;
        greenDirection = 1;
        blueDirection = 1;
        max = AUDIO_RECTANGLE_MINIMUM_HEIGHT;
        decreaseMaxCounter = 0;
        flashCooldownRemaining = 0;
        flashVisibility = 0;

    }

    private void randomizeDeltas()
    {
        Random r = new Random();
        dRed = r.nextInt()%7+1;
        dGreen = r.nextInt()%7+1;
        dBlue = r.nextInt()%7+1;
    }

    private void modifyRed()
    {

        //RED MODIFIER
        if ((currentColor[0] + dRed * redDirection > BACKGROUND_MAX_COLOR_BRIGHTNESS) || (currentColor[0] + dRed * redDirection < BACKGROUND_SLEEPING_RGB[0])) {
            redDirection *= -1;
        } else {
            currentColor[0] += dRed * redDirection;
        }
    }

    private void modifyGreen()
    {
        //GREEN MODIFIER
        if ((currentColor[1] + dGreen * greenDirection > BACKGROUND_MAX_COLOR_BRIGHTNESS) || (currentColor[1] + dGreen * greenDirection < BACKGROUND_SLEEPING_RGB[1])) {
            greenDirection *= -1;
        } else {
            currentColor[1] += dGreen * greenDirection;
        }
    }

    private void modifyBlue()
    {
        //BLUE MODIFIER
        if ((currentColor[2] + dBlue * blueDirection > BACKGROUND_MAX_COLOR_BRIGHTNESS) || (currentColor[2] + dBlue * blueDirection < BACKGROUND_SLEEPING_RGB[2])) {
            blueDirection *= -1;
        } else {
            currentColor[2] += dBlue * blueDirection;
        }
    }

    @Override
    public void awake(){
        setAnimatedHeight();
        if(colorChangeCounter >= BACKGROUND_COLOR_CHANGE_THRESHOLD)
        {
            randomizeDeltas();
            colorChangeCounter = 0;
        }
        else{
            colorChangeCounter++;
        }
        animateColorAwake();
        setFlash();
    }

    private void animateColorAwake() {
        if(colorIncrementCounter >= BACKGROUND_COLOR_INCREMENT_THRESHOLD){
            colorIncrementCounter = 0;
            modifyRed();
            modifyGreen();
            modifyBlue();
            color = new Color(currentColor[0], currentColor[1], currentColor[2]);
        }
        else
        {
            colorIncrementCounter++;
        }
    }

    @Override
    public void asleep()
    {
        resetVisualizeData();
        animateColorToSleep();
        color = new Color(currentColor[0], currentColor[1], currentColor[2]);
        if(rectangleAverage > AUDIO_RECTANGLE_MINIMUM_HEIGHT)
        {
            rectangleAverage = AUDIO_RECTANGLE_MINIMUM_HEIGHT;
        }
        if(!isVisualizeDataDefault)
        {
            isVisualizeDataDefault = true;
            resetVisualizeData();
        }
    }

    private void animateColorToSleep() {
        if(currentColor[0] > BACKGROUND_SLEEPING_RGB[0])
        {
            currentColor[0]--;
        }
        if(currentColor[1] > BACKGROUND_SLEEPING_RGB[1])
        {
            currentColor[1]--;
        }
        if(currentColor[2] > BACKGROUND_SLEEPING_RGB[2])
        {
            currentColor[2]--;
        }
    }

    private void setFlash()
    {
        if(flashVisibility > 0)
        {
            flashVisibility-=2;
        }
        else if(flashCooldownRemaining > 0)
        {
            flashCooldownRemaining--;
        }
        else if(rectangleAverage > max && rectangleAverage > FLASH_SENSITIVITY)
        {
            max = rectangleAverage;
            flashVisibility = 254;
            flashCooldownRemaining = COOLDOWN/2;
        }
        if(decreaseMaxCounter > 0)
        {
            decreaseMaxCounter--;
        }
        else if(max > FLASH_SENSITIVITY)
        {
            max--;
            decreaseMaxCounter = COOLDOWN;
        }
    }

    @Override
    public void draw(Graphics2D g2, Rectangle enclosing)
    {
        g2.setColor(color);
        g2.fill(enclosing);
        g2.setColor(new Color(255,255,255, flashVisibility));
        g2.fill(enclosing);
        System.out.println(rectangleAverage);
    }
}
