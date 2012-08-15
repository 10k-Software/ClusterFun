package clusterfun.board;
import clusterfun.state.CFState;

import junit.framework.TestCase;

/**
 *
 * @author Andrew Chan
 */
public class DeckTest extends TestCase{

    public DeckTest() {
    }

    /**
     * Tests for the methods in Deck
     */
    public void testDeck() {
        Deck instance = new Deck();
        java.util.ArrayList<Card> deck = new java.util.ArrayList<Card>();
        deck.add(new Card("One", "Red", "Squiggles", "Solid"));
        deck.add(new Card("Two", "Red", "Squiggles", "Solid"));
        deck.add(new Card("Three", "Red", "Squiggles", "Solid"));
        deck.add(new Card("One", "Purple", "Squiggles", "Solid"));
        deck.add(new Card("Two", "Purple", "Squiggles", "Solid"));
        deck.add(new Card("Three", "Purple", "Squiggles", "Solid"));
        deck.add(new Card("One", "Green", "Squiggles", "Solid"));
        deck.add(new Card("Two", "Green", "Squiggles", "Solid"));
        deck.add(new Card("Three", "Green", "Squiggles", "Solid"));
        deck.add(new Card("One", "Red", "Diamonds", "Solid"));
        deck.add(new Card("Two", "Red", "Diamonds", "Solid"));
        deck.add(new Card("Three", "Red", "Diamonds", "Solid"));
        deck.add(new Card("One", "Purple", "Diamonds", "Solid"));
        deck.add(new Card("Two", "Purple", "Diamonds", "Solid"));
        deck.add(new Card("Three", "Purple", "Diamonds", "Solid"));
        deck.add(new Card("One", "Green", "Diamonds", "Solid"));
        deck.add(new Card("Two", "Green", "Diamonds", "Solid"));
        deck.add(new Card("Three", "Green", "Diamonds", "Solid"));
        deck.add(new Card("One", "Red", "Ovals", "Solid"));
        deck.add(new Card("Two", "Red", "Ovals", "Solid"));
        deck.add(new Card("Three", "Red", "Ovals", "Solid"));
        deck.add(new Card("One", "Purple", "Ovals", "Solid"));
        deck.add(new Card("Two", "Purple", "Ovals", "Solid"));
        deck.add(new Card("Three", "Purple", "Ovals", "Solid"));
        deck.add(new Card("One", "Green", "Ovals", "Solid"));
        deck.add(new Card("Two", "Green", "Ovals", "Solid"));
        deck.add(new Card("Three", "Green", "Ovals", "Solid"));
        deck.add(new Card("One", "Red", "Squiggles", "Striped"));
        deck.add(new Card("Two", "Red", "Squiggles", "Striped"));
        deck.add(new Card("Three", "Red", "Squiggles", "Striped"));
        deck.add(new Card("One", "Purple", "Squiggles", "Striped"));
        deck.add(new Card("Two", "Purple", "Squiggles", "Striped"));
        deck.add(new Card("Three", "Purple", "Squiggles", "Striped"));
        deck.add(new Card("One", "Green", "Squiggles", "Striped"));
        deck.add(new Card("Two", "Green", "Squiggles", "Striped"));
        deck.add(new Card("Three", "Green", "Squiggles", "Striped"));
        deck.add(new Card("One", "Red", "Diamonds", "Striped"));
        deck.add(new Card("Two", "Red", "Diamonds", "Striped"));
        deck.add(new Card("Three", "Red", "Diamonds", "Striped"));
        deck.add(new Card("One", "Purple", "Diamonds", "Striped"));
        deck.add(new Card("Two", "Purple", "Diamonds", "Striped"));
        deck.add(new Card("Three", "Purple", "Diamonds", "Striped"));
        deck.add(new Card("One", "Green", "Diamonds", "Striped"));
        deck.add(new Card("Two", "Green", "Diamonds", "Striped"));
        deck.add(new Card("Three", "Green", "Diamonds", "Striped"));
        deck.add(new Card("One", "Red", "Ovals", "Striped"));
        deck.add(new Card("Two", "Red", "Ovals", "Striped"));
        deck.add(new Card("Three", "Red", "Ovals", "Striped"));
        deck.add(new Card("One", "Purple", "Ovals", "Striped"));
        deck.add(new Card("Two", "Purple", "Ovals", "Striped"));
        deck.add(new Card("Three", "Purple", "Ovals", "Striped"));
        deck.add(new Card("One", "Green", "Ovals", "Striped"));
        deck.add(new Card("Two", "Green", "Ovals", "Striped"));
        deck.add(new Card("Three", "Green", "Ovals", "Striped"));
        deck.add(new Card("One", "Red", "Squiggles", "Empty"));
        deck.add(new Card("Two", "Red", "Squiggles", "Empty"));
        deck.add(new Card("Three", "Red", "Squiggles", "Empty"));
        deck.add(new Card("One", "Purple", "Squiggles", "Empty"));
        deck.add(new Card("Two", "Purple", "Squiggles", "Empty"));
        deck.add(new Card("Three", "Purple", "Squiggles", "Empty"));
        deck.add(new Card("One", "Green", "Squiggles", "Empty"));
        deck.add(new Card("Two", "Green", "Squiggles", "Empty"));
        deck.add(new Card("Three", "Green", "Squiggles", "Empty"));
        deck.add(new Card("One", "Red", "Diamonds", "Empty"));
        deck.add(new Card("Two", "Red", "Diamonds", "Empty"));
        deck.add(new Card("Three", "Red", "Diamonds", "Empty"));
        deck.add(new Card("One", "Purple", "Diamonds", "Empty"));
        deck.add(new Card("Two", "Purple", "Diamonds", "Empty"));
        deck.add(new Card("Three", "Purple", "Diamonds", "Empty"));
        deck.add(new Card("One", "Green", "Diamonds", "Empty"));
        deck.add(new Card("Two", "Green", "Diamonds", "Empty"));
        deck.add(new Card("Three", "Green", "Diamonds", "Empty"));
        deck.add(new Card("One", "Red", "Ovals", "Empty"));
        deck.add(new Card("Two", "Red", "Ovals", "Empty"));
        deck.add(new Card("Three", "Red", "Ovals", "Empty"));
        deck.add(new Card("One", "Purple", "Ovals", "Empty"));
        deck.add(new Card("Two", "Purple", "Ovals", "Empty"));
        deck.add(new Card("Three", "Purple", "Ovals", "Empty"));
        deck.add(new Card("One", "Green", "Ovals", "Empty"));
        deck.add(new Card("Two", "Green", "Ovals", "Empty"));
        deck.add(new Card("Three", "Green", "Ovals", "Empty"));
        assertEquals(instance.getRemainingNumber(), 81);
        
        while(instance.getRemainingNumber() != 0)
        {
            assertTrue(instance.dealCard().equals(deck.remove(0)));
        }
        instance.addCard(new Card("One", "Red", "Diamonds", "Solid"));
        deck.add(new Card("One", "Red", "Diamonds", "Solid"));
        assertTrue(instance.dealCard().equals(deck.remove(0)));
    }
    public void testSetBeginner()
    {
        Deck instance = new Deck();
        instance.setBeginner();
        Card.ColorType selectedColor = instance.dealCard().getColor();
        while(instance.getRemainingNumber() != 0)
        {
            assertTrue(instance.dealCard().getColor().equals(selectedColor));
        }
    }

