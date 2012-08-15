package clusterfun.app;

import clusterfun.app.CFApp;
import clusterfun.state.CFState;
import clusterfun.state.CFState.DeckType;
import clusterfun.ui.TextUI;
import clusterfun.ui.GraphicUI;
import clusterfun.logic.CFLogic;
import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;


/**
 * CFAppTest tests the CFApp class for errors. It can only be tested to see if 
 * it can construct a new CFApp. The rest of the testing is done in a driver
 * 
 * @author Lisa Hunter
 *
 */

public class CFAppDriver{
    
    private CFApp app;
    
    public static void main(String ... args)
    {
        
        if(args.length > 0 && args[0].equals("-integrate"))
        {
            new CFAppDriver(true);
        }
        else
        {
            new CFAppDriver(false);
        }
    }
    
    
    /* The following can only be tested in the oracle and results file,
     * so there aren't any specific tests i could create, just 
     * modifications to the oracle
     * 
     * Defect 143 cannot be tested for. It only occurs in TextUI, and 
     * was adjusted for based on preference
     * Defect 154 cannot be tested for. Fakes never send exceptions.
     * Defect 177 was a checkstyle defect
     * 
     */
    
    /* Check for not using init here, Defect Ticket 135 */
    public CFAppDriver(boolean integrate)
    {
        CFLogic logic = new CFLogic();
        CFState state = new CFState(logic, DeckType.SHUFFLE);
        if(integrate)
        {
            System.out.println("CFApp Driver Integration test is running");
            String[] arg = new String[2];
            arg[0] = "-test";
            arg[1] = "-ns";
            CFApp.main(arg);
            System.out.println("CFApp Driver Integration test is done");
        }
        else
        {
            app = new CFApp(new TextUI(state), state, logic);
            testTextUIRun();
            testGraphicUIRun();
            testNoArgs();
        }
    }
    
    
    /* Check for cfstate brought into textui, Defect Ticket 139 */
    public void testTextUIRun()
    {
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.PROGRAM_END, null), GameMessage.MessageDest.CF_APP);
        app.run();
    }
    
    // Try catch checks defect 154
    public void testGraphicUIRun()
    {
        CFLogic logic = new CFLogic();
        CFState state = new CFState(logic, DeckType.SHUFFLE);
        GraphicUI ui = null;
        try 
        {
            ui = new GraphicUI(state);
        } 
        catch (Exception e) 
        {
            

            System.out.println("CFApp driver failed with GraphicUI");
        }
        app = new CFApp(ui, state, logic);
        
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.PROGRAM_END, null), GameMessage.MessageDest.CF_APP);
        app.run();
    }
    
    
    /* This tests for 153 (0 arguments)*/
    public void testNoArgs()
    {
        String[] arg = new String[0];
        MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.PROGRAM_END, null), GameMessage.MessageDest.CF_APP);
        CFApp.main(arg);
    
    }

}
