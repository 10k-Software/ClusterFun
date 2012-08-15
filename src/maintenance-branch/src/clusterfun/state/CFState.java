package clusterfun.state;

import clusterfun.board.Card;
import clusterfun.board.GameBoard;
import clusterfun.logic.CFLogic;
import clusterfun.message.MessageManager;
import clusterfun.message.GameMessage;
import clusterfun.message.GameMessage.MessageDest;
import clusterfun.message.GameMessage.MessageType;
import clusterfun.player.AbstractPlayer;
import clusterfun.player.HumanPlayer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The CFState class represents the current state of a CF game.  This includes
 * the players, the game board, the current mode, the difficulty level, and
 * the time remaining for a player to find a set (if needed).  The state is
 * updated after each frame where it makes the necessary changes to the current
 * state based upon the messages it receives.  This includes updating the
 * AIPlayers and calling the CFLogic to update
 *
 * @author Jason Swalwell
 * @version 0.1
 * @.date 2008-11-23
 */
//  Version History
//      12/4/08 Jason Swalwell added algorithm pseudocode
public class CFState
{
    /**
     * An enumeration for the different difficulty settings
     */
    public enum DifficultyType
    {
        /** The easy difficulty setting */
        EASY,
        /** The medium difficulty setting */
        MEDIUM,
        /** The hard difficulty setting */
        HARD
    }

    /**
     * An enumeration for the different game modes
     */
    public enum ModeType
    {
        /** The beginner CF game mode */
        BEGINNER,
        /** The solitare CF game mode */
        SOLITARE,
        /** The timed CF game mode */
        TIMED,
        /** The multiplayer CF game mode */
        MULTIPLAYER
    }

    /**
     * An enumeration for the different game states
     */
    public enum StatusType
    {
        /** Indicates the CF game is running */
        RUNNING,
        /** Indicates the CF game ended in a win */
        WIN,
        /** Indicates the CF game ended in a loss */
        LOSE,
        /** Indicates the CF game is just beginning */
        BEGINNING,
        /** Indicates the CF game is paused */
        PAUSED,
        /** Indicates the CF game is not currently being played */
        NOTINGAME
    }

    /**
     * An enumeration for the card shuffle type
     */
    public enum DeckType
    {
        /** Indicates a regular shuffle */
        SHUFFLE,
        /** Indicates no shuffle */
        NOSHUFFLE,
        /** Indicates a no valid set at start */
        NOSET,
        /** Indicates a beginner deck */
        BEGINNER
    }

    private static final long kDefaultTime = 60000L;

    private static final int kMinBoardSize = 12;

    /**
     * Active player attempting to choose a cluster
     */
    private AbstractPlayer activePlayer;

    /**
     * List of the computer players playing the game
     */

    /**
     * Current status of the CF game
     */
    private StatusType currentStatus;

    /**
     * Current difficulty of game being played
     */
    private DifficultyType difficulty;

    /**
     * Current game board for the players to interact with
     */
    private GameBoard gameBoard;

    /**
     * The logic system for the CF game
     */
    private CFLogic gameLogic;

    /**
     * Current game mode being played
     */
    private ModeType gameMode;

    /**
     * List of the human players playing the game
     */
    private ArrayList<HumanPlayer> humanPlayers;

    /**
     * Time, in milliseconds, remaining for the active player to choose a
     * cluster
     */
    private long timeRemaining;

    /**
     * The desired shuffle style
     */
    private DeckType deckStyle;
    
    /**
     * The given deck style from CFApp
     */
    private DeckType given;

    /**
     * Constructs a new CF game state
     *
     * @param gameLogic the game logic to be used
     * @param deckType the desired deck to play the game with
     */
    public CFState(CFLogic gameLogic, DeckType deckType)
    {
        // SET the gameLogic to the specified gameLogic
        this.gameLogic = gameLogic;
        // CREATE an empty list for human players
        humanPlayers = new ArrayList<HumanPlayer>();
        // CREATE an empty list for computer players
        // SET the game status to NOTINGAME
        currentStatus = StatusType.NOTINGAME;
        this.deckStyle = deckType;
        given = deckType;
    }

