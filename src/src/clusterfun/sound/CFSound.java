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
 * @version 2.0
 * @.date 2009-3-3
 *
 */
//  Version History
//      2/12/09 Lisa Hunter added algorithm pseudocode
public class CFSound
{
    /**
     * Enumeration for the waves files used in CFSound
     */
    public enum SoundFile
    {
        /** The success sound */
        SUCCESS("resources/sounds/success.wav", 0),
        
        /** The affirm sound */
        AFFIRM("resources/sounds/affirm.wav", 1),
        
        /** The bad sound */
        BAD("resources/sounds/bad.wav", 2),
        
        /** The cancel sound */
        CANCEL("resources/sounds/cancel.wav", 3),
        
        /** The move sound */
        MOVE("resources/sounds/move.wav", 4),
        
        /** The move sound */
        FANFARE("resources/sounds/fanfare.wav", 5);
        
        /** The file path for the wav sound */
        private final String file;
        
        /** The index in the buffer */
        private final int num;
        
        /** Constructs a new SoundFiles */
        SoundFile(String file, int num)
        {
            this.file = file;
            this.num = num;
        }
    }
    
    /** Total Number of sound files */
    private static final int kNumOfFiles = 6;
    
    /** Buffers hold sound data */
    private IntBuffer buffer = BufferUtils.createIntBuffer(kNumOfFiles);

    /** Sources are points emitting sound */
    private IntBuffer source = BufferUtils.createIntBuffer(kNumOfFiles);
    
    /** Set amount for arrays in OpenAL */
    private static final int kAlSize = 3;
    
    /** Position of the source sound */
    private FloatBuffer sourcePos = 
        BufferUtils.createFloatBuffer(kAlSize).put(new float[] {0.0f, 0.0f, 0.0f});
    
    /** Velocity of the source sound */
    private FloatBuffer sourceVel;

    /** Position of the listener */
    private FloatBuffer listenerPos;

    /** Velocity of the listener */
    private FloatBuffer listenerVel;

    /** Orientation of the listener */
    private FloatBuffer listenerOri;  
    
    /** Error bit */
    private int error;
    
    /**
     * Constructs a new CFSound
     */
    public CFSound() 
    {
        // SET the error bit to false
        error = AL10.AL_TRUE;
        
        // SET the source velocity
        sourceVel = BufferUtils.createFloatBuffer(kAlSize).put(
                new float[] {0.0f, 0.0f, 0.0f});
        // SET the listener position
        listenerPos = BufferUtils.createFloatBuffer(kAlSize).put(
                new float[] {0.0f, 0.0f, 0.0f});
        // SET the listener velocity
        listenerVel = BufferUtils.createFloatBuffer(kAlSize).put(
                new float[] {0.0f, 0.0f, 0.0f});
        // SET the listener's orientation
        listenerOri = BufferUtils.createFloatBuffer(kAlSize+kAlSize).put(
                new float[] {0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f});
        
        // FLIP all of the buffers
        sourcePos.flip();
        sourceVel.flip();
        listenerPos.flip();
        listenerVel.flip();
        listenerOri.flip();
    }
    
