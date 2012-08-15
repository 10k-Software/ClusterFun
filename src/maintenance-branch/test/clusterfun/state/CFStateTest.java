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
    private CFState state1;
    private CFState state2;

    public CFStateTest(String name)
    {
        super(name);
    }
    
    protected void setUp()
    {
        state1 = new CFState(new CFLogic(), DeckType.NOSHUFFLE);
        state2 = new CFState(new CFLogic(), DeckType.NOSHUFFLE);
        state1.init(null);
    }

    public void testGetActivePlayer()
    {
        HumanPlayer active = new HumanPlayer("default");
        assertEquals(active, state1.getActivePlayer());
    }

    public void testGetDifficulty()
    {
        assertEquals(DifficultyType.EASY, state1.getDifficulty());
    }

    public void testGetGameBoard()
    {
        assertNotNull(state1.getGameBoard());
    }

    public void testGetGameMode()
    {
        assertEquals(ModeType.SOLITARE, state1.getGameMode());
    }

    public void testGetGameStatus()
    {
        assertEquals(StatusType.BEGINNING, state1.getGameStatus());
    }

    public void testGetPlayers()
    {
        ArrayList<AbstractPlayer> players = new ArrayList<AbstractPlayer>();
        players.add(new HumanPlayer("default"));
        ArrayList<AbstractPlayer> expect = state1.getPlayers();
        for (int i = 0; i < players.size(); i++)
        {
            assertEquals(players.get(i), expect.get(i));
        }
    }

    public void testGetTimeRemaining()
    {
        assertEquals(60000L, state1.getTimeRemaining());
    }

    public void testInit()
    {
        System.out.println("Testing init");
        state2.init(null);
        assertNotNull(state2.getActivePlayer());
        assertNotNull(state2.getGameBoard());
        assertEquals(1, state2.getPlayers().size());
        assertEquals(DifficultyType.EASY, state2.getDifficulty());
        assertEquals(ModeType.SOLITARE, state2.getGameMode());
        assertEquals(StatusType.BEGINNING, state2.getGameStatus());
    }

    public void testUpdate() throws Exception
    {
        System.out.println("Testing update");
        MessageManager.sendMessage(new GameMessage(MessageType.SET_GAMEMODE, ModeType.TIMED), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.SET_TIME, new Long(30L)), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.PAUSE, null), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_PLAYER_FOCUS, "default"), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.SELECT_CARD, 
                                    new Card("One", "Red", "Ovals", "Solid")), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.DESELECT_CARD, 
                                    new Card("One", "Red", "Ovals", "Solid")), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.INVALID_CLUSTER, null), MessageDest.CF_ALL);
        state1.update(0L);
        assertEquals(ModeType.TIMED, state1.getGameMode());
        assertEquals(30L, state1.getTimeRemaining());
        assertEquals(StatusType.PAUSED, state1.getGameStatus());
        MessageManager.sendMessage(new GameMessage(MessageType.UNPAUSE, null), MessageDest.CF_ALL);
        MessageManager.sendMessage(new GameMessage(MessageType.SET_GAMEMODE, ModeType.SOLITARE), MessageDest.CF_STATE);
        state1.update(31L);
        assertEquals(StatusType.BEGINNING, state1.getGameStatus());
        assertEquals(60000L, state1.getTimeRemaining());
    }

    /**
     * Fixes Ticket #149
     * @throws java.lang.Exception
     */
    public void testChangeSettingBeforeInit() throws Exception
    {
        HashMap settings = new HashMap();
        settings.put("mode", ModeType.TIMED);
        MessageManager.sendMessage(new GameMessage(MessageType.GAME_START, settings),
                                    MessageDest.CF_STATE);
        state1.update(0L);
        assertEquals(ModeType.TIMED, state1.getGameMode());
    }

    /**
     * Fixes Ticket #151
     * @throws java.lang.Exception
     */
    public void testResetAfterValidCluster() throws Exception
    {
        MessageManager.sendMessage(new GameMessage(MessageType.GAME_START, null),
                                    MessageDest.CF_STATE);
        MessageManager.sendMessage(new GameMessage(MessageType.SET_GAMESTATUS, StatusType.RUNNING),
                                    MessageDest.CF_STATE);
        state1.update(1000L);
        assertEquals(59000L, state1.getTimeRemaining());
        MessageManager.sendMessage(new GameMessage(MessageType.VALID_CLUSTER, null),
                                    MessageDest.CF_STATE);
        state1.update(0L);
        assertEquals(60000L, state1.getTimeRemaining());
    }

    /**
     * Fixes Ticket #155
     */
    public void testUpdateBeforeInit() throws Exception
    {
        try
        {
            state2.update(0L);
        }
        catch (NullPointerException e)
        {
            fail();
        }
        state2.init(null);
        state2.update(0L);
    }
    
    /**
     * Fixes Ticket #176
     * @throws java.lang.Exception
     */
    public void testActivePlayerSetNone() throws Exception
    {
        assertEquals(0, state1.getActivePlayer().getScore());
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_PLAYER_FOCUS, null),
                                    MessageDest.CF_STATE);
        state1.update(0L);
        assertNull(state1.getActivePlayer());
        MessageManager.sendMessage(new GameMessage(MessageType.INCREMENT_SCORE, null),
                                    MessageDest.CF_STATE);
        state1.update(0L);
        assertEquals(0, state1.getActivePlayer().getScore());
        
    }
    
    /**
     * Fixes Ticket #178
     * @throws java.lang.Exception
     */
    public void testBeginnerDeckSet() throws Exception
    {
        HashMap settings = new HashMap();
        settings.put("mode", ModeType.BEGINNER);
        MessageManager.sendMessage(new GameMessage(MessageType.GAME_START, settings),
                                   MessageDest.CF_STATE);
        state2.update(0L);
        assertEquals(DeckType.BEGINNER, state2.getDeckStyle());
    }
}
