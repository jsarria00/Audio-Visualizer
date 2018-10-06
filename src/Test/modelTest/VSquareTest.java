package modelTest;

import model.VSquare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VSquareTest {
    private static final int NUMBER_OF_COLORS = 3;
    private static final double D_RADIANS = (Math.PI) / 400;
    private static final int D_GROWTH = 1;
    private static final int SLEEPNG_HALF_WIDTH = 150;
    private static final int PLAYING_HALF_WIDTH = 200;
    private static final int[] PLAYING_RGB = {135, 10, 10};
    private static final int[] SLEEPING_RGB= {45, 10, 10};
    private VSquare testSquare;

    //This allows for comparing values that use PI, we desire at least no error at 10 decimal places
    //Note that a loop adding 1/400th will produce a different result than multiplying D_Radians - This will be interfaced.
    private double floorToTenthD(double a)
    {
        return (Math.floor(a*Math.pow(10,10)))/Math.pow(10,10);
    }

    @BeforeEach
    public void runBefore() {

        testSquare = new VSquare();
    }

    @Test
    public void testRotate()
    {
        double fakeRadians = 0;
        assertEquals(testSquare.getRadians(), fakeRadians);
        //rotate 400 times
        fakeRadians = D_RADIANS*400;
        for(int i = 0; i < 400; i++)
        {
            testSquare.rotate();
        }
        assertEquals(Double.compare(floorToTenthD(testSquare.getRadians()), floorToTenthD(fakeRadians)), 0);
        //rotate 399 times
        fakeRadians += D_RADIANS*399;
        for(int i = 0; i < 399; i++)
        {
            testSquare.rotate();
        }
        assertEquals(Double.compare(floorToTenthD(testSquare.getRadians()), floorToTenthD(fakeRadians)), 0);
        //rotate 1 more time
        fakeRadians = 0;
        testSquare.rotate();
        assertEquals(Double.compare(floorToTenthD(testSquare.getRadians()), floorToTenthD(fakeRadians)), 0);
    }
    @Test
    public void testAwake()
    {
        assertEquals(SLEEPNG_HALF_WIDTH, testSquare.getSquareHalfWidth());
        assertEquals(SLEEPING_RGB[0], testSquare.getColor().getRed());
        int counter = 0;
        testSquare.awake();
        counter++;
        assertEquals(SLEEPNG_HALF_WIDTH+counter, testSquare.getSquareHalfWidth());
        assertEquals(SLEEPING_RGB[0]+counter, testSquare.getColor().getRed());
        for(int i = 0; i < 5; i++)
        {
            counter++;
            testSquare.awake();
        }
        assertEquals(SLEEPNG_HALF_WIDTH+counter, testSquare.getSquareHalfWidth());
        assertEquals(SLEEPING_RGB[0]+counter,testSquare.getColor().getRed());

        //extremes
        for(int i = 0; i < 1000; i++)
        {
            testSquare.awake();
        }
        assertEquals(PLAYING_HALF_WIDTH, testSquare.getSquareHalfWidth());
        assertEquals(PLAYING_RGB[0],testSquare.getColor().getRed());
    }

    @Test
    public void testAsleep()
    {
        for(int i = 0; i < 1000; i++)
        {
            testSquare.awake();
        }
        assertEquals(PLAYING_HALF_WIDTH ,testSquare.getSquareHalfWidth());
        assertEquals(PLAYING_RGB[0], testSquare.getColor().getRed());
        int counter = 0;

        counter++;
        testSquare.asleep();
        assertEquals(PLAYING_HALF_WIDTH-counter ,testSquare.getSquareHalfWidth());
        assertEquals(PLAYING_RGB[0]-counter, testSquare.getColor().getRed());

        for(int i = 0; i < 5; i++)
        {
            counter++;
            testSquare.asleep();
        }
        assertEquals(PLAYING_HALF_WIDTH-counter ,testSquare.getSquareHalfWidth());
        assertEquals(PLAYING_RGB[0]-counter, testSquare.getColor().getRed());

        //extremes
        for(int i = 0; i < 1000; i++)
        {
            testSquare.asleep();
        }
        assertEquals(SLEEPNG_HALF_WIDTH, testSquare.getSquareHalfWidth());
        assertEquals(SLEEPING_RGB[0], testSquare.getColor().getRed());
    }
}
