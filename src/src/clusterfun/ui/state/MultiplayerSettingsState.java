package clusterfun.ui.state;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.player.HumanPlayer;
import clusterfun.state.CFState;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.window.GameWindow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
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

public class MultiplayerSettingsState extends InGameState
{
        
    // Cancel Button
    private ButtonEntity cancelIcon;
    
    private boolean fullscreen = false;
    
    // Start Game Button
    private ButtonEntity startGameIcon;
    
    // Start Game Button
    private ButtonEntity startGameIconGrayed;
    
    // settings menu button
    private ButtonEntity settingsMenu;
    
    // player name text fields
    private ButtonEntity[] names;
    
    // player key text fields
    private ButtonEntity[] keys;
    
    // player name strings
    private String[] nameStrs;
    
    // player key strings
    private String[] keyStrs;
    
    // currently focused name field
    private int nameFocus = -1;
    
    // currently focused key field
    private int keyFocus = -1;
    
    // whether or not the game is ready to begin
    private boolean isReady = false;

    private final int maxPlayers = 4;

    private final int maxPlayerNameLen = 5;

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
     * sets the fullscreen boolean
     * 
     * @param fs fullscreen boolean
     */
    public void setFullScreen(boolean fs)
    {
        this.fullscreen = fs;
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
        
        // INITIALIZE the cancel button
        cancelIcon  = new ButtonEntity("cancel button", new Vector2f(832, 144),
                new Vector2f(160, 32), buttonTexture);
        
        // INITIALIZE the start game icon
        startGameIcon  = new ButtonEntity("start button", new Vector2f(0.6406f, 0.14062f),
                new Vector2f(0.796875f, 0.171875f), 160, 32, buttonTexture);
        
        // INITIALIZE the settings menu background
        settingsMenu = new ButtonEntity("menu", new Vector2f(0, 0.53125f),
            new Vector2f(0.625f, 1f), 640, 480, buttonTexture);
        
        startGameIconGrayed = new ButtonEntity("start button", new Vector2f(864, 192), 
            new Vector2f(160, 32), buttonTexture);
        initPlayerInputs();
    }
    
    /**
     * Initialized player input fields
     * 
     * @throws java.io.IOException button creation exception
     */
    private void initPlayerInputs() throws IOException
    {
        // INITIALIZE name fields
        names = new ButtonEntity[maxPlayers];
        
        // INITIALIZE key fields
        keys = new ButtonEntity[maxPlayers];
        
        // INITIALIZE string arrays
        nameStrs = new String[maxPlayers];
        keyStrs = new String[maxPlayers];
        
        // FOR every name and key input field
        for(int index = 0; index < maxPlayers; index++)
        {
            // INITIALIZE the player name field
            names[index] = new ButtonEntity("player name " + index, 
                    new Vector2f(832, 0), new Vector2f(128, 32), buttonTexture);
            
            // INITIALIZE the player key field
            keys[index] = new ButtonEntity("player key " + index, 
                    new Vector2f(848, 48), new Vector2f(32, 32), buttonTexture);
            
            // INITIALIZE the key and name strings
            nameStrs[index] = "";
            keyStrs[index] = "";
        }
    }

