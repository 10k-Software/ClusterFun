package clusterfun.logic;

import junit.framework.TestCase;
import clusterfun.board.Card;
import clusterfun.board.GameBoard;
import clusterfun.state.CFState;
import clusterfun.state.CFState.DeckType;
import clusterfun.message.GameMessage;
import clusterfun.message.GameMessage.MessageDest;
import clusterfun.message.GameMessage.MessageType;
import clusterfun.message.MessageManager;
import java.util.ArrayList;

/**
*
* @author Nick Artman
*/
public class CFLogicTest extends TestCase
{
    CFLogic logic;
    ArrayList<Card> board;

    public CFLogicTest()
    {
        this.board = new ArrayList<Card>();
        //create the testing board
        this.board.add(new Card("Three",   "Green",    "Squiggles",   "Empty"   ));
        this.board.add(new Card("Three",   "Red",      "Squiggles",   "Empty"   ));
        this.board.add(new Card("One",     "Red",      "Ovals",       "Empty"   ));
        this.board.add(new Card("One",     "Red",      "Ovals",       "Solid"   ));
        this.board.add(new Card("Two",     "Red",      "Diamonds",    "Empty"   ));
        this.board.add(new Card("Two",     "Purple",   "Diamonds",    "Solid"   ));
        this.board.add(new Card("Three",   "Red",      "Squiggles",   "Striped" ));
        this.board.add(new Card("One",     "Green",    "Ovals",       "Solid"   ));
        this.board.add(new Card("Two",     "Green",    "Squiggles",   "Striped" ));
        this.board.add(new Card("Three",   "Purple",   "Diamonds",    "Striped" ));
        this.board.add(new Card("One",     "Purple",   "Squiggles",   "Striped" ));
        this.board.add(new Card("One",     "Red",      "Ovals",       "Striped" ));
    }

    public void setUp()
    {
        // Clear all messages from the message manager
        MessageManager.getMessages(MessageDest.CF_UI);
        MessageManager.getMessages(MessageDest.CF_STATE);
        MessageManager.getMessages(MessageDest.CF_LOGIC);
        MessageManager.getMessages(MessageDest.CF_ALL);



        this.logic = new CFLogic();
        this.logic.setGameBoard(new GameBoard(DeckType.NOSHUFFLE));
    }

    public void tearDown()
    {
    }


