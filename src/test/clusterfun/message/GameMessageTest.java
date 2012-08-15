package clusterfun.message;

import junit.framework.TestCase;

/**
 *
 * @author Andrew Chan
 */
public class GameMessageTest extends TestCase{

    public GameMessageTest() {
    }

    /**
     * Test the GameMessage class
     */
    public void testGameMessage() 
    {
        GameMessage instance = new GameMessage(
                GameMessage.MessageType.ADD_PLAYER, 1);
        assertTrue(instance.getType().equals(
                GameMessage.MessageType.ADD_PLAYER));
        assertEquals(instance.getValue(), 1);
        assertTrue(instance.toString().equals(
                GameMessage.MessageType.ADD_PLAYER.toString()));
    }
}