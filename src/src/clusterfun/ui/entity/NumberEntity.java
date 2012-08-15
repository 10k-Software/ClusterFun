package clusterfun.ui.entity;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import clusterfun.lwjgl.Texture;
import clusterfun.lwjgl.TextureLoader;
import org.lwjgl.util.vector.Vector2f;

/**
 * NumberEntity handles the numbers in the top left hand corner of the screen, 
 * constantly updating the visual representation of numbers.
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-2-2
 */
public class NumberEntity extends Entity
{

    // current texture
    private Texture texture; 
    
    // current string
    private String curStr = "0"; 
    
    // multiplication ratio for text size
    private final float kMULTRATIODEFAULT = 0.5f;
    
    // current mult ratio
    private float mult = kMULTRATIODEFAULT;
    
    /**
     * 
     * @param location location to put the entity
     * @param file location to grab the texture
     * @throws java.io.IOException texture loader exception
     */
    public NumberEntity(String location, String file) throws IOException
    {
        this(location, file, -1);
    }
    
    
    /**
     * 
     * @param location location to put the entity
     * @param tex texture
     * @throws java.io.IOException texture loader exception
     */
    public NumberEntity(String location, Texture tex) throws IOException
    {
        this(location, tex, -1);
    }
    
    /**
     * 
     * @param location location to put the entity
     * @param file location to grab the texture
     * @param mult text size multiplier
     * @throws java.io.IOException texture loader exception
     */
    public NumberEntity(String location, String file, float mult) throws IOException
    {
        this(location, new TextureLoader().getTexture(file), mult);
    }
    
        /**
     * 
     * @param location location to put the entity
     * @param tex texture
     * @param mult text size multiplier
     * @throws java.io.IOException texture loader exception
     */
    public NumberEntity(String location, Texture tex, float mult) throws IOException
    {
        // IF the mult value is not -1
        if(mult != -1)
        {
            this.mult = mult;
        }
        // RETRIEVE the opengl name
        int id = Location.getNameId(location);
        
        // RETRIEVE the given position
        Vector3f loc = Location.getPosition(location);
        
        // SET the position
        setStartPosition(loc);
        setEndPosition(loc);
        
        // SET the name to the opengl name given
        setName(id);
        
        // LOAD the currently given texture
        texture = tex;
    }
    
    /**
     * @see Entity
     * @param timeElapsed time elapsed since last update
     */
    @Override
    protected void updateEnt(long timeElapsed)
    {
        // NOTHING
    }
    
    /**
     * Sets current string
     * 
     * @param str string to set the current value to
     */
    public void setString(String str)
    {
        curStr = str;
    }

    /**
     * @see Entity
     */
    @Override
    protected void renderEnt(boolean picking)
    {        
        // RESET delta
        float delta = 0;

        // IF pick rendering is enabled
        if(!picking)
        {
        // BIND the current texture to be rendered
        texture.bind();
        }

        // FOR every character in the current timer string
        for(int index = 0; index < curStr.length(); index++)
        {
            // GRAB the TimerNumber value of the current character
            Number num = Number.get(curStr.charAt(index));
            
            // BEGIN drawing a quad polygon
            GL11.glBegin(GL11.GL_QUADS);
                // DRAW lower left coordinate
                GL11.glTexCoord2f(num.startCoord.x, num.startCoord.y);
                GL11.glVertex2f(delta, 0);
                
                // DRAW lower left coordinate
                GL11.glTexCoord2f(num.endCoord.x, num.startCoord.y);
                GL11.glVertex2f(delta + (num.width*mult), 0);
                
                // DRAW upper left coordinate
                GL11.glTexCoord2f(num.endCoord.x, num.endCoord.y);
                GL11.glVertex2f(delta + (num.width*mult), (num.height*mult));
                
                // DRAW upper right coordinate
                GL11.glTexCoord2f(num.startCoord.x, num.endCoord.y);
                GL11.glVertex2f(delta, (num.height*mult));

            // FINISH drawing the quad
            GL11.glEnd();
            
            // IPDATE text delta
            delta += (num.width*mult);
        }
    }
    
