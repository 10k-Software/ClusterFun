
package clusterfun.player;

import clusterfun.player.AbstractPlayer;

/**
 * This is the fake for the HumanPlayer class
 * @author Lisa Hunter
 */
public class HumanPlayer extends AbstractPlayer{
    
    public HumanPlayer(String name)
    {
        super(name);
        System.out.println("HumanPlayer has been constructed.");
        
    }

}
