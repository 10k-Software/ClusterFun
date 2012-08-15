package clusterfun.ui.state;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.state.CFState;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.window.GameWindow;
import java.util.HashMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

/**
 * SEttingsMenuState acts as the game state for the settings menu of the game.  
 * Every visible menu option is rendered and tracked, and all user input is 
 * sent out as game messages if the state changes
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2008-12-3
 */
//  Version History 
//      12/2/08 Chris Gibson added algorithm pseudocode

public class ChooseDifficultyState extends InGameState
{
    
    // easy mode
    private ButtonEntity easyMode;
    private ButtonEntity easyModeCheck;

    // easy mode
    private ButtonEntity medMode;
    private ButtonEntity medModeCheck;

    // easy mode
    private ButtonEntity hardMode;
    private ButtonEntity hardModeCheck;
    
    // Start Game Button
    private ButtonEntity startGameIcon;
    
    // settings menu button
    private ButtonEntity difficultyMenu;

    // cancel button
    private ButtonEntity cancelIcon;

    // hash map given to CFState at the beginning of the game
    private HashMap<String, Object> configHash;

    // selected difficulty
    private CFState.DifficultyType difficulty;

    /**
     * @see GameState#getName() 
     * @return name state name
     */
    @Override
    public String getName()
    {
        return "DifficultyMenuState";
    }

    /**
     * @see GameState#init(clusterfun.ui.window.GameWindow)
     * @param window game window
     * @throws java.lang.Exception file load exception
     */
    @Override
    public void init(GameWindow window) throws Exception
    {
        // CALL the parent's init function
        super.init(window);
        
        // INITIALIZE the cancel button with image coordinates 656x96 with dimensions 160x32
        cancelIcon  = new ButtonEntity("cancel button", new Vector2f(656, 96), 
                new Vector2f(160, 32), buttonTexture);
        
        // INITIALIZE the start game icon with image coordinates 656x144 with dimensions 160x32
        startGameIcon  = new ButtonEntity("start button", new Vector2f(656, 144), 
                new Vector2f(160, 32), buttonTexture);
        
        // INITIALIZE the settings menu background with image coordinates 0x0 with dimensions 640x480
        difficultyMenu = new ButtonEntity("menu", new Vector2f(0, 0),
            new Vector2f(640, 480), buttonTexture2);

        // INITIALIZE checked easy icon with image coordinates 704x48 with dimensions 32x32
        easyModeCheck = new ButtonEntity("easy check", new Vector2f(704, 48),
            new Vector2f(32, 32), buttonTexture);
        // INITIALIZE unchecked easy icon with image coordinates 656x48 with dimensions 32x32
        easyMode = new ButtonEntity("easy check", new Vector2f(656, 48),
            new Vector2f(32, 32), buttonTexture);

        // INITIALIZE checked medium icon with image coordinates 704x48 with dimensions 32x32
        medModeCheck = new ButtonEntity("medium check", new Vector2f(704, 48),
            new Vector2f(32, 32), buttonTexture);
        // INITIALIZE unchecked medium icon with image coordinates 656x48 with dimensions 32x32
        medMode = new ButtonEntity("medium check", new Vector2f(656, 48),
            new Vector2f(32, 32), buttonTexture);

        // INITIALIZE checked hard icon with image coordinates 704x48 with dimensions 32x32
        hardModeCheck = new ButtonEntity("hard check", new Vector2f(704, 48),
            new Vector2f(32, 32), buttonTexture);
        // INITIALIZE unchecked hard icon with image coordinates 656x48 with dimensions 32x32
        hardMode = new ButtonEntity("hard check", new Vector2f(656, 48),
            new Vector2f(32, 32), buttonTexture);

        difficulty = CFState.DifficultyType.EASY;
    }

