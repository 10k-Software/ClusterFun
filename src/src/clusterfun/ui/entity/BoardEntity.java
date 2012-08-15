package clusterfun.ui.entity;

import java.io.IOException;
import clusterfun.lwjgl.Texture;
import clusterfun.lwjgl.TextureLoader;
import org.lwjgl.opengl.GL11;

/**
 * The BoardEntity class is the visual component of the Board class, allowing
 * the UI to correctly display every card without including useless information
 * in the Card class.
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-2-2
 */
public class BoardEntity extends Entity
{
    // game board opengl name
    private final int kBOARDNAME = 26; 
    
    // height of game board
    private final int kHEIGHT = 45; 
    
    // width of game board
    private final int kWIDTH = 50; 
    
    // horizontal texture wrap amount
    private final double kHORIZTEXMULT = 5; 
    
    // vertical texture wrap amount
    private final double kVERTTEXMULT = 4; 
    
    // game board texture
    private Texture texture; 

    /**
     * Constructs the board entity
     * @param imageLoc board image location
     * @throws java.lang.IOException image load exception
     */
    public BoardEntity(String imageLoc) throws IOException
    {
        // SET the opengl name to 26
        setName(kBOARDNAME);
        
        // CREATE a new texture loader
        TextureLoader loader = new TextureLoader();
        
        // LOAD the game board's image board
        texture = loader.getTexture(imageLoc);
    }
    
    
    /**
     * Constructs the board entity
     * @param texture board image texture
     * @throws java.lang.IOException image load exception
     */
    public BoardEntity(Texture texture) throws IOException
    {
        // SET the opengl name to 26
        setName(kBOARDNAME);
        
        // CREATE a new texture loader
        TextureLoader loader = new TextureLoader();
        
        // LOAD the game board's image board
        this.texture = texture;
    }

    /**
     * Updates any data relating to the board
     * @param timeElapsed time elapsed since last update
     */
    @Override
    protected void updateEnt(long timeElapsed)
    {
        //NOTHING so far
    }

    /**
     * Calls the necessary graphical bindings to render the board
     */
    @Override
    protected void renderEnt(boolean picking)
    {
        // IF pick rendering is enabled
        if(!picking)
        {
            // BIND the texture to openGL
            texture.bind();
        }
        // render the card
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2d(0, 0);
            GL11.glVertex3i(-kWIDTH/2, -kHEIGHT/2, 0);
            GL11.glTexCoord2d(kHORIZTEXMULT, 0.0d);
            GL11.glVertex3i(kWIDTH/2, -kHEIGHT/2, 0);
            GL11.glTexCoord2d(kHORIZTEXMULT, kVERTTEXMULT);
            GL11.glVertex3i(kWIDTH/2, kHEIGHT/2, 0);
            GL11.glTexCoord2d(0, kVERTTEXMULT);
            GL11.glVertex3i(-kWIDTH/2, kHEIGHT/2, 0);
        GL11.glEnd();

    }
}
