package clusterfun.ui.state;

import clusterfun.lwjgl.Texture;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.window.GameWindow;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

/**
 * LoadingState acts as the game state for the loading screen.  The state waits 
 * until the next entrance state is loaded before disappearing
 * 
 * @author Chris Gibson
 * @version 1.1
 * @.date 2009-2-18
 */

public class LoadingState implements GameState
{
    // please wait icon
    private ButtonEntity pleaseWait;

    
    // Theme base when loading theme pictures
    private final String kTHEMEBASE = "resources/img/theme/";
    
    // Current theme folder
    private String curTheme = "defaultTheme/";
    
    private boolean initialized = false;
    
    private boolean rendered = false;
    
    /**
     * @see GameState#getName()
     * @return name string name
     */
    public String getName()
    {
        return "LoadingState";
    }

    /**
     * @see GameState#init(clusterfun.ui.window.GameWindow)
     * @param window game window 
     * @throws java.lang.IOException file load exception
     */
    public void init(GameWindow window) throws IOException
    {
        // INITIALIZE the visual board entity
        Texture pleaseWaitTex = window.loadTexture(kTHEMEBASE + curTheme + "background/pleaseWait.png");

        // INITIALIZE the score text
        pleaseWait = new ButtonEntity("please wait", new Vector2f(0,0), 
                new Vector2f(1, 1), 512, 512, pleaseWaitTex);
        
        initialized = true;
    }
    
    
    public boolean isInitialized()
    {
        return initialized;
    }

    /**
     * @see GameState#render(clusterfun.ui.window.GameWindow, int) 
     */
    public void render(GameWindow window, boolean picking)
    {
        // CLEAR the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
        
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, -1, 1);

        // RENDER the loading image
        pleaseWait.render();
        
        rendered = true;
    }

    /**
     * @see GameState#update(clusterfun.ui.window.GameWindow, int) 
     */
    public void update(GameWindow window, long timeElapsed) throws Exception
    {
        if(initialized && rendered)
        {
            window.changeToState("SettingsMenuState");
        }
    }
   

    /**
     * @see GameState#enter(clusterfun.ui.window.GameWindow) 
     */
    public void enter(GameWindow window)
    {
        // DO nothing, we're waiting for the other states to initialize
    }

    /**
     * @see GameState#leave(clusterfun.ui.window.GameWindow) 
     */
    public void leave(GameWindow window)
    {
        // DO nothing, we don't care if the player leaves this state
    }
    
}
