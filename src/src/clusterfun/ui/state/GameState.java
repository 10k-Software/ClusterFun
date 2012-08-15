package clusterfun.ui.state;

import clusterfun.ui.window.GameWindow;

/**
 * GameState acts as a scaffold for all renderable state ins the game.  If a 
 * state is selected, it displays only what it is assigned to display when the 
 * render method is called
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2008-12-3
 */
//  Version History 
//      12/2/08 Chris Gibson added algorithm pseudocode

public interface GameState
{
    
    /**
     * Returns the state's assigned name
     * 
     * @return game state's name
     */
    String getName();

    /**
     * Returns whether or not the state 'init' has been called yet
     *
     * @return initialized initialized boolean
     */
    boolean isInitialized();

    /**
     * Resets the initialization of the state
     */
    void reset();

    /**
     * Initializes the game state
     * 
     * @param window the window that the game is drawn to
     * @throws java.io.Exception exception thrown only if the state cannot
     * be initialized
     */
    void init(GameWindow window) throws Exception;

    /**
     * Renders the current game state to the given game window
     * 
     * @param window the window that the game is drawn to
     * @param picking whether or not picking is enabled
     * @throws java.io.Exception exception thrown only if the state cannot
     * be rendered
     */
    void render(GameWindow window, boolean picking) throws Exception;

    /**
     * Updates the contents of the current game state, as well as all of the
     * connected entities to be rendered
     * 
     * @param window the window that the game is drawn to
     * @param timeElapsed time passed since last update
     * @throws java.io.Exception exception thrown only if the state cannot
     * be updated
     */
    void update(GameWindow window, long timeElapsed) throws Exception;

    /**
     * Called when the game window enters the given game state.  Used to 
     * prepare the state for rendering
     * 
     * @param window the window that the game is drawn to
     */
    void enter(GameWindow window);

    /**
     * Called when the game window leaves the given game state.  Used to clean 
     * up messes and prepare the state for the next enter call
     * 
     * @param window the window that the game is drawn to
     */
    void leave(GameWindow window);
}
