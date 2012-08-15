package clusterfun.ui.state;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.window.GameWindow;
import java.io.IOException;
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

public class MultiplayerSettingsState extends InGameState
{

    // Theme base when loading theme pictures
    private final String kTHEMEBASE = "resources/img/theme/";
    
    // Current theme folder
    private String curTheme = "defaultTheme/";
        
    // Cancel Button
    private ButtonEntity cancelIcon;
    
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
        return "MultiplayerSettingsState";
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
        
        // INITIALIZE the cancel button
        cancelIcon  = new ButtonEntity("cancel button", new Vector2f(0.6406f,0.09375f), 
                new Vector2f(0.796875f, 0.125f), 160, 32, buttonTexture);
        
        // INITIALIZE the start game icon
        startGameIcon  = new ButtonEntity("start button", new Vector2f(0.6406f,0.14062f), 
                new Vector2f(0.796875f, 0.171875f), 160, 32, buttonTexture);
        
        // INITIALIZE the settings menu background
        settingsMenu = new ButtonEntity("menu", new Vector2f(0,0.53125f), 
            new Vector2f(0.625f,1f), 640, 480, buttonTexture);
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

        // RENDER the cancel button
        cancelIcon.render();
        
        // RENDER the start game button
        startGameIcon.render();
        
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
                
                // CHANGE to the InGameState state
                window.changeToState("InGameState");
            }
            // IF the current id clicked is the cancel icon
            if(name == cancelIcon.getName())
            {
                window.changeToState("SettingsMenuState");
            }
        }
        // RESET the lastTouched item list
        window.resetLastTouched();
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
