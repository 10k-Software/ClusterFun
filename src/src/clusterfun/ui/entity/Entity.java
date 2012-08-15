package clusterfun.ui.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * The Entity class is the visual counterpart to the abstract classes such as
 * Card and 10 10 GameBoard.  Entity classes include visual information such as loaded
 * textures and geometric/world information.
 * 
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-2-2
 */
public abstract class Entity
{

    /* Starting position for entity */
    private Vector3f startPosition; 
    
    /* Destination position for entity */
    private Vector3f endPosition; 
    
    /* Scalar to determine entity point between two
     * positions */
    private float animScalar; 
    
    /* Determines speed of scalar change */
    private float animSpeed; 
    
    /* Whether or not the object is moving */
    private boolean animating; 
    
    /* Whether or not the object is to be rendered */
    private boolean rendering; 
    
    /* A millisecond delay before the animation takes 
     * place */
    private long animDelay; 
    
    // unique name id used by opengl when 'picking' mouse position
    private int name; 
    
    // one hundred
    private final float kONEHUNDRED = 100f;
    
    // milliseconds in one second
    private final long kMILLISINSECS = 1000;

    /**
     * Sets the openGL name for the entity which is used to discern from 
     * different objects on the screen
     * 
     * @param name openGL unique identifier name
     */
    public void setName(int name)
    {
        this.name = name;
    }
    
    /**
     * Retrieves the openGL name set for this entity
     * 
     * @return name openGL unique identifier name
     */
    public int getName()
    {
        return name;
    }

    /**
     * Renders the current entity, forcing a false picking setting
     */
    public void render()
    {
        render(false);
    }
    
    /**
     * Called by the owning game state, render calls the associated opengl
     * bindings to visually draw the entity on the screen
     *
     * @param picking pick render mode
     */
    public void render(boolean picking)
    {
        // IF the entity is set to render
        if(this.rendering)
        {
            GL11.glLoadName(name);
            // GET the current position
            Vector3f cur = getPosition();
            // PUSH the current translation matrix onto the stack
            GL11.glPushMatrix();
            // TRANSLATE the entity to the correct point in space
            GL11.glTranslatef(cur.x, cur.y, cur.z);
            // CALL the renderEnt method to render the entity
            renderEnt(picking);
            // POP the previous matrix from the stack
            GL11.glPopMatrix();
        }
    }
    
    /**
     * Allows in-flight entities to change course and resets the scalar
     */
    public void resetAnimation()
    {
        startPosition = getPosition();
        animScalar = 0;
    }

    /**
     * updates the entity's state and location before calling other entity-
     * defined update methods
     *
     * @param timeElapsed time that has elapsed
     */
    public void update(long timeElapsed)
    {
        // Milliseconds in a second
        final double timePerMilli = 1000d;

        // Max unit scalar
        final float maxScalar = 100;
        // IF the entity is currently animating
        if(animating)
        {
            // IF the animation delay is zeroed out
            if(animDelay == 0)
            {
                // UPDATE the scalar according to the time elapsed
                animScalar += animSpeed * (float)(timeElapsed / timePerMilli);

                // IF the scalar is over 100
                if(animScalar > maxScalar)
                {
                    // SET the scalar to 100
                    animScalar = maxScalar;
                    // DISABLE animations
                    animating = false;
                    
                    // SET start position to end position
                    startPosition = new Vector3f(endPosition);
                }
            // IF the card is not animating
            }
            else
            {
                // DECREMENT the delay by the time elapsed
                animDelay -= timeElapsed;
                // IF the delay is less than zero
                if(animDelay < 0)
                {
                    // SET the delay to zero
                    animDelay = 0;
                }
            }
        }
        // CALL the updateEnt method defined by the inheriting class
        updateEnt(timeElapsed);
    }

    /**
     * Used by the inheriting classes to update the entity
     *
     * @param timeElapsed the time that has elapsed since last update
     */
    protected abstract void updateEnt(long timeElapsed);

    /**
     * Called by the render() function.  Allows inheriting classes to specify
     * how the entity is to be rendered
     * 
     * @param picking whether or not picking is enabled
     */
    protected abstract void renderEnt(boolean picking);

    /**
     * Constructs the base components of the entity
     */
    public Entity()
    {
        startPosition = Location.DEFAULT.getPosition();
        endPosition = new Vector3f();
        animating = false;
        rendering = true;
        animScalar = 0;
        animSpeed = 0;
    }

