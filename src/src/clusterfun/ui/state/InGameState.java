package clusterfun.ui.state;

import clusterfun.board.Card;
import clusterfun.lwjgl.BitmapFont;
import clusterfun.lwjgl.Texture;
import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.player.AbstractPlayer;
import clusterfun.player.HumanPlayer;
import clusterfun.state.CFState;
import clusterfun.ui.entity.BoardEntity;
import clusterfun.ui.entity.ButtonEntity;
import clusterfun.ui.entity.CardEntity;
import clusterfun.ui.entity.NumberEntity;
import clusterfun.ui.image.CardImageCoord;
import clusterfun.ui.image.CardOutOfBoundsException;
import clusterfun.ui.window.GameWindow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

/**
 * InGameState acts as the game state for every currently running game.  The 
 * cards on the game board, along with all of the user interface graphics, are 
 * rendered to the screen
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2008-12-3
 */
//   Version History 
//      12/2/08 Chris Gibson added algorithm pseudocode

public class InGameState implements GameState
{

    // Cards on the game board
    private HashMap<String, CardEntity> cards;
    
    // Selected cards on the game board
    private HashMap<String, CardEntity> selected;
    
    // Cards that will be soon removed
    private ArrayList<CardEntity> tmpCards;

    // Visual game board entity
    private BoardEntity board;
    
    // Visual timer entity
    private NumberEntity timer;
    
    // Visual points entity
    private NumberEntity score;
    
    // Deal 3 button
    private ButtonEntity dealThree;
    
    // Deal 3 button Grayed
    private ButtonEntity dealThreeGrayed;

    // Deal 3 failed icon
    private ButtonEntity dealThreeFailed;

    // Give Hint button
    private ButtonEntity giveHint;
    
    // Give Hint button Grayed
    private ButtonEntity giveHintGrayed;

    // Score icon
    private ButtonEntity scoreIcon;
    
    private ButtonEntity gameOverIcon;
    
    // icon to open the menu
    private ButtonEntity menuIcon;

    // valid cluster icon
    private ButtonEntity validCluster;

    // invalid cluster icon
    private ButtonEntity invalidCluster;

    // explain why option
    private ButtonEntity explainWhy;

    // explain why option
    protected ButtonEntity exitButton;
    
    // selected card indication spot #1
    private CardEntity selectedCardSpot1;
    
    // selected card indication spot #2
    private CardEntity selectedCardSpot2;
    
    // selected card indication spot #3
    private CardEntity selectedCardSpot3;

    private ButtonEntity explainWhyBg;
    
    // The texture sets for cards
    private Texture[] cardTextureSets;
    
    // Theme base when loading theme pictures
    private final String kTHEMEBASE = "data/themes/";
    
    // Current theme folder
    private String curTheme = "defaultTheme/";
    
    // If a valid cluster has been called
    private boolean hasValidCluster = false;
    
    // If an invalid cluster has been called
    private boolean hasInvalidCluster = false;

    // Every column and row coordinate for every card
    private int[] posListRow = new int[]{1,1,1,1,2,2,2,2,3,3,3,3,1,2,3,1,2,3,4,4,4};
    private int[] posListCol = new int[]{1,2,3,4,1,2,3,4,1,2,3,4,5,5,5,6,6,6,1,2,3};
    
    // the cards ready to deal
    private ArrayList<Card> cardsToDeal = null;
    
    /* Animation buffer, which is in place to allow animations to wait before
     * previous animations finish before running. */
    private long animBuffer = 0;
    
    // milliseconds in seconds
    private final int kMILLISINSECS = 1000; 
    
    // seconds in a minute
    private final int kSECSINMIN = 60; 
    
    // ten seconds
    private final int kTENSECS = 10; 
    
    // time for each card to fall onto the deck
    private final int kDEALCARDTIME = 1000;
    
    // delay time for each dealt card
    private final int kDEALCARDDELAY = 500;
    
    // time until the ui is finished dealing
    private long dealTime = 0;
    
    // running flag
    private boolean running = false;
    
    // config hash used for building new game configurations
    private HashMap<String, Object> configHash = new HashMap<String, Object>();
    
    // flag indicating timed mode or not
    private boolean timedMode = true;
    
    // flag indicating whether or not a hitn has been given
    private boolean hasGivenHint = false;
    
    // explain why display time
    private long explainWhyTime = 0;
    
    // LOAD the default blank icon
    protected Texture buttonTexture;

    // LOAD the default blank icon
    protected Texture buttonTexture2;

    // LOAD the default blank icon
    protected Texture buttonTexture3;
    // last 'explain why' message
    private String explainWhyMessage = "";
    
    private long gameOverTime = 0;
    
    private boolean isGameOver;
    
    private boolean initialized = false;

    private boolean dealThreeAllowed = true;

    private long displayDealFailTime = 0;

    private long validClusterTime = 0;

    private long invalidClusterTime = 0;

    protected BitmapFont font;
    
    protected Texture greenFontTexture;

    protected Texture yellowFontTexture;

    protected Texture redFontTexture;
    
    protected CFState.ModeType gameMode;

    private boolean nameRequested = false;

    private boolean gameInProgress = false;

    private boolean outOfTime = false;

    private boolean considerResetButtons = false;

    private final int defaultGameOverTime = 4000;
    private final int defaultMessageDisplayTime = 1500;

    private boolean displayExplainWhy = false;

    private int cardLockTime = 0;
    /**
     * Returns if the game is in progress
     * 
     * @return gameInProgress gameInProgress boolean
     */
    public boolean isGameInProgress()
    {
        return gameInProgress;
    }

    /**
     * Resets the current in game initialization
     */
    public void reset()
    {
        initialized = false;
    }

    /**
     * @see GameState#getName()
     * @return name string name
     */
    public String getName()
    {
        return "InGameState";
    }

