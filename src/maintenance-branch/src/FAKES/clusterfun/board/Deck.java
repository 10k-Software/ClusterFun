package clusterfun.board;

import clusterfun.state.CFState;

/**
 * FAKE Deck Class
 * 
 * @author Andrew Chan
 */
public class Deck
{
    java.util.ArrayList<Card> fakeDeck = new java.util.ArrayList();
    
    public Deck()
    {
        System.out.println("FAKE DECK CONSTRUCTED");
        fakeDeck.add(new Card("One", "Red", "Squiggles", "Solid"));
        fakeDeck.add(new Card("Two", "Red", "Squiggles", "Solid"));
        fakeDeck.add(new Card("Three", "Red", "Squiggles", "Solid"));
        fakeDeck.add(new Card("One", "Purple", "Squiggles", "Solid"));
        fakeDeck.add(new Card("Two", "Purple", "Squiggles", "Solid"));
        fakeDeck.add(new Card("Three", "Purple", "Squiggles", "Solid"));
        fakeDeck.add(new Card("One", "Green", "Squiggles", "Solid"));
        fakeDeck.add(new Card("Two", "Green", "Squiggles", "Solid"));
        fakeDeck.add(new Card("Three", "Green", "Squiggles", "Solid"));
        fakeDeck.add(new Card("One", "Red", "Diamonds", "Solid"));
        fakeDeck.add(new Card("Two", "Red", "Diamonds", "Solid"));
        fakeDeck.add(new Card("Three", "Red", "Diamonds", "Solid"));
        fakeDeck.add(new Card("One", "Purple", "Diamonds", "Solid"));
        fakeDeck.add(new Card("Two", "Purple", "Diamonds", "Solid"));
        fakeDeck.add(new Card("Three", "Purple", "Diamonds", "Solid"));
        fakeDeck.add(new Card("One", "Green", "Diamonds", "Solid"));
        fakeDeck.add(new Card("Two", "Green", "Diamonds", "Solid"));
        fakeDeck.add(new Card("Three", "Green", "Diamonds", "Solid"));
        fakeDeck.add(new Card("One", "Red", "Ovals", "Solid"));
        fakeDeck.add(new Card("Two", "Red", "Ovals", "Solid"));
        fakeDeck.add(new Card("Three", "Red", "Ovals", "Solid"));
        fakeDeck.add(new Card("One", "Purple", "Ovals", "Solid"));
        fakeDeck.add(new Card("Two", "Purple", "Ovals", "Solid"));
        fakeDeck.add(new Card("Three", "Purple", "Ovals", "Solid"));
        fakeDeck.add(new Card("One", "Green", "Ovals", "Solid"));
        fakeDeck.add(new Card("Two", "Green", "Ovals", "Solid"));
        fakeDeck.add(new Card("Three", "Green", "Ovals", "Solid"));
        fakeDeck.add(new Card("One", "Red", "Squiggles", "Striped"));
        fakeDeck.add(new Card("Two", "Red", "Squiggles", "Striped"));
        fakeDeck.add(new Card("Three", "Red", "Squiggles", "Striped"));
        fakeDeck.add(new Card("One", "Purple", "Squiggles", "Striped"));
        fakeDeck.add(new Card("Two", "Purple", "Squiggles", "Striped"));
        fakeDeck.add(new Card("Three", "Purple", "Squiggles", "Striped"));
        fakeDeck.add(new Card("One", "Green", "Squiggles", "Striped"));
        fakeDeck.add(new Card("Two", "Green", "Squiggles", "Striped"));
        fakeDeck.add(new Card("Three", "Green", "Squiggles", "Striped"));
        fakeDeck.add(new Card("One", "Red", "Diamonds", "Striped"));
        fakeDeck.add(new Card("Two", "Red", "Diamonds", "Striped"));
        fakeDeck.add(new Card("Three", "Red", "Diamonds", "Striped"));
        fakeDeck.add(new Card("One", "Purple", "Diamonds", "Striped"));
        fakeDeck.add(new Card("Two", "Purple", "Diamonds", "Striped"));
        fakeDeck.add(new Card("Three", "Purple", "Diamonds", "Striped"));
        fakeDeck.add(new Card("One", "Green", "Diamonds", "Striped"));
        fakeDeck.add(new Card("Two", "Green", "Diamonds", "Striped"));
        fakeDeck.add(new Card("Three", "Green", "Diamonds", "Striped"));
        fakeDeck.add(new Card("One", "Red", "Ovals", "Striped"));
        fakeDeck.add(new Card("Two", "Red", "Ovals", "Striped"));
        fakeDeck.add(new Card("Three", "Red", "Ovals", "Striped"));
        fakeDeck.add(new Card("One", "Purple", "Ovals", "Striped"));
        fakeDeck.add(new Card("Two", "Purple", "Ovals", "Striped"));
        fakeDeck.add(new Card("Three", "Purple", "Ovals", "Striped"));
        fakeDeck.add(new Card("One", "Green", "Ovals", "Striped"));
        fakeDeck.add(new Card("Two", "Green", "Ovals", "Striped"));
        fakeDeck.add(new Card("Three", "Green", "Ovals", "Striped"));
        fakeDeck.add(new Card("One", "Red", "Squiggles", "Empty"));
        fakeDeck.add(new Card("Two", "Red", "Squiggles", "Empty"));
        fakeDeck.add(new Card("Three", "Red", "Squiggles", "Empty"));
        fakeDeck.add(new Card("One", "Purple", "Squiggles", "Empty"));
        fakeDeck.add(new Card("Two", "Purple", "Squiggles", "Empty"));
        fakeDeck.add(new Card("Three", "Purple", "Squiggles", "Empty"));
        fakeDeck.add(new Card("One", "Green", "Squiggles", "Empty"));
        fakeDeck.add(new Card("Two", "Green", "Squiggles", "Empty"));
        fakeDeck.add(new Card("Three", "Green", "Squiggles", "Empty"));
        fakeDeck.add(new Card("One", "Red", "Diamonds", "Empty"));
        fakeDeck.add(new Card("Two", "Red", "Diamonds", "Empty"));
        fakeDeck.add(new Card("Three", "Red", "Diamonds", "Empty"));
        fakeDeck.add(new Card("One", "Purple", "Diamonds", "Empty"));
        fakeDeck.add(new Card("Two", "Purple", "Diamonds", "Empty"));
        fakeDeck.add(new Card("Three", "Purple", "Diamonds", "Empty"));
        fakeDeck.add(new Card("One", "Green", "Diamonds", "Empty"));
        fakeDeck.add(new Card("Two", "Green", "Diamonds", "Empty"));
        fakeDeck.add(new Card("Three", "Green", "Diamonds", "Empty"));
        fakeDeck.add(new Card("One", "Red", "Ovals", "Empty"));
        fakeDeck.add(new Card("Two", "Red", "Ovals", "Empty"));
        fakeDeck.add(new Card("Three", "Red", "Ovals", "Empty"));
        fakeDeck.add(new Card("One", "Purple", "Ovals", "Empty"));
        fakeDeck.add(new Card("Two", "Purple", "Ovals", "Empty"));
        fakeDeck.add(new Card("Three", "Purple", "Ovals", "Empty"));
        fakeDeck.add(new Card("One", "Green", "Ovals", "Empty"));
        fakeDeck.add(new Card("Two", "Green", "Ovals", "Empty"));
        fakeDeck.add(new Card("Three", "Green", "Ovals", "Empty"));

    }
    
    public void addCard(Card newCard)
    {
        System.out.println("Deck.addCard() called");
    }
    
    public Card dealCard()
    {
        System.out.println("Deck.dealCard() called " + fakeDeck.get(0));
        return fakeDeck.remove(0);
    }
    
    public int getRemainingNumber()
    {
        System.out.println("Deck.getRemainingNumber() called");
        return fakeDeck.size();
    }
    
    public void shuffleDeck()
    {
        System.out.println("Deck.shuffleDeck() called");
    }
    
    public void setBeginner()
    {
        System.out.println("Deck.setBeginner() called");
    }
    
    public void debug(CFState.DeckType type)
    {
        System.out.println("Deck.debug() called");
    }
}
