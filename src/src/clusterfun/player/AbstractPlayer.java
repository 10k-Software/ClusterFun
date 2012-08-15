
package clusterfun.player;

/**
 * An AbstractPlayer is the template for all types of players within
 * the game cluster. Whether a player is human-controlled or 
 * computer-controlled, it will have a score and a name. The starting
 * score for any player will always be 0. It will also accept any
 * given name
 * 
 * @author Lisa Hunter
 * @version 1.0
 * @.date 2008-11-23
 */
//  Version History
//      12/2/08 Lisa Hunter added algorithm pseudocode
public abstract class AbstractPlayer 
{

    /** The current score for the player */
    private int score;
    
    /** The player's name */
    private String name;

    /**
     * Constructs a new AbstractPlayer with the specified name
     * 
     * @param name the name for the player
     */
    public AbstractPlayer(String name)
    {
        // SET the name of the player with the specified name
        this.name = name;
        // SET the player's score to zero
        score = 0;
    }

    /**
     * Retrieves the player's name
     * 
     * @return the name of the player
     */
    public String getName()
    {
        // RETURN the name of the player
        return name;
    }

    /**
     * Retrieves the player's score
     * 
     * @return the score of the player
     */
    public int getScore()
    {
        // RETURN the score of the player
        return score;
    }

    /**
     * Increases the player's score by 1
     */
    public void incScore()
    {
        // INCREMENT the player's score by 1
        score++;
    }

    /**
     * Decreases the player's score by 1
     */
    public void decScore()
    {
        // DECREMENT the player's score by 1
        score--;
    }


    /**
     * Compares two players by name and score
     * 
     * @param obj the object to be compared against
     * @return true if they are equal, false if they are not
     */
    @Override
    public boolean equals(Object obj) 
    {
        // The flag to tell whether they are true or not
        boolean equal = true;
        
        // IF the objects aren't the exact same object
        if (this != obj)
        {
            // IF the other object is not even an object
            if (obj == null)
            {
                // THEN the two objects aren't equal
                equal = false;
            }
            // ELSE IF the object isn't even an AbstractPlayer
            else if(!(obj instanceof AbstractPlayer))
            {
                // THEN the two objects aren't equal
                equal = false;
            }
            else
            {
                // CAST the object as an AbstractPlayer object
                AbstractPlayer other = (AbstractPlayer) obj;
                
                // IF the name is null
                if(name == null) 
                {
                    // IF the other name is not null
                    if(other.name != null)
                    {
                        // THEN the two objects aren't equal
                        equal = false;
                    }
                }
                // ELSE IF the other name does not equal this name
                else if(!name.equals(other.name))
                {
                    // THEN the two objects aren't equal
                    equal = false;
                }
                
                // IF the scores are not the same
                if(score != other.score)
                {
                    // THEN the two objects aren't equal
                    equal = false;
                }
            }
        }
            
            
        return equal;
    }




}
