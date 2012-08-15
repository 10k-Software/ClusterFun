package clusterfun.ui.entity;

import clusterfun.board.Card;
import clusterfun.lwjgl.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * The CardEntity class is the visual component of the Card class, allowing the
 * UI to correctly display every card without including useless information in
 * the Card class.
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-2-2
 */
public class CardEntity extends Entity
{
    // card texture
    private Texture texture;  
    
    // card object
    private Card card; 
    
    // starting texture coordinate for card
    private Vector2f startCoord; 
    
    // end texture coordinate for card
    private Vector2f endCoord; 
    
    // default position on the gameboard
    private String defaultPosition; 
    
    /**
     * Constructs the card entity
     * @param card card object
     * @param texture card texture
     * @param startCoord starting texture coordinate
     * @param endCoord end texture coordinate
     * @param defaultPosition default position on game board
     */
    public CardEntity(Card card, Texture texture, Vector2f startCoord, 
            Vector2f endCoord, String defaultPosition)
    {
        super();
        // SET the entity's card object as the given card
        this.card = card;
        // SET the entity's texture object to the given texture
        this.texture = texture;
        // ASSIGN the start image coordinate to the value calculated
        this.startCoord = startCoord;
        // ASSIGN the end image coordinate to the value calculated
        this.endCoord = endCoord;
        // ASSIGN the default board position to the card
        this.defaultPosition = defaultPosition;
    }
    
    /**
     * returns the card object associated with this visual representation
     * 
     * @return card card object
     */
    public Card getCardObj()
    {
        return card;
    }

    /**
     * Updates any data relating to the card
     * @param timeElapsed time elapsed since last update
     */
    @Override
    protected void updateEnt(long timeElapsed)
    {
        //NOTHING so far
    }

    /**
     * Calls the necessary graphical bindings to render the card
     */
    @Override
    protected void renderEnt(boolean picking)
    {
        final int height = 6;   // height of the card
        final int width = 4;    // width of the card
        
        if(!picking)
        {
            // BIND the current texture to be rendered
            texture.bind();
        }

        // BEGIN drawing a quad polygon
        GL11.glBegin(GL11.GL_QUADS);
            // DRAW lower left coordinate
            GL11.glTexCoord2f(startCoord.x, endCoord.y);
            GL11.glVertex3i(-width/2, -height/2, 0);
            
            // DRAW lower left coordinate
            GL11.glTexCoord2f(endCoord.x, endCoord.y);
            GL11.glVertex3i(width/2, -height/2, 0);
            
            // DRAW upper left coordinate
            GL11.glTexCoord2f(endCoord.x, startCoord.y);
            GL11.glVertex3i(width/2, height/2, 0);
            
            // DRAW upper right coordinate
            GL11.glTexCoord2f(startCoord.x, startCoord.y);
            GL11.glVertex3i(-width/2, height/2, 0);
            
        // FINISH drawing the quad
        GL11.glEnd();

    }
    
    /**
     * Returns the default card position string that relates to a vector on the 
     * game board
     * 
     * @return position default card position string
     */
    public String getDefaultCardPosition()
    {
        return defaultPosition;
    }
    
    /**
     * Changes the current default cards position so that the position returns to
     * the correct spot when deselected
     * 
     * @param pos position to change to
     */
    public void setDefaultCardPosition(String pos)
    {
        defaultPosition = pos;
    }
}