    /**
     * Adds a new player to the game
     *
     * @param player the player to be added
     */
    private void addPlayer(AbstractPlayer player)
    {
        // IF player is a human player THEN
        if (player instanceof HumanPlayer)
        {
            // ADD player to the list of human players
            humanPlayers.add((HumanPlayer) player);
        }
        // ELSE player is a computer player
            // ADD player to the list of computer players
        // ENDIF
    }

    /**
     * Returns the player who is attempted to find a cluster
     *
     * @return the current player choosing a cluster
     */
    public AbstractPlayer getActivePlayer()
    {
        // RETURN the current active player
        return activePlayer;
    }

    /**
     * Returns the deck style being used
     *
     * @return deckStyle The deck type being used
     */
    public DeckType getDeckStyle()
    {
        return deckStyle;
    }

    /**
     * Returns the current difficulty of the game
     *
     * @return the current difficulty of the game
     */
    public DifficultyType getDifficulty()
    {
        // RETURN the current difficulty
        return difficulty;
    }

    /**
     * Returns the current game board
     *
     * @return the current game board
     */
    public GameBoard getGameBoard()
    {
        // RETURN the current game board
        return gameBoard;
    }

    /**
     * Returns the current game mode
     *
     * @return the current game mode
     */
    public ModeType getGameMode()
    {
        // RETURN the current game mode
        return gameMode;
    }

    /**
     * Returns the current status of the game
     *
     * @return the current game status
     */
    public StatusType getGameStatus()
    {
        // RETURN the current status of the game
        return currentStatus;
    }

    /**
     * Returns a list of players playing the game
     *
     * @return the list of players currently playing the game
     */
    public ArrayList<AbstractPlayer> getPlayers()
    {
        // CREATE an empty list for all players
        ArrayList<AbstractPlayer> allPlayers = new ArrayList<AbstractPlayer>();
        // FOR EACH player in human players
        for (HumanPlayer player : humanPlayers)
        {
            // ADD player to list of all players
            allPlayers.add(player);
        }
        // ENDFOR
        // FOR EACH player in computer players
            // ADD player to list of all players
        // ENDFOR
        // RETURN list of all players
        return allPlayers;
    }

    /**
     * Returns the time, in milliseconds, remaining for a player to pick a
     * cluster
     *
     * @return the time, in milliseconds, remaining to pick a cluster
     */
    public long getTimeRemaining()
    {
        // RETURN the time remaining
        return timeRemaining;
    }

    /**
     * Initializes the game board and state to a default state
     * @param settings any changes to the default settings
     */
    public void init(HashMap settings)
    {
        // IF there are any changes to the default settings
        if (settings != null)
        {
            ModeType mode = (ModeType) settings.get("mode");
            // IF the mode has been changed
            if (mode != null)
            {
                gameMode = mode;
                // IF the mode is beginner
                if (mode == ModeType.BEGINNER)
                {
                    deckStyle = DeckType.BEGINNER;
                }
                else
                {
                    deckStyle = given;
                }
            }
        }
        else
        {
            gameMode = ModeType.SOLITARE;
        }
        // CREATE an instance of the deck
        // CREATE an instance of the gameBoard with deck
        gameBoard = new GameBoard(deckStyle);
        ArrayList<Card> cards = gameBoard.getCurrentCards();
        MessageManager.sendMessage(new GameMessage(MessageType.DEAL_CARDS, cards),
                                     MessageDest.CF_UI);
        // CREATE a human player
        HumanPlayer defaultPlayer = new HumanPlayer("default");
        // SET the active player to the newly created human player
        addPlayer(defaultPlayer);
        activePlayer = defaultPlayer;
        // SET the current status to BEGINNING
        currentStatus = StatusType.BEGINNING;
        // SET the difficulty to EASY
        difficulty = DifficultyType.EASY;
        timeRemaining = kDefaultTime;
        gameLogic.setGameBoard(gameBoard);
    }

