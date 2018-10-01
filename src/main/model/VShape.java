package model;

import java.awt.*;

public abstract class VShape implements VShapeable {
    protected int[] currentColor;
    protected Color color;

    public  VShape(int[] defaultColor)
    {
        currentColor = new int[NUMBER_OF_COLORS];
        for(int i = 0; i < NUMBER_OF_COLORS; i++) {
            currentColor[0] = defaultColor[i];
        }
        this.color = new Color(currentColor[0], currentColor[1], currentColor[2]);
    }

    public abstract void awake();

    public abstract void asleep();

    public abstract void draw(Graphics2D g2, Rectangle enclosing);




}
