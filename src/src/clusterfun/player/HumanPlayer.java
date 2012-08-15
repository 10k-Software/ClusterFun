package clusterfun.player;
/**
 * A HumanPlayer is a player in the game ClusterFun that is controlled by
 * a human
 * 
 * @author Lisa Hunter
 * @version 2.0
 * @.date 2009-03-03
 */
//  Version History
//      12/2/08 Lisa Hunter added algorithm pseudocode
public class HumanPlayer extends AbstractPlayer
{
    
    /** The key that the player must push in oder to take his/her turn */
    private char key;

    /**
     * Constructs a HumanPlayer with the specified name
     * 
     * @param name the name of the player
     * @param key the key assigned to the player
     */
    public HumanPlayer(String name, char key)
    {
        // CREATE the name and the score for the player
        super(name);
        
        // SET the key with the new key
        this.key = key;
    }

    /**
     * Obtain the key for the player
     * 
     * @return the player's designated
     */
    public char getKey() 
    {
        return key;
    }

    /**
     * Change the player's key to the new key
     * 
     * @param key the new key for the player
     */
    public void setKey(char key) 
    {
        this.key = key;
    }

    /** 
     * Compares two objects to see if they are equal
     * 
     * @param obj the object to be compared to
     * @return true if they are equal, false if they are not
     */
    @Override
    public boolean equals(Object obj) 
    {
        // The flag for whether they are equal or not
        boolean flag = true;
        
        // IF the objects are exactly the same
        if (this == obj)
        {
            // RETURN true
            flag = true;
        }
        // ELSE IF the supers are not equal
        else if (!super.equals(obj))
        {
            // RETURN false
            flag = false;
        }
        // ELSE IF the object is not a HumanPlayer
        else if (!(obj instanceof HumanPlayer))
        {
            // RETURN false
            flag = false;
        }
        else
        {
            // CAST the object as a HumanPlayer
            HumanPlayer other = (HumanPlayer) obj;
            
            // IF the keys are not the same
            if (key != other.key)
            {
                // RETURN false
                flag = false;
            }
        }
        
        return flag;
    }
    
    

}
