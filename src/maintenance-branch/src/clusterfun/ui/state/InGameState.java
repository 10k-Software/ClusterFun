package clusterfun.ui.state;

import clusterfun.board.Card;
import clusterfun.lwjgl.Texture;
import clusterfun.lwjgl.TextureLoader;
import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
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
import javax.swing.JOptionPane;
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
    
    // selected card indication spot #1
    private CardEntity selectedCardSpot1;
    
    // selected card indication spot #2
    private CardEntity selectedCardSpot2;
    
    // selected card indication spot #3
    private CardEntity selectedCardSpot3;
    
    // The texture sets for cards
    private Texture[] cardTextureSets;
    
    // Theme base when loading theme pictures
    private final String kTHEMEBASE = "resources/img/theme/";
    
    // Current theme folder
    private String curTheme = "defaultTheme/";
    
    // If a valid cluster has been called
    private boolean hasValidCluster = false;
    
    // If an invalid cluster has been called
    private boolean hasInvalidCluster = false;
    
    private boolean hasSelectCluster = false;
    
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
    
    // TEXTURE loader
    private TextureLoader loader = new TextureLoader();
    
    // LOAD the default blank icon
    protected Texture buttonTexture;
    // last 'explain why' message
    private String explainWhyMessage = "";
    
    private long gameOverTime = 0;
    
    private boolean isGameOver;
    
    private boolean initialized = false;
    
    private boolean dealThreeAllowed = true;
    
    private long displayDealFailTime = 0;
    
    private long validClusterTime = 0;
    
    private long invalidClusterTime = 0;
    
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
     * @throws java.lang.IOException file load exception
     */
    public void init(GameWindow window) throws IOException
    {
        // INITIALIZE the visual board entity
        board = new BoardEntity(window.loadTexture(kTHEMEBASE + curTheme + "background/board.jpg"));

        // LOAD select card texture
        Texture selectSpot = window.loadTexture(kTHEMEBASE + curTheme + "cards/select_card_spot.png");
        
        // INITIALIZE the timer entity
        timer = new NumberEntity("timer", window.loadTexture(kTHEMEBASE + curTheme + "timer.png"));
        
        buttonTexture = window.loadTexture(kTHEMEBASE + curTheme + "buttons/buttons.png");
        
        // INITIALIZE score entity
        score = new NumberEntity("score", window.loadTexture(kTHEMEBASE + curTheme + "timer.png"), 0.25f);
        
        // INITIALIZE the score text
        scoreIcon = new ButtonEntity("score icon", new Vector2f(0.0f,0.484375f), 
                new Vector2f(0.109375f, 0.515625f), 112, 32, buttonTexture);
     
        gameOverIcon = new ButtonEntity("please wait", new Vector2f(0,0), 
                new Vector2f(1, 1), 512, 512, window.loadTexture(kTHEMEBASE + curTheme + "background/gameOver.png"));
        
        // INITIALIZE the deal three button
        dealThree = new ButtonEntity("deal three", new Vector2f(0.640625f,0.296875f), 
                new Vector2f(0.828125f, 0.390625f), 192, 96, buttonTexture);

        dealThreeGrayed = new ButtonEntity("deal three", new Vector2f(656, 656), 
                new Vector2f(192, 96), buttonTexture);
        
        dealThreeFailed = new ButtonEntity("please wait", new Vector2f(0, 0), 
                new Vector2f(512, 512), window.loadTexture(kTHEMEBASE + curTheme + "background/cannotDeal.png"));
                
        validCluster = new ButtonEntity("please wait", new Vector2f(0, 0), 
                new Vector2f(512, 512), window.loadTexture(kTHEMEBASE + curTheme + "background/validCluster.png"));
        
        invalidCluster = new ButtonEntity("please wait", new Vector2f(0, 0), 
                new Vector2f(512, 512), window.loadTexture(kTHEMEBASE + curTheme + "background/invalidCluster.png"));
        
        // INITIALIZE the deal three button
        explainWhy = new ButtonEntity("explain why", new Vector2f(0.640625f,0.40625f), 
                new Vector2f(0.828125f, 0.453125f), 192, 48, buttonTexture);

        // INITIALIZE the give hint button
        giveHint = new ButtonEntity("give hint", new Vector2f(0.640625f,0.46875f), 
                new Vector2f(0.828125f, 0.5625f), 192, 96, buttonTexture);
        
        giveHintGrayed = new ButtonEntity("give hint", new Vector2f(656, 768), 
                new Vector2f(192,96), buttonTexture);
        
        // INITIALIZE the menu button
        menuIcon = new ButtonEntity("menu icon", new Vector2f(0.640625f,0.578125f), 
            new Vector2f(0.828125f,0.640625f), 192, 64, buttonTexture);
        
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
        
        // INITIALIZE list of cards
        cards = new HashMap<String, CardEntity>();
        
        // INITIALIZE list of selected cards
        selected = new HashMap<String, CardEntity>();
        
        // INITIALIZE temporary card list
        tmpCards = new ArrayList<CardEntity>();
        
        // LOAD the default font
        loadFont();
        
        // LOAD the current theme
        loadTheme(window);
        
        initialized = true;
    }
    
    
    public boolean isInitialized()
    {
        return initialized;
    }
    
    /**
     * Updates the current game configuration hash for the CFState
     * 
     * @param beginnerMode whether or not beginner mode is enabled
     * @param timedMode whether or not the timed mode is enabled
     */
    public void setConfig(boolean beginnerMode, boolean timedMode)
    {
        if(beginnerMode)
        {
            configHash.put("mode", CFState.ModeType.BEGINNER);
        }
        else
        {
            configHash.put("mode", CFState.ModeType.SOLITARE);
        }
    }

    /**
     * @see GameState#render(clusterfun.ui.window.GameWindow, int) 
     */
    public void render(GameWindow window, boolean picking)
    {
        // CLEAR the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

        GL11.glLoadIdentity();
        
        // SET the opengl perspective to 45 degrees
        GLU.gluPerspective(45.0f, ((float) 800) / ((float) 600), 0.1f, 1000.0f);
        
        GL11.glPushMatrix();
        
        // SET the camera back in order to view the objects in the scene
            // SET the current camera position to (0, -10, 25)
            // SET the current lookat position to (0,0,0)
            // SET the current 'up' camera position to (0,0,1)
        GLU.gluLookAt(
                0.0f, -13.5f, 35.0f,
                0.0f, -3.5f, 0.0f,
                0.0f, 0.0f, 1.0f
                );
        
        // IF we are not picking
        if(!picking)
        {
            // RENDER the game board
            board.render();
        }
        // ENABLE blending
        GL11.glEnable(GL11.GL_BLEND);
        
        // SET blending type
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // FOR every card in the game board
        for(CardEntity card : cards.values())
        {
            //GL11.glPushMatrix();
            
            // RENDER the card entity
            card.render(picking);
            //GL11.glPopMatrix();
        }
        
        // RENDER the first selected card indicator
        selectedCardSpot1.render();
        
        // RENDER the second selected card indicator
        selectedCardSpot2.render();
        
        // RENDER the third selected card indicator
        selectedCardSpot3.render();
        
        // FOR every card in the temporary card list
        for(CardEntity card : tmpCards)
        {
                GL11.glPushMatrix();
            
                // RENDER the card entity
                card.render();
                GL11.glPopMatrix();
        }

        // POP the current matrix
        GL11.glPopMatrix();

        // DISABLE the depth testing for all text and font output
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, -1, 1);
        
        // RENDER the timer
        if(timedMode)
        {
            timer.render();
        }
        // RENDER the point counter
        score.render();
       
        // RENDER the score icon
        scoreIcon.render();
        
        
        
        // IF we are really in the InGameState
        if(getName().equals("InGameState"))
        {
            // RENDER the menu icon
            menuIcon.render();
        }
        
        
        
        // IF we havent given a hint yet
        if(!hasGivenHint)
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
        
        // IF the explain why time is still applicable
        if(explainWhyTime > 0)
        {
            // RENDER the explain why button
            explainWhy.render();
        }
        
        if(isGameOver)
        {
            gameOverIcon.render();
        }
        
        // IF there are not 21 cards on the board, it is safe to show the deal button
        if(cards.size() <= 18)
        {
            if(dealThreeAllowed)
            {
                // RENDER the deal three button
                dealThree.render();
            }
            else
            {
                dealThreeGrayed.render();
                if(displayDealFailTime > 0)
                {
                    dealThreeFailed.render();
                }
            }
        }
        
        if(validClusterTime > 0)
        {
            validCluster.render();
        }
        
        if(invalidClusterTime > 0)
        {
            invalidCluster.render();
        }
        
        // REENABLE the depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);

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
     * @see GameState#update(clusterfun.ui.window.GameWindow, int) 
     */
    public void update(GameWindow window, long timeElapsed) throws Exception
    {
        if(!dealThreeAllowed)
        {
            if(displayDealFailTime > 0)
            {
                displayDealFailTime -= timeElapsed;
            }
            else
            {
                displayDealFailTime = 0;
            }
        }

        if(validClusterTime > 0)
        {
            validClusterTime -= timeElapsed;
        }
        else
        {
            validClusterTime = 0;
        }
        
        if(invalidClusterTime > 0)
        {
            invalidClusterTime -= timeElapsed;
        }
        else
        {
            invalidClusterTime = 0;
        }
        // RECEIVE all messages for the UI
        ArrayList<GameMessage> messages = MessageManager.getMessages(GameMessage.MessageDest.CF_UI);
        
        // FOR every message received
        for(GameMessage message : messages)
        {
            // IF the message is GIVE_HINT
                // SET the card entities that match the hint cards to do a hint 
                // animation
            // IF the message is DEAL_CARDS
            switch(message.getType())
            {
                // IF the message is DEAL_CARDS
                case DEAL_CARDS: 
                    // IF there is a valid cluster, wait to deal
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
                    // RESET hasGivenHint to allow for a new hint
                    hasGivenHint = false;
                break;
                // IF the message is VALID_CLUSTER
                case VALID_CLUSTER: 
                    hasValidCluster = true;
                    validClusterTime = 1500;
                    // RESET hasGivenHint to allow for a new hint
                    hasGivenHint = false;
                break;
                // IF the message is INVALID_CLUSTER
                case INVALID_CLUSTER: 
                    // SET hasInvalidCluster to true to alert the program
                    hasInvalidCluster = true;
                    invalidClusterTime = 1500;
                    explainWhyMessage = (String)message.getValue();
                    // RESET hasGivenHint to allow for a new hint
                    hasGivenHint = false;
                break;
                // IF the message is GAME_START
                case GAME_START: 
                    // CALL start game
                    gameStart();
                break;
                case DEAL_FAIL:
                    dealFail();
                break;
                // IF the message is GAME_END
                case GAME_END: 
                    if(message.getValue() != null)
                    {
                        gameOverTime = 4000;
                        isGameOver = true;
                    }
                    //gameEnd(message.getValue());
                break;
                // IF the message is VALID_CLUSTER
                case SELECT_CLUSTER:
                    // CALL select cluster with the given cards
                    selectCluster((ArrayList<Card>)message.getValue());
                break;
                case GIVE_HINT:
                    // CALL give hint with the given card(s)
                    giveHint((ArrayList<Card>)message.getValue());
                break;
            }            
        }
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
        
        if(explainWhyTime <= 0)
        {
            explainWhyTime = 0;
        }
        else
        {
            explainWhyTime -= timeElapsed;
            
            if((explainWhyTime <= 0) && timedMode)
            {
                MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.SET_GAMESTATUS,
                    CFState.StatusType.RUNNING
                    ), 
                GameMessage.MessageDest.CF_STATE
                );
            }
        }
        
        // FOR every unique name that was 'touched' by the mouse click
        for(Integer name : window.getLastTouched())
        {
            CardEntity selectedCard = null; // card entity placeholder
            
            if(name == explainWhy.getName())
            {
                JOptionPane.showMessageDialog(null, explainWhyMessage + 
                        " property is not all the same or all different.");
            }
            // IF the id is the give hint button
            else if(name == giveHint.getName() && !hasGivenHint)
            {
                // CALL request hint
                requestHint();
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
                    // FOR every card in the card deck
                    for(CardEntity card : cards.values())
                    {
                        // IF the card is the touched card
                        if((card.getName() == name) && !card.isAnimating())
                        {
                            // SELECT the given card
                            selectCard(card, true);
                        }
                    }
                }                
            }
        }
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
        }
        // OTHERWISE
        else
        {
            // DECREMENT the animation buffer by the time elapsed
            dealTime -= timeElapsed;
            
            // IF the deal time has finished and timed mode is enabled
            if((dealTime <= 0) && timedMode)
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
        
        // UPDATE the selected spot 1 indicator
        selectedCardSpot1.update(timeElapsed);
        
        // UPDATE the selected spot 2 indicator
        selectedCardSpot2.update(timeElapsed);
        
        // UPDATE the selected spot 3 indicator
        selectedCardSpot3.update(timeElapsed);
        
        // MANAGE the selected cards
        manageSelected();
        
        if(isGameOver)
        {
            if(gameOverTime <= 0)
            {
                window.changeToState("SettingsMenuState");
            }
            else
            {
                gameOverTime -= timeElapsed;
            }
        }
    }
    
    /**
     * lets the user know that the deal has failed
     */
    private void dealFail()
    {
        dealThreeAllowed = false;
        displayDealFailTime = 1500;
    }
    
    /**
     * Selects the cards given due to a hint
     * 
     * @param cards cards to be hinted
     * @throws java.lang.Exception exception thrown by lwjgl
     */
    private void giveHint(ArrayList<Card> cards) throws Exception
    {
        for(CardEntity card : selected.values())
        {
            deselectCard(card, true);
        }
        selected.clear();
        // FOR every card
        for(Card card : cards)
        {
            // SELECT the given card
            selectCard(findCard(card), true);
        }
    }
    
    /**
     * Sends a request hint message and remove the request hint button
     */
    private void requestHint()
    {
        // SEND a request hint button
        MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.REQUEST_HINT,
                    null
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
                    //if(!hasSelectCluster)
                    //{
                        animBuffer += 1500;
                    //}
                    //else
                    //{
                        hasSelectCluster = false;
                    //}
                    // CALL deal cards to deal the given cards
                    dealCards(cardsToDeal);
                    
                    // RESET the cardsToDeal variable
                    cardsToDeal = null;
                }
            }
            
            // IF an invalid cluster is waiting to animat
            if(hasInvalidCluster)
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
        
        hasSelectCluster = true;
        
        // FOR every selected card
        for(Card card : selCards)
        {
            // SELECT the given card
            selectCard(findCard(card), false);
        }
        // IF there are cards to deal
        if(cardsToDeal != null)
        {
            animBuffer += 1500;
        }
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
        
        timer.setString(curStr);
    }
    
    /**
     * Removes cards if the cluster is valid
     */
    private void validCluster()
    { 
        dealThreeAllowed = true;
        //selected.remove("selected 1");
        removeCard(selected.get("selected 1"), 0);

        //selected.remove("selected 2");
        removeCard(selected.get("selected 2"), 1);

        //selected.remove("selected 3");
        removeCard(selected.get("selected 3"), 2);

        // CLEAR the selected cards list
        selected.clear();

        if(cards.size() >= 12 && cards.size() <= 18)
        {
            reorderCards();
        }

        hasValidCluster = false;
    }
    
        
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
    
    private boolean findNewCardPos(CardEntity card, int last)
    {
        boolean foundSpot = false;
        for(int index = 0; (index < last) && !foundSpot; index++)
        {
            String pos = "board " + posListCol[index] + "," + posListRow[index];
            CardEntity card2 = cards.get(pos);
            if(card2 == null)
            {
                foundSpot = true;
                cards.remove(card);
                
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
        
        MessageManager.sendMessage(
                new GameMessage(
                    GameMessage.MessageType.SET_GAMESTATUS,
                    CFState.StatusType.PAUSED
                    ), 
                GameMessage.MessageDest.CF_STATE
                );
        
        // DESELECT first selected card
        deselectCard(selected.get("selected 1"), 0, false);
        
        // DESELECT second selected card
        deselectCard(selected.get("selected 2"), 1, false);
        
        // DESELECT third selected card
        deselectCard(selected.get("selected 3"), 2, false);
        
        // CLEAR the selected cards list
        selected.clear();
        
        hasInvalidCluster = false;
        
    }
    
    /**
     * Called when the game starts
     */
    private void gameStart()
    {
        
    }
    
    /**
     * Called when the game ends
     */
    private void gameEnd(Object result)
    {
        
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
        dealThreeAllowed = true;
        // current texture coordinate information
        CardImageCoord textureCoord = null;
        
        // current string position
        String curPosition = null;
        
        // AMOUNT of columns to check for
        int horizCount = 4; 
        
        // AMOUNT of columns to check for
        int vertCount = 3;
        
        // SET the deal time delay, so that the timer does not run during deal
        dealTime = kDEALCARDDELAY * (dealt.size() + 1);
        
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
        
        // RESET hasGivenHint to allow for a new hint
        hasGivenHint = false;
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
