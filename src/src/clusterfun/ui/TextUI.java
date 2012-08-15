package clusterfun.ui;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.board.Card;
import clusterfun.message.GameMessage.MessageDest;
import clusterfun.message.GameMessage.MessageType;
import clusterfun.state.CFState;
import clusterfun.state.CFState.DifficultyType;
import clusterfun.state.CFState.ModeType;
import clusterfun.state.CFState.StatusType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The TextUI class is an implementation of BasicUI used only for testing
 * purposes.  Prints cards on the game board to the console and receives text
 * input
 *
 * @see BasicUI
 *
 * @author Jason Swalwell
 * @version 0.1
 * @.date 2009-1-9
 */

public class TextUI implements BasicUI
{
    private ArrayList<Card> selectedCards;
    private ArrayList<Card> gameBoardCards;
    private Scanner sc;
    private CFState state;
    private boolean skipInput;
    private DifficultyType diff;
    private static final int kNumRows = 3;
    private static final long kMiliToMins = 60000;
    private static final long kMiliToSecs = 1000;

    /**
     * Creates an instance of TextUI
     *
     * @param state game state to reference to print to screen
     */
    public TextUI(CFState state)
    {
        selectedCards = new ArrayList<Card>();
        gameBoardCards = new ArrayList<Card>();
        sc = new Scanner(System.in);
        HashMap settings = new HashMap();
        settings.put("mode", ModeType.TIMED);
        diff = DifficultyType.MEDIUM;
        settings.put("difficulty", diff);

        MessageManager.sendMessage(new GameMessage(MessageType.GAME_START, settings),
                                    MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.SET_GAMESTATUS, StatusType.RUNNING),
                                    MessageDest.CF_STATE);
        this.state = state;
        skipInput = true;
    }

    /**
     * @param timeElapsed time elapsed since last update
     * @see BasicUI#update
     */
    public void update(long timeElapsed)
    {
        String input = new String();
        ArrayList<Card> hints = new ArrayList<Card>();
        // CALL getMessages from messageManager
        ArrayList<GameMessage> messages = MessageManager.getMessages(MessageDest.CF_UI);
        hints = processMessages(messages, hints);
        // IF game has actually started
        if (state.getGameStatus() != StatusType.NOTINGAME)
        {
            System.out.println("Time Remaining - " + state.getTimeRemaining()/kMiliToMins + ":"
                                + state.getTimeRemaining()%kMiliToMins/kMiliToSecs);
            if (state.getActivePlayer() != null)
            {
                System.out.println("Score: " + state.getActivePlayer().getScore());
            }
        }
        // IF skip prompted the user for input
        if (!skipInput)
        {
            // PROMPT the user for imput
            System.out.print("\nCF > ");
            handleInput(hints);
        }
        // IF the input was skipped
        if (skipInput)
        {
            // SkipInput is set to false
            skipInput = false;
        }
    }

    private void printCards(ArrayList<Card> hintedCards)
    {
        int count = 0;
        // PRINT "selected cards:"
        System.out.println("\nSelected Cards");
        // PRINT any cards that are selected
        for (Card card : selectedCards)
        {
            // IF the card is to be displayed as a hint
            if (hintedCards.contains(card))
            {
                System.out.println(count + " *" + card.toString() + "*");
            }
            else
            {
                System.out.println(count + " " + card.toString());
            }
            count++;
        }
        // PRINT "cards on game board:"
        System.out.println("\nGameboard Cards");
        count = 0;
        // PRINT the cards on the game board
        for (Card card : gameBoardCards)
        {
            // IF the card is to be displayed as a hint
            if (hintedCards.contains(card))
            {
                System.out.print(count + " *" + card.toString() + "*   ");
            }
            else
            {
                System.out.print(count + " " + card.toString() + "   ");
            }
            // IF a newline needs to be printed
            if ((count % kNumRows == (kNumRows - 1)) || count == gameBoardCards.size())
            {
                System.out.println("");
            }
            count++;
        }
        System.out.println("");
    }

    private void handleInput(ArrayList<Card> hints)
    {
        // RECIEVE the input from the user
        String input = sc.nextLine();
        Scanner strScan = new Scanner(input);
        // IF input is selectCard
        if (input.startsWith("s "))
        {
            strScan.next();
            Card selCard = gameBoardCards.remove(strScan.nextInt());
            // SEND a SELECT_CARD message
            MessageManager.sendMessage(new GameMessage(MessageType.SELECT_CARD, selCard),
                                         MessageDest.CF_LOGIC, MessageDest.CF_STATE);
            // MOVE card from game board to selected
            selectedCards.add(selCard);
            // CALL printCards
            printCards(hints);
        }
        // ELSE IF input is deselectCard
        else if (input.startsWith("d "))
        {
            strScan.next();
            Card deselCard = selectedCards.remove(strScan.nextInt());
            // SEND a DESELECT_CARD message
            MessageManager.sendMessage(new GameMessage(MessageType.DESELECT_CARD, deselCard),
                                         MessageDest.CF_LOGIC, MessageDest.CF_STATE);
            // MOVE card from selected to game board
            gameBoardCards.add(deselCard);
            // CALL printCards
            printCards(hints);
        }
        // ELSE IF input is quit
        else if (input.equals("quit"))
        {
            // SEND a QUIT message
            MessageManager.sendMessage(new GameMessage(MessageType.PROGRAM_END, null),
                                          MessageDest.CF_ALL);
        }
        // ELSE IF input is hint
        else if (input.equals("hint"))
        {
            MessageManager.sendMessage(new GameMessage(MessageType.REQUEST_HINT, diff),
                                           MessageDest.CF_LOGIC);
        }
        // ELSE IF input is deal 3 more
        else if (input.equals("3 more"))
        {
            MessageManager.sendMessage(new GameMessage(MessageType.REQUEST_DEAL, null),
                                           MessageDest.CF_LOGIC);
        }
        // ELSE IF input is help
        else if (input.equals("help"))
        {
            System.out.println("\nThe following commands are valid\n"
                              + "s (card number) : selects the card with givin number\n"
                              + "d (card number) : deselects the card with givin number\n"
                              + "hint : highlights one card that forms a set\n"
                              + "3 more : adds three more cards to the board only if there is no set\n"
                              + "quit : exits the game\n");
        }
        else
        {
            System.out.println("Not a valid input");
        }
    // ENDIF
    }

    /**
     * Takes in a list of messages and takes the appropiate action for each
     *
     * @param messages the messages to process
     * @param hints the list of cards to be displayed as hints
     * @return the list of cards to be displayed as hints
     */
    private ArrayList<Card> processMessages(ArrayList<GameMessage> messages, ArrayList<Card> hints)
    {
        // FOR EACH message recieved
        for (GameMessage message : messages)
        {
            // IF message is DEAL_CARDS
            if (message.getType() == MessageType.DEAL_CARDS)
            {
                System.out.println("Deal Cards");
                ArrayList<Card> cards = (ArrayList<Card>) message.getValue();
                // ADD the cards to the gameBoardCards list
                for (Card card : cards)
                {
                    gameBoardCards.add(card);
                }
                printCards(hints);
            }
            // ELSE IF the message is VALID_CLUSTER
            if(message.getType() == MessageType.VALID_CLUSTER)
            {
                // PRINT "valid cluster"
                System.out.println("Valid Cluster");
                // REMOVE the cards from selectedCards
                selectedCards = new ArrayList<Card>();
                printCards(hints);
            }
            // ELSE IF the message is INVALID_CLUSTER
            else if(message.getType() == MessageType.INVALID_CLUSTER)
            {
                // PRINT "Invalid cluster"
                System.out.println("Invalid Cluster");
                // MOVE the cards from selected to gameBoard
                gameBoardCards.addAll(selectedCards);
                selectedCards = new ArrayList<Card>();
                printCards(hints);
            }
            // ELSE IF the message is DEAL_FAIL
            else if (message.getType() == MessageType.DEAL_FAIL)
            {
                System.out.println("Deal failed, there is at least one valid cluster");
                printCards(hints);
            }
            // ELSE IF the message is GIVE_HINT
            else if (message.getType() == MessageType.GIVE_HINT)
            {
                hints.addAll((ArrayList<Card>) message.getValue());
                printCards(hints);
            }
            // ELSE IF the message is SELECT_CLUSTER
            else if (message.getType() == MessageType.SELECT_CLUSTER)
            {
                System.out.println("Time has run out");
                System.out.println("A cluster will be chosen for you");
                gameBoardCards.addAll(selectedCards);
                selectedCards = (ArrayList<Card>) message.getValue();
                // FOR each card selected
                for (Card card : selectedCards)
                {
                    gameBoardCards.remove(card);
                }
                skipInput = true;
                printCards(hints);

                MessageManager.sendMessage(new GameMessage(MessageType.SET_GAMESTATUS, StatusType.RUNNING),
                                            MessageDest.CF_STATE);
            }
            // CALL printCards
        }
        return hints;
        //ENDFOR
    }
}
