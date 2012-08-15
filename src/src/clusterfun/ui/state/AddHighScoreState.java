package clusterfun.ui.state;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.window.GameWindow;
import org.lwjgl.input.Keyboard;
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

public class AddHighScoreState extends InGameState
{
    // next icon
    private ButtonEntity nextIcon;

    // menu icon
    private ButtonEntity hsMenu;

    
    // player name strings
    private String nameStr;


    /**
     * @see GameState#getName() 
     * @return name state name
     */
    @Override
    public String getName()
    {
        return "AddHighScoreState";
    }
    
    /**
     * @see GameState#init(clusterfun.ui.window.GameWindow)
     * @param window game window
     * @throws java.lang.IOException file load exception
     */
    @Override
    public void init(GameWindow window) throws Exception
    {
        // CALL the parent's init function
        super.init(window);
        
        // INITIALIZE the cancel button
        nextIcon = new ButtonEntity("next button", new Vector2f(832, 96),
                new Vector2f(160, 32), buttonTexture);

        // INITIALIZE the high score state
        hsMenu = new ButtonEntity("menu", new Vector2f(0, 0),
                new Vector2f(640, 480), buttonTexture3);

        // CLEAR the name string
        nameStr = "";
    }

    /**
     * @see GameState#render(clusterfun.ui.window.GameWindow, int) 
     */
    @Override
    public void render(GameWindow window, boolean picking) throws Exception
    {
        // RENDER the parent state
        super.render(window, picking);
        
        // DISABLE depth testing to propery display the menu GUI
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        // SETUP the screen for 2d rendering        
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, -1, 1);


        hsMenu.render();
        // RENDER the next button
        nextIcon.render();

        
        // IF the current key input field is focused
        if(nameStr.length() < 5)
        {
            // DRAW the key string with an additional carrot at the end
            font.drawString(greenFontTexture, nameStr + "_", 448, 344);
        }
        // OTHERWISE
        else
        {
            // DRAW the key string
            font.drawString(greenFontTexture, nameStr, 448, 344);
        }
        
        // REENABLE the depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * @see GameState#update(clusterfun.ui.window.GameWindow, int) 
     */
    @Override
    public void update(GameWindow window, long timeElapsed) throws Exception
    {      
        // IF new keyboard inputs have been added to the input queue
        if(Keyboard.next())
        {
            // RETRIEVE the character event
            char chr = Keyboard.getEventCharacter();
            int chkey = Keyboard.getEventKey();
            
            // IF the key pressed is alphanumeric
            if(Keyboard.isKeyDown(chkey) && Character.isLetterOrDigit(chr))
            {
                // IF one of the name inputs is focused and nothing is inputted yet
                if(nameStr.length() < 5)
                {
                    // ADD to the name input field
                    nameStr += chr;
                }
            }
            // OTHERWISE if the backspace was pressed
            else if(Keyboard.isKeyDown(Keyboard.KEY_BACK))
            {
                // IF a name input is focused and has some text
                if(nameStr.length() > 0)
                {
                    // REMOVE the last character
                    nameStr = nameStr.substring(0, nameStr.length() - 1);
                }
            }
        }
        // FOR every unique name that was 'touched' by the mouse click
        for(Integer name : window.getLastTouched())
        {
            if(name == exitButton.getName())
            {
                window.setCloseRequested(true);
            }
            // IF the current id clicked is the start game button
            if(name == nextIcon.getName())
            {
                putName(window);
                window.changeToState("SettingsMenuState");
            }
        }
        // RESET the lastTouched item list
        window.resetLastTouched();
    }

    /**
     * Places the name in the hall of fame
     *
     * @param window game window
     */
    public void putName(GameWindow window)
    {
        // SEND the player's name to the CFState to place in the hall of fame
        MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.PLAYER_NAME, nameStr),
                GameMessage.MessageDest.CF_STATE
                );
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