    /**
     * @see GameState#render(clusterfun.ui.window.GameWindow, int)
     *
     * @param window game window
     * @param picking pick rendering
     * @throws java.lang.Exception exception
     */
    @Override
    public void render(GameWindow window, boolean picking) throws Exception
    {
        final int width = 800;
        final int height = 600;
        // RENDER the parent state
        super.render(window, picking);
        
        // DISABLE depth testing to propery display the menu GUI
        GL11.glDisable(GL11.GL_DEPTH_TEST);
              
        GL11.glLoadIdentity();
        
        GL11.glOrtho(0, width, height, 0, -1, 1);

        // RENDER the blank icons (mostly for buttons)
        difficultyMenu.render();

        // RENDER the start game button
        startGameIcon.render();

        // SWITCH for every difficulty
        switch(difficulty)
        {
            // IF the difficulty is set to easy
            case EASY:
                easyModeCheck.render();
                medMode.render();
                hardMode.render();
                break;
            // IF the difficulty is set to medium
            case MEDIUM:
                easyMode.render();
                medModeCheck.render();
                hardMode.render();
                break;
            // IF the difficulty is set to hard
            case HARD:
                easyMode.render();
                medMode.render();
                hardModeCheck.render();
                break;
            default:
                break;
        }
        
        // RENDER the cancel button
        cancelIcon.render();
        
        // REENABLE the depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * @see GameState#update(clusterfun.ui.window.GameWindow, int)
     *
     * @param window game window
     * @param timeElapsed elapsed time since last update
     * @throws java.lang.Exception exception
     */
    @Override
    public void update(GameWindow window, long timeElapsed) throws Exception
    {        
        // FOR every unique name that was 'touched' by the mouse click
        for(Integer name : window.getLastTouched())
        {
            handleInput(window, name);
        }
        // RESET the lastTouched item list
        window.resetLastTouched();
    }

    private void handleInput(GameWindow window, int name) throws Exception
    {
        // IF the exit button matches the 'touched' entity
        if(name == exitButton.getName())
        {
            window.setCloseRequested(true);
        }
        // IF the easy mode radio matches the 'touched' entity
        if(name == easyMode.getName())
        {
            difficulty = CFState.DifficultyType.EASY;
        }
        // IF the medium mode radio matches the 'touched' entity
        if(name == medMode.getName())
        {
            difficulty = CFState.DifficultyType.MEDIUM;
        }
        // IF the hard mode radio matches the 'touched' entity
        if(name == hardMode.getName())
        {
            difficulty = CFState.DifficultyType.HARD;
        }
        // IF the current id clicked is the start game button
        if(name == startGameIcon.getName())
        {
            // UPDATE the current display to compensate for changing resolutions
            updateDisplay(window);

            // GRAB the current InGameState
            InGameState igs = (InGameState)window.getState("InGameState");

            handleEndGame(igs);

            // SET running to false in the current InGameState
            igs.setRunning(false);

            configHash.put("difficulty", difficulty);

            // UPDATE the current config
            igs.setConfig(configHash);


            // CHANGE to the InGameState state
            window.changeToState("InGameState");
        }
        // IF the current id clicked is the cancel icon
        if(name == cancelIcon.getName())
        {

            // CHANGE the current state to InGameState
            window.changeToState("SettingsMenuState");
        }
    }

    private void handleEndGame(InGameState igs)
    {
        // IF the current InGameState is running
        if(igs.isRunning())
        {
            // SEND a GAME_END message to end the game
            MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.GAME_END,
                    null
                    ),
                    GameMessage.MessageDest.CF_ALL
                );
        }
    }

    /**
     * Sets the current configuration hash map
     * 
     * @param map configuration hash map
     */
    public void setConfigHash(HashMap<String, Object> map)
    {
        configHash = map;
    }
    
    /**
     * Updates the current display to reflect the current resolution
     * 
     * @throws org.lwjgl.LWJGLException lwjgl operation exception
     */
    private void updateDisplay(GameWindow window) throws Exception
    {
        final int width = 800;
        final int height = 600;

        boolean fullscreen =  (Boolean)configHash.get("fullscreen");

        boolean changeScreen = false;

        // IF fullscreen mode has changed
        if(window.isFullScreen() != fullscreen)
        {
            changeScreen = true;
            // CHANGE the fullscreen mode
            Display.setFullscreen(fullscreen);
            window.setFullScreen(fullscreen);
        }
        
        // RETRIEVE the current Bpp
        int currentBpp = Display.getDisplayMode().getBitsPerPixel();
        
        // IF the window must be recreated
        if(changeScreen || (window.getScreenHeight() != height) || (window.getScreenWidth() != width))
        {
            GameWindow.setDisplayMode(width, height, currentBpp);
            
        }
    }

    /**
     * @see GameState#enter(clusterfun.ui.window.GameWindow)
     *
     * @param window game window
     */
    @Override
    public void enter(GameWindow window)
    {
        // DO nothing
    }

    /**
     * @see GameState#leave(clusterfun.ui.window.GameWindow)
     * @param window game window
     */
    @Override
    public void leave(GameWindow window)
    {
        // DO nothing
    }

}
