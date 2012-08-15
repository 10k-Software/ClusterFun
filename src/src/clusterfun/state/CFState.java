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
        EASY (90000L),
        /** The medium difficulty setting */
        MEDIUM (60000L),
        /** The hard difficulty setting */
        HARD (30000L);

        private long time;

        DifficultyType(long time)
        {
            this.time = time;
        }

        /**
         * Returns time to select cluster for difficulty
         * 
         * @return time time for given difficulty
         */
        public long getTime()
        {
            return time;
        }
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

    private static final int kMinBoardSize = 12;

    private static final long kMultiTime = 6000L;

    /**
     * Active player attempting to choose a cluster
     */
    private AbstractPlayer activePlayer;

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
     * If a timer is running
     */
    private boolean hasTimer;

    /**
     * The players hall of fame information
     */
    private HallOfFame hof;

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
        // SET the game status to NOTINGAME
        currentStatus = StatusType.NOTINGAME;
        this.deckStyle = deckType;
        given = deckType;
        hof = new HallOfFame();
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
        // RETURN list of all players
        return allPlayers;
    }

    /**
     * Returns the hall of fame scores from the desired difficulty
     *
     * @param level the desired difficulty scores
     * @return the list of scores for difficulty specified
     * @throws java.lang.Exception exception thrown by getHighScores()
     */
    public ArrayList<Score> getScores(DifficultyType level) throws Exception
    {
        return hof.getHighScores(level);
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
        humanPlayers = new ArrayList<HumanPlayer>();
        // CREATE a human player
        HumanPlayer defaultPlayer = new HumanPlayer("default", 'a');
        // SET the active player to the newly created human player
        addPlayer(defaultPlayer);
        activePlayer = defaultPlayer;

        deckStyle = given;
        timeRemaining = 0L;
          
        gameMode = (ModeType) settings.get("mode");
        difficulty = (DifficultyType) settings.get("difficulty");
        
        // IF mode is solitare
        if (gameMode == ModeType.SOLITARE)
        {
            hasTimer = false;
        }
        // ELSE IF mode is timed
        else if (gameMode == ModeType.TIMED)
        {
            hasTimer = true;
        }
        // ELSE IF the mode is beginner
        else if (gameMode == ModeType.BEGINNER)
        {
            deckStyle = DeckType.BEGINNER;
            Boolean timer = (Boolean) settings.get("timer");
            hasTimer = timer;
        }
        // ELSE the mode is multiplayer
        else
        {
            humanPlayers = (ArrayList<HumanPlayer>) settings.get("players");
            activePlayer = null;
            hasTimer = false;
        }

        // IF there is a timer
        if (hasTimer)
        {
            timeRemaining = difficulty.getTime();
        }

        // CREATE an instance of the gameBoard with deck
        gameBoard = new GameBoard(deckStyle);
        ArrayList<Card> cards = gameBoard.getCurrentCards();
        MessageManager.sendMessage(new GameMessage(MessageType.DEAL_CARDS, cards),
                                     MessageDest.CF_UI, MessageDest.CF_SOUND);

        // SET the current status to BEGINNING
        currentStatus = StatusType.BEGINNING;
        
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
            // IF the message is AFFIRM_DEAL
            if (message.getType() == MessageType.AFFIRM_DEAL)
            {
                ArrayList<Card> addedCards = gameBoard.addThreeCards();
                MessageManager.sendMessage(new GameMessage(MessageType.DEAL_CARDS, addedCards),
                                            MessageDest.CF_UI, MessageDest.CF_SOUND);
                
                // IF there are no more cards to be dealt
                if (gameBoard.getRemainingDeckNumber() == 0)
                {
                    MessageManager.sendMessage(new GameMessage(MessageType.CHECK_GAME_OVER, null),
                                                    MessageDest.CF_LOGIC);
                }
            }
            // ELSE IF the message is DECREMENT_SCORE
            else if (message.getType() == MessageType.DECREMENT_SCORE)
            {
                // IF active player is not null
                if (activePlayer != null)
                {
                    activePlayer.decScore();
                }
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
                // IF the mode is timed
                if (gameMode == ModeType.TIMED)
                {
                    hof.proposeNewHighScore(activePlayer.getScore(), difficulty);
                }
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
                AbstractPlayer focus = (AbstractPlayer) message.getValue();
                // IF name equals null
                if (focus == null)
                {
                    activePlayer = null;
                }
                else
                {
                    // FOR EACH player in the game
                    for (AbstractPlayer player : getPlayers())
                    {
                        // IF the players name equals the name in the message
                        if (player.equals(focus))
                        {
                            activePlayer = player;
                        }
                    }
                }

                // IF the mode is multiplayer
                if (gameMode == ModeType.MULTIPLAYER)
                {
                    timeRemaining = kMultiTime;
                    currentStatus = StatusType.RUNNING;
                    hasTimer = true;
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
            // ELSE IF the message is player name
            else if (message.getType() == MessageType.PLAYER_NAME)
            {
                hof.addProposedScoreToList((String) message.getValue());
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
            // ELSE IF the message is SET_GAMESTATUS
            else if (message.getType() == MessageType.SET_GAMESTATUS)
            {
                currentStatus = (StatusType) message.getValue();
            }
            // ELSE IF the message is VALID_CLUSTER
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
                                            MessageDest.CF_UI, MessageDest.CF_SOUND);
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

                    // IF there are no more cards to be dealt
                    if (gameBoard.getRemainingDeckNumber() == 0)
                    {
                        MessageManager.sendMessage(new GameMessage(MessageType.CHECK_GAME_OVER, null),
                                                    MessageDest.CF_LOGIC);
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

                timeRemaining = 0L;
                // IF mode is multiplayer
                if (gameMode == ModeType.MULTIPLAYER)
                {
                    activePlayer = null;
                    hasTimer = false;
                }
                // IF there is a timer
                if (hasTimer)
                {
                    timeRemaining = difficulty.getTime();
                }
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
        if (hasTimer && currentStatus == StatusType.RUNNING)
        {
            // UPDATE the time remaining based from the specified time
            timeRemaining -= tpf;
            // IF time remaining reaches zero
            if (timeRemaining <= 0)
            {
                // IF mode is multiplayer
                if (gameMode == ModeType.MULTIPLAYER)
                {
                    activePlayer.decScore();
                    activePlayer = null;
                    hasTimer = false;
                    MessageManager.sendMessage(new GameMessage(MessageType.OUT_OF_TIME, null),
                                                 MessageDest.CF_UI);
                    MessageManager.sendMessage(new GameMessage(MessageType.DECREMENT_SCORE, null),
                                                 MessageDest.CF_SOUND);
                }
                else
                {
                    MessageManager.sendMessage(new GameMessage(MessageType.GIVE_AI_CLUSTER, null),
                                                MessageDest.CF_LOGIC);
                    MessageManager.sendMessage(new GameMessage(MessageType.OUT_OF_TIME, null),
                                                MessageDest.CF_UI, MessageDest.CF_SOUND);
                    timeRemaining = difficulty.getTime();
                    currentStatus = StatusType.BEGINNING;
                }
            }
            // ENDIF
        // ENDIF
        }
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
