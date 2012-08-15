package clusterfun.ui;

import clusterfun.ui.state.InGameState;
import clusterfun.ui.state.SettingsMenuState;
import clusterfun.ui.state.GameState;
import clusterfun.ui.state.LoadingState;
import clusterfun.ui.window.GameWindow;
import java.io.IOException;

/**
 *
 * @author cgibson
 */
public class GameWindowDriver{

    public static void main(String ...args) throws IOException, Exception
    {
        new GameWindowDriver();
    }

    public GameWindowDriver() throws IOException, Exception
    {
        GameWindow window = new GameWindow(null);
        GameState state1 = new LoadingState();
        GameState state2 = new InGameState();
        GameState state3 = new SettingsMenuState();
        window.initialize();

        window.addState(state1);
        window.addState(state2);
        window.addState(state3);
        
        window.update(0);

        window.changeToState("SettingsState");

        window.update(0);
    }
}