    /**
     * Calculates the current position between the start and end positions
     * provided.
     * 
     * @return position current position based on the animation's scalar
     */
    public Vector3f getPosition()
    {
        Vector3f r1 = new Vector3f(); // temporary vector
        Vector3f r2 = new Vector3f(); // temporary vector
        
        // SUBTRACT the start position and the end position
        Vector3f.sub(endPosition, startPosition, r1);
        
        // MULTIPLY the resulting vector by the current scalar
        r1.x *= animScalar / kONEHUNDRED;
        r1.y *= animScalar / kONEHUNDRED;
        r1.z *= animScalar / kONEHUNDRED;
        
        // ADD the result to the start position
        Vector3f.add(startPosition, r1, r2);
        
        // RETURN end result
        return r2;
    }

    /**
     * Sets the current animation scalar, which determines the location of the
     * entity between two points
     * 
     * @param scalar animation scalar (between 0 and 100)
     */
    public void setAnimationScalar(float scalar)
    {
        animScalar = scalar;
    }

    /**
     * Returns the current animation scalar
     *
     * @return scalar animation scalar
     */
    public double getAnimationScalar()
    {
        return animScalar;
    }
    
    /**
     * Sets the current animation delay, which determines how long the entity 
     * will wait until the animation is run.
     * 
     * @param delay animation delay
     */
    public void setAnimationDelay(long delay)
    {
        animDelay = delay;
    }
    
    /**
     * Returns the current animation delay
     * 
     * @return delay animation delay
     */
    public long getAnimationDelay()
    {
        return animDelay;
    }
    
    /**
     * Sets the current starting position for the object. Does not reset any
     * animation or scalar values
     *
     * @param position starting position
     */
    public void setStartPosition(String position)
    {
        startPosition = Location.getPosition(position);
    }
    
    /**
     * Sets the current starting position for the object. Does not reset any
     * animation or scalar values
     *
     * @param position starting position (vector)
     */
    public void setStartPosition(Vector3f position)
    {
        startPosition = position;
    }

    /**
     * Gets the current starting position for the entity.
     *
     * @return vector current starting position
     */
    public Vector3f getStartPosition()
    {
        return startPosition;
    }

    /**
     * Sets the current ending position for the object.  Does not reset any
     * animation or scalar values
     *
     * @param position ending position
     */
    public void setEndPosition(String position)
    {
        endPosition = Location.getPosition(position);
        name = Location.getNameId(position);
    }
    
    /**
     * Sets the current ending position for the object.  Does not reset any
     * animation or scalar values
     *
     * @param position ending position
     */
    public void setEndPosition(Vector3f position)
    {
        endPosition = position;
    }

    /**
     * Gets the current ending position for the entity.
     *
     * @return vector current ending position
     */
    public Vector3f getEndPosition()
    {
        return endPosition;
    }

    /**
     * Sets the speed of the current running animation.  Does not effect static
     * objects.
     *
     * @param speed animation speed
     */
    public void setAnimationSpeed(float speed)
    {
        animSpeed = speed;
    }
    
    /**
     * Sets the speed of the current running animation.  Does not effect static
     * objects.
     *
     * @param time animation time
     */
    public void setAnimationTime(long time)
    {
        float secs = ((float)time) / kMILLISINSECS;
        animSpeed = (1.0f / secs) * kONEHUNDRED;
    }

    /**
     * Gets the current starting position for the entity.
     *
     * @return vector current starting position
     */
    public float getAnimationSpeed()
    {
        return animSpeed;
    }

    /**
     * Sets the entity's current animation state
     *
     * @param animating whether or not the entity is animating
     */
    public void setAnimating(boolean animating)
    {
        this.animating = animating;
    }

    /**
     * Gets whether or not the entity is animating
     *
     * @return animating whether or not the entity is animating
     */
    public boolean isAnimating()
    {
        return animating;
    }
    
    /**
     * sets the rendering value to the given boolean
     * 
     * @param rendering render flag
     */
    public void setRendering(boolean rendering)
    {
        this.rendering = rendering;
    }
    
    /**
     * returns the rendering boolean
     * 
     * @return rendering whether or not the entity is being rendered
     */
    public boolean isRendering()
    {
        return rendering;
    }

    /**
     * A set of locations that entities can be placed or moved to
     */
    enum Location
    {
        // DEFAULT zero position
        DEFAULT("default", new Vector3f(0, 0, 0), -1),
        
        // GAME BOARD position
        BOARD("board", new Vector3f(0, 0, 0), 26),
        
        // DEALING position
        DEAL("deal", new Vector3f(0, 0, 30), -1),
        
        // SELECTED 1 position
        SELECTED_1("selected 1", new Vector3f(-16.0f, 7.0f, 2.0f), 1),
        
