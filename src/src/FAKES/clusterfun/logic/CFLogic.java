package clusterfun.logic;

import junit.framework.TestCase;
import clusterfun.board.Card;
import clusterfun.board.GameBoard;
import java.util.ArrayList;

/**
 * CFLogic Fake Class
 *
 * @author nartman
 */
public class CFLogic 
{

    public CFLogic()
    {
        System.out.println("CFLogic constructor called");        
    }

    public void update(long tpf)
    {
        System.out.println("CFLogic update method called");
    }

    public void setGameBoard(GameBoard board)
    {
        System.out.println("CFLogic setGameBoard method called");
    }

    public ArrayList<ArrayList<Card>> findAllClusters(ArrayList<Card> cardsOnBoard)
    {
        return null;
    }

    public ArrayList<ArrayList<Card>> findClustersWithCard(Card card, 
                                                  ArrayList<Card> cardsOnBoard)
    {
        return null;
    }


    public boolean validateCluster(ArrayList<Card> cluster)
    {
        return false;
    }
    
    

    private boolean checkIfAllAlikeOrAllDifferent(int card1Property, 
                                                  int card2Property, 
                                                  int card3Property)
    {
        return false;
    }
}