    /**
     * @see GameState#init(clusterfun.ui.window.GameWindow)
     * @param window game window 
     * @throws java.lang.Exception file load exception
     */
    public void init(GameWindow window) throws Exception
    {
        // SCALAR definition
        final float scoreScalar = 0.25f;

        // DEFAULT font size
        final int fontSize = 32;

        // LOAD the current theme
        loadTheme(window);

        // INITIALIZE the visual board entity
        board = new BoardEntity(window.loadTexture(kTHEMEBASE + curTheme + "background/board.jpg"));
        
        // INITIALIZE the timer entity
        timer = new NumberEntity("timer", window.loadTexture(kTHEMEBASE + curTheme + "timer.png"));
        
        // LOAD all images required for the game
        loadImages(window);
        
        // INITIALIZE score entity
        score = new NumberEntity("score", window.loadTexture(kTHEMEBASE + curTheme + "timer.png"), scoreScalar);

        exitButton = new ButtonEntity("exit icon", new Vector2f(128, 496),
                new Vector2f(32, 32), buttonTexture);

        // INITIALIZE the score text
        scoreIcon = new ButtonEntity("score icon", new Vector2f(0, 496), 
                new Vector2f(112, 32), buttonTexture);
     
        gameOverIcon = new ButtonEntity("please wait", new Vector2f(0,0), 
                new Vector2f(512, 512), window.loadTexture(kTHEMEBASE + curTheme + "background/gameOver.png"));
        
        // INITIALIZE the deal three button
        dealThree = new ButtonEntity("deal three", new Vector2f(656, 304), 
                new Vector2f(192, 96), buttonTexture);

        dealThreeFailed = new ButtonEntity("please wait", new Vector2f(0, 0),
                new Vector2f(512, 512), window.loadTexture(kTHEMEBASE + curTheme + "background/cannotDeal.png"));

        validCluster = new ButtonEntity("please wait", new Vector2f(0, 0),
                new Vector2f(512, 512), window.loadTexture(kTHEMEBASE + curTheme + "background/validCluster.png"));

        invalidCluster = new ButtonEntity("please wait", new Vector2f(0, 0),
                new Vector2f(512, 512), window.loadTexture(kTHEMEBASE + curTheme + "background/invalidCluster.png"));

        // INITIALIZE the deal three button
        explainWhy = new ButtonEntity("explain why", new Vector2f(656, 416), 
                new Vector2f(192, 48), buttonTexture);

        // INITIALIZE the give hint button
        giveHint = new ButtonEntity("give hint", new Vector2f(656, 480), 
                new Vector2f(192, 96), buttonTexture);
        
        giveHintGrayed = new ButtonEntity("give hint", new Vector2f(656, 768),
                new Vector2f(192,96), buttonTexture);

        dealThreeGrayed = new ButtonEntity("deal three", new Vector2f(656, 656),
                new Vector2f(192, 96), buttonTexture);

        // INITIALIZE the menu button
        menuIcon = new ButtonEntity("menu icon", new Vector2f(656, 592), 
            new Vector2f(176, 48), buttonTexture);
        
        initializeSelectedSpots(window);

        explainWhyBg = new ButtonEntity("explain why message", new Vector2f(0, 496),
                new Vector2f(544, 224), buttonTexture3);
        
        // INITIALIZE list of cards
        cards = new HashMap<String, CardEntity>();
        
        // INITIALIZE list of selected cards
        selected = new HashMap<String, CardEntity>();
        
        // INITIALIZE temporary card list
        tmpCards = new ArrayList<CardEntity>();
        
        // LOAD the default font
        loadFont();        

        font = new BitmapFont(greenFontTexture, fontSize, fontSize);
        
        initialized = true;
    }

    /**
     * Initialize the selected card spots on the game board
     *
     * @param window
     * @throws java.io.IOException
     */
    private void initializeSelectedSpots(GameWindow window) throws IOException
    {
        // LOAD select card texture
        Texture selectSpot = window.loadTexture(kTHEMEBASE + curTheme + "cards/select_card_spot.png");
        
        // INITIALIZE the first selected card spot
        selectedCardSpot1 = new CardEntity(null, selectSpot, new Vector2f(0,0),
            new Vector2f(0.6563f,0.9844f), "selected 1 spot");
        selectedCardSpot1.setStartPosition("selected 1 spot");
        selectedCardSpot1.setEndPosition("selected 1 spot");
        selectedCardSpot1.setName(26);

        // INITIALIZE the second selected card spot
        selectedCardSpot2 = new CardEntity(null, selectSpot, new Vector2f(0,0),
            new Vector2f(0.6563f,0.9844f), "selected 2 spot");
        selectedCardSpot2.setStartPosition("selected 2 spot");
        selectedCardSpot2.setEndPosition("selected 2 spot");
        selectedCardSpot2.setName(26);

        // INITIALIZE the third selected card spot
        selectedCardSpot3 = new CardEntity(null, selectSpot, new Vector2f(0,0),
            new Vector2f(0.6563f,0.9844f), "selected 3 spot");
        selectedCardSpot3.setStartPosition("selected 3 spot");
        selectedCardSpot3.setEndPosition("selected 3 spot");
        selectedCardSpot3.setName(26);
    }

    /**
     * Loads all of the images required for the game
     *
     * @param window game window
     * @throws java.io.IOException exception
     */
    private void loadImages(GameWindow window) throws IOException
    {
        greenFontTexture = window.loadTexture("data/themes/font_green.png");

        yellowFontTexture = window.loadTexture("data/themes/font_yellow.png");

        redFontTexture = window.loadTexture("data/themes/font_red.png");

        buttonTexture = window.loadTexture(kTHEMEBASE + curTheme + "buttons/buttons.png");

        buttonTexture2 = window.loadTexture(kTHEMEBASE + curTheme + "buttons/buttons2.png");

        buttonTexture3 = window.loadTexture(kTHEMEBASE + curTheme + "buttons/buttons3.png");

    }
    

    /**
     * Returns whether or not the state is initialized
     *
     * @return boolean
     */
    public boolean isInitialized()
    {
        return initialized;
    }
    
    /**
     * Updates the current game configuration hash for the CFState
     * 
     * @param map configuration hash map
     */
    public void setConfig(HashMap<String, Object> map)
    {
        configHash = map;
        gameMode = (CFState.ModeType)map.get("mode");
    }

    /**
     * @see GameState#render(clusterfun.ui.window.GameWindow, int)
     * @param window game window
     * @param picking render picking
     * @throws java.lang.Exception exception
     */
    public void render(GameWindow window, boolean picking) throws Exception
    {
        final float width = 800;
        final float height = 600;
        final float nearField = 0.1f;
        final float farField = 1000f;
        final float fieldOfView = 45;

        // CLEAR the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

        GL11.glLoadIdentity();
        
        // SET the opengl perspective to 45 degrees
        GLU.gluPerspective(fieldOfView, width / height, nearField, farField);
        
        GL11.glPushMatrix();
        
        setupLookat();

        renderBoard(picking);
        // ENABLE blending
        GL11.glEnable(GL11.GL_BLEND);
        
        // SET blending type
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        renderCards(picking);
        
        // RENDER the first selected card indicator
        selectedCardSpot1.render();
        
        // RENDER the second selected card indicator
        selectedCardSpot2.render();
        
        // RENDER the third selected card indicator
        selectedCardSpot3.render();
        
        renderTempCards();

        // POP the current matrix
        GL11.glPopMatrix();

        // DISABLE the depth testing for all text and font output
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);

        renderTimer();
        
        renderMenuIcon();

        renderHint(window);
        
