
package clusterfun.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;

import clusterfun.logic.CFLogic;
import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.sound.CFSound;
import clusterfun.state.CFState;
import clusterfun.state.CFState.DeckType;
import clusterfun.ui.BasicUI;
import clusterfun.ui.GraphicUI;
import clusterfun.ui.TextUI;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * A CFApp is the application that initializes and runs the ClusterFun game.
 * It will handle any necessary command line arguments required by ClusterFun
 * and initialize the game with respect to them. Currently, the argument "-test"
 * will run the game with the text UI., the argument "-ns" will use an 
 * unshuffled deck, and "-nd" will use a predetermined unshuffled desk that can
 * test the deal 3 more option. Otherwise, it will run the graphical UI
 * by default
 *
 * @author Lisa Hunter
 * @version 2.0
 * @.date 2009-03-03
 */
//  Version History
//      12/2/08 Lisa Hunter added algorithm pseudocode
public class CFApp 
{

    /** The logic for the game */
    private CFLogic logic;
    
    /** The state of the game */
    private CFState state;
    
    /** The user interface for the game */
    private BasicUI ui;
    
    /** The sound player for the game */
    private CFSound sound;
    
    /** The minimum amount of update time in ms*/
    private static final long kMinTime = 1;
    
    /** The maximum amount of update time in ms*/
    private static final long kMaxTime = 9000000;
    
    /** The default amount of update time in ms*/
    private static final long kDefaultTime = 10;
    
    /** The config file to load preset display data */
    private static final String kConfigFile = "data/config.properties";
    
    /**
     * Constructs a default CFApp
     * 
     * @param ui the ui for the ClusterFun game
     * @param state the state for the ClusterFun game
     * @param logic the game logic for the ClusterFun game
     */
    public CFApp(BasicUI ui, CFState state, CFLogic logic)
    {
        // SET the user interface with the specified one
        this.ui = ui;
        // CREATE the game logic
        this.logic = logic;
        // CREATE the game state with the game logic
        this.state = state;
        // CREATE the sound player with the game logic
        this.sound = new CFSound();

        try 
        {
            // INITIALIZE the sound player
            sound.init();
        } 
        catch (LWJGLException e) 
        {
            // ALERT the user about the exception
            JOptionPane.showMessageDialog(null, 
                    "There was a problem loading the sound for ClusterFun.\n" 
                    + "Please make sure your hardware is compatibles with OpenAL.");
            sound = null;
            // PRINT the stack
            e.printStackTrace();
        }
    }
    
