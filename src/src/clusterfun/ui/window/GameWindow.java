package clusterfun.ui.window;

import clusterfun.lwjgl.Texture;
import clusterfun.lwjgl.TextureLoader;
import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.state.CFState;
import clusterfun.ui.state.AddHighScoreState;
import clusterfun.ui.state.ChooseDifficultyState;
import clusterfun.ui.state.InGameState;
import clusterfun.ui.state.SettingsMenuState;
import clusterfun.ui.state.GameState;
import clusterfun.ui.state.HighScoreState;
import clusterfun.ui.state.LoadingState;
import clusterfun.ui.state.MultiplayerSettingsState;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

/**
 * GameWindow acts as a secondary level of abstraction, handling gamestates and 
 * creating the application window.  Graphics acceleration is enabled and the 
 * game begins in a default game state
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2008-12-1
 */
//  Version History 
//      12/2/08 Chris Gibson added algorithm pseudocode

public class GameWindow
{
    /** default length of milliseconds in a second */
    private final long kMILLISINSECS = 1000;
    
    /** default screen width */
    private final int kDEFAULTWIDTH = 640;
    
    /** default screen height */
    private final int kDEFAULTHEIGHT = 480;
    
    /** Default fullscreen setting */
    private final boolean kDEFAULTFULLSCREEN = false;
    
    /** a list of game states */
    private HashMap<String, GameState> gameStates = new HashMap<String, GameState>();

    /** the current gamestate to be rendered */
    private GameState currentState;

    /** config file to load preset display data */
    private String configFile = "data/config.properties";

    /** attached CFState instance used to grab player and score information */
    private CFState logicState;
    
    /** Currently acquired mouse position */
    private Vector2f mousePos = new Vector2f(0.0f, 0.0f);
    
    /** Last known click position */
    private Vector2f lastClick = new Vector2f(0.0f, 0.0f);
    
    /** Whether or not the mouse is currently clicked */
    private boolean mouseClicked = false;
    
    /** The last clicked entities */
    private ArrayList<Integer> lastTouched;
    
    /** Current frames per second */
    private int fps = 0;
    
    /** Current rendered frame count */
    private int frames = 0;
    
    /** Time until fps is recalculated */
    private long nextFps = kMILLISINSECS;
    
    /** Width of window */
    private int width = kDEFAULTWIDTH;
    
    /** Height of window */
    private int height = kDEFAULTHEIGHT;
    
    private final int kFRAMERATE = 60;

    private DisplayMode mode;
    
    /** Fullscreen option */
    private boolean fullScreen = kDEFAULTFULLSCREEN;
    
    private HashMap<String, Texture> textureMap;
    
    private TextureLoader loader = new TextureLoader();
    
    private Properties properties;

    private boolean closeRequested = false;

    private boolean isDisplayed = true;

    private boolean resetRequested = false;

    /**
     * Return if a reset is requested
     *
     * @return resetRequested resetRequested boolean
     */
    public boolean isResetRequested()
    {
        return resetRequested;
    }

    /**
     * Sets the reset requested boolean
     * @param reset reset requested boolean
     */
    public void setResetRequested(boolean reset)
    {
        resetRequested = reset;
    }

    /**
     * Sets if a close has been requested
     * @param request close requested boolean
     */
    public void setCloseRequested(boolean request)
    {
        closeRequested = request;
    }

