package clusterfun.ui.image;

import org.lwjgl.util.vector.Vector2f;

/**
 * A CardImageCoord is the coordinates for a card on either of the two image
 * files of cards required for the ClusterFun game. It is comprised of the
 * starting point of the card (the upper-left hand corner) and the ending
 * point of the card (the bottom right hand corner) as well as which image
 * file the card is on
 *
 * @author Lisa Hunter
 * @version 0.1
 * @.date 2009-1-28
 */
//  Version History
//      1/28/09 Lisa Hunter added algorithm pseudocode


public class CardImageCoord 
{

    /** The total number of cards for a game */
    private static final int kNumCardsTotal = 81;
    
    /** The total number of cards for the first image file */
    private static final int kNumCardsFile = 54;
    
    /** The standard card image height in pixels*/
    private static final int kCardHeight = 310;
    
    /** The standard card image width in pixels*/
    private static final int kCardWidth = 210;
    
    /** The standard image file row in cards */
    private static final int kCardsPerRow = 9;
    
    /** The standard image file width in pixels */
    private static final int kFileWidth = 2048;
    
    /** The standard image file height in pixels */
    private static final int kFileHeight = 2048;
    
    /** Which image file the card is on */
    private int fileSwitch;
    
    /** The starting point for the card */
    private Vector2f startCoord;
    
    /** The ending point for the card */
    private Vector2f endCoord;

    /**
     * Constructs a new CardImageCoord for the given card number
     * 
     * @param cardNum the card number whose image will be obtained
     * @throws CardOutOfBoundsException thrown when a card number is not a valid 
     */
    public CardImageCoord(int cardNum) throws CardOutOfBoundsException
    {
        // IF the card number is less than 0 or more than the allowed amount
        if(cardNum >= kNumCardsTotal || cardNum < 0 )
        {
            // THROW an exception that tells the user what went wrong
            throw new CardOutOfBoundsException();
        }
        
        // SET the file image switch based on whether the card is in image 
        // file 0 or 1
        fileSwitch = cardNum / kNumCardsFile;
        
        // SET the coordinates for the start of the card
        startCoord = calcStartPoint(cardNum);
        // SET the coordinates for the end of the card
        endCoord = calcEndPoint(cardNum);
        
    }
    
    /**
     * Calculates the top left corner of the card
     * 
     * @param cardNum the number of the card to calculate
     * @return the starting point of the card
     */
    private Vector2f calcStartPoint(int cardNum)
    {
        // ADJUST the card number so that it is file image independent
        cardNum = cardNum % kNumCardsFile;
        
        // CALCULATE the x coordinate by finding the x position
        float x = (cardNum % kCardsPerRow);
        // MULTIPLY the result by the card width
        x = x*kCardWidth;
        // DIVIDE to obtain the ratio
        x=x/kFileWidth;

        // CALCULATE the y coordinate by finding the y position
        float y = (cardNum / kCardsPerRow);
        // MULTIPLY the result by the card height
        y = y*kCardHeight;
        // DIVIDE to obtain the ratio
        y = y/kFileHeight;
            

        return new Vector2f(x , y);
    }
    
    /**
     * Calculates the bottom right corner of the card
     * 
     * @param cardNum the number of the card to calculate
     * @return the ending point of the card
     */
    private Vector2f calcEndPoint(int cardNum)
    {
        
        // ADJUST the card number so that it is file image independent
        cardNum = cardNum % kNumCardsFile;
        
        // CALCULATE the x coordinate by finding the x position
        float x = (cardNum % kCardsPerRow);
        // MULTIPLY the result by the card width
        x = x*kCardWidth;
  
        // ADJUST the point so that it is the far right instead of far left
        x = x + kCardWidth - 1;
        
        // DIVIDE to obtain the ratio
        x /= kFileWidth;
        
        
        // CALCULATE the y coordinate by finding the y position
        float y = (cardNum / kCardsPerRow);
        // MULTIPLY the result by the card height
        y = y*kCardHeight;
        
        // ADJUST the point so that it is the bottom instead of the top
        y = y + kCardHeight - 1;
            
        // DIVIDE to obtain the ratio
        y /= kFileHeight;
        
        return new Vector2f(x , y);
    }
    
    /**
     * Obtain the file image that the card is on
     * 
     * @return 0 for the first file image, 1 for the second file image
     */
    public int getWhichFile() {
        return fileSwitch;
    }

    /**
     * Obtain the starting point for the card
     * 
     * @return the upper-left hand point of the card
     */
    public Vector2f getStartCoord() {
        return startCoord;
    }

    /**
     * Obtain the ending point for the card
     * 
     * @return the bottom-right hand point of the card
     */
    public Vector2f getEndCoord() {
        return endCoord;
    }
    
    
}
