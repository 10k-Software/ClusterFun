package clusterfun.ui.image;

import junit.framework.TestCase;

/**
 * CardImageCoordTest tests the CardImageCoord class for errors
 * 
 * @author Lisa Hunter
 *
 */

public class CardImageCoordTest extends TestCase{

    private CardImageCoord coord;
    
    public void testConstruct() throws Exception
    {
        coord = new CardImageCoord(80);
        try
        {
            coord = new CardImageCoord(81);
        }
        catch(CardOutOfBoundsException e)
        {}
        try
        {
            coord = new CardImageCoord(-1);
        }
        catch(CardOutOfBoundsException e)
        {}
        
        coord = new CardImageCoord(0);
    }
    
    public void testStartCoordFirstFile() throws Exception
    {
        coord = new CardImageCoord(0);
        assertEquals(coord.getStartCoord().getX(), (float)0);
        assertEquals(coord.getStartCoord().getY(), (float)0);
        
        coord = new CardImageCoord(1);
        assertEquals(coord.getStartCoord().getX(), ((float)210)/2048);
        assertEquals(coord.getStartCoord().getY(), (float)0);
        
        coord = new CardImageCoord(9);
        assertEquals(coord.getStartCoord().getX(), (float)0);
        assertEquals(coord.getStartCoord().getY(), ((float)310)/2048);
        
        coord = new CardImageCoord(53);
        assertEquals(coord.getStartCoord().getX(), ((float)1680)/2048);
        assertEquals(coord.getStartCoord().getY(), ((float)1550)/2048);
    }
    
    public void testStartCoordSecondFile() throws Exception
    {
        coord = new CardImageCoord(54);
        assertEquals(coord.getStartCoord().getX(), (float)0);
        assertEquals(coord.getStartCoord().getY(), (float)0);
        
        coord = new CardImageCoord(55);
        assertEquals(coord.getStartCoord().getX(), ((float)210)/2048);
        assertEquals(coord.getStartCoord().getY(), (float)0);
        
        coord = new CardImageCoord(63);
        assertEquals(coord.getStartCoord().getX(), (float)0);
        assertEquals(coord.getStartCoord().getY(), ((float)310)/2048);
        
        coord = new CardImageCoord(80);
        assertEquals(coord.getStartCoord().getX(), ((float)1680)/2048);
        assertEquals(coord.getStartCoord().getY(), ((float)620)/2048);
    }
    
    public void testEndCoordFirstFile() throws Exception
    {
        coord = new CardImageCoord(0);
        assertEquals(coord.getEndCoord().getX(), ((float)209)/2048);
        assertEquals(coord.getEndCoord().getY(), ((float)309)/2048);
        
        coord = new CardImageCoord(1);
        assertEquals(coord.getEndCoord().getX(), ((float)419)/2048);
        assertEquals(coord.getEndCoord().getY(), ((float)309)/2048);
        
        coord = new CardImageCoord(9);
        assertEquals(coord.getEndCoord().getX(), ((float)209)/2048);
        assertEquals(coord.getEndCoord().getY(), ((float)619)/2048);
        
        coord = new CardImageCoord(53);
        assertEquals(coord.getEndCoord().getX(), ((float)1889)/2048);
        assertEquals(coord.getEndCoord().getY(), ((float)1859)/2048);
    }
    
    public void testEndCoordSecondFile() throws Exception
    {
        coord = new CardImageCoord(54);
        assertEquals(coord.getEndCoord().getX(), ((float)209)/2048);
        assertEquals(coord.getEndCoord().getY(), ((float)309)/2048);
        
        coord = new CardImageCoord(55);
        assertEquals(coord.getEndCoord().getX(), ((float)419)/2048);
        assertEquals(coord.getEndCoord().getY(), ((float)309)/2048);
        
        coord = new CardImageCoord(63);
        assertEquals(coord.getEndCoord().getX(), ((float)209)/2048);
        assertEquals(coord.getEndCoord().getY(), ((float)619)/2048);
        
        coord = new CardImageCoord(80);
        assertEquals(coord.getEndCoord().getX(), ((float)1889)/2048);
        assertEquals(coord.getEndCoord().getY(), ((float)929)/2048);
    }
}
