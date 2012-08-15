package clusterfun.logic;

import clusterfun.board.Card;
import clusterfun.board.GameBoard;
import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.state.CFState.DifficultyType;
import java.util.ArrayList;


/**
* CFLogic is a logic processing module that executes
* all cluster-related tasks such as giving the AI a cluster to
* select, finding clusters on the board, listing all clusters,
* and verifying clusters. It also updates each frame and processes
* any pending events so that other modules are instructed to act
* as necessary.
*
* Poseidon, look at me!
*
*
* @author Nick Artman
* @version 2.0
* @.date 2009-2-6
*/
//  Version History
//       12/2/08 Nick Artman added algorithm pseudocode
//       1/25/09 Nick Artman added and tested code for findAllClustersWithCard, findAllClusters, ValidateCluster,
//                           checkIfAllAlikeOrAllDifferent.
//       1/28/09 Nick Artman added and tested code for update() and all the process___ methods
//       2/1/09  Nick Artman added and tested code for processGiveAICluster method
//       2/4/09  Nick Artman added and tested code for processCheckGameOver method

public class CFLogic
{

    /**
    * A list of the cards that the user currently has selected on the board
    */
    private ArrayList<Card> currentlySelectedCards;

    /**
    * The gameboard that this logic module references
    */
    private GameBoard gameBoard;

    /**
    * The number of cards that constitutes a cluster
    */
    private static final int kNumberOfCardsInACluster = 3;

    /**
    * The most recent property that didn't properly match and thus rendered a cluster invalid.
    */
    private String propertyThatRenderedClusterInvalid;


    /**
    * Constructs a new CFLogic
    */
    public CFLogic()
    {
        this.currentlySelectedCards = new ArrayList<Card>();
        propertyThatRenderedClusterInvalid = new String();
    }

    /**
    * Sets the game board that this logic module will use while finding clusters on the board
    * @param board The gameboard for which to get cards from for finding clusters
    */
    public void setGameBoard(GameBoard board)
    {
        this.gameBoard = board;
    }

    /**
    * Checks to see if the player has selected a cluster or
    * if the AI should be given a cluster. Then, if there is a timer that
    * is counting down, this checks to see if it has expired; if so, it
    * takes appropriate action - either by taking focus off a player if they
    * were supposed to make a match, or by removing a cluster if the
    * timer expired in solitaire mode
    *
    *
    * @param tpf the number of milliseconds per frame
    * @throws Exception Exceptions thrown while processing when a message is sent that shouldn't have been sent.
    */
    public void update(long tpf) throws Exception
    {
        // All the received game messages for this frame
        ArrayList<GameMessage> receivedMessages  = new ArrayList<GameMessage>();

        // All the cards on the game board
        ArrayList<Card> cardsOnBoard;

        // GET the current cards on the game board
        cardsOnBoard = this.gameBoard.getCurrentCards();

        // RECEIVE all messages from the MessageManager
        receivedMessages = MessageManager.getMessages(GameMessage.MessageDest.CF_LOGIC);

        // FOR each message
        for (GameMessage currentMessage : receivedMessages)
        {
            // Perform the necessary actions depending on which message was received
            switch (currentMessage.getType())
            {
                case SELECT_CARD: processSelectCard((Card)currentMessage.getValue(), cardsOnBoard); break;
                case DESELECT_CARD: processDeselectCard((Card)currentMessage.getValue()); break;
                case GIVE_AI_CLUSTER: processGiveAICluster(cardsOnBoard); break;
                case REQUEST_DEAL: processRequestDeal(cardsOnBoard); break;
                case REQUEST_HINT: processRequestHint(cardsOnBoard, (DifficultyType)currentMessage.getValue()); break;
                case CHECK_GAME_OVER: processCheckGameOver(cardsOnBoard); break;
                case GAME_START: this.currentlySelectedCards.clear(); break;
                default: break;
            }
        }
    }



