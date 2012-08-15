package clusterfun.board;
import clusterfun.state.CFState;
import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * 
 * @author Andrew Chan
 */
public class GameBoardTest extends TestCase
{
    public GameBoardTest()
    {
        
    }

    public void testGameBoard(){
        ArrayList<Card> expectedField = new ArrayList<Card>();
        ArrayList<Card> currentField;
        GameBoard instance = new GameBoard(CFState.DeckType.NOSHUFFLE);
    
        expectedField.add(new Card("One", "Red", "Squiggles", "Solid"));
        expectedField.add(new Card("Two", "Red", "Squiggles", "Solid"));
        expectedField.add(new Card("Three", "Red", "Squiggles", "Solid"));
        expectedField.add(new Card("One", "Purple", "Squiggles", "Solid"));
        expectedField.add(new Card("Two", "Purple", "Squiggles", "Solid"));
        expectedField.add(new Card("Three", "Purple", "Squiggles", "Solid"));
        expectedField.add(new Card("One", "Green", "Squiggles", "Solid"));
        expectedField.add(new Card("Two", "Green", "Squiggles", "Solid"));
        expectedField.add(new Card("Three", "Green", "Squiggles", "Solid"));
        expectedField.add(new Card("One", "Red", "Diamonds", "Solid"));
        expectedField.add(new Card("Two", "Red", "Diamonds", "Solid"));
        expectedField.add(new Card("Three", "Red", "Diamonds", "Solid"));
        
        currentField = instance.getCurrentCards();
        for(int index = 0; index < currentField.size(); index++)
        {
            assertTrue(currentField.get(index).equals(expectedField.get(index)));
        }
    
        instance.selectCard(new Card("One", "Red", "Squiggles", "Solid"));
        instance.selectCard(new Card("One", "Purple", "Squiggles", "Solid"));
        instance.selectCard(new Card("One", "Green", "Squiggles", "Solid"));
        instance.deselectCard(new Card("One", "Green", "Squiggles", "Solid"));
        instance.selectCard(new Card("One", "Red", "Diamonds", "Solid"));
        instance.updateBoard();
        expectedField.remove(new Card("One", "Red", "Squiggles", "Solid"));
        expectedField.remove(new Card("One", "Purple", "Squiggles", "Solid"));
        expectedField.remove(new Card("One", "Red", "Diamonds", "Solid"));
        expectedField.add(new Card("One", "Purple", "Diamonds", "Solid"));
        expectedField.add(new Card("Two", "Purple", "Diamonds", "Solid"));
        expectedField.add(new Card("Three", "Purple", "Diamonds", "Solid"));
        currentField = instance.getCurrentCards();
        
        for(int index = 0; index < currentField.size(); index++)
        {
            assertTrue(currentField.get(index).equals(expectedField.get(index)));
        }
    
        instance.selectCard(new Card("One", "Purple", "Diamonds", "Solid"));
        instance.selectCard(new Card("Two", "Red", "Squiggles", "Solid"));
        instance.selectCard(new Card("Three", "Red", "Squiggles", "Solid"));
        instance.deselectAllCards();
        instance.selectCard(new Card("Two", "Purple", "Diamonds", "Solid"));
        instance.selectCard(new Card("Two", "Purple", "Squiggles", "Solid"));
        instance.selectCard(new Card("Three", "Purple", "Squiggles", "Solid"));
        instance.updateBoard();
        expectedField.remove(new Card("Two", "Purple", "Diamonds", "Solid"));
        expectedField.remove(new Card("Two", "Purple", "Squiggles", "Solid"));
        expectedField.remove(new Card("Three", "Purple", "Squiggles", "Solid"));
        expectedField.add(new Card("One", "Green", "Diamonds", "Solid"));
        expectedField.add(new Card("Two", "Green", "Diamonds", "Solid"));
        expectedField.add(new Card("Three", "Green", "Diamonds", "Solid"));
        currentField = instance.getCurrentCards();
        
        for(int index = 0; index < currentField.size(); index++)
        {
            assertTrue(currentField.get(index).equals(expectedField.get(index)));
        }
    
        instance.addThreeCards();
        expectedField.add(new Card("One", "Red", "Ovals", "Solid"));
        expectedField.add(new Card("Two", "Red", "Ovals", "Solid"));
        expectedField.add(new Card("Three", "Red", "Ovals", "Solid"));
        currentField = instance.getCurrentCards();
        
        for(int index = 0; index < currentField.size(); index++)
        {
            assertTrue(currentField.get(index).equals(expectedField.get(index)));
        }
        instance.selectCard(new Card("One", "Red", "Ovals", "Solid"));
        instance.selectCard(new Card("Two", "Red", "Ovals", "Solid"));
        instance.selectCard(new Card("Three", "Red", "Ovals", "Solid"));
        instance.updateBoard();
        expectedField.remove(new Card("One", "Red", "Ovals", "Solid"));
        expectedField.remove(new Card("Two", "Red", "Ovals", "Solid"));
        expectedField.remove(new Card("Three", "Red", "Ovals", "Solid"));
        currentField = instance.getCurrentCards();
        
        for(int index = 0; index < currentField.size(); index++)
        {
            assertTrue(currentField.get(index).equals(expectedField.get(index)));
        }
    }
    
}