    /**
     * Takes in a list of messages and makes the appropriate changes to the
     * state
     *
     * @param messages the messages to process and update the state
     */
    private void processMessages(ArrayList<GameMessage> messages) throws Exception
    {
        // FOR EACH message in the provided list
        for (GameMessage message : messages)
        {
            // IF the message is ADD_PLAYER
            if (message.getType() == MessageType.ADD_PLAYER)
            {
                addPlayer((AbstractPlayer) message.getValue());
            }
            // ELSE IF the message is AFFIRM_DEAL
            else if (message.getType() == MessageType.AFFIRM_DEAL)
            {
                ArrayList<Card> addedCards = gameBoard.addThreeCards();
                MessageManager.sendMessage(new GameMessage(MessageType.DEAL_CARDS, addedCards),
                                            MessageDest.CF_UI);
            }
            // ELSE IF the message is DECREMENT_SCORE
            else if (message.getType() == MessageType.DECREMENT_SCORE)
            {
                activePlayer.decScore();
            }
            // ELSE IF the message is DESELECT_CARD
            else if (message.getType() == MessageType.DESELECT_CARD)
            {
                Card card = (Card) message.getValue();
                // IF the card was not in the selected area
                if (!gameBoard.deselectCard(card))
                {
                    throw new Exception("Deselect card: " + card.toString() + " failed");
                }
            }
            // ELSE IF the message is GAME_END
            else if (message.getType() == MessageType.GAME_END)
            {
                currentStatus = StatusType.NOTINGAME;
            }
            // ELSE IF the message is GAME_START
            else if (message.getType() == MessageType.GAME_START)
            {
                humanPlayers = new ArrayList<HumanPlayer>();
                HashMap settings = (HashMap) message.getValue();
                init(settings);
            }
            // ELSE IF the message is GIVE_PLAYER_FOCUS
            else if (message.getType() == MessageType.GIVE_PLAYER_FOCUS)
            {
                String name = (String) message.getValue();
                // IF name equals null
                if (name == null)
                {
                    activePlayer = null;
                }
                else
                {
                    // FOR EACH player in the game
                    for (AbstractPlayer player : getPlayers())
                    {
                        // IF the players name equals the name in the message
                        if (player.getName().equals(name))
                        {
                            activePlayer = player;
                        }
                    }
                }
            }
            // ELSE IF the message is INCREMENT_SCORE
            else if (message.getType() == MessageType.INCREMENT_SCORE)
            {
                // IF the activePlayer is set
                if (activePlayer != null)
                {
                    activePlayer.incScore();
                }
                else
                {
                    ArrayList<AbstractPlayer> players = getPlayers();
                    activePlayer = players.get(0);
                }
            }
            // ELSE IF the message is INVALID_CLUSTER
            else if (message.getType() == MessageType.INVALID_CLUSTER)
            {
                gameBoard.deselectAllCards();
            }
            // ELSE IF the message is PAUSE
            else if (message.getType() == MessageType.PAUSE)
            {
                currentStatus = StatusType.PAUSED;
            }
            // ELSE IF the message is SELECT_CARD
            else if (message.getType() == MessageType.SELECT_CARD)
            {
                Card card = (Card) message.getValue();
                // IF the card is not on the gameboard
                if (!gameBoard.selectCard(card))
                {
                    throw new Exception("Select card: " + card.toString() + " failed");
                }
            }
            // ELSE IF the message is SET_GAMEMODE
            else if (message.getType() == MessageType.SET_GAMEMODE)
            {
                gameMode = (ModeType) message.getValue();
            }
            // ELSE IF the message is SET_GAMESTATUS
            else if (message.getType() == MessageType.SET_GAMESTATUS)
            {
                currentStatus = (StatusType) message.getValue();
            }
            // ELSE IF the message is SET_TIME
            else if (message.getType() == MessageType.SET_TIME)
            {
                timeRemaining = ((Long) message.getValue()).longValue();
            }
            // ELSE IF the message is UNPAUSE
            else if (message.getType() == MessageType.UNPAUSE)
            {
                currentStatus = StatusType.RUNNING;
            }
            // ELSE IF the message is UNPAUSE
            else if (message.getType() == MessageType.VALID_CLUSTER)
            {
                ArrayList<Card> newCards = gameBoard.updateBoard();
                // IF there deck has not run out
                if (newCards != null)
                {
                    // IF there are cards to deal
                    if (newCards.size() > 0)
                    {
                        MessageManager.sendMessage(new GameMessage(MessageType.DEAL_CARDS, newCards),
                                            MessageDest.CF_UI);
                    }
                    else
                    {
                        currentStatus = StatusType.RUNNING;
                        // IF the gameboard size is less than minimum board size
                        if (gameBoard.getCurrentCards().size() < kMinBoardSize)
                        {
                            throw new Exception("Cards should have been dealt");
                        }
                    }
                }
                else
                {
                    // IF there are still cards on the field
                    if(gameBoard.getCurrentCards().size() != 0)
                    {
                        MessageManager.sendMessage(new GameMessage(MessageType.CHECK_GAME_OVER, null),
                                                    MessageDest.CF_LOGIC);
                        currentStatus = StatusType.RUNNING;
                    }
                    else
                    {
                        MessageManager.sendMessage(new GameMessage(MessageType.GAME_END, true),
                                                    MessageDest.CF_ALL);
                        currentStatus = StatusType.WIN;
                    }
                }
                timeRemaining = kDefaultTime;
            }

        }
        // ENDFOR
    }

