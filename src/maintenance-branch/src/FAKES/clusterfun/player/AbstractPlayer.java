
package clusterfun.player;

/**
 * This is the fake for the AbstractPlayer class
 * @author Lisa Hunter
 */
public class AbstractPlayer {

    /**
     * Constructs a new AbstractPlayer with the specified name
     * 
     * @param name the name for the player
     */
    public AbstractPlayer(String name)
    {
        System.out.println("AbstractPlayer with name " + 
                name + "has been constructed.");
    }

    /**
     * Retrieves the player's name
     * 
     * @return the name of the player
     */
    public String getName()
    {
        return "name";
    }

    /**
     * Retrieves the player's score
     * 
     * @return the score of the player
     */
    public int getScore()
    {
        return 1;
    }

    /**
     * Increases the player's score by 1
     */
    public void incScore()
    {
        System.out.println("The player's score has been incremented.");
    }

    /**
     * Decreases the player's score by 1
     */
    public void decScore()
    {
        System.out.println("The player's score has been decremented.");
    }
}