    /**
     * @see GameState#render(clusterfun.ui.window.GameWindow, int)
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
        
        // SETUP the screen for 2d rendering        
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);

        
        // RENDER the blank icons (mostly for buttons)
        settingsMenu.render();

        // RENDER the cancel button
        cancelIcon.render();
        
        // IF the game is ready to start
        if(isReady)
        {
            // RENDER the start game button
            startGameIcon.render();
        }
        // OTHERWISE
        else
        {
            // RENDER the grayed out start button
            startGameIconGrayed.render();
        }
        
        // FOR every key and name input field
        for(int index = 0; index < maxPlayers; index++)
        {
            // RENDER the name input field
            names[index].render();
            
            // RENDEr the key input field
            keys[index].render();
            
            // IF the current name input field is focused
            if(nameFocus == index && nameStrs[index].length() < maxPlayerNameLen)
            {
                // DRAW the name string with an additional carrot at the end
                font.drawString(greenFontTexture, nameStrs[index] + "_", 352, 280 + (64 * index));
            }
            // OTHERWISE
            else
            {
                // DRAW the name string
               font.drawString(greenFontTexture, nameStrs[index], 352, 280 + (64 * index));
            }
            
            // IF the current key input field is focused
            if(keyFocus == index && keyStrs[index].length() < 1)
            {
                // DRAW the key string with an additional carrot at the end
                font.drawString(greenFontTexture, keyStrs[index] + "_", 528, 280 + (64 * index));
            }
            // OTHERWISE
            else
            {
                // DRAW the key string
                font.drawString(greenFontTexture, keyStrs[index], 528, 280 + (64 * index));
            }
        }
        
        // REENABLE the depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * @see GameState#update(clusterfun.ui.window.GameWindow, int)
     * 
     * @param window game window
     * @param timeElapsed time elapsed since last update
     * @throws java.lang.Exception exception
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
                // IF one of the key inputs is focused and nothing is inputted yet
                if(keyFocus >= 0 && keyStrs[keyFocus].length() < 1)
                {
                    // ADD to the key input field
                    keyStrs[keyFocus] += chr;
                }
                // IF one of the name inputs is focused and nothing is inputted yet
                if(nameFocus >= 0 && nameStrs[nameFocus].length() < maxPlayerNameLen)
                {
                    // ADD to the name input field
                    nameStrs[nameFocus] += chr;
                }
                
                // UPDATE whether or not the game is ready
                updateReady();
            }
            // OTHERWISE if the backspace was pressed
            else if(Keyboard.isKeyDown(Keyboard.KEY_BACK))
            {
               // IF a key input is focused and has some text
               if(keyFocus >= 0 && keyStrs[keyFocus].length() > 0)
                {
                   // REMOVE the last character
                    keyStrs[keyFocus] = keyStrs[keyFocus].substring(0, keyStrs[keyFocus].length() - 1);
                }
                // IF a name input is focused and has some text
                if(nameFocus >= 0 && nameStrs[nameFocus].length() > 0)
                {
                    // REMOVE the last character
                    nameStrs[nameFocus] = nameStrs[nameFocus].substring(0, nameStrs[nameFocus].length() - 1);
                }
                
                // UPDATE whether or not the game is ready to begin
                updateReady(); 
            }
        }
        // FOR every unique name that was 'touched' by the mouse click
        for(Integer name : window.getLastTouched())
        {
            // IF the exit button was the 'touched' entity
            if(name == exitButton.getName())
            {
                window.setCloseRequested(true);
            }
            // FOR every user name and user key icon
            for(int index = 0; index < maxPlayers; index++)
            {
                // IF the current player name icon has been pressed
                if(name == names[index].getName())
                {
                    // SET the current focused text area to the player's name
                    nameFocus = index;
                    keyFocus = -1;
                    nameStrs[index] = "";
                }
                // IF the current player key icon has been pressed
                if(name == keys[index].getName())
                {
                    // SET the current focused text area to the player's key
                    keyFocus = index;
                    nameFocus = -1;
                    keyStrs[index] = "";
                }
            }
            // IF the current id clicked is the start game button
            if(name == startGameIcon.getName() && isReady)
            {
                // UPDATE the window display
                updateDisplay(window);
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
                
                igs.setConfig(generateHash());
                
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
     * Generates a hashmap based on the current configuration state
     * 
     * @return hashmap configuration hashmap for CFState
     */
    private HashMap<String, Object> generateHash()
    {
        // INITIALIZE a new hashmap
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        // SET the timed mode to false
        map.put("timer", false);
        
        // SET the game mode to multiplayer
        map.put("mode", CFState.ModeType.MULTIPLAYER);
        
        // CREATE the players arraylist
        ArrayList<HumanPlayer> players = new ArrayList<HumanPlayer>();
        
        // FOR every possible player
        for(int index = 0; index < maxPlayers; index++)
        {
            // IF the player is created correctly
            if(!nameStrs[index].equals("") && !keyStrs[index].equals(""))
            {

                // CREATE new player object
                HumanPlayer player = new HumanPlayer(nameStrs[index], keyStrs[index].charAt(0));
                // ADD the player
                players.add(player);
            }
        }
        
        // PLACE the players inside the hashmap
        map.put("players", players);
        
        return map;
    }
    
    /**
     * updates whether or not the game is ready to begin, taking into account 
     * what inputs have been set in the menu
     */
    private void updateReady()
    {
        // SETS isReady to true by default
        isReady = true;
        
        // INITIALIZES the player count
        int count = 0;
        
        // FOR every potential player
        for(int index = 0; index < maxPlayers; index++)
        {
            // IF the player string is empty
            if(nameStrs[index].equals(""))
            {
                // CHECK to make sure the key string is empty
                isReady &= keyStrs[index].equals("");
            }
            // OTHERWISE
            else
            {
                // CHECK to make sure the key string is not empty
                isReady &= !keyStrs[index].equals("");
                
                // ADD to the count of set up players
                count++;
            }
        }
        
        // CHECK to make sure there is more than one player
        isReady &= (count > 0);
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
        
        boolean changeScreen = false;

        // IF the window is set to fullscreen
        if(window.isFullScreen() != fullscreen)
        {
            changeScreen = true;
            // CHANGE the fullscreen mode
            Display.setFullscreen(fullscreen);
            window.setFullScreen(fullscreen);
        }
        
        // RETRIEVE the current Bpp
        int currentBpp = Display.getDisplayMode().getBitsPerPixel();

        // IF the screen must be changed
        if(changeScreen || (window.getScreenHeight() != height) || (window.getScreenWidth() != width))
        {
            // GRAB an appropriate 640x480 window
            GameWindow.setDisplayMode(width, height, currentBpp);
        }
    }
    
    /**
     * @see GameState#enter(clusterfun.ui.window.GameWindow)
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
