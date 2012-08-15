package clusterfun.board;
import java.util.Random;
import clusterfun.state.CFState;

/**
 * The Deck represents the deck of cards used in the game.  It contains multiple
 * unique Cards and can be filled with more cards, dealt cards from the top, and
 * shuffled
 * 
 * @author Andrew Chan
 * @version 0.1
 * @.date 2008-11-26
 */
//  Version History
//      12/2/08 Andrew Chan added algorithm pseudocode
public class Deck
{
    /**
     * The number of different types specified in Fill
     */
    private final int kNumberOfColorTypes = 3;
    
    /**
     * All the Cards in the Deck
     */
    private java.util.ArrayList<Card> deck;
    
    /**
     * Constructor for the Deck
     */
    public Deck()
    {
        deck = new java.util.ArrayList<Card>();
        int index = 0;
        // FOR EACH fill type
        for(Card.FillType fill : Card.FillType.values())
        {
            // FOR EACH symbol type
            for(Card.SymbolType symbol : Card.SymbolType.values())
            {
                // FOR EACH color type
                for(Card.ColorType color : Card.ColorType.values())
                {
                    // FOR EACH number type
                    for(Card.NumberType number : Card.NumberType.values())
                    {
                        // ADD card to deck with index, number, color, symbol, fill
                        deck.add(new Card(index, number, color, symbol, fill));
                        index++;
                    }
                    // ENDFOR
                }
                // ENDFOR
            }
            // ENDFOR
        }
        // ENDFOR
    }
    
    /**
     * Adds a new Card to the Deck
     * 
     * @param newCard the Card to be added
     */
    public void addCard(Card newCard)
    {
        // ADD the new card to the deck
        deck.add(newCard);
    }
    
    /**
     * Deals a Card from the top of the Deck.  A null will be passed if no cards
     * are left in the Deck
     * 
     * @return a Card from the top of the Deck
     */
    public Card dealCard()
    {
        Card dealtCard = null;
        // IF there are more than 0 cards in the deck
        if(getRemainingNumber()>0)
        {
            // REMOVE the card from the front of the deck
            // RETURN the card
            dealtCard = deck.remove(0);
        }
        return dealtCard;
    }
    
    /**
     * Requests the number of cards remaining in the Deck
     * 
     * @return the number of cards remaining in the Deck
     */
    public int getRemainingNumber()
    {
        // RETURN the size of the deck
        return deck.size();
    }
    
    /**
     * Randomizes the cards stored within the Deck
     */
    public void shuffleDeck()
    {
        Card tempCard = null;
        Random rand = new Random();
        int randomIndex = 0;
        // FOR EACH card in the deck
        for(int index = 0; index < deck.size(); index++)
        {
            // SWAP card with a random card location
            randomIndex = rand.nextInt(deck.size());
            tempCard = deck.get(randomIndex);
            deck.set(randomIndex, deck.get(index));
            deck.set(index, tempCard);
        }
        // ENDFOR
    }
 
    /**
     * Sets beginner mode by deleting all cards that don't match a random Fill
     * type
     */
    public void setBeginner()
    {
        
        Random rand = new Random();
        Card.ColorType selectedColor;
        Card.ColorType[] colorTypes;
        java.util.ArrayList<Card> newDeck = new java.util.ArrayList<Card>();

        // SELECT at a fill type at random
        colorTypes = Card.ColorType.values();
        selectedColor = colorTypes[rand.nextInt(kNumberOfColorTypes)];
        
        // FOR every card in the deck
        for(Card currentCard : deck)
        {
            // IF the fill of the card is the specified fill
            if(currentCard.getColor() == selectedColor)
            {
                // ADD the card to a new deck
                newDeck.add(currentCard);
            }
        }
        deck = newDeck;
    }
    
    /**
     * Debug method that allows for more control over the deck used for testing
     * and debugging
     * 
     * @param type  the type of deck required for the testing
     */
    public void debug(CFState.DeckType type)
    {
        // SWITCH the deck type
        switch(type)
        {
            // CASE the deck type is NOSET
            case NOSET:
                java.util.ArrayList<Card> newBoard = new java.util.ArrayList<Card>();
                	 	                newBoard.add(deck.get(0)); 
                newBoard.add(new Card(2, "One", "Red", "Squiggles", "Solid"));
                newBoard.add(new Card(7, "Three", "Red", "Squiggles", "Solid"));
                newBoard.add(new Card(13, "Two", "Green", "Squiggles", "Solid"));
                newBoard.add(new Card(15, "Two", "Purple", "Diamonds", "Solid"));
                newBoard.add(new Card(17, "One", "Green", "Diamonds", "Solid"));
                newBoard.add(new Card(18, "Three", "Green", "Diamonds", "Solid"));
                newBoard.add(new Card(20, "One", "Red", "Ovals", "Solid"));
                newBoard.add(new Card(25, "Three", "Red", "Ovals", "Solid"));
                newBoard.add(new Card(28, "Two", "Green", "Ovals", "Solid"));
                newBoard.add(new Card(33, "Two", "Red", "Squiggles", "Striped"));
                newBoard.add(new Card(35, "One", "Green", "Squiggles", "Striped"));
                newBoard.add(new Card(36, "Three", "Green", "Squiggles", "Striped"));
                newBoard.add(new Card(38, "One", "Red", "Diamonds", "Striped"));
                newBoard.add(new Card(40, "Three", "Red", "Diamonds", "Striped"));
                newBoard.add(new Card(46, "Two", "Purple", "Diamonds", "Striped"));
                newBoard.add(new Card(51, "Two", "Red", "Ovals", "Striped"));
                newBoard.add(new Card(53, "One", "Green", "Ovals", "Striped"));
                newBoard.add(new Card(64, "Three", "Green", "Ovals", "Striped"));
                newBoard.add(new Card(70, "Two", "Red", "Diamonds", "Empty"));
                newBoard.add(new Card(1, "Two", "Green", "Diamonds", "Empty"));
                newBoard.add(new Card(3, "Two", "Red", "Squiggles", "Solid"));
                newBoard.add(new Card(4, "One", "Purple", "Squiggles", "Solid"));
                newBoard.add(new Card(5, "Two", "Purple", "Squiggles", "Solid"));
                newBoard.add(new Card(6, "Three", "Purple", "Squiggles", "Solid"));
                newBoard.add(new Card(8, "One", "Green", "Squiggles", "Solid"));
                newBoard.add(new Card(9, "Three", "Green", "Squiggles", "Solid"));
                newBoard.add(new Card(10, "One", "Red", "Diamonds", "Solid"));
                newBoard.add(new Card(11, "Two", "Red", "Diamonds", "Solid"));
                newBoard.add(new Card(12, "Three", "Red", "Diamonds", "Solid"));
                newBoard.add(new Card(0, "Two", "Green", "Ovals", "Empty"));
                deck = newBoard;
                break;
            // DEFAULT
            default:
                // DO nothing
                break;
        }
    }
}
