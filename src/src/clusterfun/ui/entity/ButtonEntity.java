package clusterfun.ui.entity;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import clusterfun.lwjgl.Texture;
import clusterfun.lwjgl.TextureLoader;
import org.lwjgl.util.vector.Vector2f;

/**
 * ButtonEntity handles the buttons available on the screen.
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-2-2
 */
public class ButtonEntity extends Entity
{
    // current texture
    private Texture texture;
   
    // start texture coordinate
    private Vector2f startCoord;
    
    // end texture coordinate
    private Vector2f endCoord;
    
    // button width
    private float width;
    
    // button height
    private float height;
    
    
        /**
     * 
     * @param location location to put the entity
     * @param tex entity texture
     * @param topLeft top left coordinate within texture
     * @param dimensions image dimensions
     * @throws java.io.IOException texture loader exception
     */
    public ButtonEntity(String location, Vector2f topLeft, Vector2f dimensions,
            Texture tex) throws IOException
    {
        int tWidth = tex.getImageWidth();
        int tHeight = tex.getImageHeight();
        width = dimensions.x;
        height = dimensions.y;
        this.startCoord = new Vector2f(topLeft.x / tWidth, topLeft.y / tHeight);
        this.endCoord = new Vector2f((topLeft.x + dimensions.x) / tWidth,
                (topLeft.y + dimensions.y) / tHeight);

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
     * 
     * @param location location to put the entity
     * @param tex entity texture
     * @param width button width
     * @param height button height
     * @param startCoord start texture coordinate
     * @param endCoord end texture coordinate
     * @throws java.io.IOException texture loader exception
     */
    public ButtonEntity(String location, Vector2f startCoord, Vector2f endCoord,
            float width, float height, Texture tex) throws IOException
    {
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.width = width;
        this.height = height;
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
     * 
     * @param location location to put the entity
     * @param file location to grab the texture
     * @param width button width
     * @param height button height
     * @param startCoord start texture coordinate
     * @param endCoord end texture coordinate
     * @throws java.io.IOException texture loader exception
     */
    public ButtonEntity(String location, Vector2f startCoord, Vector2f endCoord,
            float width, float height, String file) throws IOException
    {
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.width = width;
        this.height = height;
        // RETRIEVE the opengl name
        int id = Location.getNameId(location);
        
        // RETRIEVE the given position
        Vector3f loc = Location.getPosition(location);
        
        // SET the position
        setStartPosition(loc);
        setEndPosition(loc);
        
        // SET the name to the opengl name given
        setName(id);
        
        // CREATE a new texture loader
        TextureLoader loader = new TextureLoader();
        
        // LOAD the currently given texture
        texture = loader.getTexture(file);
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
     * @see Entity
     */
    @Override
    protected void renderEnt(boolean picking)
    {
        // IF pick rendering is enabled
        if(!picking)
        {
            // BIND the current texture to be rendered
            texture.bind();
        }

        // BEGIN drawing a quad polygon
        GL11.glBegin(GL11.GL_QUADS);
        
            // DRAW lower left coordinate
            GL11.glTexCoord2f(startCoord.x, startCoord.y);
            GL11.glVertex2f(0, 0);

            // DRAW lower left coordinate
            GL11.glTexCoord2f(endCoord.x, startCoord.y);
            GL11.glVertex2f(width, 0);

            // DRAW upper left coordinate
            GL11.glTexCoord2f(endCoord.x, endCoord.y);
            GL11.glVertex2f(width, height);

            // DRAW upper right coordinate
            GL11.glTexCoord2f(startCoord.x, endCoord.y);
            GL11.glVertex2f(0, height);

        // FINISH drawing the quad
        GL11.glEnd();

    }
}
