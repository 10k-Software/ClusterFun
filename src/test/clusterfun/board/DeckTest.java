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

    //Defect #147
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

    //Defect #150
    public void testDebug()
    {
        Deck instance = new Deck();
        
        instance.debug(CFState.DeckType.NOSET);
        assertEquals(instance.getRemainingNumber(), 81);
        
        java.util.ArrayList<Card> newBoard = new java.util.ArrayList<Card>();
        newBoard.add(new Card(0, "One", "Red", "Squiggles", "Solid"));
newBoard.add(new Card(2, "Three", "Red", "Squiggles", "Solid"));
newBoard.add(new Card(7, "Two", "Green", "Squiggles", "Solid"));
newBoard.add(new Card(13, "Two", "Purple", "Diamonds", "Solid"));
newBoard.add(new Card(15, "One", "Green", "Diamonds", "Solid"));
newBoard.add(new Card(17, "Three", "Green", "Diamonds", "Solid"));
newBoard.add(new Card(18, "One", "Red", "Ovals", "Solid"));
newBoard.add(new Card(20, "Three", "Red", "Ovals", "Solid"));
newBoard.add(new Card(25, "Two", "Green", "Ovals", "Solid"));
newBoard.add(new Card(28, "Two", "Red", "Squiggles", "Striped"));
newBoard.add(new Card(33, "One", "Green", "Squiggles", "Striped"));
newBoard.add(new Card(35, "Three", "Green", "Squiggles", "Striped"));
newBoard.add(new Card(36, "One", "Red", "Diamonds", "Striped"));
newBoard.add(new Card(38, "Three", "Red", "Diamonds", "Striped"));
newBoard.add(new Card(40, "Two", "Purple", "Diamonds", "Striped"));
newBoard.add(new Card(46, "Two", "Red", "Ovals", "Striped"));
newBoard.add(new Card(51, "One", "Green", "Ovals", "Striped"));
newBoard.add(new Card(53, "Three", "Green", "Ovals", "Striped"));
newBoard.add(new Card(64, "Two", "Red", "Diamonds", "Empty"));
newBoard.add(new Card(70, "Two", "Green", "Diamonds", "Empty"));
newBoard.add(new Card(71, "Three", "Green", "Diamonds", "Empty"));
newBoard.add(new Card(72, "One", "Red", "Ovals", "Empty"));
newBoard.add(new Card(73, "Two", "Red", "Ovals", "Empty"));
newBoard.add(new Card(74, "Three", "Red", "Ovals", "Empty"));
newBoard.add(new Card(75, "One", "Purple", "Ovals", "Empty"));
newBoard.add(new Card(76, "Two", "Purple", "Ovals", "Empty"));
newBoard.add(new Card(77, "Three", "Purple", "Ovals", "Empty"));
newBoard.add(new Card(78, "One", "Green", "Ovals", "Empty"));
newBoard.add(new Card(79, "Two", "Green", "Ovals", "Empty"));
newBoard.add(new Card(80, "Three", "Green", "Ovals", "Empty"));
newBoard.add(new Card(1, "Two", "Red", "Squiggles", "Solid"));
newBoard.add(new Card(3, "One", "Purple", "Squiggles", "Solid"));
newBoard.add(new Card(4, "Two", "Purple", "Squiggles", "Solid"));
newBoard.add(new Card(5, "Three", "Purple", "Squiggles", "Solid"));
newBoard.add(new Card(6, "One", "Green", "Squiggles", "Solid"));
newBoard.add(new Card(8, "Three", "Green", "Squiggles", "Solid"));
newBoard.add(new Card(9, "One", "Red", "Diamonds", "Solid"));
newBoard.add(new Card(10, "Two", "Red", "Diamonds", "Solid"));
newBoard.add(new Card(11, "Three", "Red", "Diamonds", "Solid"));
newBoard.add(new Card(12, "One", "Purple", "Diamonds", "Solid"));
newBoard.add(new Card(14, "Three", "Purple", "Diamonds", "Solid"));
newBoard.add(new Card(16, "Two", "Green", "Diamonds", "Solid"));
newBoard.add(new Card(19, "Two", "Red", "Ovals", "Solid"));
newBoard.add(new Card(21, "One", "Purple", "Ovals", "Solid"));
newBoard.add(new Card(22, "Two", "Purple", "Ovals", "Solid"));
newBoard.add(new Card(23, "Three", "Purple", "Ovals", "Solid"));
newBoard.add(new Card(24, "One", "Green", "Ovals", "Solid"));
newBoard.add(new Card(26, "Three", "Green", "Ovals", "Solid"));
newBoard.add(new Card(27, "One", "Red", "Squiggles", "Striped"));
newBoard.add(new Card(29, "Three", "Red", "Squiggles", "Striped"));
newBoard.add(new Card(30, "One", "Purple", "Squiggles", "Striped"));
newBoard.add(new Card(31, "Two", "Purple", "Squiggles", "Striped"));
newBoard.add(new Card(32, "Three", "Purple", "Squiggles", "Striped"));
newBoard.add(new Card(34, "Two", "Green", "Squiggles", "Striped"));
newBoard.add(new Card(37, "Two", "Red", "Diamonds", "Striped"));
newBoard.add(new Card(39, "One", "Purple", "Diamonds", "Striped"));
newBoard.add(new Card(41, "Three", "Purple", "Diamonds", "Striped"));
newBoard.add(new Card(42, "One", "Green", "Diamonds", "Striped"));
newBoard.add(new Card(43, "Two", "Green", "Diamonds", "Striped"));
newBoard.add(new Card(44, "Three", "Green", "Diamonds", "Striped"));
newBoard.add(new Card(45, "One", "Red", "Ovals", "Striped"));
newBoard.add(new Card(47, "Three", "Red", "Ovals", "Striped"));
newBoard.add(new Card(48, "One", "Purple", "Ovals", "Striped"));
newBoard.add(new Card(49, "Two", "Purple", "Ovals", "Striped"));
newBoard.add(new Card(50, "Three", "Purple", "Ovals", "Striped"));
newBoard.add(new Card(52, "Two", "Green", "Ovals", "Striped"));
newBoard.add(new Card(54, "One", "Red", "Squiggles", "Empty"));
newBoard.add(new Card(55, "Two", "Red", "Squiggles", "Empty"));
newBoard.add(new Card(56, "Three", "Red", "Squiggles", "Empty"));
newBoard.add(new Card(57, "One", "Purple", "Squiggles", "Empty"));
newBoard.add(new Card(58, "Two", "Purple", "Squiggles", "Empty"));
newBoard.add(new Card(59, "Three", "Purple", "Squiggles", "Empty"));
newBoard.add(new Card(60, "One", "Green", "Squiggles", "Empty"));
newBoard.add(new Card(61, "Two", "Green", "Squiggles", "Empty"));
newBoard.add(new Card(62, "Three", "Green", "Squiggles", "Empty"));
newBoard.add(new Card(63, "One", "Red", "Diamonds", "Empty"));
newBoard.add(new Card(65, "Three", "Red", "Diamonds", "Empty"));
newBoard.add(new Card(66, "One", "Purple", "Diamonds", "Empty"));
newBoard.add(new Card(67, "Two", "Purple", "Diamonds", "Empty"));
newBoard.add(new Card(68, "Three", "Purple", "Diamonds", "Empty"));
newBoard.add(new Card(69, "One", "Green", "Diamonds", "Empty"));

        while(instance.getRemainingNumber() != 0)
        {
            assertTrue(instance.dealCard().equals(newBoard.remove(0)));
        }
    }

    /**
     * Tests for defect #158
     */
    public void test158()
    {
        Deck instance = new Deck();
        for(int i = 0; i < 81; i++)
        {
            assertEquals(instance.dealCard().getIndex(), i);
        }
    }
}