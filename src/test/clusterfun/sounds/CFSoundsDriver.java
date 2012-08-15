package clusterfun.sounds;

import org.lwjgl.LWJGLException;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.message.GameMessage.MessageDest;
import clusterfun.message.GameMessage.MessageType;
import clusterfun.sound.CFSound;

/**
 * CFSoundsDriver is the driver that tests the clas CFSound
 * 
 * @author Lisa Hunter
 *
 */
public class CFSoundsDriver 
{
    CFSound sound;
    
    public CFSoundsDriver() throws Exception
    {
        sound = new CFSound();
        sound.init();
    }
    public static void main(String[] args) 
    {
        try 
        {
            CFSoundsDriver cf = new CFSoundsDriver();
            cf.testCurrentSounds();
        } 
        catch (LWJGLException e) 
        {
            e.printStackTrace();
            
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }    
    }
    
    public void testCurrentSounds() throws Exception
    {
        MessageManager.sendMessage(new GameMessage(MessageType.SELECT_CARD, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND); 
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.DESELECT_CARD, null),
                    MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_AI_CLUSTER, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.REQUEST_DEAL, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.REQUEST_HINT, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.CHECK_GAME_OVER, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.GAME_START, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.PAUSE, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.UNPAUSE, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.INCREMENT_SCORE, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.DECREMENT_SCORE, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_PLAYER_FOCUS, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_HINT, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.AFFIRM_DEAL, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.SELECT_CLUSTER, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.REMOVE_CLUSTER, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.INVALID_CLUSTER, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.VALID_CLUSTER, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.EXPLAIN_INVALID_CLUSTER, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.DEAL_CARDS, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.DEAL_FAIL, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.OUT_OF_TIME, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.GAME_END, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.PROGRAM_END, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        MessageManager.sendMessage(new GameMessage(MessageType.PLAY_FANFARE, null),
                MessageDest.CF_SOUND, MessageDest.CF_SOUND);
        sound.update(0);
        
        Thread.sleep(2000);
    }

}
