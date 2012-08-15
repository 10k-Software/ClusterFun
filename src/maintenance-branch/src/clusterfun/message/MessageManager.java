package clusterfun.message;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * The MessageManager class models a messaging system used between the different
 * classes throughout the program.  It allows classes to request changes
 * and actions throughout the program while providing a sequential order to
 * execute changes and actions for requested classes
 * 
 * @author Andrew Chan
 * @version 0.1
 * @.date 2008-12-01
 */
//  Version History
//      12/2/08 Andrew Chan added algorithm pseudocode
public final class MessageManager
{
    /**
     * A map for all the messageboxes per class
     */
    private static HashMap<GameMessage.MessageDest, ArrayList> msgList = 
            new HashMap<GameMessage.MessageDest, ArrayList>();

    /**
     * Constructor for MessageManager
     */
    private MessageManager()
    {
        
    }
    
    static
    {
        //FOR EACH messagebox destination
        for(GameMessage.MessageDest messagebox 
            : GameMessage.MessageDest.values())
        {
                //ADD messagebox to destination
                msgList.put(messagebox, new ArrayList<GameMessage>());
        }
    }
    
    /**
     * Stores a message in the addressed messagebox or messageboxes
     * 
     * @param message   the message to be sent
     * @param recipients     the recipients of the message
     */
    public static void sendMessage(GameMessage message, 
                                   GameMessage.MessageDest... recipients)
    {
        // FOR EACH specified recipient
        for(GameMessage.MessageDest recipient : recipients)
        {
            // IF GAME_END is sent
            if(message.getType().equals(GameMessage.MessageType.GAME_END))
            {
                // DELETE all messages
                clearMessages();
            }
            // ENDIF
            // IF message is sent to CF_ALL
            if(GameMessage.MessageDest.CF_ALL.compareTo(recipient) == 0)
            {
                // FOR EACH possible destination
                for(GameMessage.MessageDest everybody : GameMessage.MessageDest.values())
                {
                    // ADD message to mailbox destination
                    msgList.get(everybody).add(message);
                }
                // ENDFOR
            }
            // ELSE
            else
            {
                // ADD message to specified mailbox
                msgList.get(recipient).add(message);
            }
            // ENDIF
        }
        // ENDFOR
    }
    
    /**
     * Requests all messages for the recipient
     * 
     * @param requester the name of the recipient
     * @return  all the messages for the recipient
     */
    public static ArrayList<GameMessage> getMessages(
            GameMessage.MessageDest requester)
    {
        // CLONE requested messagebox
        ArrayList<GameMessage> requestedMessages = 
                new ArrayList<GameMessage>(msgList.get(requester));
        
        // DELETE all messages in the messagebox
        msgList.get(requester).clear();
        
        // RETURN cloned messagebox
        return requestedMessages;
    }
    
    /**
     * Clears all messages from storage
     */
    public static void clearMessages()
    {
        // FOR EACH messagebox
        for(GameMessage.MessageDest messagebox 
            : GameMessage.MessageDest.values())
        {
            // DELETE all messages in the messagebox
            msgList.get(messagebox).clear();
        }
        // ENDFOR
    }
}
