package clusterfun.board;

import clusterfun.board.Card.ColorType;
import clusterfun.board.Card.FillType;
import clusterfun.board.Card.NumberType;
import clusterfun.board.Card.SymbolType;
import junit.framework.TestCase;

/**
 *
 * @author Andrew Chan
 */
public class CardTest extends TestCase{

    public CardTest()
    {
        
    }

    /**
     * Test of Setter and Getter methods in Card
     */
    public void testSettersGetters() 
    {
        Card instance = new Card();
        instance.setNumber(NumberType.One);
        assertEquals(NumberType.One, instance.getNumber());
        instance.setColor(ColorType.Red);
        assertEquals(ColorType.Red, instance.getColor());
        instance.setSymbol(SymbolType.Squiggles);
        assertEquals(SymbolType.Squiggles, instance.getSymbol());
        instance.setFill(FillType.Solid);
        assertEquals(FillType.Solid, instance.getFill());
    }
    
    /**
     * Test of Constructor methods in Card
     */
    public void testConstructors() 
    {
        Card instance = new Card(NumberType.One, ColorType.Red, 
                                 SymbolType.Squiggles, FillType.Solid);
        assertEquals(NumberType.One, instance.getNumber());
        assertEquals(ColorType.Red, instance.getColor());
        assertEquals(SymbolType.Squiggles, instance.getSymbol());
        assertEquals(FillType.Solid, instance.getFill());
        Card instance2 = new Card("One", "Red", "Squiggles", "Solid");
        assertEquals(NumberType.One, instance2.getNumber());
        assertEquals(ColorType.Red, instance2.getColor());
        assertEquals(SymbolType.Squiggles, instance2.getSymbol());
        assertEquals(FillType.Solid, instance2.getFill());
    }
    
    /**
     * Test of Equals method in Card
     */
    public void testEquals() 
    {
        Card instance = new Card(NumberType.One, ColorType.Red, 
                                 SymbolType.Squiggles, FillType.Solid);
        Card instance2 = new Card("One", "Red", "Squiggles", "Solid");
        Card instance3 = new Card("One", "Red", "Squiggles", "Empty");
        assertTrue(instance.equals(instance2));
        assertFalse(instance.equals(instance3));
    }
    
    /**
     * Test of toString method in Card
     */
    public void testToString() 
    {
        Card instance = new Card(NumberType.One, ColorType.Red, 
                                 SymbolType.Squiggles, FillType.Solid);
        assertEquals(instance.toString(), "1RSqSo");
    }
}