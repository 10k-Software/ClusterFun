package clusterfun.sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;




/** 
 * CFSound is a sound manager that plays sounds by request of other modules.
 * Messages are sent to the sound manager, who will play them.
 * 
 * @author Lisa Hunter
 * @version 0.1
 * @.date 2009-2-18
 *
 */
//  Version History
//      2/12/09 Lisa Hunter added algorithm pseudocode
public class CFSound
{
    
    
    /**
     * Constructs a new CFSound
     */
    public CFSound() 
    {
        System.out.println("CFSound has been constructed");
    }
    
    
        
      
    
    
    /**
     * Initializes the sound player
     * 
     * @throws LWJGLException OpenAL error
     */
    public void init() throws LWJGLException 
    {
        System.out.println("CFSound has been initialized");
        
    }
    
    /**
     * Updates the sound player with the specified time
     * 
     * @param time the amount of time in ms since the last update
     */
    public void update(long time)
    {
        

        System.out.println("CFSound has been updated");
    }
 
}

