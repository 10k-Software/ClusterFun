package clusterfun.ui.image;

import junit.framework.TestCase;

/**
 * CardOutOfBoundsExceptionTest tests the CardOutOfBoundsException class for errors
 * 
 * @author Lisa Hunter
 *
 */

public class CardOutOfBoundsExceptionTest extends TestCase{

   
    
    public CardOutOfBoundsExceptionTest()
    {
        
    }
    
    public void testMakeException()
    {
        try
        {
            new CardImageCoord(95);
            fail();
        }
        catch(CardOutOfBoundsException e)
        {
            
        }

    }
    
}