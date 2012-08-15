package clusterfun.ui;

import clusterfun.board.Card;
import clusterfun.logic.CFLogic;
import clusterfun.message.GameMessage;
import clusterfun.message.GameMessage.MessageDest;
import clusterfun.message.GameMessage.MessageType;
import clusterfun.message.MessageManager;
import clusterfun.state.CFState;
import clusterfun.state.CFState.DeckType;
import java.util.ArrayList;

/**
 *
 * @author Jason Swalwell
 */
public class TextUIDriver
{
    private BasicUI ui;

    public static void main(String ... args) throws Exception
    {
        new TextUIDriver();
    }

    public TextUIDriver() throws Exception
    {
        ui = new TextUI(new CFState(new CFLogic(), DeckType.NOSHUFFLE));
        testDealCards();
        ui.update(0);
        ui.update(0);
        ui.update(0);
        testInvalidCluster();
        ui.update(0);
        ui.update(0);
        testValidCluster();
        testHints();
        testSelectCluster();
    }

    public void testDealCards() throws Exception
    {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add( new Card("One", "Red", "Ovals", "Solid"));
        cards.add( new Card("Two", "Red", "Ovals", "Solid"));
        cards.add( new Card("Three", "Red", "Ovals", "Solid"));
        cards.add( new Card("One", "Green", "Ovals", "Solid"));
        cards.add( new Card("Two", "Green", "Ovals", "Solid"));
        cards.add(  new Card("Three", "Green", "Ovals", "Solid"));
        MessageManager.sendMessage(new GameMessage(MessageType.DEAL_CARDS, cards), MessageDest.CF_UI);
        ui.update(0);
    }

    public void testValidCluster() throws Exception
    {
        MessageManager.sendMessage(new GameMessage(MessageType.VALID_CLUSTER, null), MessageDest.CF_UI);
        ui.update(0);
    }

    public void testInvalidCluster() throws Exception
    {
        MessageManager.sendMessage(new GameMessage(MessageType.INVALID_CLUSTER, null), MessageDest.CF_UI);
        ui.update(0);
    }

    public void testHints() throws Exception
    {
        ArrayList<Card> hints = new ArrayList<Card>();
        hints.add( new Card("One", "Red", "Ovals", "Solid"));
        MessageManager.sendMessage(new GameMessage(MessageType.GIVE_HINT, hints), MessageDest.CF_UI);
        ui.update(0);
    }

    public void testSelectCluster() throws Exception
    {
        ArrayList<Card> cluster = new ArrayList<Card>();
        cluster.add( new Card("One", "Red", "Ovals", "Solid"));
        cluster.add( new Card("Two", "Red", "Ovals", "Solid"));
        cluster.add( new Card("One", "Green", "Ovals", "Solid"));
        MessageManager.sendMessage(new GameMessage(MessageType.SELECT_CLUSTER, cluster),
                                      MessageDest.CF_UI);
        ui.update(0);
        MessageManager.sendMessage(new GameMessage(MessageType.VALID_CLUSTER, null),
                                      MessageDest.CF_UI);
        ui.update(0);
    }
}