    /**
     * Hides the window
     * 
     * @throws org.lwjgl.LWJGLException exception
     */
    public void hide() throws LWJGLException
    {
        isDisplayed = false;
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public void unHide() throws Exception
    {

        gameStates.clear();

        currentState = null;
        // CALL addState to add the settings_menu state
        addState(new LoadingState());

        // CALL addState to add the settings_menu state
        addState(new SettingsMenuState());

        // CALL addState to add the settings_menu state
        addState(new MultiplayerSettingsState());

        // CALL addState to add the in_game state
        addState(new InGameState());

        // CALL addState to add the high_score state
        addState(new HighScoreState());

        // CALL addState to add the choose_difficulty state
        addState(new ChooseDifficultyState());

        // CALL addState to add the add_high_score state
        addState(new AddHighScoreState());

        //ENTER the first state
        currentState.enter(this);
    }
    
    public Texture loadTexture(String texture) throws IOException
    {
        Texture tex = textureMap.get(texture);
        if(tex == null)
        {
            tex = loader.getTexture(texture);
        }
        return tex;
    }
    
    /**
     * Create a new instance of GameWindow
     * @param logicState CFState object
     * @throws java.lang.Exception file load exception
     */
    public GameWindow(CFState logicState) throws Exception
    {
        initWindow();

        this.logicState = logicState;

    }

    private void initWindow() throws Exception
    {
        gameStates.clear();
        currentState = null;

        textureMap = new HashMap<String, Texture>();
        lastTouched = new ArrayList<Integer>();
        // CREATE OpenGL display object
        properties = new Properties();

        // LOAD the properties file
        properties.load(new FileInputStream(configFile));

        // PARSE the given width in the properties file
        width = Integer.parseInt(properties.getProperty("width"));

        // PARSE the given height in the properties file
        height = Integer.parseInt(properties.getProperty("height"));

        // LOAD preset configuration file
        int currentBpp = Display.getDisplayMode().getBitsPerPixel();

        // INITIALIZE display mode given configuration file width, height and
        // bpp
        setDisplayMode(width, height, currentBpp);
        // SET display title
        Display.setTitle("Cluster Fun!");

        // SET fullscreen mode based on config file
        Display.setFullscreen(fullScreen);

        // CALL display's create function
        if(!Display.isCreated())
        {
            Display.create();
        }

        // CREATE the mouse input device
        if(Mouse.isCreated())
        {
            Mouse.create();
        }
    }
    
    
    public Object getProperty(String key)
    {
        return properties.get(key);
    }

    /**
     * Determines if an appropriate display mode can be acquired
     *
     * @param width the desired screen width
     * @param height the desired screen height
     * @param bpp the desired color depth
     * @return an appropriate display mode
     * @throws LWJGLException if there is a failure in the library
     */
    public static DisplayMode findDisplayModeX(int width, int height, int bpp)
            throws LWJGLException
    {
            // CALL getAvailableDisplayModes from Display and save them as
            // possible modes
            DisplayMode[] modes = Display.getAvailableDisplayModes();

            // CREATE a default mode
            DisplayMode mode = null;

            // FOR every mode in modes
            for (int i=0;i<modes.length;i++) {
                // IF the default mode is null or the current mode meets
                // the bpp requirement
                if ((modes[i].getBitsPerPixel() == bpp) || (mode == null))
                {
                    // IF the mode matches the required width and
                    // height
                    if ((modes[i].getWidth() == width) &&
                            (modes[i].getHeight() == height))
                    {
                        // SET the default mode to the current mode
                        mode = modes[i];
                    }
                }
            }

            // RETURN the mode that best fits the current display requirements
            return mode;
    }

    /**
     * Determines if an appropriate display mode can be acquired
     *
     * @param width the desired screen width
     * @param height the desired screen height
     * @param bpp the desired color depth
     * @return an appropriate display mode
     * @throws LWJGLException if there is a failure in the library
     */
    public static void setDisplayMode(int width, int height, int bpp)
            throws Exception {
        DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(width, height, -1, -1, -1, -1, -1, -1);

        org.lwjgl.util.Display.setDisplayMode(dm, new String[]{
                    "width=" + width,
                    "height=" + height,
                    "bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
                });
    }

    /**
     * Adds a new state to the list of possible renderable states
     * 
     * @param state the state to be added
     */
    public void addState(GameState state) throws Exception {
        // IF the current state is null
        if(currentState == null)
        {
            // SET the current state to the given state
            currentState = state;
            
            currentState.init(this);
        }

        // PLACE the game state into the game state hash
        gameStates.put(state.getName(), state);
    }

    /**
     * Initializes all 3d parameters and prepares all gamestates
     * 
     * @throws IOException exception
     */
    public void initialize() throws Exception {

        // CALL opengl bindings to enable GL_TEXTURE_2D
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        //GL11.glHint (GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);

        // CALL opengl bindings to enable GL_CULL_FACE
        GL11.glDisable(GL11.GL_CULL_FACE);
        
                // ENABLE lighting
        GL11.glDisable(GL11.GL_LIGHTING);

        // CALL opengl bindings to enable GL_DEPTH_TEST
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        // CALL opengl to set the matrix mode to GL_PROJECTION
        GL11.glMatrixMode(GL11.GL_PROJECTION);

        // CALL opengl's loadIdentity binding
        GL11.glLoadIdentity();
        
        // SET the opengl perspective to 45 degrees
        GLU.gluPerspective(45.0f, ((float) 800) / ((float) 600), 0.1f, 1000.0f);

        // CALL opengl's bindings to set the matrix mode to GL_MODELVIEW
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

        // CALL addState to add the settings_menu state
        addState(new LoadingState());
        
        // CALL addState to add the settings_menu state
        addState(new SettingsMenuState());
        
        // CALL addState to add the settings_menu state
        addState(new MultiplayerSettingsState());
        
        // CALL addState to add the in_game state
        addState(new InGameState());

        // CALL addState to add the high_score state
        addState(new HighScoreState());

        // CALL addState to add the choose_difficulty state
        addState(new ChooseDifficultyState());

        // CALL addState to add the add_high_score state
        addState(new AddHighScoreState());

        //ENTER the first state
        currentState.enter(this);
    }

    /**
     * Update current window contents and update the current render state
     * @param timeElapsed
     */
    public void update( long timeElapsed ) throws Exception
    {
        // CALL the update command for Display
        if(isDisplayed)
        {
            Display.update();
        }
        // INCREMENT frames rendered
        frames++;
        // UPDATE the time until the next 'frame per second' calculation
        nextFps -= timeElapsed;
        // IF the frame per second counter has gone below zero
        if(nextFps < 0)
        {
            // RESET the counter
            nextFps += kMILLISINSECS;
            // SET fps to the current rendered frame count
            fps = frames;
            // RESET the current rendered frame count
            frames = 0;
            //System.out.println("FPS: " + fps);
        }
        
        // CALL isCloseExpected by display
        // IF close is expected
        if (isDisplayed &&
                (Display.isCloseRequested() ||
                Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) ||
                closeRequested
                )
           )
        {
            // SEND an PROGRAM_END message to all modules.
            MessageManager.sendMessage(
                    new GameMessage(GameMessage.MessageType.PROGRAM_END, null),
                    GameMessage.MessageDest.CF_ALL);
            isDisplayed = false;
            Display.destroy();
        }
        // IF the display is actively being used
        else if(isDisplayed && Display.isActive())
        {
            // CALL updateInput to check for mouse clicks
            updateInput();

            // CALL the update method for the current state
            currentState.update(this, timeElapsed);

            if(isDisplayed)
            {
                // IF the mouse has been recently clicked
                if(mouseClicked)
                {
                    // CALL the pick render to find the click location
                    pickRender();

                    // EAT the lastClicked event
                    mouseClicked = false;
                }

                // CALL the render function for the current state and do not use picking
                render(false);
            }

        }
        // OTHERWISE if the window is not selected or hidden
        else if(isDisplayed)
        {
            // UPDATE the current state
            currentState.update(this, timeElapsed);
            
            // IF the display is visible or needs to be repainted
            if (isDisplayed && (Display.isVisible() || Display.isDirty()))
            {
                // RENDER the screen
                render(false);
            }
            // SLEEP for a short period so other programs can run
            Thread.sleep(200);
        }
        else
        {
            // UPDATE the current state
            currentState.update(this, timeElapsed);
        }

    }

