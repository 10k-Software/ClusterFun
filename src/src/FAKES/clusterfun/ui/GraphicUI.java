package clusterfun.ui;

import clusterfun.state.CFState;

/**
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-1-11
 */
public class GraphicUI implements BasicUI
{

    /**
     * constructor for fake GraphicUI
     */
    public GraphicUI(CFState logicState)
    {
        System.out.println("GraphicUI created");
    }

    public void update(long timeElapsed) {
        System.out.println("GraphicUI updated");
    }


}
