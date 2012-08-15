package clusterfun.ui.state;

import clusterfun.ui.window.GameWindow;
import java.io.IOException;

/**
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-1-11
 */
public class InGameState implements GameState
{

    private boolean initialized = false;

    public String getName() {
        return "InGameState";
    }

    public void init(GameWindow window) throws Exception {
        System.out.println("In game state initialized");
        initialized = true;
    }

    public void render(GameWindow window, boolean picking) throws Exception {
        System.out.println("In game state rendered");
    }

    public void update(GameWindow window, long timeElapsed) {
        System.out.println("In game state updated");
    }

    public void enter(GameWindow window){
        System.out.println("In game state entered");
    }

    public void leave(GameWindow window) {
        System.out.println("In game state left");
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void reset() {
        System.out.println("Reset");
    }

}
