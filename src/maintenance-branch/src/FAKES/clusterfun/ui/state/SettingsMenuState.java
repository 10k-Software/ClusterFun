package clusterfun.ui.state;

import clusterfun.ui.window.GameWindow;
import java.io.IOException;

/**
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-1-11
 */
public class SettingsMenuState implements GameState
{

    public String getName() {
        return "SettingsState";
    }

    public void init(GameWindow window) throws IOException {
        System.out.println("Settings state initialized");
    }

    public void render(GameWindow window) {
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

}