    private void processSelectCard(Card selectedCard, ArrayList<Card> cardsOnBoard) throws Exception
    {
        // IF there is less than a full cluster cards currently selected
        if (this.currentlySelectedCards.size() < kNumberOfCardsInACluster)
        {
            // ADD the card to the list of currently selected cards
            this.currentlySelectedCards.add(selectedCard);
        }
        else
        {
            throw new Exception("Attempt to select more than 3 cards at once");
        }


        // IF there are 3 cards currently selected
        if (currentlySelectedCards.size() == kNumberOfCardsInACluster)
        {
            // IF the cluster is valid
            if (validateCluster(this.currentlySelectedCards))
            {
                // SEND an increment score message
                MessageManager.sendMessage(
                    new GameMessage(GameMessage.MessageType.INCREMENT_SCORE, null),
                        GameMessage.MessageDest.CF_STATE, GameMessage.MessageDest.CF_SOUND);

                // SEND a valid cluster message
                MessageManager.sendMessage(
                    new GameMessage(GameMessage.MessageType.VALID_CLUSTER, null),
                        GameMessage.MessageDest.CF_STATE, GameMessage.MessageDest.CF_UI,
                        GameMessage.MessageDest.CF_SOUND);

                // CLEAR the currently selected card
                this.currentlySelectedCards.clear();
            }
            // ELSE the cluster is not valid
            else
            {
                // SEND a decrement score message
                // MessageManager.sendMessage(
                // new GameMessage(GameMessage.MessageType.DECREMENT_SCORE, null), GameMessage.MessageDest.CF_STATE);

                // SEND an invalid cluster message
                MessageManager.sendMessage(
                    new GameMessage(GameMessage.MessageType.INVALID_CLUSTER, propertyThatRenderedClusterInvalid),
                    GameMessage.MessageDest.CF_STATE, GameMessage.MessageDest.CF_UI,
                    GameMessage.MessageDest.CF_SOUND);

                // CLEAR the currently selected card
                this.currentlySelectedCards.clear();
            }
        }
    }


    private void processDeselectCard(Card deselectedCard) throws Exception
    {
        // IF there are no cards currently selected
        if (this.currentlySelectedCards.size() == 0)
        {
            // THROW an exception indicating invalid card deselection
            throw new Exception("Attempt to deselect card " + deselectedCard.toString() + " when none were selected");
        }
        // ELSE deselect the card
        else
        {
            // IF there was an attempt to deselect a card that is not selected
            if (!this.currentlySelectedCards.remove(deselectedCard))
            {
                throw new Exception("Attempt to deselect card that is not selected");
            }
        }
    }


