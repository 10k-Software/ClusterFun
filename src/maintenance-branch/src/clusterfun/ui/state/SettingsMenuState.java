package clusterfun.ui.state;

import clusterfun.lwjgl.Texture;
import clusterfun.lwjgl.TextureLoader;
import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.state.CFState;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.window.GameWindow;
import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
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

public class SettingsMenuState extends InGameState
{

    // Theme base when loading theme pictures
    private final String kTHEMEBASE = "resources/img/theme/";
    
    // Current theme folder
    private String curTheme = "defaultTheme/";
    
    // full screen check icon
    private ButtonEntity fullScreenCheck;
    
    // cull screen check icon (blank icon)
    private ButtonEntity fullScreenCheckBlank;
    
    // whether or not fullscreen is enabled
    private boolean fullScreen = false;
    
    // beginner mode check button
    private ButtonEntity beginnerModeCheck;
    
    // beginner mode check button (blank icon)
    private ButtonEntity beginnerModeCheckBlank;
    
    // whether or not the beginner mode is enabled
    private boolean beginnerMode = false;
    
    // timed mode check button
    private ButtonEntity timedModeCheck;
    
    // timed mode check button (blank icon)
    private ButtonEntity timedModeCheckBlank;
    
    // whether or not timed mode is enabled
    private boolean timedMode = true;
        
    // Cancel Button
    private ButtonEntity cancelIcon;
    
    // Cancel Button
    private ButtonEntity multiPLayerButton;
    
    // Start Game Button
    private ButtonEntity startGameIcon;
    
    // settings menu button
    private ButtonEntity settingsMenu;

    /**
     * @see GameState#getName() 
     * @return name state name
     */
    @Override
    public String getName()
    {
        return "SettingsMenuState";
    }

    /**
     * @see GameState#init(clusterfun.ui.window.GameWindow)
     * @param window game window
     * @throws java.lang.IOException file load exception
     */
    @Override
    public void init(GameWindow window) throws IOException
    {
        // CALL the parent's init function
        super.init(window);
        
        // INITIALIZE the full screen icons
        fullScreenCheck = new ButtonEntity("fullscreen check", new Vector2f(0.6875f,0.046875f), 
            new Vector2f(0.71875f,0.078125f), 32, 32, buttonTexture);
        fullScreenCheckBlank = new ButtonEntity("fullscreen check", new Vector2f(0.640625f,0.046875f), 
            new Vector2f(0.671875f,0.078125f), 32, 32, buttonTexture);
        
        // INITIALIZE the beginner mode icons
        beginnerModeCheck = new ButtonEntity("beginner mode check", new Vector2f(0.6875f,0.046875f), 
            new Vector2f(0.71875f,0.078125f), 32, 32, buttonTexture);
        beginnerModeCheckBlank = new ButtonEntity("beginner mode check", new Vector2f(0.640625f,0.046875f), 
            new Vector2f(0.671875f,0.078125f), 32, 32, buttonTexture);
        
        // INITIALIZE the timed mode icons
        timedModeCheck = new ButtonEntity("timed mode check", new Vector2f(0.6875f,0.046875f), 
            new Vector2f(0.71875f,0.078125f), 32, 32, buttonTexture);
        timedModeCheckBlank = new ButtonEntity("timed mode check", new Vector2f(0.640625f,0.046875f), 
            new Vector2f(0.671875f,0.078125f), 32, 32, buttonTexture);
        
        // INITIALIZE the cancel button
        cancelIcon  = new ButtonEntity("cancel button", new Vector2f(0.6406f,0.09375f), 
                new Vector2f(0.796875f, 0.125f), 160, 32, buttonTexture);
        
        // INITIALIZE the start game icon
        startGameIcon  = new ButtonEntity("start button", new Vector2f(0.6406f,0.14062f), 
                new Vector2f(0.796875f, 0.171875f), 160, 32, buttonTexture);
        
        // INITIALIZE the settings menu background
        settingsMenu = new ButtonEntity("menu", new Vector2f(0,0), 
            new Vector2f(0.625f,0.4688f), 640, 480, buttonTexture);
        
        multiPLayerButton = new ButtonEntity("multiplayer button", new Vector2f(0.640625f,0), 
            new Vector2f(0.75f,0.03125f), 112, 32, buttonTexture);
    }

    /**
     * @see GameState#render(clusterfun.ui.window.GameWindow, int) 
     */
    @Override
    public void render(GameWindow window, boolean picking)
    {
        // RENDER the parent state
        super.render(window, picking);
        
        // DISABLE depth testing to propery display the menu GUI
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
                
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, -1, 1);


        
        // RENDER the blank icons (mostly for buttons)
        settingsMenu.render();