    public void testProcessSelectCard_validCluster()
    {
        boolean validClusterMessageWasSent = false;

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Three",  "Red",     "Squiggles",  "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("One",    "Red",     "Ovals",      "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Two",    "Red",     "Diamonds",   "Empty")), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }


        ArrayList<GameMessage> messages = (ArrayList<GameMessage>) MessageManager.getMessages(MessageDest.CF_STATE);

        for (GameMessage currentMessage : messages)
        {
            validClusterMessageWasSent |= (GameMessage.MessageType.VALID_CLUSTER == currentMessage.getType());
        }

        assertTrue(validClusterMessageWasSent);
    }


    // Reveals bug for ticket #138
    public void testProcessSelectCard_validClusterAndExtraCard()
    {
        boolean validClusterMessageWasSent = false;

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Three",  "Red",     "Squiggles",  "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("One",    "Red",     "Ovals",      "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Two",    "Red",     "Diamonds",   "Empty")), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Two",    "Green",     "Diamonds",   "Empty")), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }
    }

    public void testProcessSelectCard_invalidCluster()
    {
        boolean validClusterMessageWasSent = false;

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Three",  "Red",     "Squiggles",  "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("One",    "Red",     "Squiggles",      "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Two",    "Red",     "Diamonds",   "Empty")), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }


        ArrayList<GameMessage> messages = MessageManager.getMessages(MessageDest.CF_STATE);

        for (GameMessage currentMessage : messages)
        {
            validClusterMessageWasSent |= (GameMessage.MessageType.VALID_CLUSTER == currentMessage.getType());
        }

        assertTrue(!validClusterMessageWasSent);

    }

    // Reveals bug for ticket #136
    public void testProcessSelectCard_invalidClusterAndExtraCard()
    {
        boolean validClusterMessageWasSent = false;

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Three",  "Red",     "Squiggles",  "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("One",    "Red",     "Squiggles",      "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Two",    "Red",     "Diamonds",   "Empty")), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Two",    "Green",     "Diamonds",   "Empty")), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }
    }

    // Reveals bug from ticket #148
    public void testProcessDeselectCard_valid()
    {
        // Select a card
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Three",  "Red",     "Squiggles",  "Empty")), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.DESELECT_CARD,
            new Card("Three",  "Red",     "Squiggles",  "Empty")), MessageDest.CF_LOGIC);


        // Now deselect the card and make sure no errors were thrown
        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Three",  "Red",     "Squiggles",  "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("One",    "Red",     "Ovals",      "Empty")), MessageDest.CF_LOGIC);
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD,
            new Card("Two",    "Red",     "Diamonds",   "Empty")), MessageDest.CF_LOGIC);

        // Now select 3 cards. If deselection is working, it won't throw an error
        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }
    }

    public void testProcessDeselectCard_invalid_removeWhenNoneAreSelected()
    {
        String exceptionMessage = "";
        
        // Deselect a card when no cards are selected
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.DESELECT_CARD,
            new Card("Three",  "Red",     "Squiggles",  "Empty")), MessageDest.CF_LOGIC);


        // Now deselect the card and make sure no errors were thrown
        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            exceptionMessage = e.getMessage();
        }

        assertEquals("Attempt to deselect card 3RSqEm when none were selected", exceptionMessage);
    }

    public void testProcessRequestDeal()
    {
        boolean decrementScoreMessageWasSent = false;
        boolean dealFailMessageWasSent = false;
        boolean affirmDealMessageWasSent = false;

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.REQUEST_DEAL, null), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }


        ArrayList<GameMessage> messagesForState= MessageManager.getMessages(MessageDest.CF_STATE);

        for (GameMessage currentMessage : messagesForState)
        {
            decrementScoreMessageWasSent |= (GameMessage.MessageType.DECREMENT_SCORE == currentMessage.getType());
            dealFailMessageWasSent |= (GameMessage.MessageType.DEAL_FAIL == currentMessage.getType());
            affirmDealMessageWasSent |= (GameMessage.MessageType.AFFIRM_DEAL == currentMessage.getType());
        }

        ArrayList<GameMessage> messagesForUI = MessageManager.getMessages(MessageDest.CF_UI);

        for (GameMessage currentMessage : messagesForUI)
        {
            decrementScoreMessageWasSent |= (GameMessage.MessageType.DECREMENT_SCORE == currentMessage.getType());
            dealFailMessageWasSent |= (GameMessage.MessageType.DEAL_FAIL == currentMessage.getType());
            affirmDealMessageWasSent |= (GameMessage.MessageType.AFFIRM_DEAL == currentMessage.getType());
        }

        assertFalse(affirmDealMessageWasSent);
        assertTrue(decrementScoreMessageWasSent);
        assertTrue(dealFailMessageWasSent);

    }

    public void testProcessRequestHint()
    {
        boolean giveHintMessageWasSent = false;

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.REQUEST_HINT, null), MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }


        ArrayList<GameMessage> messages = MessageManager.getMessages(MessageDest.CF_UI);

        for (GameMessage currentMessage : messages)
        {
            giveHintMessageWasSent |= (GameMessage.MessageType.GIVE_HINT == currentMessage.getType());
        }

        assertTrue(giveHintMessageWasSent);

    }
    
    
    public void testProcessGiveAICluster()
    {
        int selectCardMessages = 0;
        int givePlayerFocusMessages = 0;
        int selectClusterMessages = 0;

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.GIVE_AI_CLUSTER, null), 
            MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }


        ArrayList<GameMessage> messages = MessageManager.getMessages(MessageDest.CF_STATE);
        ArrayList<GameMessage> messages2 = MessageManager.getMessages(MessageDest.CF_UI);

        for (GameMessage currentMessage : messages)
        {
            if (currentMessage.getType() == GameMessage.MessageType.SELECT_CARD)
            {
                selectCardMessages++;
            }
            else if (currentMessage.getType() == GameMessage.MessageType.GIVE_PLAYER_FOCUS)
            {
                givePlayerFocusMessages++;
            }
        }
        
        for (GameMessage currentMessage : messages2)
        {
            if (currentMessage.getType() == GameMessage.MessageType.SELECT_CLUSTER)
            {
                selectClusterMessages++;
            }
        }

        assertEquals(3, selectCardMessages);
        assertEquals(1, selectClusterMessages);
        assertEquals(1, givePlayerFocusMessages);

    }
    
    public void testProcessCheckGameOver()
    {
        int gameEndMessages = 0;

        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.CHECK_GAME_OVER, null), 
            MessageDest.CF_LOGIC);

        try
        {
            this.logic.update(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            fail();
        }


        ArrayList<GameMessage> messages = MessageManager.getMessages(MessageDest.CF_ALL);

        for (GameMessage currentMessage : messages)
        {
            if (currentMessage.getType() == GameMessage.MessageType.GAME_END)
            {
                gameEndMessages++;
            }
        }

        assertEquals(0, gameEndMessages);

    }

    public void testFindAllClusters()
    {
        ArrayList<ArrayList<Card>> allExpectedClusters = new ArrayList<ArrayList<Card>>();
        ArrayList<ArrayList<Card>> allFoundClusters;

        // fill this array with all the clusters on the testing board
        ArrayList<Card> cluster1 = new ArrayList<Card>();
        cluster1.add(new Card("Three",  "Green",   "Squiggles",  "Empty"));
        cluster1.add(new Card("Two",    "Purple",  "Diamonds",   "Solid"));
        cluster1.add(new Card("One",    "Red",     "Ovals",      "Striped"));

        ArrayList<Card> cluster2 = new ArrayList<Card>();
        cluster2.add(new Card("Three",  "Red",     "Squiggles",  "Empty"));
        cluster2.add(new Card("One",    "Red",     "Ovals",      "Empty"));
        cluster2.add(new Card("Two",    "Red",     "Diamonds",   "Empty"));

        ArrayList<Card> cluster3 = new ArrayList<Card>();
        cluster3.add(new Card("One",    "Red",     "Ovals",      "Empty"));
        cluster3.add(new Card("One",    "Red",     "Ovals",      "Solid"));
        cluster3.add(new Card("One",    "Red",     "Ovals",      "Striped"));

        ArrayList<Card> cluster4 = new ArrayList<Card>();
        cluster4.add(new Card("One",    "Red",     "Ovals",      "Solid"));
        cluster4.add(new Card("Two",    "Red",     "Diamonds",   "Empty"));
        cluster4.add(new Card("Three",  "Red",     "Squiggles",  "Striped"));

        ArrayList<Card> cluster5 = new ArrayList<Card>();
        cluster5.add(new Card("Three",  "Red",     "Squiggles",  "Striped"));
        cluster5.add(new Card("Two",    "Green",   "Squiggles",  "Striped"));
        cluster5.add(new Card("One",    "Purple",  "Squiggles",  "Striped"));

        ArrayList<Card> cluster6 = new ArrayList<Card>();
        cluster6.add(new Card("Two",    "Green",   "Squiggles",  "Striped"));
        cluster6.add(new Card("Three",  "Purple",  "Diamonds",   "Striped"));
        cluster6.add(new Card("One",    "Red",     "Ovals",      "Striped"));

        allExpectedClusters.add(cluster1);
        allExpectedClusters.add(cluster2);
        allExpectedClusters.add(cluster3);
        allExpectedClusters.add(cluster4);
        allExpectedClusters.add(cluster5);
        allExpectedClusters.add(cluster6);

        allFoundClusters = this.logic.findAllClusters(this.board);

        assertTrue(allFoundClusters != null);

        boolean match = true;
        boolean hasSet = false;
        for(ArrayList<Card> expected : allExpectedClusters)
        {
            hasSet = false;
            for(ArrayList<Card> actual : allFoundClusters)
            {
                hasSet |= actual.get(0).equals(expected.get(0))
                       && actual.get(0).equals(expected.get(0))
                       && actual.get(0).equals(expected.get(0));
            }
            match &= hasSet;
        }

        assertTrue(match);
    }


	public void testFindClustersWithCard_onevalidcluster()
    {
        ArrayList<ArrayList<Card>> allFoundClusters;
        ArrayList<Card> foundCluster;
        Card cardInClusters = new Card("Three",  "Purple",  "Diamonds",   "Striped");
        int numberOfMatchingCardsInClusters = 0;

        // fill the expected clusters based on the test board
        ArrayList<Card> expectedCluster = new ArrayList<Card>();
        expectedCluster.add(new Card("Three",  "Purple",  "Diamonds",   "Striped"));
        expectedCluster.add(new Card("Two",    "Green",   "Squiggles",  "Striped"));
        expectedCluster.add(new Card("One",    "Red",     "Ovals",      "Striped"));

        // get the single correct cluster out of the found ones and ensure there was only one found
        allFoundClusters = this.logic.findClustersWithCard(cardInClusters, this.board);
        assertTrue(allFoundClusters != null);
        assertEquals(1, allFoundClusters.size());
        foundCluster = allFoundClusters.get(0);
        assertTrue(foundCluster != null);

        for (int cardNumber = 0; cardNumber < 3; cardNumber++)
        {
            for (int expCardNumber = 0; expCardNumber < 3; expCardNumber++)
            {
                if (expectedCluster.get(cardNumber).equals(foundCluster.get(expCardNumber)))
                {
                    numberOfMatchingCardsInClusters++;
                }
            }
        }

        assertEquals(3, numberOfMatchingCardsInClusters);

    }

    public void testFindClustersWithCard_novalidclusters()
    {
        ArrayList<ArrayList<Card>> allFoundClusters;
        Card cardInClusters = new Card("One",  "Green",  "Ovals",   "Solid");


        allFoundClusters = this.logic.findClustersWithCard(cardInClusters, this.board);
        assertTrue (allFoundClusters != null);
        assertTrue (allFoundClusters.size() == 0);

    }

    public void testvalidateCluster_valid_1diff3same()
    {
        //a valid cluster
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add(new Card("One", "Red", "Ovals", "Empty"));
        cluster.add(new Card("Two", "Red", "Ovals", "Empty"));
        cluster.add(new Card("Three", "Red", "Ovals", "Empty"));

        assertTrue(this.logic.validateCluster(cluster));
    }

    public void testvalidateCluster_valid_4diff()
    {
        //a valid cluster
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add(new Card("One", "Red", "Squiggles", "Solid"));
        cluster.add(new Card("Two", "Purple", "Ovals", "Striped"));
        cluster.add(new Card("Three", "Green", "Diamonds", "Empty"));

        assertTrue(this.logic.validateCluster(cluster));
    }

    public void testvalidateCluster_valid_2diff2same()
    {
        //a valid cluster
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add(new Card("One", "Red", "Ovals", "Striped"));
        cluster.add(new Card("Two", "Green", "Ovals", "Striped"));
        cluster.add(new Card("Three", "Purple", "Ovals", "Striped"));

        assertTrue(this.logic.validateCluster(cluster));
    }

    public void testvalidateCluster_invalid_1issue()
    {
        //an invalid cluster
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add(new Card("One", "Red", "Ovals", "Solid"));
        cluster.add(new Card("One", "Red", "Ovals", "Striped"));
        cluster.add(new Card("Two", "Red", "Ovals", "Empty"));

        assertFalse(this.logic.validateCluster(cluster));
    }

    public void testvalidateCluster_invalid_2issue()
    {
        //an invalid cluster
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add(new Card("One", "Red", "Squiggles", "Empty"));
        cluster.add(new Card("One", "Green", "Squiggles", "Empty"));
        cluster.add(new Card("Two", "Purple", "Diamonds", "Empty"));

        assertFalse(this.logic.validateCluster(cluster));
    }

    public void testvalidateCluster_invalid_identicalcards()
    {
        //an invalid cluster
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add(new Card("One", "Red", "Squiggles", "Empty"));
        cluster.add(new Card("One", "Red", "Squiggles", "Empty"));
        cluster.add(new Card("Three", "Green", "Diamonds", "Empty"));

        assertFalse(this.logic.validateCluster(cluster));
    }

    public void testvalidateCluster_invalid_lessthanthreecards()
    {
        //an invalid cluster
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add(new Card("One", "Green", "Squiggles", "Empty"));
        cluster.add(new Card("Two", "Red", "Squiggles", "Empty"));

        assertFalse(this.logic.validateCluster(cluster));
    }

    public void testvalidateCluster_invalid_morethanthreecards()
    {
        //an invalid cluster
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add(new Card("One", "Red", "Squiggles", "Empty"));
        cluster.add(new Card("Two", "Red", "Squiggles", "Empty"));
        cluster.add(new Card("Three", "Red", "Squiggles", "Empty"));
        cluster.add(new Card("Three", "Green", "Squiggles", "Empty"));


        assertFalse(this.logic.validateCluster(cluster));
    }

    public void testvalidateCluster_invalid_nullobject()
    {
        //an invalid cluster
        ArrayList<Card> cluster = null;

        assertFalse(this.logic.validateCluster(cluster));
    }

}
