package clusterfun.board;
import clusterfun.board.Card;
import clusterfun.state.CFState;
/**
 * FAKE GameBoard Class
 * 
 * @author Andrew Chan
 */
public class GameBoard
{
    private java.util.ArrayList<Card> board;
    public GameBoard(CFState.DeckType type)
    {
        System.out.println("GameBoard constructed using" + type.toString());
    }

    public boolean selectCard(Card card)
    {
        System.out.println("GameBoard.selectCard() called with " + card.toString());
        return true;
    }
    
    public boolean deselectCard(Card card)
    {
        System.out.println("GameBoard.deselectCard() called with " + card.toString());
        return true;
    }

    public void deselectAllCards()
    {
        System.out.println("GameBoard.deselectAllCards() called");
    }
    
    public java.util.ArrayList<Card> addThreeCards()
    {
        System.out.println("GameBoard.addThreeCards() called");
        java.util.ArrayList<Card> newCards = new java.util.ArrayList<Card>();
        newCards.add(new Card("One", "Red", "Diamond", "Empty"));
        newCards.add(new Card("Two", "Red", "Diamond", "Empty"));
        newCards.add(new Card("Three", "Red", "Diamond", "Empty"));
        return newCards;
    }
    
    public java.util.ArrayList<Card> getCurrentCards()
    {
        java.util.ArrayList<Card> currentCards = new java.util.ArrayList<Card>();
        currentCards.add(new Card("Three",   "Green",    "Squiggles",   "Empty"   ));
        currentCards.add(new Card("Three",   "Red",      "Squiggles",   "Empty"   ));
        currentCards.add(new Card("One",     "Red",      "Ovals",       "Empty"   ));
        currentCards.add(new Card("One",     "Red",      "Ovals",       "Solid"   ));
        currentCards.add(new Card("Two",     "Red",      "Diamonds",    "Empty"   ));
        currentCards.add(new Card("Two",     "Purple",   "Diamonds",    "Solid"   ));
        currentCards.add(new Card("Three",   "Red",      "Squiggles",   "Striped" ));
        currentCards.add(new Card("One",     "Green",    "Ovals",       "Solid"   ));
        currentCards.add(new Card("Two",     "Green",    "Squiggles",   "Striped" ));
        currentCards.add(new Card("Three",   "Purple",   "Diamonds",    "Striped" ));
        currentCards.add(new Card("One",     "Purple",   "Squiggles",   "Striped" ));
        currentCards.add(new Card("One",     "Red",      "Ovals",       "Striped" ));
        return currentCards;
    }
    
    public java.util.ArrayList<Card> updateBoard()
    {
        System.out.println("GameBoard.updateBoard() called");
        return new java.util.ArrayList<Card>();
    }
}