    /**
     * Renders the glPicking mode to select the card or button under the cursor
     */
    private void pickRender() throws Exception
    {
        // hits
        int hits;
        
        // number of names to grab
        int numNames;
        
        // buffer index point
        int bufferPoint = 0; 
        
        // CREATE the select buffer
        IntBuffer selectBuffer = BufferUtils.createIntBuffer(64);

        // BIND select buffer to select mode
        GL11.glSelectBuffer(selectBuffer);

        // SET the current render mode to GL_SELECT
        GL11.glRenderMode(GL11.GL_SELECT);

        // CALL the render function for the current state and use picking
        render(true);

        // FLUSH the pipeline
        GL11.glFlush();

        // SET the current render mode back to GL_RENDER
        hits = GL11.glRenderMode(GL11.GL_RENDER);

        // REINSTANTIATE lastTouched array
        lastTouched = new ArrayList<Integer>();


        // FOR the amount of hits received
        for(int j = 0; j < hits; j++)
        {
            // GRAB the number of touched entities by grabbing the first item in the buffer
            numNames = selectBuffer.get(bufferPoint++);

            // DIGEST next two data points
            bufferPoint += 2;

            // WHILE the number of names is greater than zero
            while(numNames-- > 0)
            {
                // ADD the touched entity's name id
                lastTouched.add(selectBuffer.get(bufferPoint++));
            }
        }
    }
    
