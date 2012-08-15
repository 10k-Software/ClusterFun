package clusterfun.ui;

import clusterfun.board.Card;
import clusterfun.ui.entity.CardEntity;
import java.io.IOException;
import junit.framework.TestCase;
import org.lwjgl.util.vector.Vector3f;

/**
 * CardEntityTest thoroughly checks the methods within the CardEntity class.
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-1-11
 */
public class CardEntityTest extends TestCase
{

    private CardEntity card;
    
    @Override
    public void setUp() throws IOException
    {
        card = new CardEntity(new Card(), null, null, null, null);
    }

    /**
     * Tests to make sure that default card entity constructors set up the card
     * entity correctly.
     */
    public void testCreateCard()
    {
        /* Test to make sure all of the variables are set by default */
        assertEquals("animation scalar", 0.0d, card.getAnimationScalar(), 0.01d);
        assertEquals("animation speed", 0.0f, card.getAnimationSpeed(), 0.01f);
        assertEquals("starting position", new Vector3f().toString(),
                card.getStartPosition().toString());
        assertEquals("ending position", new Vector3f().toString(),
                card.getEndPosition().toString());
        assertEquals("current position", new Vector3f().toString(),
                card.getPosition().toString());

        // SET the end position to a test location
        card.setEndPosition("Test Location");
        assertEquals("test location", new Vector3f(50,50,50).toString(),
                card.getEndPosition().toString());
    }

    /**
     * Test to make sure the animation and update methods work correctly
     */
    public void testCardAnimation()
    {
        /* Set animation speed so that the scalar goes to 100 in one second */
        card.setAnimationSpeed(100);
        card.setAnimating(true);
        /* Set the ending position to 50, 50, 50 */
        card.setEndPosition("Test Location");
        /* Check the starting position */
        assertEquals("card position at scalar zero", 
                new Vector3f(0,0,0).toString(),
                card.getPosition().toString());
        card.update(500);
        /* Check the midway position */
        assertEquals("card position at scalar fifty", 
                new Vector3f(25,25,25).toString(),
                card.getPosition().toString());
        card.update(500);
        /* Check the end position */
        assertEquals("card position at scalar one hundred",
                new Vector3f(50,50,50).toString(),
                card.getPosition().toString());
    }
}
