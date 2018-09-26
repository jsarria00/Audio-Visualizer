package model;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class VSquare {
    //VShape(<- abstract class idea) interface idea
    private static final int NUMBER_OF_COLORS = 3;
    private static final double D_RADIANS = (Math.PI) / 400;
    private static final int D_GROWTH = 1;
    private static final int SLEEPNG_HALF_WIDTH = 150;
    private static final int PLAYING_HALF_WIDTH = 200;
    private static final int[] PLAYING_RGB = {135, 10, 10};
    private static final int[] SLEEPING_RGB= {45, 10, 10};

    private double radians = 0;
    private int xHalfWidth;
    private int currentColor[];
    private Color vSquareColor; // For extends Abstraction, this will be named vShapeColor



    //REQUIRES: nothing
    //MODIFIES: This
    //Effects: Creates a VSquare with a reference to the current screen width and height
    /**
     *
     */
    public VSquare()
    {
        xHalfWidth = SLEEPNG_HALF_WIDTH;
        currentColor = new int[NUMBER_OF_COLORS];
        for(int i = 0; i < 3; i++)
        {
            currentColor[i] = SLEEPING_RGB[i];
        }
        vSquareColor = new Color(currentColor[0], currentColor[1], currentColor[2]); // Set to sleeping color
    }
    //REQUIRES: nothing
    //MODIFIES: This
    //EFFECTS: Adds one four-hundredth of a PI radian to the static class variable
            //- If there are 800 four-hundredths of a PI, then reset the rotation to 0
    public void rotate()
    {
        if(radians + D_RADIANS >= 2*(Math.PI))
        {
            radians = 0;
        }
        else
        {
            radians += D_RADIANS;
        }
    }

    //REQUIRES: nothing
    //MODIFIES: This
    //EFFECTS: Adds one to the current half-width of the square unless it's already at value 200
            //Increments the Red value by one of the color unless it's already at value 135
    public void awake()
    {
        if(xHalfWidth < PLAYING_HALF_WIDTH)
        {
            xHalfWidth += D_GROWTH;
        }
        //Since red is the only changing color here here.
        if(currentColor[0] < PLAYING_RGB[0]) {
            currentColor[0]++;
            vSquareColor = new Color(currentColor[0], currentColor[1], currentColor[2]);
        }
    }

    //REQUIRES: nothing
    //MODIFIES: This
    //EFFECTS: Subtracts one from the current half-width of the square unless it's already at value 200
    //Decrements the Red value by one of the color unless it's already at value 135
    public void asleep()
    {
        if(xHalfWidth > SLEEPNG_HALF_WIDTH)
        {
            //BECAUSE I do not like the visuals of it suddenly stopping, it will rotate until it is done shifting size
            rotate();
            xHalfWidth -= D_GROWTH;
        }
        if(currentColor[0] > SLEEPING_RGB[0]) {
            currentColor[0]--;
            vSquareColor = new Color(currentColor[0], currentColor[1], currentColor[2]);
        }
    }

    public double getRadians()
    {
        return radians;
    }

    public int getSquareHalfWidth()
    {
        return xHalfWidth;
    }

    public Color getVSquareColor()
    {
        return vSquareColor;
    }

    public void draw(Graphics2D g2, Rectangle enclosing)
    {
        int enclosingW =(int)enclosing.getWidth();
        int enclosingH = (int)enclosing.getHeight();
        Rectangle square = new Rectangle(enclosingW/2-xHalfWidth, enclosingH/2-xHalfWidth, 2*xHalfWidth, 2*xHalfWidth);
        //AffineTransform rotatedSquare = new AffineTransform();
        //rotatedSquare.rotate(radians, square.getX() + square.width/2, square.getY() + square.width/2);
        g2.rotate(radians, enclosingW/2, enclosingH/2);
        g2.setColor(vSquareColor);
        g2.fill(square);
        //g2.fill((Shape) rotatedSquare);
        g2.setColor(new Color(0));
        g2.setStroke(new BasicStroke(2));
        g2.draw(square);
        g2.rotate(0);
        //g2.draw((Shape) rotatedSquare);
    }
}
