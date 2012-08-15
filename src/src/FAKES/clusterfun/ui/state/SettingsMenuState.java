package clusterfun.ui.state;

import clusterfun.ui.window.GameWindow;
/**
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-1-11
 */
public class SettingsMenuState implements GameState
{

    private boolean initialized = false;
    public String getName() {
        return "SettingsState";
    }

    public void init(GameWindow window) throws Exception {
        System.out.println("Settings state initialized");
        initialized = true;
    }

    public void render(GameWindow window, boolean picking) throws Exception{
        System.out.println("Settings state rendered");
    }

    public void update(GameWindow window, long timeElapsed) {
        System.out.println("Settings state updated");
    }

    public void enter(GameWindow window) {
        System.out.println("Settings state entered");
    }

    public void leave(GameWindow window) {
        System.out.println("Settings state left");
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void reset() {
        System.out.println("Settings Menu Reset");
    }

}