    /**
     * NumberEnum describes all information for each digit
     */
    enum Number
    {
        // Number "1"
        NUMBER_ONE('1', new Vector2f(0.0f, 0.0f), new Vector2f(0.1094f, 0.25f), 56f, 128f),
        // Number "2"
        NUMBER_TWO('2', new Vector2f(0.25f, 0.0f), new Vector2f(0.4102f, 0.25f), 82f, 128),
        // Number "3"
        NUMBER_THREE('3', new Vector2f(0.5f, 0.0f), new Vector2f(0.6602f, 0.25f), 82f, 128),
        // Number "4"
        NUMBER_FOUR('4', new Vector2f(0.75f, 0.0f), new Vector2f(0.9492f, 0.25f), 102f, 128),
        // Number "5"
        NUMBER_FIVE('5', new Vector2f(0.0f, 0.25f), new Vector2f(0.1641f, 0.5f), 84f, 128),
        // Number "6"
        NUMBER_SIX('6', new Vector2f(0.25f, 0.25f), new Vector2f(0.4180f, 0.5f), 86f, 128),
        // Number "7"
        NUMBER_SEVEN('7', new Vector2f(0.5f, 0.25f), new Vector2f(0.6563f, 0.5f), 80f, 128),
        // Number "8"
        NUMBER_EIGHT('8', new Vector2f(0.75f, 0.25f), new Vector2f(0.9219f, 0.5f), 88f, 128),
        // Number "9"
        NUMBER_NINE('9', new Vector2f(0.0f, 0.5f), new Vector2f(0.1680f, 0.75f), 86f, 128),
        // Number "0"
        NUMBER_ZERO('0', new Vector2f(0.25f, 0.5f), new Vector2f(0.4375f, 0.75f), 96f, 128),
        // Colon
        COLON(':', new Vector2f(0.5f, 0.5f), new Vector2f(0.5625f, 0.75f), 32f, 128),
        // Dash
        DASH('-', new Vector2f(0.75f, 0.5f), new Vector2f(0.8594f, 0.75f), 56f, 128);
        
        /**
         * Constructor for Number
         * @param timerChar timer character to bind to
         * @param startCoord start timer texture coordinate
         * @param endCoord end timer texture coorinate
         * @param width text width
         * @param height text height
         */
        Number(char bindChr, Vector2f startCoord, Vector2f endCoord, float width, float height)
        {
            this.bindChr = bindChr;
            this.startCoord = startCoord;
            this.endCoord = endCoord;
            this.width = width;
            this.height = height;
        }
        
        private char bindChr;
        private Vector2f startCoord;
        private Vector2f endCoord;
        private float width;
        private float height;

        /**
         * Return the end texture coordinate
         * 
         * @return endCoord end coordinate
         */
        public Vector2f getEndCoord()
        {
            return endCoord;
        }

        /**
         * Return the character height
         * 
         * @return height character height
         */
        public float getHeight()
        {
            return height;
        }

        /**
         * Return the texture start coordinate
         * 
         * @return startCoord texture start coordinate
         */
        public Vector2f getStartCoord()
        {
            return startCoord;
        }

        /**
         * Return the bound character
         * 
         * @return boundChar bound character
         */
        public char getBoundChar()
        {
            return bindChr;
        }

        /**
         * Return the character width
         * 
         * @return width character width
         */
        public float getWidth()
        {
            return width;
        }
        
        /**
         * Retrieve a TimerNumber enum based on the given character
         * 
         * @param chr
         * @return
         */
        static Number get(char chr)
        {
            Number result = null; // Resulting timer class
            
            // FOR every TimerNumber enum value
            for(Number number : values())
            {
                // IF the character of the current enum matches the given character
                if(number.getBoundChar() == chr)
                {
                    // SET the result to the current enum
                    result = number;
                }
            }
            return result;
        }
    }

}
