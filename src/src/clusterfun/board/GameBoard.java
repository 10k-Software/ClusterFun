package clusterfun.board;

import java.util.ArrayList;
import clusterfun.state.CFState;

/**
 * The GameBoard represents the current game board environment.  It contains
 * the data for all the cards currently on the board including the cards on the
 * field and the cards currently selected.  It may select and deselect cards,
 * deal new cards, and remove cards from play
 * 
 * @author Andrew Chan
 * @version 0.1
 * @.date 2008-11-26
 */
//  Version History
//      12/2/08 Andrew Chan added algorithm pseudocode
public class GameBoard
{
    /** The intial number of cards dealt on the field */
    private static final int kMAXCARDS = 12;
    
    /** The number required for a set */
    private static final int kSETSIZE = 3;
    /**
     * All the cards currently on the field
     */
    private ArrayList<Card> currentField;
    
    /**
     * The cards currently selected.  Only 3 may be selected at a time
     */
    private ArrayList<Card> selectedCards = new ArrayList<Card>();
    
    /**
     * The deck used for this GameBoard
     */
    private Deck currentDeck;
    
    /**
     * Constructor for the GameBoard using the default deck.
     * 
     * @param type the modification type for the Deck
     */
    public GameBoard(CFState.DeckType type)
    {
        currentField = new ArrayList<Card>();
        currentDeck = new Deck();
        // SWITCH the Deck type
        switch(type)
        {
            // CASE the game is in beginner mode
            case BEGINNER:
                // SET deck to beginner mode
                currentDeck.setBeginner();
                // SHUFFLE the deck
                currentDeck.shuffleDeck();
                break;
            // CASE the deck should be shuffled
            case SHUFFLE:
                // SHUFFLE the deck
                currentDeck.shuffleDeck();
                break;
            // CASE the deck needs to have no sets
            case NOSET:
                // SET deck to have no sets
                currentDeck.debug(CFState.DeckType.NOSET);
                break;
            // DEFAULT
            default:
                // DO nothing
                break;
        }
        // IF there are more than 12 cards in the deck
        if(currentDeck.getRemainingNumber() >= kMAXCARDS)
        {
            // FOR 12 cards to be dealt to the field
            for(int index = 0; index < kMAXCARDS; index++)
            {
                // DEAL a card from the deck to the field
                this.currentField.add(currentDeck.dealCard());
            }
            // ENDFOR
        }
        // ENDIF
    }
    
    /**
     * Constructor for the GameBoard.  It will deal 12 cards onto the field
     * 
     * @param starterDeck the deck to be used in the game
     */
    public GameBoard(Deck starterDeck)
    {
        currentDeck = starterDeck;
        // IF there are more than 12 cards in the deck
        if(currentDeck.getRemainingNumber() >= kMAXCARDS)
        {
            // FOR 12 cards to be dealt to the field
            for(int index = 0; index < kMAXCARDS; index++)
            {
                // DEAL a card from the deck to the field
                currentField.add(currentDeck.dealCard());
            }
            // ENDFOR
        }
        // ENDIF
    }

    /**
     * Marks the specified card as a selected card
     * 
     * @param selectedCard the field Card to be selected
     * @return      success if the card can be selected
     */
    public boolean selectCard(Card selectedCard)
    {
        boolean cardSelected = false;
        // IF the number of cards selected is less than 3 THEN
        if(selectedCards.size() < kSETSIZE)
        {
            // ADD the card from index into selected array
            selectedCards.add(selectedCard);
            // FLAG return as true
            cardSelected = true;
        }
        // ENDIF
        // RETURN flag
        return cardSelected;
    }
    
    /**
     * Unmarks the specified card as a selected card
     * 
     * @param deselectedCard the field Card to be deselected
     * @return      success if the card can be deselected
     */
    public boolean deselectCard(Card deselectedCard)
    {
        boolean cardDeselected = false;
        // IF the index is found in selectedCards
        if(selectedCards.contains(deselectedCard))
        {
            // REMOVE the index from selectedCards
            selectedCards.remove(deselectedCard);
            cardDeselected = true;
        }
        return cardDeselected;
    }
    
    /**
     * Deselects all cards
     */
    public void deselectAllCards()
    {
        // SET number of cards selected to zero
        selectedCards.clear();
    }
    
    /**
     * Adds three more cards onto the field.  These cards are appended to the
     * end of the board
     * 
     * @return  the three cards added to the field
     */
    public ArrayList<Card> addThreeCards()
    {
        Card newCard;
        ArrayList<Card> threeCards = new ArrayList<Card>();
        // IF there are three or more cards left in the deck
        if(currentDeck.getRemainingNumber() >= kSETSIZE)
        {
            // FOR 3 cards
            for(int index = 0; index < kSETSIZE; index++)
            {
                // DEAL a new card onto the field
                newCard = currentDeck.dealCard();
                currentField.add(newCard);
                threeCards.add(newCard);
            }
            // ENDFOR
        }
        // ENDIF
        return threeCards;
    }
    
    /**
     * Requests the cards currently on the field
     * 
     * @return an ArrayList of the Cards on the field
     */
    public ArrayList<Card> getCurrentCards()
    {
        // RETURN the field array
        return currentField;
    }
    
    /**
     * Returns the number of cards left in the deck
     */
    public int getRemainingDeckNumber()
    {
        return currentDeck.getRemainingNumber();
    }
    
    /**
     * Removes the selected cards from the field then deals 3 more cards.
     * If the number of cards on the field is below 12, it will deal these 3
     * cards into the locations of the removed cards.  If the number of cards
     * on the field is above 12, it will not deal any new cards, and it will
     * shift the cards over to fill in the vacant locations instead
     * 
     * @return a list of new Cards that have been dealt
     */
    public ArrayList<Card> updateBoard()
    {
        ArrayList<Card> newCards = new ArrayList<Card>();
        Card dealtCard;
        // IF the field has more than 12 cards
        if(currentField.size() > kMAXCARDS)
        {
            // FOR EACH selected card
            for(Card eachCard : selectedCards)
            {
                // REMOVE the card
                currentField.remove(eachCard);
            }
            // ENDFOR
        }
        // ELSE IF currentDeck has less than 3 cards remaining
        else if (currentDeck.getRemainingNumber() < kSETSIZE)
        {
            //FOR EACH card in the selected list
            for(Card eachCard : selectedCards)
            {
                // REMOVE the card from the field
                currentField.remove(eachCard);
            }
            newCards = null;
        }
        else
        {
            // FOR EACH selected card
            for(Card eachCard : selectedCards)
            {
                // DEAL a new card onto the selected card's spot
                dealtCard = currentDeck.dealCard();
                currentField.remove(eachCard);
                currentField.add(dealtCard);
                newCards.add(dealtCard);
            }
            // ENDFOR
        }
        // ENDIF
        // CLEAR the selected cards
        selectedCards.clear();
        return newCards;
    }
}
