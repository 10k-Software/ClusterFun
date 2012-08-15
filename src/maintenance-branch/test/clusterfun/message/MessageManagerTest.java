package clusterfun.message;

import clusterfun.message.GameMessage.MessageDest;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author Andrew Chan
 */
public class MessageManagerTest extends TestCase{

    public MessageManagerTest() {
    }

    /**
     * Test the MessageManager class
     */
    public void testMessageManager() 
    {
        GameMessage message = new GameMessage(
                GameMessage.MessageType.ADD_PLAYER, 1);
        ArrayList<GameMessage> expectedResult = new ArrayList<GameMessage>();
        ArrayList<GameMessage> actualResult;
        expectedResult.add(message);
        MessageManager.sendMessage(message, MessageDest.CF_AI);
        
        actualResult = MessageManager.getMessages(MessageDest.CF_AI);
        for(int index = 0; index < expectedResult.size(); index ++)
        {
            assertTrue(actualResult.get(index).toString().equals(
                    expectedResult.get(index).toString()));
        }

        MessageManager.sendMessage(message, MessageDest.CF_AI);
        MessageManager.sendMessage(
                new GameMessage(GameMessage.MessageType.GAME_END, 1), 
                MessageDest.CF_AI);
        expectedResult.clear();
        expectedResult.add(
                new GameMessage(GameMessage.MessageType.GAME_END, 1));
        
        actualResult = MessageManager.getMessages(MessageDest.CF_AI);
        for(int index = 0; index < expectedResult.size(); index ++)
        {
            assertTrue(actualResult.get(index).toString().equals(
                    expectedResult.get(index).toString()));
        }

        MessageManager.sendMessage(message, MessageDest.CF_AI);
        MessageManager.clearMessages();
        MessageManager.sendMessage(message, MessageDest.CF_AI);
        expectedResult.clear();
        expectedResult.add(message);
        
        actualResult = MessageManager.getMessages(MessageDest.CF_AI);
        for(int index = 0; index < expectedResult.size(); index ++)
        {
            assertTrue(actualResult.get(index).toString().equals(
                    expectedResult.get(index).toString()));
        }
    }
}