
package clusterfun.player;


import clusterfun.player.AbstractPlayer;
import clusterfun.state.CFState.DifficultyType;

/**
 * This is the fake for the AIPlayer class
 * @author Lisa Hunter
 */
public class AIPlayer extends AbstractPlayer{

    /**
     * Constructs a new AIPlayer with the specified name and difficulty setting
     * 
     * @param name the name for the player
     * @param difficulty the difficulty setting of the player
     */
    public AIPlayer(String name, DifficultyType difficulty)
    {
        super(name);
        System.out.println("AIPlayer has been constructed.");
    }
    
    /**
     * Obtains any reset messages for the player and saves the message's 
     * information. If multiple messages are sent, the most recent one is
     * saved 
     * 
     * @return true if a message to reset has been received, false otherwise
     */
    private boolean receiveMessages()
    {
        return true;
    }

    /**
     * Decides a randomized amount of time to wait based on the difficulty
     * setting of the player. The player will wait this long before finding a
     * cluster
     * 
     * @return the time to wait in milliseconds
     */
    private long calcTimeToFindCluster()
    {
        return 0;
    }

    /**
     * Determines a cluster on the board and selects it. If no clusters exist, 
     * the player will add three more cards the board. It will then reset 
     * itself in order to wait again
     */
    private void takeTurn()
    {
        System.out.println("AIPlayer has taken his turn.");
    }

    /**
     * Updates the AIPlayer with the current amount of elapsed time.
     * As time within the game progresses, the amount of time the AIPlayer
     * should wait decreases accordingly until one of two things occur. First,
     * if the wait time reaches zero, then the player will proceed to take 
     * its turn handling the board; second, if another player finds a cluster
     * first, then the AIPlayer will receive a message telling it to reset, and
     * will do so
     * 
     * @param timeElapsed the amount of time that had elapsed since the last
     * update in milliseconds
     */
    public void update(long timeElapsed)
    {
        System.out.println("AIPlayer has been updated.");

    }

    /**
     * Resets the player with a new amount of time to wait before finding
     * another cluster
     */
    private void reset()
    {
        System.out.println("AIPlayer has been resetted");
    }

}