        // SELECTED 2 position
        SELECTED_2("selected 2", new Vector3f(-16.0f, 0.0f, 2.0f), 2),
        
        // SELECTED 3 position
        SELECTED_3("selected 3", new Vector3f(-16.0f, -7.0f, 2.0f), 3),
        
        // SELECTED 1 indicator position
        SELECTED_1_SPOT("selected 1 spot", new Vector3f(-16.0f, 7.0f, 1.8f), 1),
        
        // SELECTED 2 indicator position
        SELECTED_2_SPOT("selected 2 spot", new Vector3f(-16.0f, 0.0f, 1.8f), 2),
        
        // SELECTED 3 indicator position
        SELECTED_3_SPOT("selected 3 spot", new Vector3f(-16.0f, -7.0f, 1.8f), 3),

        EXPLAIN_WHY_MESSAGE("explain why message", new Vector3f(144, 236, 0), 3),
        
        // CARD positions 1 - 21
        LOC_1_1("board 1,1", new Vector3f(-10.0f, 7.0f, 2.0f), 4),
        LOC_2_1("board 2,1", new Vector3f(-5.0f, 7.0f, 2.0f), 5),
        LOC_3_1("board 3,1", new Vector3f(0.0f, 7.0f, 2.0f), 6),
        LOC_4_1("board 4,1", new Vector3f(5.0f, 7.0f, 2.0f), 7),
        LOC_5_1("board 5,1", new Vector3f(10.0f, 7.0f, 2.0f), 8),
        LOC_6_1("board 6,1", new Vector3f(15.0f, 7.0f, 2.0f), 9),
        LOC_1_2("board 1,2", new Vector3f(-10.0f, 0.0f, 2.0f), 10),
        LOC_2_2("board 2,2", new Vector3f(-5.0f, 0.0f, 2.0f), 11),
        LOC_3_2("board 3,2", new Vector3f(0.0f, 0.0f, 2.0f), 12),
        LOC_4_2("board 4,2", new Vector3f(5.0f, 0.0f, 2.0f), 13),
        LOC_5_2("board 5,2", new Vector3f(10.0f, 0.0f, 2.0f), 14),
        LOC_6_2("board 6,2", new Vector3f(15.0f, 0.0f, 2.0f), 15),
        LOC_1_3("board 1,3", new Vector3f(-10.0f, -7.0f, 2.0f), 16),
        LOC_2_3("board 2,3", new Vector3f(-5.0f, -7.0f, 2.0f), 17),
        LOC_3_3("board 3,3", new Vector3f(0.0f, -7.0f, 2.0f), 18),
        LOC_4_3("board 4,3", new Vector3f(5.0f, -7.0f, 2.0f), 19),
        LOC_5_3("board 5,3", new Vector3f(10.0f, -7.0f, 2.0f), 20),
        LOC_6_3("board 6,3", new Vector3f(15.0f, -7.0f, 2.0f), 21),
        LOC_1_4("board 1,4", new Vector3f(5.0f, -14.0f, 2.0f), 22),
        LOC_2_4("board 2,4", new Vector3f(10.0f, -14.0f, 2.0f), 23),
        LOC_3_4("board 3,4", new Vector3f(15.0f, -14.0f, 2.0f), 24),
        
        // TIMER position
        TIMER("timer", new Vector3f(616, 2, 1), 27),
        
        // SCORE position
        SCORE("score", new Vector3f(496, 8, 1), 27),
        
        // MENU button position
        MENU_ICON("menu icon", new Vector3f(0, -8, 0), 36),

        // MENU background position
        MENU("menu", new Vector3f(80, 120, 0), 28),

        // MENU background position
        EXIT("exit icon", new Vector3f(768, 0, 0), 60),

        // MENU background position
        HIGH_SCORE("high scores", new Vector3f(592, 184, 0), 59),

        // MENU background position
        EASY_TAB("easy tab", new Vector3f(336, 216, 0), 4),

        // MENU background position
        MED_TAB("medium tab", new Vector3f(432, 216, 0), 5),

        // MENU background position
        HARD_TAB("hard tab", new Vector3f(528, 216, 0), 6),

        // MENU background position
        EASY_CHECK("easy check", new Vector3f(224, 248, 0), 4),

        // MENU background position
        MED_CHECK("medium check", new Vector3f(224, 312, 0), 5),

        // MENU background position
        HARD_CHECK("hard check", new Vector3f(224, 376, 0), 6),
        
        // CANCEL BUTTON position
        CANCEL_BUTTON("cancel button", new Vector3f(208, 536, 0), 29),
        
