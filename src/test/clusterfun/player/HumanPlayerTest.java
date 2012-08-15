package clusterfun.player;
import clusterfun.player.HumanPlayer;
import junit.framework.*;

/**
 * HumanPlayerTest tests the HumanPlayer class for errors
 * 
 * @author Lisa Hunter
 *
 */

public class HumanPlayerTest extends TestCase{

    private HumanPlayer one;
    
    public HumanPlayerTest()
    {
        one = new HumanPlayer("Bob",'j');
    }
    
    /**
     * Tests to see if the player is initialized properly
     */
    public void testConstructor()
    {
        assertEquals(one.getName(), "Bob");
        assertEquals(one.getScore(), 0);
        assertEquals(one.getKey(), 'j');
    }
    
    /**
     * Tests to see if the score is incremented properly
     */
    public void testIncrement()
    {   
        one.incScore();
        assertEquals(one.getScore(), 1);
        one.incScore();
        assertEquals(one.getScore(), 2);

    }
    
    /**
     * Tests to see if the score is decremented properly
     */
    public void testDecrement()
    {
        one.decScore();
        assertEquals(one.getScore(), -1);
        one.decScore();
        assertEquals(one.getScore(), -2);
        one.decScore();
        assertEquals(one.getScore(), -3);
    }
    
    /**
     * Tests to see if the equals method works properly
     */
    public void testEquals()
    {
        assertEquals(one, one);
        HumanPlayer two = one;
        assertEquals(one, two);
        
        two = new HumanPlayer("Bob", 'j');
        two.incScore();
        assertFalse(one.equals(two));
        
        assertFalse(one.equals(null));
        
        two = new HumanPlayer(null, 'j');
        assertFalse(one.equals(two));
        
        two = new HumanPlayer("Jane", 'j');
        assertFalse(one.equals(two));
        
        two = new HumanPlayer("Bob", 'q');
        assertFalse(one.equals(two));
        
        two.setKey('j');
        assertTrue(one.equals(two));
    }
}
