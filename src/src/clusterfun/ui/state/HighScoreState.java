package clusterfun.ui.state;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.state.CFState;
import clusterfun.state.Score;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.window.GameWindow;
import java.util.ArrayList;
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

public class HighScoreState extends InGameState
{
        
    // Cancel Button
    private ButtonEntity backIcon;

    // settings menu button
    private ButtonEntity highScoreMenu;
    // settings menu button

    private ButtonEntity easyButton;
    private ButtonEntity easyButtonSelected;
    // settings menu button

    private ButtonEntity medButton;
    private ButtonEntity medButtonSelected;
    // settings menu button

    private ButtonEntity hardButton;
    private ButtonEntity hardButtonSelected;

    private ArrayList<Score> scores;

    private CFState.DifficultyType difficulty;

    /**
     * @see GameState#getName() 
     * @return name state name
     */
    @Override
    public String getName()
    {
        return "HighScoreState";
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
        backIcon  = new ButtonEntity("cancel button", new Vector2f(832, 144),
                new Vector2f(160, 32), buttonTexture);
        
        // INITIALIZE the settings menu background
        highScoreMenu = new ButtonEntity("menu", new Vector2f(0, 544),
            new Vector2f(640, 480), buttonTexture2);

        // INITIALIZE the easy button
        easyButton = new ButtonEntity("easy tab", new Vector2f(656, 16),
            new Vector2f(96, 32), buttonTexture2);

        // INITIALIZE the medium button
        medButton = new ButtonEntity("medium tab", new Vector2f(656, 64),
            new Vector2f(96, 32), buttonTexture2);

        // INITIALIZE the hard button
        hardButton = new ButtonEntity("hard tab", new Vector2f(656, 112),
            new Vector2f(96, 32), buttonTexture2);

        // INITIALIZE the easy button
        easyButtonSelected = new ButtonEntity("easy tab", new Vector2f(768, 16),
            new Vector2f(96, 32), buttonTexture2);

        // INITIALIZE the medium button
        medButtonSelected = new ButtonEntity("medium tab", new Vector2f(768, 64),
            new Vector2f(96, 32), buttonTexture2);

        // INITIALIZE the hard button
        hardButtonSelected = new ButtonEntity("hard tab", new Vector2f(768, 112),
            new Vector2f(96, 32), buttonTexture2);

        difficulty = CFState.DifficultyType.EASY;

        updateScores(window);

    }

    private void updateScores(GameWindow window) throws Exception
    {
        scores = window.getLogicState().getScores(difficulty);
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
        highScoreMenu.render();

        renderHighScores();
        
        // RENDER the cancel button
        backIcon.render();

        // SWITCH for every difficulty
        switch(difficulty)
        {
            // IF the difficulty is set to easy
            case EASY:
                easyButtonSelected.render();
                medButton.render();
                hardButton.render();
                break;
            // IF the difficulty is set to medium
            case MEDIUM:
                easyButton.render();
                medButtonSelected.render();
                hardButton.render();
                break;
            // IF the difficulty is set to hard
            case HARD:
                easyButton.render();
                medButton.render();
                hardButtonSelected.render();
                break;
            default: break;
        }
        
        
        // REENABLE the depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }


    private void renderHighScores()
    {
        // base horizontal coordinate
        final int baseX = 224;
        // base vertical coordinate
        final int baseY = 280;
        // the horizontal step per column
        final int xStep = 272;
        // the vertical step per row
        final int yStep = 32;
        // how many rows per column of names
        final int yCount = 5;
        // base score offset to name
        final int extScoreOff = 144;
        // extended score offset to name
        final int baseScoreOff = 144;
        // ten
        final int ten = 10;
        
        int index = 0;
        // FOR every high score in the high score list
        for(int bIndex = scores.size() - 1; bIndex >= 0; bIndex--)
        {
            int xCoord = baseX + ((index / yCount) * xStep);
            int yCoord = baseY + ((index % yCount) * yStep);
            Score score = scores.get(bIndex);
            font.drawString(greenFontTexture, score.getName(), xCoord, yCoord);

            // IF the score was less than ten, then add the extended score offset
            if(score.getScore() < ten)
            {
                xCoord += extScoreOff;
            }
            // OTHERWISE add the basic score offset
            else
            {
                xCoord += baseScoreOff;
            }
            font.drawString(yellowFontTexture, "" + score.getScore(), xCoord, yCoord);
            index++;
        }
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
        ArrayList<GameMessage> messages = MessageManager.getMessages(GameMessage.MessageDest.CF_UI);

        // FOR every unique name that was 'touched' by the mouse click
        for(Integer name : window.getLastTouched())
        {
            // IF the exit button was the 'touched' entity
            if(name == exitButton.getName())
            {
                window.setCloseRequested(true);
            }
            // IF the easy icon was the 'touched' entity
            if(name == easyButton.getName())
            {
                difficulty = CFState.DifficultyType.EASY;
            }
            // IF the medium button was the 'touched' entity
            if(name == medButton.getName())
            {
                difficulty = CFState.DifficultyType.MEDIUM;
            }
            // IF the hard button was the 'touched' entity
            if(name == hardButton.getName())
            {
                difficulty = CFState.DifficultyType.HARD;
            }
            // IF the current id clicked is the start game button
            if(name == backIcon.getName())
            {
                // CHANGE to the InGameState state
                window.changeToState("SettingsMenuState");
            }

            updateScores(window);
        }
        // RESET the lastTouched item list
        window.resetLastTouched();
    }
    
    /**
     * @see GameState#enter(clusterfun.ui.window.GameWindow)
     * @param window game window
     */
    @Override
    public void enter(GameWindow window)
    {
        
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