    /**
     * Load all of the necessary data for OpenAL
     *
     * @return the status of the AL module
     */
    public int loadALData() 
    {
        // LOAD all of the wav data into the buffer
        AL10.alGenBuffers(buffer);

        // IF there has been an error
        if(AL10.alGetError() != AL10.AL_NO_ERROR)
        {
            // SET the flag to false
            error = AL10.AL_FALSE;
        }
        
        // IF the flag is true
        if(error != AL10.AL_FALSE)
        {
            // FOR EACH soundfile currently being used
            for(SoundFile fileName : SoundFile.values())
            {
                // OBTAIN the wavefile information
                WaveData waveFile = WaveData.create(fileName.file);
                
                // ADD the buffer data to the AL
                AL10.alBufferData(buffer.get(fileName.num), 
                        waveFile.format, waveFile.data, waveFile.samplerate);
                
                // REMOVE the wave file information
                waveFile.dispose();
            }
            

            // Bind the buffer with the source.
            AL10.alGenSources(source);
        }
        
        // IF there was an error
        if(AL10.alGetError() != AL10.AL_NO_ERROR)
        {
            // SET the flag to false
            error = AL10.AL_FALSE;
        }
        
        // IF the flag is true
        if(error != AL10.AL_FALSE)
        {
            // FOR every file in the buffer
            for(int inc = 0; inc < kNumOfFiles; inc++)
            {
                // SET the buffer information
                AL10.alSourcei(source.get(inc), AL10.AL_BUFFER,   buffer.get(inc));
                // SET the pitch information
                AL10.alSourcef(source.get(inc), AL10.AL_PITCH, 1.0f);
                // SET the gain information
                AL10.alSourcef(source.get(inc), AL10.AL_GAIN, 1.0f);
                // SET the position information
                AL10.alSource(source.get(inc), AL10.AL_POSITION, sourcePos);
                // SET the velocity information
                AL10.alSource(source.get(inc), AL10.AL_VELOCITY, sourceVel);
            }
        }  
        
        // IF there was an errors
        if(AL10.alGetError() != AL10.AL_NO_ERROR)
        {
            // SET the flag to false
            error = AL10.AL_FALSE;
        }
        
        return error;
    }  
    
    /**
     * Set all of the listener values
     *
     */
    public void setListenerValues() 
    {
        AL10.alListener(AL10.AL_POSITION,    listenerPos);
        AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
        AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
    }  

    
    /**
     * Initializes the sound player
     * 
     * @throws LWJGLException OpenAL error
     */
    public void init() throws LWJGLException 
    {
        // IF there have been no errors
        if(error == AL10.AL_TRUE)
        {
            // IF it hasn't already been created
            if(!AL.isCreated())
            {
                // CREATE the OpenAL and clear the error bit
                AL.create(null, 15, 22050, true);
            }

    
            // OBTAIN any errors
            AL10.alGetError();
    
            // IF the wav data can not be loaded
            if(loadALData() == AL10.AL_FALSE) 
            {
                // PRINT a message and exit
                System.out.println("Sound loading failed");
            }
            // ELSE the data can be loaded
            else
            {
                // SET the listener values
                setListenerValues();  
            }
        }
    }
    
    /**
     * Updates the sound player with the specified time
     * 
     * @param time the amount of time in ms since the last update
     */
    public void update(long time)
    {
        // IF there have been no errors
        if(error == AL10.AL_TRUE)
        {
            // OBTAIN the list of messages for CFSound
            ArrayList<GameMessage> list = 
                    MessageManager.getMessages(GameMessage.MessageDest.CF_SOUND);
            
            // FOR EACH message in the list
            for(GameMessage msg : list)
            {
                // DETERMINE the file type 
                switch(msg.getType()) 
                {
                    // IF a card is selected then play a move sound
                    case SELECT_CARD:  
                        AL10.alSourcePlay(source.get(SoundFile.MOVE.num));
                        break;
                        
                    // IF a card is deselected then play a cancel sound
                    case DESELECT_CARD:  
                        AL10.alSourcePlay(source.get(SoundFile.CANCEL.num)); 
                        break;
                        
                    // IF a game is started then play an affirm sound
                    case GAME_START: 
                        AL10.alSourcePlay(source.get(SoundFile.AFFIRM.num));
                        break;
                        
                    // IF the score is incremented then play a success sound
                    case INCREMENT_SCORE:  
                        AL10.alSourcePlay(source.get(SoundFile.SUCCESS.num));
                        break;
                        
                    // IF the score is decremented then play a bad sound
                    case DECREMENT_SCORE:  
                        AL10.alSourcePlay(source.get(SoundFile.BAD.num));
                        break;
                        
                    // IF a cluster is invalid then play a bad sound
                    case INVALID_CLUSTER: 
                        AL10.alSourcePlay(source.get(SoundFile.BAD.num)); 
                        break;
                        
                    // IF a fanfare sound is requested to be played then play
                    // a fanfare sound
                    case PLAY_FANFARE:  
                        AL10.alSourcePlay(source.get(SoundFile.FANFARE.num)); 
                        break;
                        
                    default: 
                        break;
                }
            }

        }
    }
 
}

