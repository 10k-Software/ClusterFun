package clusterfun.ui.image;
/**
 * A CardOutOfBoundsException is an exception that occurs when a card outside
 * of the allowed amount in the game ClusterFun is referenced.
 *
 * @author Lisa Hunter
 * @version 1.0
 * @.date 2009-1-28
 */
//  Version History
//      1/28/09 Lisa Hunter added algorithm pseudocode
public class CardOutOfBoundsException extends Exception
{
    /**
     * Constructs a new CardOutOfBoundsException
     */
    public CardOutOfBoundsException()
    {
        // CALL the super with a message
        super("Improper card number in CardImageCoord");
    }
}