        // IF the fullscreen mode is enabled
        if(fullScreen)
        {
            // RENDER the full screen checkmark
            fullScreenCheck.render();
        }
        else
        {
            fullScreenCheckBlank.render();
        }
        // IF the beginner mode is enabled
        if(beginnerMode)
        {
            // RENDER the beginner mode checkmark
            beginnerModeCheck.render();
        }
        else
        {
            beginnerModeCheckBlank.render();
        }
        // IF the timed mode is enabled
        if(timedMode)
        {
            // RENDER the timed mode checkmark
            timedModeCheck.render();
        }
        else
        {
            timedModeCheckBlank.render();
        }
        // RENDER the cancel button
        cancelIcon.render();
        
        // RENDER the start game button
        startGameIcon.render();
        
        //multiPLayerButton.render();
        
        // REENABLE the depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * @see GameState#update(clusterfun.ui.window.GameWindow, int) 
     */
    @Override
    public void update(GameWindow window, long timeElapsed) throws Exception
    {        
        // FOR every unique name that was 'touched' by the mouse click
        for(Integer name : window.getLastTouched())
        {
            if(name == multiPLayerButton.getName())
            {
                updateDisplay(window);
                
                window.changeToState("MultiplayerSettingsState");
            }
            // IF the current id clicked is the beginner mode radio
            if(name == beginnerModeCheckBlank.getName())
            {
                // CALL a not operator on the beginner mode flag
                beginnerMode = !beginnerMode;
            }
            // IF the current id clicked is the full screen checkbox
            if(name == fullScreenCheckBlank.getName())
            {
                // CALL a not operator on the beginner mode flag
                fullScreen = !fullScreen;
            }
            // IF the current id clicked is the timed mode checkbox
            if(name == timedModeCheckBlank.getName())
            {
                // CALL a not operator on the timed mode flag
                timedMode = !timedMode;
            }
            // IF the current id clicked is the start game button
            if(name == startGameIcon.getName())
            {
                // GRAB the current InGameState
                InGameState igs = (InGameState)window.getState("InGameState");
               
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
                
                // SET running to false in the current InGameState
                igs.setRunning(false);
                
                // UPDATE the current display to compensate for changing resolutions
                updateDisplay(window);
                
                // UPDATE the current config
                igs.setConfig(beginnerMode, timedMode);
                
                // SET the timed mode flag in the InGameState
                igs.setTimedMode(timedMode);
                
                // CHANGE to the InGameState state
                window.changeToState("InGameState");
            }
            // IF the current id clicked is the cancel icon
            if(name == cancelIcon.getName())
            {
                // SET the current gamestatus to running by sending a SET_GAMESTATUS
                if(timedMode)
                {
                    MessageManager.sendMessage(
                            new GameMessage(
                                GameMessage.MessageType.SET_GAMESTATUS, 
                                CFState.StatusType.RUNNING
                                ),
                                GameMessage.MessageDest.CF_STATE
                            );
                }
                
                // CHANGE the current state to InGameState
                window.changeToState("InGameState");
            }
        }
        // RESET the lastTouched item list
        window.resetLastTouched();
    }
    
    /**
     * Updates the current display to reflect the current resolution
     * 
     * @throws org.lwjgl.LWJGLException lwjgl operation exception
     */
    private void updateDisplay(GameWindow window) throws LWJGLException
    {
        int width = 800;
        int height = 600;
        
        boolean changeScreen = false;
        if(window.isFullScreen() != fullScreen)
        {
            changeScreen = true;
            // CHANGE the fullscreen mode
            Display.setFullscreen(fullScreen);
            window.setFullScreen(fullScreen);
        }
        
        // RETRIEVE the current Bpp
        int currentBpp = Display.getDisplayMode().getBitsPerPixel();
        
        DisplayMode mode = null;
        
        if(changeScreen || (window.getScreenHeight() != height) || (window.getScreenWidth() != width))
        {
            // GRAB an appropriate 640x480 window
            mode = GameWindow.findDisplayMode(width, height, currentBpp);
            
            // UPDATE the display mode
            Display.setDisplayMode(mode);
        }
    }

    /**
     * @see GameState#enter(clusterfun.ui.window.GameWindow) 
     */
    @Override
    public void enter(GameWindow window)
    {
        // DO nothing
    }

    /**
     * @see GameState#leave(clusterfun.ui.window.GameWindow) 
     */
    @Override
    public void leave(GameWindow window)
    {
        // DO nothing
    }

}