        // IF the explain why time is still applicable
        if(explainWhyTime > 0)
        {
            // RENDER the explain why button
            explainWhy.render();
        }

        // IF the game is over
        if(isGameOver)
        {
            gameOverIcon.render();
        }

        renderSingleScore(window);

        exitButton.render();
        
        renderDealThree();

        // IF the valid cluster image is still being displayed
        if(validClusterTime > 0)
        {
            validCluster.render();
        }

        // IF the invalid cluster image is still being displayed
        if(invalidClusterTime > 0)
        {
            invalidCluster.render();
        }

        renderExplainWhyButton();
        
        // REENABLE the depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);

    }

    /**
     * Renders the current running timer
     */
    private void renderTimer()
    {
        // RENDER the timer
        if(timedMode)
        {
            timer.render();
        }
    }

    /**
     * Setup the lookat vector for the 3d camera
     */
    private void setupLookat()
    {
        final float cameraX = 0;
        final float cameraY = -13.5f;
        final float cameraZ = 35.0f;

        final float cameraLookatX = 0;
        final float cameraLookatY = -3.5f;
        final float cameraLookatZ = 0;
        // SET the camera back in order to view the objects in the scene
            // SET the current camera position to (0, -10, 25)
            // SET the current lookat position to (0,0,0)
            // SET the current 'up' camera position to (0,0,1)
        GLU.gluLookAt(
                cameraX, cameraY, cameraZ,
                cameraLookatX, cameraLookatY, cameraLookatZ,
                0.0f, 0.0f, 1.0f
                );
        
    }

    /**
     * Render all temporary cards that will be taken from the board soon enough
     */
    private void renderTempCards()
    {
        // FOR every card in the temporary card list
        for(CardEntity card : tmpCards)
        {
                GL11.glPushMatrix();

                // RENDER the card entity
                card.render();
                GL11.glPopMatrix();
        }
    }

    /**
     * Render the explain why button
     */
    private void renderExplainWhyButton()
    {
        // IF the explain why message is being displayed
        if(displayExplainWhy)
        {
            renderExplainWhyMessage();
        }
    }

    /**
     * Render the menu button
     */
    private void renderMenuIcon()
    {
        // IF we are really in the InGameState
        if(getName().equals("InGameState"))
        {
            // RENDER the menu icon
            menuIcon.render();
        }
    }

    /**
     * Render the deal three button
     */
    private void renderDealThree()
    {
        final int maxDealThreeCards = 18;
        // IF there are not 21 cards on the board, it is safe to show the deal button
        if(cards.size() <= maxDealThreeCards)
        {
            // IF we are allowed to display a deal three button
            if(dealThreeAllowed)
            {
                // RENDER the deal three button
                dealThree.render();
            }
            else
            {
                dealThreeGrayed.render();

                // IF the failed deal image is still being displayed
                if(displayDealFailTime > 0)
                {
                    dealThreeFailed.render();
                }
            }
        }
    }

    /**
     * Render all of the cards on the board
     * @param picking pick rendering
     */
    private void renderCards(boolean picking)
    {
        // FOR every card in the game board
        for(CardEntity card : cards.values())
        {
            //GL11.glPushMatrix();

            // RENDER the card entity
            if(!displayExplainWhy || selected.containsKey(card.getDefaultCardPosition()))
            {
                card.render(picking);
            }
            //GL11.glPopMatrix();
        }
    }

    /**
     * Render the current game board
     * @param picking pick rendering
     */
    private void renderBoard(boolean picking)
    {
        // IF we are not picking
        if(!picking)
        {
            // RENDER the game board
            board.render();
        }
    }

    /**
     * Render the single player score
     * 
     * @param window game window
     */
    private void renderSingleScore(GameWindow window)
    {
        // IF the game is in progress and the mode is multiplayer
        if(gameInProgress && (gameMode == CFState.ModeType.MULTIPLAYER))
        {
            drawScore(window.getLogicState().getPlayers());
        }
        // OThERWISE
        else
        {
            // RENDER the point counter
            score.render();

            // RENDER the score icon
            scoreIcon.render();
        }
    }

    /**
     * Render the hint button
     * 
     * @param window game window
     */
    private void renderHint(GameWindow window)
    {
        // IF the game mode is not multiplayer
        if(window.getLogicState() != null && window.getLogicState().getGameMode() != CFState.ModeType.MULTIPLAYER)
        {
            // IF we havent given a hint yet and if the difficulty is not hard
            if(!hasGivenHint && window.getLogicState().getDifficulty() != CFState.DifficultyType.HARD)
            {
                // RENDER the hint button
                giveHint.render();
            }
            // OTHERWISE
            else
            {
                // RENDER the grayed out hint button
                giveHintGrayed.render();
            }
        }
    }
    
    /**
     * Draw the multiplayer scores
     *
     * @param players game players
     */
    private void drawScore(ArrayList<AbstractPlayer> players)
    {
        final int maxPlayerNameLen = 5;
        // X offset for the score list
        final int baseXOffset = 16;
        int xOffset = baseXOffset;
        final int threeDigits = 3;
        
        // Y offset for the score list
        final int baseYoffset = 560;
        int yOffset = baseYoffset;
        
        // Temporary string for printing names and scores
        String tmpString = "";
        
        // FOR every player
        for(AbstractPlayer abPlayer : players)
        {
            HumanPlayer player = (HumanPlayer)abPlayer;
            // SET the temporary string to the player's name
            tmpString = player.getName();
            
            // FOR every character from the string length to 5
            for(int length = tmpString.length(); length < maxPlayerNameLen; length++)
            {
                // ADD a padding space character to the string
                tmpString = " " + tmpString;
            }
            
            // UPDATE the x offset after drawing the player's name
            xOffset += font.drawString(greenFontTexture, tmpString + "-", xOffset, yOffset);

            xOffset += font.drawString(redFontTexture, player.getKey() + "", xOffset, yOffset);

            xOffset += font.drawString(greenFontTexture, ":", xOffset, yOffset);

            // SET the temporary string to the player's score
            tmpString = "" + player.getScore();

            // IF the string length is 1
            if(tmpString.length() == 1)
            {
                tmpString += "   ";
            }
            // IF the string length is 2
            else if(tmpString.length() == 2)
            {
                tmpString += "  ";
            }
            // IF the string length is 3
            else if(tmpString.length() == threeDigits)
            {
                tmpString += " ";
            }
            
            // UPDATE the x offset after drawing the player's score
            xOffset += font.drawString(yellowFontTexture, tmpString, xOffset, yOffset);
        }
    }
    
    /**
     * Loads the given font for text output
     * 
     * @throws java.io.IOException load the given font
     */
    private void loadFont() throws IOException
    {
    }

    /**
     * Render the current explain why message
     */
    private void renderExplainWhyMessage()
    {
        // explain why location
        final int explainWhyMsgX = 144, explainWhyMsgY = 236;

        // DRAW the explain why background
        explainWhyBg.render();
        // DRAW the explain why message
        font.drawString(yellowFontTexture, explainWhyMessage, explainWhyMsgX, explainWhyMsgY);
    }

    /**
     * @see GameState#update(clusterfun.ui.window.GameWindow, int)
     * @param window game window object
     * @param timeElapsed elapsed time
     * @throws java.lang.Exception exception
     */
    public void update(GameWindow window, long timeElapsed) throws Exception
    {
        manageTimers(timeElapsed);

        // IF the game mode is multiplayer
        if(gameMode == CFState.ModeType.MULTIPLAYER)
        {
            updateInput(window, window.getLogicState().getPlayers());
        }
        // RECEIVE all messages for the UI
        ArrayList<GameMessage> messages = MessageManager.getMessages(GameMessage.MessageDest.CF_UI);


        // FOR every message received
        for(GameMessage message : messages)
        {
            manageMessage(message);
        }

        updateCards(timeElapsed);
        
        handleInput(window);
        
        // RESET the lastTouched item list
        window.resetLastTouched();
        
        // UPDATE the timer image
        setTimeRemaining(window.getLogicState().getTimeRemaining());
        
        // IF the active player is created
        if(window.getLogicState().getActivePlayer() != null)
        {
            // SET the current score to the player's score
            score.setString(window.getLogicState().getActivePlayer().getScore() + "");
        }

        // UPDATE the selected spot 1 indicator
        selectedCardSpot1.update(timeElapsed);
        
        // UPDATE the selected spot 2 indicator
        selectedCardSpot2.update(timeElapsed);
        
        // UPDATE the selected spot 3 indicator
        selectedCardSpot3.update(timeElapsed);
        
        // MANAGE the selected cards
        manageSelected();
        
        updateGameOver(window, timeElapsed);

        // IF the player ran out of time during his turn
        if(outOfTime && (window.getLogicState().getGameMode() == CFState.ModeType.MULTIPLAYER))
        {
            deselectCards();
            outOfTime = false;
        }
    }

    private void deselectCards()
    {
        // DESELECT first selected card
        deselectCard(selected.get("selected 3"), 0, true);

        // DESELECT second selected card
        deselectCard(selected.get("selected 2"), 1, true);

        // DESELECT third selected card
        deselectCard(selected.get("selected 1"), 2, true);

        selected.clear();
    }

    /**
     * Manages a given game message and acts accordingly
     * 
     * @param message game message
     * @throws java.lang.Exception exception
     */
    private void manageMessage(GameMessage message) throws Exception
    {
        // IF the message is DEAL_CARDS
        switch(message.getType())
        {
            // IF the message is DEAL_CARDS
            case DEAL_CARDS: handleDealCards(message); break;
            // IF the message is VALID_CLUSTER
            case VALID_CLUSTER: handleValidCluster(); break;
            // IF the message is INVALID_CLUSTER
            case INVALID_CLUSTER: handleInvalidCluster(message); break;
            case OUT_OF_TIME: outOfTime = true; break;
            // IF the message is GAME_START
            case GAME_START:  handleGameStart(); break;
            case DEAL_FAIL: dealFail(); break;
            // IF the message is GAME_END
            case GAME_END: handleGameEnd(message); break;
            // IF the message is VALID_CLUSTER
            case SELECT_CLUSTER:
                // CALL select cluster with the given cards
                selectCluster((ArrayList<Card>)message.getValue());
                considerResetButtons = true;
            break;
            case GIVE_HINT:
                // CALL give hint with the given card(s)
                giveHint((ArrayList<Card>)message.getValue());
            break;
            case AFFIRM_HIGH_SCORE: nameRequested = true; break;
            // DEFAULT
            default: break;
        }
    }

    /**
     * Update the game over state
     * @param window game window
     * @param timeElapsed time elapsed since last update
     * @throws java.lang.Exception exception
     */
    private void updateGameOver(GameWindow window, long timeElapsed) throws Exception
    {
        // IF the game is over
        if(isGameOver)
        {
            // IF the game over time is eaten up
            if(gameOverTime <= 0)
            {
                // if the game mode is set to timed
                if(window.getLogicState().getGameMode() == CFState.ModeType.TIMED)
                {
                    // IF the players name is requested
                    if(nameRequested)
                    {
                        window.changeToState("AddHighScoreState");
                    }
                    else
                    {
                        window.changeToState("HighScoreState");
                    }
                }
                else
                {
                    // CHANGE the state to the settings menu state
                    window.changeToState("SettingsMenuState");
                }
            }
            // OTHERWISE
            else
            {
                // DECREMENT the game over time
                gameOverTime -= timeElapsed;
            }
        }
    }

    /**
     * Handle all of the in-game input
     *
     * @param window game window
     * @throws java.lang.Exception exception
     */
    private void handleInput(GameWindow window) throws Exception
    {
        // FOR every unique name that was 'touched' by the mouse click
        for(Integer name : window.getLastTouched())
        {
            // IF the explain why background is the 'touched' entity
            if(name == explainWhyBg.getName())
            {
                closeExplainWhy();
            }
            // IF the exit button was the 'touched' entity
            else if(name == exitButton.getName())
            {
                window.setCloseRequested(true);
            }
            // IF the explain why button was the 'touched' entity
            else if(name == explainWhy.getName())
            {
                generateExplainWhyMessage();
            }
            // IF the id is the give hint button
            else if(name == giveHint.getName() && !hasGivenHint)
            {
                // CALL request hint
                requestHint(window.getLogicState().getDifficulty());
            }
            // OTHERWISE IF the id clicked is the menu icon
            else if(name == menuIcon.getName())
            {
                // SEND a SET_GAMESTATUS message to change the status to paused
                MessageManager.sendMessage(
                    new GameMessage(
                        GameMessage.MessageType.SET_GAMESTATUS,
                        CFState.StatusType.PAUSED
                        ),
                        GameMessage.MessageDest.CF_STATE
                    );
                // CHANGE the game state to the settings menu
                window.changeToState("SettingsMenuState");
            }
            // OTHERWISE IF the id clicked is the deal three button
            else if(name == dealThree.getName() && dealThreeAllowed)
            {
                // CALL a REQUEST_DEAL message
                MessageManager.sendMessage(
                    new GameMessage(
                        GameMessage.MessageType.REQUEST_DEAL, null
                        ),
                        GameMessage.MessageDest.CF_LOGIC
                    );
            }
            // OTHERWISE IF the id clicked is not the game board
            else if(name != board.getName())
            {
                handleCardInput(window, name);
            }
        }
    }

    /**
     * Close the explain why message and start the game up again
     */
    private void closeExplainWhy()
    {
        displayExplainWhy = false;
        // SEND a SET_GAMESTATUS to RUNNING message to unpause timer
        MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.SET_GAMESTATUS,
                    CFState.StatusType.RUNNING
                    ),
                GameMessage.MessageDest.CF_STATE
                );
    }

    /**
     * Generate the current explain why message
     */
    private void generateExplainWhyMessage()
    {
        // SEND a SET_GAMESTATUS to RUNNING message to unpause timer
        MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.SET_GAMESTATUS,
                    CFState.StatusType.PAUSED
                    ),
                GameMessage.MessageDest.CF_STATE
                );
        displayExplainWhy = true;
        explainWhyTime = 0;
        explainWhyMessage = explainWhyMessage.toLowerCase();
        explainWhyMessage =
                "The cluster chosen was invalid\n"
               +"because two of the cards had \n"
               +"the same " + explainWhyMessage + " and one had a\n"
               +"different " + explainWhyMessage + ". All cards \n"
               +"should have different " + explainWhyMessage + "s\n"
               +"or all the same " + explainWhyMessage + ".\n\n"
               +"       Click Here to close.";
    }

    /**
     * Handle all on-board card input
     *
     * @param window game window
     * @param name currently touched entity
     * @throws java.lang.Exception exception
     */
    private void handleCardInput(GameWindow window, int name) throws Exception
    {
        // card entity placeholder
        CardEntity selectedCard = null;
        
        // IF the active player is not null
        if(window.getLogicState().getActivePlayer() != null && !hasValidCluster && !hasInvalidCluster)
        {
            // FOR every selected card
            for(CardEntity card : selected.values())
            {
                // IF the id matches the selected id
                if((card.getName() == name) && !card.isAnimating())
                {
                    // CHOOSE the current card
                    selectedCard = card;

                    // DESELECT the card in the UI
                    deselectCard(card, true);
                }
            }

            // IF the card was found
            if(selectedCard != null)
            {
                // REMOVE the current card from the selected list
                removeSelectedCard(selectedCard);
            }
            // OTHERWISE check for the card in the normal game board
            else
            {
                handleCardSelects(name);
            }
        }
    }

    /**
     * Handle all of the card selection logic
     * @param name currently selected entity
     * @throws java.lang.Exception exception
     */
    private void handleCardSelects(int name) throws Exception
    {
        // FOR every card in the card deck
        for(CardEntity card : cards.values())
        {
            // IF the card is the touched card
            if((card.getName() == name) && !card.isAnimating() 
                    && (invalidClusterTime <= 0) && (validClusterTime <=0)
                    && (cardLockTime <= 0))
            {
                // SELECT the given card
                selectCard(card, true);
            }
        }
    }

    /**
     * Update all of the cards on the deck
     *
     * @param timeElapsed time elapsed since last update
     */
    private void updateCards(long timeElapsed)
    {
        // FOR every card in the game board
        for(CardEntity card : cards.values())
        {
            // IF the card is being animated UPDATE the animation
            card.update(timeElapsed);
        }

        ArrayList<CardEntity> deadCards = new ArrayList<CardEntity>();

        // FOR every temporary card
        for(CardEntity card : tmpCards)
        {
            // IF the card is animating
            if(card.isAnimating())
            {
                // IF the card is being animated UPDATE the animation
                card.update(timeElapsed);
            }
            // OTHERWISE
            else
            {
                // ADD the card to the dead card list
                deadCards.add(card);
            }
        }

        // FOR every dead card
        for(CardEntity card : deadCards)
        {
            // REMOVE the card from the temp list
            cards.remove(card);
        }
    }

    /**
     * Handle the current end-game logic
     *
     * @param message game message
     */
    private void handleGameEnd(GameMessage message)
    {
        // IF the message value is not null
        if(message.getValue() != null)
        {
            // SET the game over display time to 4 seconds
            gameOverTime = defaultGameOverTime;
            isGameOver = true;
            // SEND a fanfare message to the sound module
            MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.PLAY_FANFARE, null),
                    GameMessage.MessageDest.CF_SOUND);
        }
        gameEnd();
    }

    /**
     * Handle an invalid cluster message
     * 
     * @param message game message
     */
    private void handleInvalidCluster(GameMessage message)
    {
        
        // SET hasInvalidCluster to true to alert the program
        hasInvalidCluster = true;
        invalidClusterTime = defaultMessageDisplayTime;
        explainWhyMessage = (String)message.getValue();
        // RESET hasGivenHint to allow for a new hint
        considerResetButtons = true;
    }

    /**
     * Handle a valid cluster message
     */
    private void handleValidCluster()
    {
        hasValidCluster = true;
        // IF the user hadn't run out of time
        if(!outOfTime)
        {
            validClusterTime = defaultMessageDisplayTime;
        }
        outOfTime = false;
    }

    /**
     * Handle a deal cards message
     *
     * @param message game message
     * @throws clusterfun.ui.image.CardOutOfBoundsException exception
     */
    private void handleDealCards(GameMessage message) throws CardOutOfBoundsException
    {
        // IF the cluster was valid
        if(hasValidCluster)
        {
            // SAVE the cards to deal to deal later
            cardsToDeal = (ArrayList<Card>)message.getValue();
        }
        // OTHERWISE
        else
        {
            // DEAL the following cards
            dealCards((ArrayList<Card>)message.getValue());
        }
    }

    /**
     * Manage all of the in-game timers
     *
     * @param timeElapsed elapsed time since last update
     */
    private void manageTimers(long timeElapsed)
    {
        if(cardLockTime > 0)
        {
            cardLockTime -= timeElapsed;
        }
        else
        {
            cardLockTime = 0;
        }
        // IF dealing three is allowed
        if(!dealThreeAllowed)
        {
            // IF the display deal fail time is not zero
            if(displayDealFailTime > 0)
            {
                displayDealFailTime -= timeElapsed;
            }
            // OTHERWISE zero out the display deal fail time
            else
            {
                displayDealFailTime = 0;
            }
        }
        // IF the valid cluster image display time is not zero
        if(validClusterTime > 0)
        {
            validClusterTime -= timeElapsed;
        }
        // OTHERWISE zero out the valid cluster image display time
        else
        {
            validClusterTime = 0;
        }

        // IF the valid cluster display time is not zero
        if(invalidClusterTime > 0)
        {
            invalidClusterTime -= timeElapsed;
            hasGivenHint = true;
            dealThreeAllowed = false;
        }
        // OTHERWISE zero out the valid cluster display time
        else
        {
            invalidClusterTime = 0;
        }

        // IF the explain why time is less than zero
        if(explainWhyTime <= 0)
        {
            // ZERO out the explain why time
            explainWhyTime = 0;
        }
        else
        {
            explainWhyTime -= timeElapsed;

            // IF the explain why time is up
            if((explainWhyTime <= 0) && timedMode && !displayExplainWhy)
            {
                // SEND a running status message
                MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.SET_GAMESTATUS,
                    CFState.StatusType.RUNNING
                    ),
                GameMessage.MessageDest.CF_STATE
                );
            }
        }
        // UPDATE animation buffer
        // IF the animation buffer is done
        if(animBuffer <= 0)
        {
            // RESET animation buffer
            animBuffer = 0;
        }
        // OTHERWISE
        else
        {
            // DECREMENT the animation buffer by the time elapsed
            animBuffer -= timeElapsed;
        }

        // IF the deal time has finished
        if(dealTime <= 0)
        {
            // RESET the deal time
            dealTime = 0;

            // IF we are meant to consider resetting the buttons shown
            if(considerResetButtons && !displayExplainWhy && (invalidClusterTime <= 0))
            {
                // RESET hasGivenHint to allow for a new hint
                hasGivenHint = false;

                dealThreeAllowed = true;
                considerResetButtons = false;
            }
        }
        // OTHERWISE
        else
        {
            // RESET hasGivenHint to allow for a new hint
            hasGivenHint = true;

            dealThreeAllowed = false;

            // DECREMENT the animation buffer by the time elapsed
            dealTime -= timeElapsed;

            // IF the deal time has finished and timed mode is enabled
            if((dealTime <= 0) && timedMode && !displayExplainWhy)
            {
                // SEND a SET_GAMESTATUS to RUNNING message to unpause timer
                MessageManager.sendMessage(
                        new GameMessage(
                            GameMessage.MessageType.SET_GAMESTATUS,
                            CFState.StatusType.RUNNING
                            ),
                        GameMessage.MessageDest.CF_STATE
                        );
            }
        }
    }

    /**
     * lets the user know that the deal has failed
     */
    private void dealFail()
    {
        dealThreeAllowed = false;
        displayDealFailTime = defaultMessageDisplayTime;
    }
    
    /**
     * Update all of the keyboard based input
     *
     * @param window game window
     * @param players list of current running players
     */
    private void updateInput(GameWindow window, ArrayList<AbstractPlayer> players)
    {
        if(window.getLogicState().getActivePlayer() == null)
        {
            // IF there are more keyboard messages to be grabbed
            if(Keyboard.next())
            {
                char chr = Keyboard.getEventCharacter();
                int chkey = Keyboard.getEventKey();

                // IF the keyboard button is a letter or digit
                if(Keyboard.isKeyDown(chkey) && Character.isLetterOrDigit(chr))
                {
                    // FOR every abstract player playing
                    for(AbstractPlayer player : players)
                    {
                        //IF the player's key matches the character pressed
                        if(player instanceof HumanPlayer && ((HumanPlayer)player).getKey() == chr)
                        {
                            MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.GIVE_PLAYER_FOCUS,
                                    player), GameMessage.MessageDest.CF_STATE);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Selects the cards given due to a hint
     * 
     * @param cards cards to be hinted
     * @throws java.lang.Exception exception thrown by lwjgl
     */
    private void giveHint(ArrayList<Card> cardList) throws Exception
    {
        // FOR every selected card
        for(CardEntity card : selected.values())
        {
            deselectCard(card, true);
        }

        selected.clear();
        // FOR every card
        for(Card card : cardList)
        {
            // SELECT the given card
            selectCard(findCard(card), true);
        }
    }
    
    /**
     * Sends a request hint message and remove the request hint button
     */
    private void requestHint(CFState.DifficultyType difficulty)
    {
        // SEND a request hint button
        MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.REQUEST_HINT,
                    difficulty
                    ),
                GameMessage.MessageDest.CF_LOGIC
                );
        
        // TELL the ui that a hint has already been given
        hasGivenHint = true;
    }
    
    /**
     * Manage the selected cards, updating the animations
     * 
     * @throws clusterfun.ui.image.CardOutOfBoundsException card exception
     */
    private void manageSelected() throws CardOutOfBoundsException
    {
        // IF the animation buffer has finished
        if(animBuffer <= 0)
        {
            // IF a valid cluster is ready to process
            if(hasValidCluster)
            {
                // CALL validCluster
                validCluster();
                
                // IF there are cards to deal
                if(cardsToDeal != null)
                {
                    animBuffer += defaultMessageDisplayTime;
                    // CALL deal cards to deal the given cards
                    dealCards(cardsToDeal);
                    
                    // RESET the cardsToDeal variable
                    cardsToDeal = null;
                }

                considerResetButtons = true;
            }
            
            // IF an invalid cluster is waiting to animat
            if(hasInvalidCluster && !displayExplainWhy)
            {
                // CALL invalid cluster to handle the given invalid cluster
                invalidCluster( true );
            }
        }
    }
    
    /**
     * Selects the given cluster
     * 
     * @param selCards selected cards
     */
    public void selectCluster(ArrayList<Card> selCards) throws Exception
    {
        // CALL invalidCluster
        invalidCluster( false );
        
        // FOR every selected card
        for(Card card : selCards)
        {
            // SELECT the given card
            selectCard(findCard(card), false);
        }
        // IF there are cards to deal
        if(cardsToDeal != null)
        {
            animBuffer += defaultMessageDisplayTime;
        }
        dealTime = animBuffer;
    }
     
    /**
     * Finds the given card entity in the game board
     * 
     * @param card card object
     * @return cardEntity card entity object
     */
    public CardEntity findCard(Card card)
    {
        // flag card entity object
        CardEntity endCard = null;
        
        // FOR every card on the board
        for(CardEntity c : cards.values())
        {
            // IF the card object in the current visual card matches
            if(card.equals(c.getCardObj()))
            {
                // SET the flag
                endCard = c;
            }
        }
        
        // RETURN the resulting card
        return endCard;
    }
    /**
     * removes the currently selected card
     * 
     * @param card selected card to be removed
     */
    public void removeSelectedCard(CardEntity card)
    {
        String curVal = null; // current test value

        // FOR every selected card
        for(String val : selected.keySet())
        {
            // IF the card matches the selected card
            if(card.equals(selected.get(val)))
            {
                // SET the flag to the current card
                curVal = val;
            }
        }

        // IF we found the card
        if(curVal != null)
        {
            // REMOVE the card
            selected.remove(curVal);
        }
    }
    
    /**
     * Selects the given card and begins the animation to move it from the 
     * set of cards to the selected card slot
     * 
     * @param card the card to be put into the selected spot on the game board
     */
    public void selectCard(CardEntity card, boolean sendMessage) throws Exception
    {
        boolean foundSelected = false; // Found selected flag
        String curSelected = null; // Current selected location
        
        if(card == null)
        {
            throw new Exception("Card selected was NOT on the table!");
        }
        
        // FOR every selected card location
        for(int selectedIndex = 1; (selectedIndex <= 3) && !foundSelected; selectedIndex++)
        {
            // GENERATE the location's hash string
            curSelected = "selected " + selectedIndex;
            
            // SET the flag to true if the location is not taken
            foundSelected = !selected.containsKey(curSelected);
        }
        
        if(foundSelected)
        {
            // TAKE the current selected location
            selected.put(curSelected, card);

            // RESET the animation scalar
            card.resetAnimation();
            
            // SET the card's destination position
            card.setEndPosition(curSelected);

            // SET the card's animation speed
            card.setAnimationTime(500);
            
            // SET the card's animation speed
            card.setAnimationDelay(animBuffer);

            animBuffer += 500;
            // SET the card to animating
            card.setAnimating(true);
            
            // IF sendMessage is enabled
            if(sendMessage)
            {
                // SEND the appropriate SELECT_CARD message
                MessageManager.sendMessage(
                    new GameMessage(
                        GameMessage.MessageType.SELECT_CARD, 
                        card.getCardObj()
                        ), 
                    GameMessage.MessageDest.CF_LOGIC,
                    GameMessage.MessageDest.CF_STATE,
                    GameMessage.MessageDest.CF_SOUND
                    );
            }
        }
    }
    
    /**
     * Deselects the given card and begins the animation to move it from the 
     * set of selected cards back to the board
     * 
     * @param card the card to be replaced on the board
     */
    private void deselectCard(CardEntity card, boolean sendMessage)
    {
        deselectCard(card, 0, sendMessage);
    }
    
    /**
     * Deselects the given card and begins the animation to move it from the 
     * set of selected cards back to the board
     * 
     * @param card the card to be replaced on the board
     */
    private void deselectCard(CardEntity card, int index, boolean sendMessage)
    {
        // IF the card exists
        if(card != null)
        {
            selected.remove(card);
            // SET the card's animation speed
            card.setAnimationTime(500);

            // SET the card's animation delay
            card.setAnimationDelay(animBuffer + (index * 500));

            // RESET the animation scalar
            card.setAnimationScalar(0);
            
            card.setStartPosition(card.getEndPosition());

            // SET the card's destination position
            card.setEndPosition(card.getDefaultCardPosition());

            // SET the card to animating
            card.setAnimating(true);

            // IF the message is scheduled to be sent to CFLogic
            if(sendMessage)
            {
                // SEND the appropriate DESELECT_CARD message
                MessageManager.sendMessage(
                        new GameMessage(
                            GameMessage.MessageType.DESELECT_CARD, 
                            card.getCardObj()
                            ), 
                        GameMessage.MessageDest.CF_LOGIC,
                        GameMessage.MessageDest.CF_STATE,
                        GameMessage.MessageDest.CF_SOUND
                        );
            }
        }
    }
    
    /**
     * Updates the time remaining on the game timer
     * 
     * @param timeLeft time remaining
     */
    public void setTimeRemaining(long timeLeft)
    {
        String curStr;
        // CALCULATE seconds
        int seconds = (int)(timeLeft / kMILLISINSECS) % 60;
        
        // CALCULATE minutes
        int minutes = (int)(timeLeft / (kSECSINMIN * kMILLISINSECS));
        
        // IF seconds is less than ten
        if(seconds < kTENSECS)
        {
            // SET the timer string (with padding)
            curStr = minutes + ":0" + seconds;
        }
        // IF seconds is more than ten
        else
        {
            // SET the timer string (without padding)
            curStr = minutes + ":" + seconds;
        }
        if(minutes + seconds == 0)
        {
            timer.setString("");
        }
        else
        {
            timer.setString(curStr);
        }
    }
    
    /**
     * Removes cards if the cluster is valid
     */
    private void validCluster()
    { 
            //selected.remove("selected 1");
            removeCard(selected.get("selected 1"), 0);
            
            //selected.remove("selected 2");
            removeCard(selected.get("selected 2"), 1);
            
            //selected.remove("selected 3");
            removeCard(selected.get("selected 3"), 2);

            dealTime += kDEALCARDDELAY * 3;
            
            // CLEAR the selected cards list
            selected.clear();
            
            if(cards.size() >= 12 && cards.size() <= 18)
            {
                reorderCards();
            }
            
            hasValidCluster = false;
    }
    
    /**
     * Reorder all of the cards on the deck to fit the minimum cluster of cards on the board
     */
    private void reorderCards()
    {
        boolean noSpots = true;
        for(int index = posListCol.length-1; (index >= 12); index--)
        {
            String pos = "board " + posListCol[index] + "," + posListRow[index];
            CardEntity card = cards.get(pos);
            if(card != null)
            {
                noSpots = !findNewCardPos(card, index);
            }
        }
    }

    /**
     * Find a new position for the given card
     * @param card card to move
     * @param last last card to check
     * @return whether or not a spot was found
     */
    private boolean findNewCardPos(CardEntity card, int last)
    {
        boolean foundSpot = false;

        // FOR every card from the current card to the last card to check
        for(int index = 0; (index < last) && !foundSpot; index++)
        {
            String pos = "board " + posListCol[index] + "," + posListRow[index];
            CardEntity card2 = cards.get(pos);

            // IF the card was found
            if(card2 == null)
            {
                foundSpot = true;
                cards.remove(card.getDefaultCardPosition());
                
                card.setAnimating(true);
                card.setAnimationTime(500);
                card.setAnimationDelay(animBuffer);
                animBuffer += 500;
                card.setEndPosition(pos);
                card.setDefaultCardPosition(pos);
                card.setAnimationScalar(0);
                
                cards.put(pos, card);
            }
        }
        return foundSpot;
    }
    
    /**
     * Replaces every selected card if the cluster is invalid
     */
    private void invalidCluster( boolean explainWhy )
    {
        if(explainWhy)
        {
            // ADD a buffer to the exlain why time
            explainWhyTime += 2000;
        }
        
        // ADD a buffer to the animation buffer
        animBuffer += 2000;

        cardLockTime += animBuffer + selected.size() * kDEALCARDDELAY;
        
        MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.SET_GAMESTATUS,
                    CFState.StatusType.PAUSED
                    ), 
                GameMessage.MessageDest.CF_STATE
                );
        
        // DESELECT first selected card
        deselectCard(selected.get("selected 3"), 0, false);
        
        // DESELECT second selected card
        deselectCard(selected.get("selected 2"), 1, false);
        
        // DESELECT third selected card
        deselectCard(selected.get("selected 1"), 2, false);
        
        // CLEAR the selected cards list
        selected.clear();
        
        hasInvalidCluster = false;

        
    }
    
    /**
     * Called when the game starts
     */
    private void handleGameStart()
    {
        gameInProgress = true;
    }
    
    /**
     * Called when the game ends
     */
    private void gameEnd()
    {
        gameInProgress = false;
    }

    /**
     * @see GameState#enter(clusterfun.ui.window.GameWindow) 
     */
    public void enter(GameWindow window)
    {
        // IF the game is currently not running
        if(!running)
        {
            // CLEAR the card set
            cards.clear();
            
            // CLEAR the selected set
            selected.clear();
            
            // RESET animation buffer
            animBuffer = 0;
            
            // RESET deal time
            dealTime = 0;
            
            isGameOver = false;
            
            gameOverTime = 0;

            // SEND a GAME_START message to indicate that the app is ready to run
            MessageManager.sendMessage(
                    new GameMessage(GameMessage.MessageType.GAME_START, configHash),
                    GameMessage.MessageDest.CF_ALL
                    );
            
            // SET running to true
            running = true;
        }
    }

    /**
     * @see GameState#leave(clusterfun.ui.window.GameWindow) 
     */
    public void leave(GameWindow window)
    {
        // DO nothing, we don't care if the player leaves this state
    }
    
    
    /**
     * Calls for a deal animation, adding three new cards to the visual deck.  
     * This allows the UI to begin the introduction of the new cards, allowing 
     * them to be 'dealt' onto the game board
     * 
     * @param cards the cards to be dealt onto the game board
     */
    private void dealCards(ArrayList<Card> dealt) throws CardOutOfBoundsException
    {
        // current texture coordinate information
        CardImageCoord textureCoord = null;
        
        // current string position
        String curPosition = null;
        
        // AMOUNT of columns to check for
        int horizCount = 4; 
        
        // AMOUNT of columns to check for
        int vertCount = 3;
        
        // SET the deal time delay, so that the timer does not run during deal
        dealTime += kDEALCARDDELAY * (dealt.size() + 1);
        
        //FOR every card to be dealt
        for(int index = 0; index < dealt.size(); index++)
        {

            // RETRIEVE  the card from the list
            Card card = dealt.get(index);

            // CALCULATE the texture coordinates for the card entity
            textureCoord = new CardImageCoord(card.getIndex());

            // IF there are more than 18 cards on the board already
            if(cards.size() >= 18)
            {
                // SET the horizontal test value to 6
                horizCount = 6;
                // SET the vertical test value to 4
                vertCount = 4;
            }
            else if(cards.size() >= 15)
            {
                // SET the horizontal test value to 6
                horizCount = 6;
            }else if(cards.size() >= 12)
            {
                // SET the horizontal test value to 5
                horizCount = 5;
            }

            boolean notFound = true; // notFound flag

            // FOR every horizontal card to be tested
            for(int checkHoriz = 1; (checkHoriz <= horizCount) && notFound; checkHoriz++)
            {
                // FOR every vertical card to be tested
                for(int checkVert = 1; (checkVert <= vertCount) && notFound; checkVert++)
                {
                    // UPDATE position string
                    curPosition = "board " + checkHoriz + "," + checkVert;
                    // UPDATE notFound flag
                    notFound = cards.containsKey(curPosition);
                }
            }

            // DEAL the new card
            dealCard(card, curPosition, textureCoord, index);

        }

        considerResetButtons = true;

        hasGivenHint = true;
        dealThreeAllowed = false;
    }
    
    /**
     * Deals the given card, setting correct animations
     * 
     * @param card card to be added
     * @param location location to be delt to
     * @param textureCoord texture coordinate information
     * @param index card index (for use in delays)
     */
    private void dealCard(Card card, String location, 
            CardImageCoord textureCoord, int index)
    {
        // CREATE a new card entity from the given card
        CardEntity newCard = new CardEntity(
            card, 
            cardTextureSets[textureCoord.getWhichFile()], 
            textureCoord.getStartCoord(), 
            textureCoord.getEndCoord(),
            location
            );

        // SET the new position to the dealing position
        newCard.setStartPosition("deal");

        // SET the card's destination position
        newCard.setEndPosition(location);

        // SET the card's animation speed
        newCard.setAnimationTime(kDEALCARDTIME);

        /* SET the card's animation delay based on it's position
         in the list of cards being dealt*/
        newCard.setAnimationDelay(animBuffer + (index * kDEALCARDDELAY));

        // SET the card to animating
        newCard.setAnimating(true);

        // ADD the card to the list of card entities
        cards.put(location, newCard);
    }
    
    private void removeCard(CardEntity card, int index)
    {
        // IF the card exists
        if(card != null)
        {
            card.setStartPosition(card.getEndPosition());
            
            card.setAnimationScalar(0);
            
            // SET the new position to the dealing position
            card.setEndPosition("deal");

            // SET the card's animation speed
            card.setAnimationTime(500);

            /* SET the card's animation delay based on it's position
             in the list of cards being dealt*/
            card.setAnimationDelay(animBuffer + (index * 750));
            
            // SET the card to animating
            card.setAnimating(true);

            // REMOVE the card from the regular deck
            cards.remove(card.getDefaultCardPosition());

            // REMOVE the card from the selected deck if it is selected
            removeSelectedCard(card);

            // ADD the card to a temporary list of cards to be thrown out later
            tmpCards.add(card);
        }
    }
    

    /**
     * Loads in the images within a directory and sets them as the current 
     * theme image set
     * 
     * @param newTheme directory where theme resides
     */
    private void setTheme(String newTheme)
    {
        curTheme = newTheme;
    }
    
    /**
     * Loads each sound file and image file individually, identifies them and 
     * resets all of the cards and the game board accordingly
     * 
     * @param themeDir directory that points to the theme image and sound files
     */
    private void loadTheme(GameWindow window) throws IOException
    {
        Object theme = window.getProperty("theme");

        // IF the theme is null
        if(theme != null)
        {
            curTheme = theme.toString();
        }
        
        cardTextureSets = new Texture[2];
        cardTextureSets[0] = window.loadTexture(kTHEMEBASE + curTheme + "cards/cards_list1.png");
        cardTextureSets[1] = window.loadTexture(kTHEMEBASE + curTheme + "cards/cards_list2.png");
                
    }
    
    public boolean isRunning()
    {
        return running;
    }
    
    public void setRunning(boolean running)
    {
        this.running = running;
    }
    
    public boolean isTimedMode()
    {
        return timedMode;
    }
    
    public void setTimedMode(boolean timedMode)
    {
        this.timedMode = timedMode;
    }

}
