package clusterfun.ui;

import clusterfun.state.CFState;

/**
 *
 * @author Jason Swalwell
 */
public class TextUI implements BasicUI
{
    public TextUI(CFState state)
    {
        System.out.println("TextUI created");
    }

    public void update(long timeElapsed) {
        System.out.println("TextUI updated");
    }
}