package clusterfun.state;

import clusterfun.board.Card;
import clusterfun.logic.CFLogic;
import clusterfun.message.GameMessage;
import clusterfun.message.GameMessage.MessageDest;
import clusterfun.message.GameMessage.MessageType;
import clusterfun.message.MessageManager;
import clusterfun.player.AbstractPlayer;
import clusterfun.player.HumanPlayer;
import clusterfun.state.CFState.DifficultyType;
import clusterfun.state.CFState.ModeType;
import clusterfun.state.CFState.DeckType;
import clusterfun.state.CFState.StatusType;
import java.util.ArrayList;
import java.util.HashMap;
import junit.framework.*;

/**
 *
 * @author Jason Swalwell
 */
public class CFStateTest extends TestCase
{
    private CFState solitaireState;
    private CFState timedState;
    private CFState multiState;
    private HashMap solitaireSettings;
    private HashMap timedSettings;
    private HashMap multiSettings;

    public CFStateTest(String name)
    {
        super(name);
    }
    
    protected void setUp()
    {
        solitaireState = new CFState(new CFLogic(), DeckType.NOSHUFFLE);
        timedState = new CFState(new CFLogic(), DeckType.NOSHUFFLE);
        multiState = new CFState(new CFLogic(), DeckType.NOSHUFFLE);

        solitaireSettings = new HashMap();
        solitaireSettings.put("mode", ModeType.SOLITARE);
        solitaireSettings.put("difficulty", DifficultyType.HARD);
        solitaireState.init(solitaireSettings);

        timedSettings = new HashMap();
        timedSettings.put("mode", ModeType.TIMED);
        timedSettings.put("difficulty", DifficultyType.MEDIUM);
        timedState.init(timedSettings);

        multiSettings = new HashMap();
        multiSettings.put("mode", ModeType.MULTIPLAYER);
        ArrayList<HumanPlayer> players = new ArrayList<HumanPlayer>();
        HumanPlayer test = new HumanPlayer("joe", 'a');
        players.add(test);
        multiSettings.put("players", players);
        multiState.init(multiSettings);
    }

    public void testGetActivePlayer()
    {
        HumanPlayer active = new HumanPlayer("default", 'a');
        assertEquals(active, solitaireState.getActivePlayer());
    }

    public void testGetDifficulty()
    {
        assertEquals(DifficultyType.HARD, solitaireState.getDifficulty());
    }

    public void testGetGameBoard()
    {
        assertNotNull(solitaireState.getGameBoard());
    }

    public void testGetGameMode()
    {
        assertEquals(ModeType.SOLITARE, solitaireState.getGameMode());
    }

    public void testGetGameStatus()
    {
        assertEquals(StatusType.BEGINNING, solitaireState.getGameStatus());
    }

    public void testGetPlayers()
    {
        ArrayList<AbstractPlayer> players = new ArrayList<AbstractPlayer>();
        players.add(new HumanPlayer("default", 'a'));
        ArrayList<AbstractPlayer> expect = solitaireState.getPlayers();
        for (int i = 0; i < players.size(); i++)
        {
            assertEquals(players.get(i), expect.get(i));
        }
    }

    public void testGetTimeRemaining()
    {
        assertEquals(60000L, timedState.getTimeRemaining());
    }

    public void testInitSolitaire()
    {
        CFState newState = new CFState(new CFLogic(), DeckType.NOSHUFFLE);
        HumanPlayer defaultPlayer = new HumanPlayer("default", 'a');

        newState.init(solitaireSettings);
        assertNotNull(newState.getGameBoard());
        assertEquals(defaultPlayer, newState.getActivePlayer());
        assertEquals(1, newState.getPlayers().size());
        assertEquals(DifficultyType.HARD, newState.getDifficulty());
        assertEquals(ModeType.SOLITARE, newState.getGameMode());
        assertEquals(StatusType.BEGINNING, newState.getGameStatus());
        assertEquals(0L, newState.getTimeRemaining());
    }

    public void testInitTimed()
    {
        CFState newState = new CFState(new CFLogic(), DeckType.NOSHUFFLE);
        HumanPlayer defaultPlayer = new HumanPlayer("default", 'a');

        newState.init(timedSettings);
        assertNotNull(newState.getGameBoard());
        assertEquals(defaultPlayer, newState.getActivePlayer());
        assertEquals(1, newState.getPlayers().size());
        assertEquals(DifficultyType.MEDIUM, newState.getDifficulty());
        assertEquals(ModeType.TIMED, newState.getGameMode());
        assertEquals(StatusType.BEGINNING, newState.getGameStatus());
        assertEquals(60000L, newState.getTimeRemaining());
    }

    public void testInitBeginner()
    {
        CFState newState = new CFState(new CFLogic(), DeckType.NOSHUFFLE);
        HashMap beginnerSettings = new HashMap();
        beginnerSettings.put("mode", ModeType.BEGINNER);
        beginnerSettings.put("difficulty", DifficultyType.EASY);
        beginnerSettings.put("timer", true);
        HumanPlayer defaultPlayer = new HumanPlayer("default", 'a');

        newState.init(beginnerSettings);
        assertNotNull(newState.getGameBoard());
        assertEquals(defaultPlayer, newState.getActivePlayer());
        assertEquals(1, newState.getPlayers().size());
        assertEquals(DifficultyType.EASY, newState.getDifficulty());
        assertEquals(ModeType.BEGINNER, newState.getGameMode());
        assertEquals(StatusType.BEGINNING, newState.getGameStatus());
        assertEquals(90000L, newState.getTimeRemaining());
    }

