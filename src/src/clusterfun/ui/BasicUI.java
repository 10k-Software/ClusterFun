package clusterfun.ui;

/**
 * The BasicUI interface describes the basic methods that will be required by 
 * any user interface applied to the Cluster Fun game
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2008-12-3
 */
//  Version History 
//      12/2/08 Chris Gibson added algorithm pseudocode

public interface BasicUI
{
    
    /**
     * Refreshes the screen and updates the visual objects based on the time
     * elapsed since the last frame
     * 
     * @param timeElapsed the time that has elapsed since the last update
     * @throws java.lang.Exception exception
     */
    void update(long timeElapsed) throws Exception;
    
    
}
