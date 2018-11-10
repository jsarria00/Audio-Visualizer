package model;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class VSquare extends VShape implements Rotatable {
    private double radians;
    private int xHalfWidth;

    //REQUIRES: nothing
    //MODIFIES: This
    //Effects: Creates a VSquare with a reference to the current screen width and height
    /**
     *
     */
    public VSquare()
    {
        super(SQUARE_SLEEPING_RGB);
        xHalfWidth = SLEEPING_HALF_WIDTH;
        radians = 0;
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
    @Override
    public void awake()
    {
        rotate();
        setAnimatedHeight();
        awakeAnimation();
    }

    private void awakeAnimation() {
        if(xHalfWidth < PLAYING_HALF_WIDTH)
        {
            xHalfWidth += D_GROWTH;
        }
        //Since red is the only changing color here here.
        if(currentColor[0] < SQUARE_PLAYING_RGB[0]) {
            currentColor[0]++;
            color = new Color(currentColor[0], currentColor[1], currentColor[2]);
        }
    }

    //REQUIRES: nothing
    //MODIFIES: This
    //EFFECTS: Subtracts one from the current half-width of the square unless it's already at value 200
    //Decrements the Red value by one of the color unless it's already at value 135
    @Override
    public void asleep()
    {
        resetAudioRectangles();
        if(!isVisualizeDataDefault)
        {
            isVisualizeDataDefault = true;
            resetVisualizeData();
        }
        sleepAnimation();
    }

    private void sleepAnimation() {
        if(xHalfWidth > SLEEPING_HALF_WIDTH)
        {
            //BECAUSE I do not like the visuals of it suddenly stopping, it will rotate until it is done shifting size
            rotate();
            xHalfWidth -= D_GROWTH;
        }
        if(currentColor[0] > SQUARE_SLEEPING_RGB[0]) {
            currentColor[0]--;
            color = new Color(currentColor[0], currentColor[1], currentColor[2]);
        }
    }


//    @Override
//    public void visualize(int[] magnitudes)
//    {
//        isVisualizeDataDefault = false;
//    }

    public double getRadians()
    {
        return radians;
    }

    public int getSquareHalfWidth()
    {
        return xHalfWidth;
    }

    public Color getColor()
    {
        return color;
    }

    @Override
    public void draw(Graphics2D g2, Rectangle enclosing)
    {
        //Due to rotate, save the current orientation
        AffineTransform oldTransform = g2.getTransform();
        int enclosingW =(int)enclosing.getWidth();
        int enclosingH = (int)enclosing.getHeight();
        //MATH: Needs to reposition itself by half of the added average to simulate growth from the center
        Rectangle square = new Rectangle(enclosingW/2-xHalfWidth-rectangleAverage, enclosingH/2-xHalfWidth-rectangleAverage, 2*xHalfWidth+2*rectangleAverage, 2*xHalfWidth+2*rectangleAverage);
        g2.rotate(radians, enclosingW/2.0, enclosingH/2.0);
        g2.setColor(color);
        g2.fill(square);
        g2.setColor(new Color(0));
        g2.setStroke(new BasicStroke(2));
        g2.draw(square);
        g2.setTransform(oldTransform);

    }
}
