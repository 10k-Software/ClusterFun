package clusterfun.state;

import clusterfun.player.AbstractPlayer;
import clusterfun.board.GameBoard;
import clusterfun.board.Deck;
import clusterfun.logic.CFLogic;
import clusterfun.player.HumanPlayer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Fake CFState class
 *
 * @author Jason Swalwell
 * @version 0.1
 * @.date 2008-11-23
 */

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
    private ArrayList<AbstractPlayer> humanPlayers;

    /**
     * Time, in milliseconds, remaining for the active player to choose a
     * cluster
     */
    private long timeRemaining;

    /**
     * Constructs a new CF game state
     */
    public CFState(CFLogic gameLogic, DeckType deck)
    {
        System.out.println("CFState created");
    }

    /**
     * Returns the player who is attempted to find a cluster
     *
     * @return the current player choosing a cluster
     */
    public AbstractPlayer getActivePlayer()
    {
        return new HumanPlayer("human", 'a');
    }

    public DeckType getDeckStyle()
    {
        return DeckType.NOSHUFFLE;
    }
    /**
     * Returns the current difficulty of the game
     *
     * @return the current difficulty of the game
     */
    public DifficultyType getDifficulty()
    {
        return DifficultyType.EASY;
    }

    /**
     * Returns the current game board
     *
     * @return the current game board
     */
    public GameBoard getGameBoard()
    {
        return new GameBoard(DeckType.NOSHUFFLE);
    }

    /**
     * Returns the current game mode
     *
     * @return the current game mode
     */
    public ModeType getGameMode()
    {
        return ModeType.SOLITARE;
    }

    /**
     * Returns the current status of the game
     *
     * @return the current game status
     */
    public StatusType getGameStatus()
    {
        return StatusType.RUNNING;
    }

    /**
     * Returns a list of players playing the game
     *
     * @return the list of players currently playing the game
     */
    public ArrayList<AbstractPlayer> getPlayers()
    {
        ArrayList<AbstractPlayer> players = new ArrayList<AbstractPlayer>();
        players.add(new HumanPlayer("human", 'c'));
        return players;
    }

    /**
     * Returns the time, in milliseconds, remaining for a player to pick a
     * cluster
     *
     * @return the time, in milliseconds, remaining to pick a cluster
     */
    public long getTimeRemaining()
    {
        return 0L;
    }

    /**
     * Initializes the game board and state to a default state
     */
    public void init(HashMap settings)
    {
        System.out.println("CFState has been init");
    }

    /**
     * Updates the current state of the game based on the messages it receives.
     * Will make all changes to state, including updated AIPlayers, before
     * calling the CFLogic to update.  Any changes CFLogic requires will be
     * performed before returning control
     *
     * @param tpf how much time, in milliseconds, has elapsed since last frame
     * (or update)
     */
    public void update(long tpf)
    {
        System.out.println("CFState has been updated");
    }
}