    /**
     * Updates the current state of the game based on the messages it receives.
     * Will make all changes to state, including updated AIPlayers, before
     * calling the CFLogic to update.  Any changes CFLogic requires will be
     * performed before returning control
     *
     * @param tpf how much time, in milliseconds, has elapsed since last frame
     * (or update)
     * @throws Exception Exceptions thrown by CFLogic during udpate
     */
    public void update(long tpf) throws Exception
    {
        // CREATE an empty list for GameMessages
        ArrayList<GameMessage> messages = new ArrayList<GameMessage>();
        // CALL getMessages in messageManager and place result in created list
        messages = MessageManager.getMessages(MessageDest.CF_STATE);
        // CALL processMessages with messages from messageManager
        processMessages(messages);
        // IF the game is running
        if (currentStatus == StatusType.RUNNING)
        {
            // UPDATE the time remaining based from the specified time
            timeRemaining -= tpf;
            // IF time remaining reaches zero
            if (timeRemaining <= 0)
            {
                // CREATE a GameMessage for the sound ui if time has run out
                // GameMessage timeRanOut = new GameMessage(MessageType.PLAY_SOUND, null);
                // SEND the message by calling sendMessage in messageManager
                // MessageManager.sendMessage(timeRanOut, MessageDest.CF_ALL);
                if (gameMode == ModeType.TIMED)
                {
                    MessageManager.sendMessage(new GameMessage(MessageType.GAME_END, false),
                                                MessageDest.CF_ALL);
                    currentStatus = StatusType.LOSE;
                }
                else
                {
                    MessageManager.sendMessage(new GameMessage(MessageType.GIVE_AI_CLUSTER, null),
                                                MessageDest.CF_LOGIC);
                    MessageManager.sendMessage(new GameMessage(MessageType.OUT_OF_TIME, null),
                                                MessageDest.CF_UI);
                    timeRemaining = kDefaultTime;
                    currentStatus = StatusType.BEGINNING;
                }
            }
            // ENDIF
        // ENDIF
        }
        // IF there are any computer players
            // FOR EACH AIPlayer in the computerPlayers list
                // CALL the update function for the current AIPlayer
            // ENDFOR
        // ENDIF
        // IF the state has been init
        if (currentStatus != StatusType.NOTINGAME)
        {
            // CALL update in the CFLogic
            gameLogic.update(tpf);
        }
        // CALL getMessages in messageManager to make any changes from CFLogic
        messages = MessageManager.getMessages(MessageDest.CF_STATE);
        // CALL processMessages with messages from messageManager
        processMessages(messages);
    }
}