    public void testDebug()
    {
        Deck instance = new Deck();
        
        instance.debug(CFState.DeckType.NOSET);
        assertEquals(instance.getRemainingNumber(), 30);
        
        java.util.ArrayList<Card> deck = new java.util.ArrayList<Card>();
        deck.add(new Card("One", "Red", "Squiggles", "Solid"));
        deck.add(new Card("Three", "Red", "Squiggles", "Solid"));
        deck.add(new Card("Two", "Green", "Squiggles", "Solid"));
        deck.add(new Card("Two", "Purple", "Diamonds", "Solid"));
        deck.add(new Card("One", "Green", "Diamonds", "Solid"));
        deck.add(new Card("Three", "Green", "Diamonds", "Solid"));
        deck.add(new Card("One", "Red", "Ovals", "Solid"));
        deck.add(new Card("Three", "Red", "Ovals", "Solid"));
        deck.add(new Card("Two", "Green", "Ovals", "Solid"));
        deck.add(new Card("Two", "Red", "Squiggles", "Striped"));
        deck.add(new Card("One", "Green", "Squiggles", "Striped"));
        deck.add(new Card("Three", "Green", "Squiggles", "Striped"));
        deck.add(new Card("One", "Red", "Diamonds", "Striped"));
        deck.add(new Card("Three", "Red", "Diamonds", "Striped"));
        deck.add(new Card("Two", "Purple", "Diamonds", "Striped"));
        deck.add(new Card("Two", "Red", "Ovals", "Striped"));
        deck.add(new Card("One", "Green", "Ovals", "Striped"));
        deck.add(new Card("Three", "Green", "Ovals", "Striped"));
        deck.add(new Card("Two", "Red", "Diamonds", "Empty"));
        deck.add(new Card("Two", "Green", "Diamonds", "Empty"));
        deck.add(new Card("Two", "Red", "Squiggles", "Solid"));
        deck.add(new Card("One", "Purple", "Squiggles", "Solid"));
        deck.add(new Card("Two", "Purple", "Squiggles", "Solid"));
        deck.add(new Card("Three", "Purple", "Squiggles", "Solid"));
        deck.add(new Card("One", "Green", "Squiggles", "Solid"));
        deck.add(new Card("Three", "Green", "Squiggles", "Solid"));
        deck.add(new Card("One", "Red", "Diamonds", "Solid"));
        deck.add(new Card("Two", "Red", "Diamonds", "Solid"));
        deck.add(new Card("Three", "Red", "Diamonds", "Solid"));
        deck.add(new Card("Two", "Green", "Ovals", "Empty"));
        
        while(instance.getRemainingNumber() != 0)
        {
            assertTrue(instance.dealCard().equals(deck.remove(0)));   
        }
    }
}