    /**
     * Parses the command-line arguments and handles them as required.
     * It then runs the program
     * 
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        // A flag for whether to continue or not if there is an error
        boolean error = false;
        
        // The desired user interface
        BasicUI ui = null;
        
        // The flag for which preference is requested
        DeckType pref = DeckType.SHUFFLE;
        
        // CREATE OpenGL display object
        Properties properties = new Properties();

        // LOAD the properties file
        try 
        {
            properties.load(new FileInputStream(kConfigFile));
        } 
        catch (FileNotFoundException e1) 
        {
            // TELL the user what the problem is and quit
            JOptionPane.showMessageDialog(null, 
                    "The properties configuration file is missing");
            error = true;
        } 
        catch (IOException e1) 
        {
            // TELL the user what the problem is and quit
            JOptionPane.showMessageDialog(null, 
                    "The properties configuration file could not be opened");
            error = true;
        }
        
        // PARSE the given width in the properties file
        String debugMode = properties.getProperty("debug");
        
        // FOR all of the arguments on the command line
        for(int iter = 0; iter < args.length; iter++)
        {
            // IF the debug mode is for no shuffling
            if(args[iter].equals("-ns"))
            {
                // SET the no-shuffling flag
                pref = DeckType.NOSHUFFLE;
            }
            // IF the debug mode is for deal 3 more configuration
            if(args[iter].equals("-nd"))
            {
                // SET the deal 3 more flag
                pref = DeckType.NOSET;
            }
            // IF the debug mode is for beginner mode
            if(args[iter].equals("-beg"))
            {
                // SET the deal 3 more flag
                pref = DeckType.BEGINNER;
            }
        }
        
        // IF the debug mode is for no shuffling
        if(debugMode.equals("ns"))
        {
            // SET the no-shuffling flag
            pref = DeckType.NOSHUFFLE;
        }
        // ELSE IF the debug mode is for deal 3 more configuration
        else if(debugMode.equals("nd"))
        {
            // SET the deal 3 more flag
            pref = DeckType.NOSET;
        }
        
        // CREATE the game logic
        CFLogic mainLogic = new CFLogic();
        // CREATE the game state with the game logic and preferences
        CFState mainState = new CFState(mainLogic, pref);
        
        
        // IF there are arguments and the user wishes to run the CF tests
        if(args.length > 0 && args[0].equals("-test"))
        {
            // CREATE a text-based ui
            ui = new TextUI(mainState);
        }
        else
        {
            try
            {
                // CREATE the graphical ui
                ui = new GraphicUI(mainState);
            }
            catch(Exception e)
            {
                // ALERT the user about the exception
                //System.out.println("Exception was found when creating GraphicUI");
                error = true;
                ui = null;
                // PRINT the stack
                e.printStackTrace();
            }
        }
        
        // IF there hasn't been a bad error
        if(!error && ui != null)
        {
            // CREATE the application with the user interface
            CFApp app = new CFApp(ui, mainState, mainLogic);
            // RUN the application
            app.run();
        }
        // ELSE IF the ui wasn't created
        else if(ui == null)
        {
            // TELL the user what the problem is and quit
            JOptionPane.showMessageDialog(null, 
                    "Clusterfun was unable to instantiate " 
                    +  "a display mode to run the application.\n" 
                    + "This may be because your graphics card does not meet " 
                    + "the minimum requirements,\n" 
                    + "or that your graphics driver is not installed correctly");
        }

    }
    
    /**
     * Initializes and runs the game ClusterFun. NOTE that if the user changes
     * the system clock during runtime it could possible cause problems to 
     * occur in-game
     */
    public void run()
    {
        // The amount of time lapsed between system clock calls in ms
        long timer = kMinTime;
        
        // The previous system time in ms
        long prevSys = System.currentTimeMillis();
        
        // The current system time in ms
        long currSys = System.currentTimeMillis();
        
        // Whether or not the game should end
        boolean gameEnd = false;
        
        //WHILE a message to end the game has not been received
        while(!gameEnd)
        {
            
            try 
            {
                // UPDATE the user interface
                ui.update(timer);
                
                // UPDATE the CFState
                state.update(timer);
                
                // IF the sound is working
                if(sound != null)
                {
                    // UPDATE the CFSound
                    sound.update(timer);
                }
            } 
            catch (Exception e) 
            {
                // ALERT the user about the exception
                JOptionPane.showMessageDialog(null, 
                        "Clusterfun was unable to update one of its components\n" 
                        + "correctly and will be shut down. Please contact us\n" 
                        + "at team@10ksoftware.com or check our website at\n" 
                        + "http://wiki.csc.calpoly.edu/10k for assistance.\n");
                // PRINT the stack
                e.printStackTrace();
                // STOP the program
                MessageManager.sendMessage(new GameMessage(
                        GameMessage.MessageType.PROGRAM_END, null), 
                        GameMessage.MessageDest.CF_APP);
                
            }
            // RECEIVE any messages to end the game
            gameEnd = isEndGame();
            // OBTAIN the current system time
            currSys = System.currentTimeMillis();
            // CALCULATE the new amount of time lapsed
            timer = currSys - prevSys;
            // SET the old system time to the new system time
            prevSys = currSys;
            // SANITIZE the new time of bugs
            timer = sanitizeTime(timer);
        }
        // END WHILE
        
    }
    
    
    /**
     * Checks to see if there are any messages to end the game
     * 
     * @return true if the game should end, false if not
     */
    private boolean isEndGame()
    {
        // Whether the game should end or not, initially set to false
        boolean endGame = false;
        
        // OBTAIN the list of messages for CFApp
        ArrayList<GameMessage> list = 
                MessageManager.getMessages(GameMessage.MessageDest.CF_APP);
        
        // FOR EACH message in the list
        for(GameMessage msg : list)
        {
            // IF the message is a message to end the program
            if(msg.getType().equals(GameMessage.MessageType.PROGRAM_END))
            {
                // SET the flag to true
                endGame = true;
            }
        }
       
        // RETURN whether to end the program or not
        return endGame;
    }
 
    /**
     * Changes the update time to the default amount of time in ms
     * if the time is under the allowed minimum or over the allowed
     * maximum
     * 
     * @param time the current time to update in ms
     * @return the appropriate amount of time
     */
    private long sanitizeTime(long time)
    {
        // IF the time is over or under the allowed amounts
        if(time < kMinTime || time > kMaxTime)
        {
            // SET the time to the default time
            time = kDefaultTime;
        }
        
        // RETURN the appropriate amount of time
        return time;
    }
}
