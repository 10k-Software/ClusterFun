package clusterfun.ui;

/**
 * Tests the initialization and update methods of the GraphicUI class
 *
 * @author Chris Gibson
 * @version 0.1
 * @.date 2009-1-11
 */
public class GraphicUIDriver
{

    /**
     * Called by the script running the drivers
     *
     * @param args console arguments
     */
    public static void main(String ... args) throws Exception
    {
        new GraphicUIDriver();
    }
    
    public GraphicUIDriver() throws Exception
    {
        BasicUI ui = new GraphicUI(null);
        ui.update(0);
    }

}
