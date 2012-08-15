package clusterfun.ui.state;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.state.CFState;
import clusterfun.ui.ThemeLoader;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.window.GameWindow;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    // beginner mode check button (grayed out icon)
    private ButtonEntity beginnerModeCheckGrayed;
    
    // whether or not the beginner mode is enabled
    private boolean beginnerMode = false;
    
    // timed mode check button
    private ButtonEntity timedModeCheck;
    
    // timed mode check button (blank icon)
    private ButtonEntity timedModeCheckBlank;
    
    // timed mode check button (grayed out icon)
    private ButtonEntity timedModeCheckGrayed;
    
    private ButtonEntity solitaireRadio;
    
    private ButtonEntity solitaireRadioBlank;
    
    // explain why option
    private ButtonEntity selectTheme;
    
    private ButtonEntity multiplayerRadio;
    
    private ButtonEntity multiplayerRadioBlank;
    
    private boolean solitaireMode = true;
    
    // whether or not timed mode is enabled
    private boolean timedMode = true;
        
    // Cancel Button
    private ButtonEntity cancelIcon;
    
    // Start Game Button
    private ButtonEntity startGameIcon;
    
    private ButtonEntity nextIcon;
    
    // settings menu button
    private ButtonEntity settingsMenu;

    private ButtonEntity highScores;

    private boolean gameInProgress = false;

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
     * @throws java.lang.Exception file load exception
     */
    @Override
    public void init(GameWindow window) throws Exception
    {
        // CALL the parent's init function
        super.init(window);

        // INITIALIZE the full screen icons
        solitaireRadio = new ButtonEntity("solitaire radio", new Vector2f(800, 48), 
            new Vector2f(32, 32), buttonTexture);
        solitaireRadioBlank = new ButtonEntity("solitaire radio", new Vector2f(752, 48), 
            new Vector2f(32, 32), buttonTexture);
        
        // INITIALIZE the beginner mode icons
        beginnerModeCheck = new ButtonEntity("beginner mode check", new Vector2f(704, 48), 
            new Vector2f(32, 32), buttonTexture);
        beginnerModeCheckBlank = new ButtonEntity("beginner mode check", new Vector2f(656, 48), 
            new Vector2f(32, 32), buttonTexture);
        beginnerModeCheckGrayed = new ButtonEntity("beginner mode check", new Vector2f(784, 0), 
            new Vector2f(32, 32), buttonTexture);
        
        // INITIALIZE the timed mode icons
        timedModeCheck = new ButtonEntity("timed mode check", new Vector2f(704, 48), 
            new Vector2f(32, 32), buttonTexture);
        timedModeCheckBlank = new ButtonEntity("timed mode check", new Vector2f(656, 48), 
            new Vector2f(32, 32), buttonTexture);
        timedModeCheckGrayed = new ButtonEntity("timed mode check", new Vector2f(784, 0), 
            new Vector2f(32, 32), buttonTexture);
        
        // INITIALIZE the multiplayer icons
        multiplayerRadio = new ButtonEntity("multiplayer radio", new Vector2f(800, 48), 
            new Vector2f(32, 32), buttonTexture);
        multiplayerRadioBlank = new ButtonEntity("multiplayer radio", new Vector2f(752, 48), 
            new Vector2f(32, 32), buttonTexture);
        
        // INITIALIZE the full screen icons
        fullScreenCheck = new ButtonEntity("fullscreen check", new Vector2f(704, 48), 
            new Vector2f(32, 32), buttonTexture);
        fullScreenCheckBlank = new ButtonEntity("fullscreen check", new Vector2f(656, 48), 
            new Vector2f(32, 32), buttonTexture);
        
        // INITIALIZE the cancel button
        cancelIcon  = new ButtonEntity("cancel button", new Vector2f(656, 96), 
                new Vector2f(160, 32), buttonTexture);
        
        // INITIALIZE the start game icon
        startGameIcon  = new ButtonEntity("start button", new Vector2f(656, 144), 
                new Vector2f(160, 32), buttonTexture);
        
        // INITIALIZE the start game icon
        nextIcon  = new ButtonEntity("next button", new Vector2f(832, 96), 
                new Vector2f(160, 32), buttonTexture);
        
        // INITIALIZE the settings menu background
        settingsMenu = new ButtonEntity("menu", new Vector2f(0, 0),
            new Vector2f(640, 480), buttonTexture);
        
        selectTheme = new ButtonEntity("select theme", new Vector2f(656, 0),
            new Vector2f(112, 32), buttonTexture);

        highScores = new ButtonEntity("high scores", new Vector2f(864, 240),
            new Vector2f(112, 32), buttonTexture);
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
        
                
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);


        
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

        // IF solitaire mode is enabled
        if(solitaireMode)
        {
            solitaireRadio.render();
            multiplayerRadioBlank.render();
            
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
        }
        else
        {
            
            solitaireRadioBlank.render();
            multiplayerRadio.render();
            timedModeCheckGrayed.render();
            beginnerModeCheckGrayed.render();
        }
        
        nextIcon.render();

        selectTheme.render();

        // IF the game is in progress
        if(gameInProgress)
        {
            // RENDER the cancel button
            cancelIcon.render();
        }

        highScores.render();
        
        // REENABLE the depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * @see GameState#update(clusterfun.ui.window.GameWindow, int)
     * @param window game window
     * @param timeElapsed time elapsed since last update
     * @throws java.lang.Exception exception
     */
    @Override
    public void update(GameWindow window, long timeElapsed) throws Exception
    {
        ArrayList<GameMessage> msgs = MessageManager.getMessages(GameMessage.MessageDest.CF_UI);
        // FOR every game message
        for(GameMessage msg : msgs)
        {
            // IF the game message is RESET UI
            if(msg.getType() == GameMessage.MessageType.RESET_UI)
            {
                // RESET the user interface
                window.setResetRequested(true);
            }
        }

        // FOR every unique name that was 'touched' by the mouse click
        for(Integer name : window.getLastTouched())
        {
            // IF the exit button is the 'touched' entity
            if(name == exitButton.getName())
            {
                window.setCloseRequested(true);
            }
            // IF the high scores icon is the 'touched' entity
            if(name == highScores.getName())
            {
                window.changeToState("HighScoreState");
            }
            // IF the select theme is the 'touched' entity
            if(name == selectTheme.getName())
            {
                window.hide();
                new ThemeLoader();
            }
            // IF the multiplayer radio is the 'touched' entity
            if(name == multiplayerRadioBlank.getName())
            {
                solitaireMode = false;
            }
            // IF the solitaire radio is the 'touched' entity
            if(name == solitaireRadioBlank.getName())
            {
                solitaireMode = true;
            }
            // IF the next icon is the 'touched' entity
            if(name == nextIcon.getName())
            {
                // IF solitaire mode is enabled
                if(solitaireMode)
                {
                    // GRAB the current InGameState
                    ChooseDifficultyState cds = (ChooseDifficultyState)window.getState("DifficultyMenuState");

                    // UPDATE the current config
                    cds.setConfigHash(generateHash());

                    // CHANGE to the InGameState state
                    window.changeToState("DifficultyMenuState");
                }
                // OTHERWISE
                else
                {
                    MultiplayerSettingsState state =
                            (MultiplayerSettingsState)window.getState("MultiplayerSettingsState");

                    state.setFullScreen(fullScreen);

                    window.changeToState("MultiplayerSettingsState");
                }
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
     * Generates a hashmap based on the current configuration state
     * 
     * @return hashmap configuration hashmap for CFState
     */
    private HashMap<String, Object> generateHash()
    {
        // INITIALIZE the configuration hash map
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        // IF beginner mode is enabled
        if(beginnerMode)
        {
            // SET the 'mode' hash key to beginner mode
            map.put("mode", CFState.ModeType.BEGINNER);
            // PLACE the timed mode boolean in the hash
            map.put("timer", timedMode);
        }
        // OTHERWISE IF timed mode is enabled
        else if(timedMode)
        {
            // SET the 'mode' hash key to solitaire mode
            map.put("mode", CFState.ModeType.TIMED);
        }
        else
        {
            // SET the 'mode' hash key to solitaire mode
            map.put("mode", CFState.ModeType.SOLITARE);
        }

        map.put("fullscreen", fullScreen);
        
        return map;
    }
    
    /**
     * @see GameState#enter(clusterfun.ui.window.GameWindow)
     * @param window game window
     */
    @Override
    public void enter(GameWindow window)
    {
        InGameState state = (InGameState)window.getState("InGameState");
        gameInProgress = state.isGameInProgress();
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
