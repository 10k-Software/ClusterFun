package clusterfun.state;
import java.io.Serializable;

/**
 * Score is a simple module that stores a player's score
 * and name, and is sortable using score as the natural order
 *
 * @author Nick Artman
 * @version 2.0
 * @.date 2009-2-24
*/
//  Version History
//       2/22/09 Nick Artman moved class outside HallOfFame and into discrete public class

public class Score implements Comparable<Object>, Serializable
{
    // The player's score
    private int score;
    // The player's name
    private String name;

    /**
     * Creates a new score
     *
     * @param theName The player's name
     * @param theScore The player's score
    */
    public Score(String theName, int theScore)
    {
        this.score = theScore;
        this.name = theName;
    }

    /**
     * Returns the score
     *
     * @return The score
     *
    */
    public int getScore()
    {
        return this.score;
    }

    /**
    * Returns the player's name
    *
    * @return The player's name
    *
    */
    public String getName()
    {
        return this.name;
    }

    /**
    * Returns the string description of the score
    *
    * @return The name and score in the format name: score
    *
    */
    public String toString()
    {
        return this.name + ": " + this.score + "\n";
    }
    
    /**
    * Overrides parent's compareTo for natural order sorting based on score
    *
    * @param obj The Score object to compare to
    *
    * @return 1, -1, or 0 depending on if this is greater, less than, or equal
    * to the compared Score
    */
    public int compareTo(Object obj)
    {
        // IF the object is a score object, and thus can be compared
        if (obj instanceof Score)
        {
            Score givenScore = (Score)obj;

            // IF this score is greater than the given score
            if (this.score > givenScore.getScore())
            {
                return 1;
            }
            // ELSE IF this score is less than the given score
            else if (this.score < givenScore.getScore())
            {
                return -1;
            }
            // ELSE they're equal
            else
            {
                return 0;
            }
        }
        else
        {
            throw new ClassCastException();
        }
    }
    
    /**
     * Overrides parent equals method. Determines if two scores have the same
     * player name and score value
     *
     * @param obj The score to compare to
     * @return Whether they are equal
    */
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        // IF the object is a score object
        if (obj instanceof Score)
        {
            // CAST the object
            Score givenScore = (Score)obj;
            
            // IF all the fields are equal
            if (givenScore.getScore() == this.score && givenScore.getName().equals(this.name))
            {
                // They are equal
                isEqual = true;
            }
        }
        return isEqual;
    }
    
}
