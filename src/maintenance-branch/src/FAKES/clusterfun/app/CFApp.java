

package clusterfun.app;

import clusterfun.ui.BasicUI;



/**
 * This is the fake for the CFApp class
 * @author Lisa Hunter
 */
public class CFApp {
    
    public CFApp(BasicUI ui)
    {
        System.out.println("A new CFApp has been constructed.");
    }
    
    /**
     * Parses the command-line arguments and handles them as required.
     * It then runs the program
     * 
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        System.out.println("The main for CFApp has been reached.");
    }
    
    /**
     * Initializes and runs the game ClusterFun
     */
    public void run()
    {
        System.out.println("ClusterFun is now being run.");        
    }
}
