package clusterfun.ui;

import clusterfun.state.CFState;
import clusterfun.ui.window.GameWindow;

/**
 * GraphicUI is a BasicUI implementation that uses an OpenGL port used to 
 * display the cards in a semi-3d environment
 * 
 * @see BasicUI
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2008-12-1
 */
//  Version History 
//      12/2/08 Chris Gibson added algorithm pseudocode

public class GraphicUI implements BasicUI
{

    /** The game window which displays the contents in the game environment */
    private GameWindow window;

    private CFState logicState;
    
    /**
     * Sets up any class variables and calls initialize
     * @param  logicState CFState object
     * @throws java.lang.Exception exception
     */
    public GraphicUI(CFState logicState) throws Exception
    {
        this.logicState = logicState;
        // INITIALIZE the game window object
        window = new GameWindow(logicState);
        window.initialize();
        
        
    }
    
    /**
     * @see BasicUI#update
     * @param timeElapsed time elapsed since last update
     * @throws java.lang.Exception exception
     */
    public void update(long timeElapsed) throws Exception
    {
        // CALL updateState to update the current running state
        window.update(timeElapsed);
        // CALL render method
        // CALL the game window updateInput to grab input messages
        window.updateInput();

        // IF the UI needs to be reset
        if(window.isResetRequested())
        {
            window = new GameWindow(logicState);
            window.initialize();
        }
    }

}
