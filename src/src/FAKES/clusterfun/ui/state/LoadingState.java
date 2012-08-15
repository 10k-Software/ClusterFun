package clusterfun.ui.state;

import clusterfun.ui.window.GameWindow;

/**
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-1-11
 */
public class LoadingState implements GameState
{

    private boolean initialized = false;
    public String getName() {
        return "LoadingState";
    }

    public void init(GameWindow window) throws Exception {
        System.out.println("Loading state initialized");
        initialized = true;
    }

    public void render(GameWindow window, boolean picking) throws Exception {
        System.out.println("Loading state rendered");
    }

    public void update(GameWindow window, long timeElapsed) {
        System.out.println("Loading state updated");
    }

    public void enter(GameWindow window){
        System.out.println("Loading state entered");
    }

    public void leave(GameWindow window) {
        System.out.println("Loading state left");
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void reset() {
        System.out.println("Loading State Reset");
    }

}
