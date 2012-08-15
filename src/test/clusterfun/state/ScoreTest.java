package clusterfun.state;

import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.state.Score;
import junit.framework.TestCase;


public class ScoreTest extends TestCase
{
    public ScoreTest()
    {
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testGetScore_positive()
    {
        Score score = new Score("Test", 10);
        assertEquals(10, score.getScore());
    }

    public void testGetScore_negative()
    {
        Score score = new Score("Test", -10);
        assertEquals(-10, score.getScore());
    }

    public void testGetPlayer()
    {
        Score score = new Score("Test", 10);
        assertEquals("Test", score.getName());
    }

    public void testEquals_areEqual()
    {
        Score score1 = new Score("Test", 10);
        Score score2 = new Score("Test", 10);
        assertEquals(score1, score2);
    }

    public void testEquals_namesNotEqual()
    {
        Score score1 = new Score("Test", 10);
        Score score2 = new Score("Nottest", 10);
        assertFalse(score1.equals(score2));
    }

    public void testEquals_scoresNotEqual()
    {
        Score score1 = new Score("Test", 10);
        Score score2 = new Score("Test", 100);
        assertFalse(score1.equals(score2));
    }

    public void testCompareTo_greaterThan()
    {
        Score score1 = new Score("Test",  10);
        Score score2 = new Score("Test2", 100);
        assertTrue(score1.compareTo(score2) < 0);
    }

    public void testCompareTo_lessThan()
    {
        Score score1 = new Score("Test",  10);
        Score score2 = new Score("Test2", 4);
        assertTrue(score1.compareTo(score2) > 0);
    }

    public void testCompareTo_equalTo()
    {
        Score score1 = new Score("Test",  10);
        Score score2 = new Score("Test2", 10);
        assertTrue(score1.compareTo(score2) == 0);
    }

    public void testCompareTo_notAScoreObject()
    {
        Score score1 = new Score("Test",  10);
        Object obj = new Object();
        
        try 
        {
            score1.compareTo(obj);
        }
        catch (ClassCastException e)
        {
        }
        catch (Exception e)
        {
            fail("Exception thrown other than a ClassCastException");
        }
    }



}