    private void processGiveAICluster(ArrayList<Card> cardsOnBoard) throws Exception
    {
        // All the possible clusters on the board
        ArrayList<ArrayList<Card>> allClustersOnBoard;

        // The cluster to use
        ArrayList<Card> cluster;

        // FIND all clusters on the board
        allClustersOnBoard = findAllClusters(cardsOnBoard);

        // IF there are no valid clusters
        if (allClustersOnBoard.size() == 0)
        {
            // SEND an affirm deal message
            MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.AFFIRM_DEAL, null), GameMessage.MessageDest.CF_STATE,
                GameMessage.MessageDest.CF_SOUND);
        }
        // ELSE choose a cluster and send it out
        else
        {
            // CHOOSE a cluster
            cluster = allClustersOnBoard.get(0);

            // DESELECT all cards that are currently selected
            for (Card selectedCard : this.currentlySelectedCards)
            {
                MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.DESELECT_CARD, selectedCard),
                    GameMessage.MessageDest.CF_LOGIC, GameMessage.MessageDest.CF_STATE,
                    GameMessage.MessageDest.CF_SOUND);
            }

            // SEND the ui a select cluster message with the cluster to be cleared
            MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.GIVE_PLAYER_FOCUS, null),
                GameMessage.MessageDest.CF_STATE,
                GameMessage.MessageDest.CF_SOUND);

            // SEND three select card messages, one for each card in the cluster
            for (Card cardToSelect : cluster)
            {
                MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CARD, cardToSelect),
                    GameMessage.MessageDest.CF_LOGIC, GameMessage.MessageDest.CF_STATE,
                    GameMessage.MessageDest.CF_SOUND);
            }

            // IF the cluster found is null or improperly sized
            if (cluster == null || cluster.size() != kNumberOfCardsInACluster)
            {
                throw new Exception("Attempted to have the AI send an empty or improperly sized cluster");
            }
            // SEND the ui a select cluster message with the cluster to be cleared
            MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.SELECT_CLUSTER, cluster),
                GameMessage.MessageDest.CF_UI, GameMessage.MessageDest.CF_SOUND);

        }
    }


    private void processRequestDeal(ArrayList<Card> cardsOnBoard)
    {
        // FIND all clusters on the board
        ArrayList<ArrayList<Card>> allClustersOnBoard = findAllClusters(cardsOnBoard);

        // IF there are no clusters on the board
        if (allClustersOnBoard.size() == 0)
        {
            // SEND an affirm deal message
            MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.AFFIRM_DEAL, null), GameMessage.MessageDest.CF_STATE,
                GameMessage.MessageDest.CF_SOUND);
        }
        // ELSE
        else
        {
            // SEND a decrement score message
            MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.DECREMENT_SCORE, null), GameMessage.MessageDest.CF_STATE,
                GameMessage.MessageDest.CF_SOUND);
            // SEND a deal fail message
            MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.DEAL_FAIL, null), GameMessage.MessageDest.CF_UI,
                GameMessage.MessageDest.CF_SOUND);

        }
    }

    private void processRequestHint(ArrayList<Card> cardsOnBoard, DifficultyType difficulty) throws Exception
    {
        // ENSURE a difficulty was specified
        if (difficulty == null)
        {
            throw new Exception("No difficulty specified in hint request message.");
        }

        // FIND all clusters on the board
        ArrayList<ArrayList<Card>> allClustersOnBoard = findAllClusters(cardsOnBoard);

        // IF there is at least one cluster on the board
        if (allClustersOnBoard.size() > 0)
        {
            // CHOOSE a cluster
            ArrayList<Card> cluster = allClustersOnBoard.get(0);

            // CREATE a list to hold the hint cards
            ArrayList<Card> hintCards = new ArrayList<Card>();

            // IF the difficulty is medium or easy
            if (difficulty == DifficultyType.MEDIUM || difficulty == DifficultyType.EASY)
            {
                // ADD one card to the hint card list
                hintCards.add(cluster.get(0));
            }

            // IF the difficulty is easy
            if (difficulty == DifficultyType.EASY)
            {
                // ADD a second card to the hint card list
                hintCards.add(cluster.get(1));
            }

            // SEND a GIVE_HINT message containing the hint cards
            MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.GIVE_HINT, hintCards), GameMessage.MessageDest.CF_UI,
                GameMessage.MessageDest.CF_SOUND);
        }
        else
        {
            // SEND an affirm deal message
            MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.AFFIRM_DEAL, null), GameMessage.MessageDest.CF_STATE,
                GameMessage.MessageDest.CF_SOUND);
        }
    }

    private void processCheckGameOver(ArrayList<Card> cardsOnBoard)
    {
        // All the possible clusters on the board
        ArrayList<ArrayList<Card>> allClustersOnBoard;

        // FIND all clusters on the board
        allClustersOnBoard = findAllClusters(cardsOnBoard);

        // IF there are no valid clusters, the player's game is over
        if (allClustersOnBoard.size() == 0)
        {
            // SEND a game end message
            MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.GAME_END, false), GameMessage.MessageDest.CF_ALL);
        }
        // OTHERWISE do nothing so the game continues as normal
    }

    /**
    * Finds all possible clusters on the game board
    *
    * @param cardsOnBoard the cards currently on the game board
    *
    * @return a list of all possible clusters on the board
    */
    public ArrayList<ArrayList<Card>> findAllClusters(ArrayList<Card> cardsOnBoard)
    {
        // The list used to hold all the possible clusters
        ArrayList<ArrayList<Card>> allClusters = new ArrayList<ArrayList<Card>>();

        ArrayList<Card> possibleCards = (ArrayList<Card>)cardsOnBoard.clone();

        // The current card being examined
        Card currentCard;

        // WHILE there are cards on the board that could be in a cluster
        while (possibleCards.size() > 0)
        {

            // GET the head of the list, it will become the 2nd card in the cluster
            currentCard = possibleCards.get(0);
            // REMOVE the head of the list, since any clusters it can be in will be found now
            possibleCards.remove(0);

            // ADD all clusters with that card in them to the list
            allClusters.addAll(findClustersWithCard(currentCard, possibleCards));

            // DEBUG: ensure the card was removed properly
            assert(!possibleCards.contains(currentCard));
        }

        // RETURN the list of all possible clusters on the board
        return allClusters;
    }

    /**
    * Finds all possible clusters on the board that can be made
    * with the given card included
    *
    *
    * @param baseCard the card that the cluster must contain
    *
    * @param cardsOnBoard the cards currently on the game board
    *
    * @return all of the clusters possible that contain the given card
    */
    public ArrayList<ArrayList<Card>> findClustersWithCard(Card baseCard,
                                                  ArrayList<Card> cardsOnBoard)
    {
        // The list of all possible clusters containing the given card
        ArrayList<ArrayList<Card>> clustersWithCard = new ArrayList<ArrayList<Card>>();

        // The current cluster being examined
        ArrayList<Card> currentCluster = new ArrayList<Card>();

        // A shallow copy of the cards on the board, for use in preventing redundant sets
        ArrayList<Card> possibleCards = (ArrayList<Card>)cardsOnBoard.clone();

        // The second card in the potentially valid cluster
        Card secondCardInCluster;

        // ADD the provided card to the current cluster being examined
        currentCluster.add(baseCard);

        // WHILE there are potential second cards for the cluster
        while (possibleCards.size() > 0)
        {
            // GET the head of the list, it will become the 2nd card in the cluster
            secondCardInCluster = possibleCards.get(0);
            // REMOVE the head of the list, since any clusters it can be in will be found now
            possibleCards.remove(0);

            // ADD the second card to the current cluster being examined
            currentCluster.add(secondCardInCluster);

            // FOR each card [as the third card in the cluster]
            for (Card thirdCardInCluster : possibleCards)
            {

                // ADD the third card to the current cluster being examined
                currentCluster.add(thirdCardInCluster);

                // IF the three cards make a valid cluster
                if (validateCluster(currentCluster))
                {

                    // ADD the cluster to the list
                    clustersWithCard.add((ArrayList<Card>)currentCluster.clone());
                }
                // REMOVE the third card in the cluster so a new third card can take its place
                currentCluster.remove(thirdCardInCluster);
            }
            // REMOVE the second card in the cluster so a new third card can take its place
            currentCluster.remove(secondCardInCluster);
        }


        // RETURN the list of all valid clusters containing the given card
        return clustersWithCard;
    }


    /**
    * Determines if the given cluster is valid (for each property it
    * either matches or is different for each card)
    *
    * @param cluster the cluster to be validated
    *
    * @return true if the cluster is valid (for each property it either matches
    * or is different for each card), or false if it is invalid
    */
    public boolean validateCluster(ArrayList<Card> cluster)
    {
        // methods needed: card.getNumber/getFill/getShape/getColor

        // Determined via attempted contradiction - the cluster is
        // assumed valid until it is ascertained that it is not


        // Whether or not the cluster is valid
        boolean isValid = true;

        // A list of all the property names
        ArrayList<String> propertyNames = new ArrayList<String>();
        propertyNames.add("Number");
        propertyNames.add("Color");
        propertyNames.add("Symbol");
        propertyNames.add("Fill");

        // IF the cluster is null or has more/less than three cards
        if (cluster == null || cluster.size() != kNumberOfCardsInACluster)
        {
            // SET the validity of the cluster to false
            isValid = false;
        }
        // ELSE validate the cluster
        else
        {
            // FOR each property
            for (String currentProperty : propertyNames)
            {
                // IF that property is not all alike or all different for each card
                if(!checkIfAllAlikeOrAllDifferent(cluster.get(0).getProperty(Card.Properties.valueOf(currentProperty)),
                                                 cluster.get(1).getProperty(Card.Properties.valueOf(currentProperty)),
                                                 cluster.get(2).getProperty(Card.Properties.valueOf(currentProperty)))
                                                 && isValid)
                {
                    // SET the validity of the cluster to false;
                    isValid = false;
                    // SET which property last invalidated the cluster in case they click explain why
                    propertyThatRenderedClusterInvalid = currentProperty;
                }
            }
        }


        //RETURN whether the cluster is valid or not
        return isValid;
    }



    /**
    * Determines if a certain property shared among 3 cards is either different
    * for each card or the same for every card (note the specific language)
    *
    * @param card1Property The property of the first card in the cluster to
    * compare
    *
    * @param card2Property The property of the second card in the cluster to
    * compare
    *
    * @param card3Property The property of the third card in the cluster to
    * compare
    */
    private boolean checkIfAllAlikeOrAllDifferent(Enum card1Property,
                                                  Enum card2Property,
                                                  Enum card3Property)
    {
        // The flag showing whether the properties are all alike or all different
        boolean areAllAlikeOrAllDifferent = false;

        // IF each property is different or all are alike
        if (card1Property == card2Property && card2Property == card3Property
            || card1Property != card2Property && card2Property != card3Property
            && card1Property != card3Property)
        {
            // SET the all alike or all different flag to true
            areAllAlikeOrAllDifferent = true;
        }

        // RETURN whether the properties are all alike or all different
        return areAllAlikeOrAllDifferent;
    }

}
