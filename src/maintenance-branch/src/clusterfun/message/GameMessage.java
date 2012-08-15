package clusterfun.message;

/**
 * The GameMessage class is used to convey messages back and forth between the 
 * UI classes and the logic classes.  The class includes an enumerated type to 
 * define what type of message it is, and a generic object to include what data 
 * the message contains
 * 
 * @author Andrew Chan
 * @version 0.1
 * @.date 2008-11-23
 */
//  Version History
//      12/2/08 Chris Gibson added algorithm pseudocode

public class GameMessage
{
    
    /** the type of message that is being sent */
    private MessageType type;
    
    /** the value of the message */
    private Object value;
    
    /**
     * Creates a game message with the given type and message value
     * 
     * @param type enumerated type that describes the message type
     * @param val the object the message contains
     */
    public GameMessage(MessageType type, Object val)
    {
        // SET the type and value arguments with their respective class 
        // variables
        this.type = type;
        this.value = val;
    }

    /**
     * Returns the message type for the given game message.  Often times
     * describes the nature of the game action
     * 
     * @return message type
     */
    public MessageType getType()
    {
        return type;
    }
    
    /**
     * Returns the contents of the given game message.  This describes the 
     * details of the game action that occurred
     * 
     * @return message value
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Defines the type of message
     */
    public enum MessageType
    {
        /** A new player has been added */
        ADD_PLAYER,
        /** A card has been deselected */
        DESELECT_CARD,
        /** Time was set in the options */
        SET_TIME,
        /** Difficulty was set in the options */
        SET_DIFFICULTY,
        /** Game mode was set */
        SET_GAMEMODE,
        /** Game status was set */
        SET_GAMESTATUS,
        /** The AI player is requesting to have a cluster chosen for it */
        GIVE_AI_CLUSTER,
        /** The player has pressed pause */
        PAUSE,
        /** The player has pressed unpause */
        UNPAUSE,
        /** The score is to be incremented for a player */
        INCREMENT_SCORE,
        /** The score is to be decremented for a player */
        DECREMENT_SCORE,
        /** A player has requested focus (to select cards) */
        GIVE_PLAYER_FOCUS,
        /** A card has been selected by the focus player */
        SELECT_CARD,
        /** A player has requested for a hint */
        REQUEST_HINT,
        /** A hint has been defined, and should be shown to the player */
        GIVE_HINT,
        /** Deal another set of cards or if a valid cluster needs to be replaced */
        AFFIRM_DEAL,
        /** If the hint timer has run out or a hint is requested */
        AFFIRM_HINT,
        /** The timer has run out and a cluster has been selected to be removed */
        SELECT_CLUSTER,
        /** A cluster has been selected to be removed */
        REMOVE_CLUSTER,
        /** A cluster is invalid, and thus the cards should return to the deck*/
        INVALID_CLUSTER,
        /** The selected cluster is valid and thus the cards should be removed*/
        VALID_CLUSTER,
        /** An explaination of why the selected cards is an invalid cluster is requested */
        EXPLAIN_INVALID_CLUSTER,
        /** A player has requested to deal 3 more cards */
        REQUEST_DEAL,
        /** The given cards should be dealt to the board */
        DEAL_CARDS,
        /** The deal failed */
        DEAL_FAIL,
        /** Checks to see if the game is actually over */
        CHECK_GAME_OVER,
        /** The timer has run out */
        OUT_OF_TIME,
        /** A given sound should be played */
        PLAY_SOUND,
        /** The AI player should be reset using the given cluster amount */
        SET_CLSTR_AMT,
        /** The game has ended */
        GAME_END,
        /** The game has begun */
        GAME_START,
        /** The user has chosen to end the game */
        PROGRAM_END
    }
    
    /**
     * Defines the class the message is destined to
     */
    public enum MessageDest
    {
        /** The message is meant for AIPlayer */
        CF_AI,
        /** The message is meant for CFLogic */
        CF_LOGIC,
        /** The message is meant for CFState */
        CF_STATE,
        /** The message is meant for SoundManager */
        CF_SOUND,
        /** The message is meant for GraphicUI */
        CF_UI,
        /** The message is meant for CFApp */
        CF_APP,
        /** The message is meant for all classes */
        CF_ALL
    }
    
    /**
     * Requests a string representation of the messagetype
     * 
     * @return the messagetype as a string
     */
    @Override
    public java.lang.String toString()
    {
        return type.toString();
    }
}