    /**
     * Changes the current rendered state
     * 
     * @param name the state to change to
     */
    public void changeToState(String name) throws Exception {
        // GRAB the state from the hash table of states
        GameState state = (GameState) gameStates.get(name);
        // IF the state does exist
        if(state != null)
        {
            // CALL the leave function for the current state
            currentState.leave(this);
            // SET the current state to the new state
            currentState = state;
            if(!currentState.isInitialized())
            {
                currentState.init(this);
            }
            // CALL the enter function for the current state
            currentState.enter(this);
        }
    }
    
    
    public ArrayList<Integer> getLastTouched()
    {
        return lastTouched;
    }
    
    public void resetLastTouched()
    {
        lastTouched.clear();
    }
    
    /**
     * Takes input information from the window and checks to see if the user 
     * has made any legal inputs, handling each input respective to the context
     * (including only what is to be displayed currently)
     */
    public void updateInput()
    {
        if(isDisplayed)
        {
            // CHECK every mouse event that has been buffered since last update
            while(Mouse.next())
            {
                // MODIFY clicked if the mouse button has been pressed
                mouseClicked |= (Mouse.getEventButton() == 0 && Mouse.getEventButtonState());
            }

            // IF the mouse has been clicked
            if(mouseClicked)
            {
                // UPDATE the mouse coordinates
                mousePos.x = Mouse.getX();
                mousePos.y = Mouse.getY();

                // UPDATE the last clicked position
                lastClick = new Vector2f(Mouse.getX(), Mouse.getY());
            }
        }
    }

    /**
     * Renders the current state
     */
    public void render(boolean picking) throws Exception
    {
        /* CALL the glClear function to clear the current screen and depth
           buffers */
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // CALL opengl to set the matrix mode to GL_PROJECTION
        GL11.glMatrixMode(GL11.GL_PROJECTION);

        // CALL opengl's loadIdentity binding
        GL11.glLoadIdentity();
        
        if(picking)
        {
            IntBuffer viewport = BufferUtils.createIntBuffer(16);
            GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
            GLU.gluPickMatrix((float)lastClick.getX(),
                (float)(lastClick.getY()),
                0.5f, 0.5f, viewport);
            
            GL11.glInitNames();
            GL11.glPushName(0);
        }
        
        // SET the opengl perspective to 45 degrees
        //GLU.gluPerspective(45.0f, ((float) 800) / ((float) 600), 0.1f, 1000.0f);

        // CALL opengl's bindings to set the matrix mode to GL_MODELVIEW
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        // PUSH the current world matrix to save the matrix data
        GL11.glPushMatrix();
        
        // CALL render
        currentState.render(this, picking);
        
        //POP the matrix out of the stack
        GL11.glPopMatrix();
        
        Display.sync(kFRAMERATE);
    }
    
    public Vector2f getCurMousePos()
    {
        return mousePos;
    }
    
    /**
     * Returns the currently calculated frames per second
     * 
     * @return fps frames per second
     */
    public float getFPS()
    {
        return fps;
    }
    
    public CFState getLogicState()
    {
        return logicState;
    }
    
    public GameState getCurrentState()
    {
        return currentState;
    }
    
    public GameState getState(String state)
    {
        return (GameState)gameStates.get(state);
    }
    
    public boolean isFullScreen()
    {
        return fullScreen;
    }
    
    public void setFullScreen(boolean fullScreen)
    {
        this.fullScreen = fullScreen;
    }
    
    public int getScreenWidth()
    {
        return width;
    }
    
    public int getScreenHeight()
    {
        return height;
    }

}