        // START BUTTON position
        START_BUTTON("start button", new Vector3f(528, 536, 0), 30),
        
        // START BUTTON position
        NEXT_BUTTON("next button", new Vector3f(528, 536, 0), 42),
        
        // START BUTTON position
        MULTI_BUTTON("multiplayer button", new Vector3f(592, 136, 0), 39),
        
        // START BUTTON position
        PLEASE_WAIT("please wait", new Vector3f(144, 44, 0), 27),
        
        // FULLSCREEN CHECK position
        FULLSCREEN_CHECK("fullscreen check", new Vector3f(192, 408, 0), 33),
        
        // FULLSCREEN CHECK position
        SOLITAIRE_RADIO("solitaire radio", new Vector3f(192, 248, 0), 41),
        
        // FULLSCREEN CHECK position
        MULTI_RADIO("multiplayer radio", new Vector3f(192, 344, 0), 40),
        
        // BEGINNER MODE CHECK position
        BEGINNER_MODE_CHECK("beginner mode check", new Vector3f(240, 312, 0), 34),
        
        // TIMED MODE CHECK position
        TIMED_MODE_CHECK("timed mode check", new Vector3f(240, 280, 0), 35),
        
        // SCORE TEXT position
        SCORE_ICON("score icon", new Vector3f(384, 8, 0), 26),
        
        // GIVE HINT position
        GIVE_HINT("give hint", new Vector3f(256, 448, 1), 37),
        
        // EXPLAIN WHY position
        EXPLAIN_WHY("explain why", new Vector3f(16, 472, 1), 38),
        
        // EXPLAIN WHY position
        GAME_OVER("game over", new Vector3f(150, -200, 1), 26),
        
        // EXPLAIN WHY position
        SELECT_THEME("select theme", new Vector3f(592, 136, 0), 58),
        
        // DEAL THREE position
        DEAL_THREE("deal three", new Vector3f(544, 448, 1), 28),
        // DEAL THREE position
        PLAYER_NAME_1("player name 0", new Vector3f(352, 280, 1), 50),
        // DEAL THREE position
        PLAYER_NAME_2("player name 1", new Vector3f(352, 344, 1), 51),
        // DEAL THREE position
        PLAYER_NAME_3("player name 2", new Vector3f(352, 408, 1), 52),
        // DEAL THREE position
        PLAYER_NAME_4("player name 3", new Vector3f(352, 472, 1), 53),
        // DEAL THREE position
        PLAYER_KEY_1("player key 0", new Vector3f(528, 280, 1), 54),
        // DEAL THREE position
        PLAYER_KEY_2("player key 1", new Vector3f(528, 344, 1), 55),
        // DEAL THREE position
        PLAYER_KEY_3("player key 2", new Vector3f(528, 408, 1), 56),
        // DEAL THREE position
        PLAYER_KEY_4("player key 3", new Vector3f(528, 472, 1), 57),
        
        // TEST DATA position
        TEST_DATA("test location", new Vector3f(50, 50, 50), 25);

        // LOCATION constructor
        Location(String name, Vector3f position, int nameId)
        {
            this.nameId = nameId;
            this.position = position;
            this.name = name;
        }

        /**
         * retrieve the position
         * 
         * @return position position vector
         */
        public Vector3f getPosition()
        {
            return position;
        }

        /**
         * retrieve the name id
         * 
         * @return id name id
         */
        public String getName()
        {
            return name;
        }
        
        /**
         * retrieve the current position based on the string value
         * 
         * @param val location value
         * @return vector location value
         */
        static Vector3f getPosition(String val)
        {
            val = val.toLowerCase();
            Vector3f vec = Location.DEFAULT.getPosition();
            // FOR every location
            for(Location loc : Location.values())
            {
                // IF the location name matches the given name
                if(loc.getName().equals(val))
                {
                    // SET the current location as the result
                    vec = loc.getPosition();
                    break;
                }
            }
            // RETURN the resulting vector
            return vec;
        }
        
        /**
         * retrieve the id
         * 
         * @return id given name id
         */
        public int getId()
        {
            return nameId;
        }
        
        /**
         * get the openGl name of the based value
         * 
         * @param val location value
         * @return name openGl name
         */
        static int getNameId(String val)
        {
            val = val.toLowerCase();
            int nameId = -1;
            // FOR every location
            for(Location loc : Location.values())
            {
                // IF the location name matches the given name
                if(loc.getName().equals(val))
                {
                    // SET the current location as the result
                    nameId = loc.getId();
                    break;
                }
            }
            // RETURN the resulting vector
            return nameId;
        }

        private int nameId;
        private Vector3f position;
        private String name;
    }
}
