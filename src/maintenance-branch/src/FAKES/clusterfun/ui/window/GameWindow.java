package clusterfun.ui.window;

import clusterfun.state.CFState;
import clusterfun.ui.state.GameState;
import java.awt.DisplayMode;
import org.lwjgl.LWJGLException;

/**
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-1-11
 */
public class GameWindow
{

    /**
     * Constructor for fake gamewindow class
     */
    public GameWindow(CFState logicState)
    {
        System.out.println("GameWindow created");
    }

    private DisplayMode findDisplayMode(int width, int height, int bpp)
            throws LWJGLException
    {
            System.out.println("GameWindow display mode found");

            return null;
    }

    public void addState(GameState state) {
            System.out.println("GameWindow state added");
    }

    public void initialize() {
        System.out.println("GameWindow initialized");
    }

    public void update( long timeElapsed )
    {
        System.out.println("GameWindow updated");
    }

    public void changeToState(String name) {
            System.out.println("GameWindow state changed");
    }

    public void render(boolean picking)
    {
        if(picking)
        {
            System.out.println("GameWindow rendered with picking");
        }
        else
        {
            System.out.println("GameWindow rendered without picking");
        }
    }

    public void updateInput()
    {
        System.out.println("GameWindow input updated");
    }

}