    public void testInitMultiplayerMode() throws Exception
    {
        assertNull(multiState.getActivePlayer());
        assertEquals(0L, multiState.getTimeRemaining());
        assertEquals(multiState.getGameStatus(), StatusType.BEGINNING);
        assertEquals(ModeType.MULTIPLAYER, multiState.getGameMode());
        ArrayList<AbstractPlayer> addedPlayers = multiState.getPlayers();
        HumanPlayer aPlayer = (HumanPlayer) addedPlayers.get(0);
        HumanPlayer test = new HumanPlayer("joe", 'a');
        assertEquals(aPlayer, test);

        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_PLAYER_FOCUS, test),
                                   MessageDest.CF_STATE);
        multiState.update(0L);
        assertEquals(multiState.getActivePlayer(), test);
        assertEquals(multiState.getGameStatus(), StatusType.RUNNING);
        assertEquals(multiState.getTimeRemaining(), 6000L);
    }

    public void testUpdate() throws Exception
    {
        HumanPlayer giveFocus = new HumanPlayer("default", 'a');
        MessageManager.sendMessage(new GameMessage(MessageType.SET_GAMESTATUS, StatusType.RUNNING),
                                    MessageDest.CF_STATE);
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_PLAYER_FOCUS, giveFocus), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.SELECT_CARD, 
                                    new Card("One", "Red", "Ovals", "Solid")), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.DESELECT_CARD, 
                                    new Card("One", "Red", "Ovals", "Solid")), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.INVALID_CLUSTER, null), MessageDest.CF_ALL);
        timedState.update(1000L);
        assertEquals(StatusType.RUNNING, timedState.getGameStatus());
        assertEquals(59000L, timedState.getTimeRemaining());
        timedState.update(60000L);
        assertEquals(StatusType.BEGINNING, timedState.getGameStatus());
        assertEquals(60000L, timedState.getTimeRemaining());
    }

    /**
     * Fixes Ticket #149
     * @throws java.lang.Exception
     */
    public void testChangeSettingBeforeInit() throws Exception
    {
        HashMap settings = new HashMap();
        settings.put("mode", ModeType.TIMED);
        settings.put("difficulty", DifficultyType.MEDIUM);
        assertEquals(ModeType.SOLITARE, solitaireState.getGameMode());
        MessageManager.sendMessage(new GameMessage(MessageType.GAME_START, settings),
                                    MessageDest.CF_STATE);
        solitaireState.update(0L);
        assertEquals(ModeType.TIMED, solitaireState.getGameMode());
    }

    /**
     * Fixes Ticket #151
     * @throws java.lang.Exception
     */
    public void testResetAfterValidCluster() throws Exception
    {
        MessageManager.sendMessage(new GameMessage(MessageType.SET_GAMESTATUS, StatusType.RUNNING),
                                    MessageDest.CF_STATE);
        timedState.update(1000L);
        assertEquals(59000L, timedState.getTimeRemaining());
        MessageManager.sendMessage(new GameMessage(MessageType.VALID_CLUSTER, null),
                                    MessageDest.CF_STATE);
        timedState.update(0L);
        assertEquals(60000L, timedState.getTimeRemaining());
    }

    /**
     * Fixes Ticket #155
     */
    public void testUpdateBeforeInit() throws Exception
    {
        try
        {
            timedState.update(0L);
        }
        catch (NullPointerException e)
        {
            fail();
        }
        timedState.init(timedSettings);
        timedState.update(0L);
    }
    
    /**
     * Fixes Ticket #176
     * @throws java.lang.Exception
     */
    public void testActivePlayerSetNone() throws Exception
    {
        assertEquals(0, solitaireState.getActivePlayer().getScore());
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_PLAYER_FOCUS, null),
                                    MessageDest.CF_STATE);
        solitaireState.update(0L);
        assertNull(solitaireState.getActivePlayer());
        MessageManager.sendMessage(new GameMessage(MessageType.INCREMENT_SCORE, null),
                                    MessageDest.CF_STATE);
        solitaireState.update(0L);
        assertEquals(0, solitaireState.getActivePlayer().getScore());
        
    }
    
    /**
     * Fixes Ticket #178
     * @throws java.lang.Exception
     */
    public void testBeginnerDeckSet() throws Exception
    {
        HashMap settings = new HashMap();
        settings.put("mode", ModeType.BEGINNER);
        settings.put("difficulty", DifficultyType.EASY);
        settings.put("timer", true);
        MessageManager.sendMessage(new GameMessage(MessageType.GAME_START, settings),
                                   MessageDest.CF_STATE);
        timedState.update(0L);
        assertEquals(DeckType.BEGINNER, timedState.getDeckStyle());
    }

    /**
     * Fixes ticket #239
     * @throws java.lang.Exception
     */
    public void testResetTimerInMulti() throws Exception
    {
        HumanPlayer test = new HumanPlayer("joe", 'a');
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_PLAYER_FOCUS, test),
                                   MessageDest.CF_STATE);
        multiState.update(3000L);
        assertEquals(multiState.getTimeRemaining(), 3000L);
        MessageManager.sendMessage(new GameMessage(MessageType.VALID_CLUSTER, null),
                                   MessageDest.CF_STATE);
        multiState.update(0L);
        assertEquals(multiState.getTimeRemaining(), 0L);
    }

    /**
     * Fixes Ticket #246
     * @throws java.lang.Exception
     */
    public void testDecrementScoreErrorInMulti() throws Exception
    {
        try
        {
            MessageManager.sendMessage(new GameMessage(MessageType.DECREMENT_SCORE, null),
                                        MessageDest.CF_STATE);
            multiState.update(0L);
        }
        catch (Exception e)
        {
            fail();
        }
    }

    /**
     * Fixes Ticket #274
     */
    public void testGetTimeSolitaireMode()
    {
        assertEquals(solitaireState.getTimeRemaining(), 0L);
    }
}
