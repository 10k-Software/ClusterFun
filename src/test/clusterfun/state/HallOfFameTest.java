package clusterfun.state;

import java.util.ArrayList;
import clusterfun.message.MessageManager;
import clusterfun.message.GameMessage;
import clusterfun.message.GameMessage.MessageDest;
import clusterfun.message.GameMessage.MessageType;
import clusterfun.state.CFState.DifficultyType;
import clusterfun.state.CFState;
import clusterfun.state.HallOfFame;
import clusterfun.state.Score;
import junit.framework.TestCase;


public class HallOfFameTest extends TestCase
{
    private HallOfFame hallOfFame;
    
    public HallOfFameTest()
    {
    }
    
    public void setUp()
    {
        ArrayList<Score> scoresList = new ArrayList<Score>();
        scoresList.add(new Score("A", 1));
        scoresList.add(new Score("B", 2));
        scoresList.add(new Score("C", -4));
        scoresList.add(new Score("D", 0));
        scoresList.add(new Score("E", 0));
        scoresList.add(new Score("Reggi", 14));
        scoresList.add(new Score("Bill", -100));
        scoresList.add(new Score("Seven", 777));
        scoresList.add(new Score("Wuss", 0));
        scoresList.add(new Score("Bendr", 990));
        
        this.hallOfFame = new HallOfFame(scoresList);
    }
    
    public void tearDown()
    {
    }

    public void testGetHighScores()
    {
        try {
            ArrayList<Score> scoresList = new ArrayList<Score>();
	        scoresList.add(new Score("A", 1));
	        scoresList.add(new Score("B", 2));
	        scoresList.add(new Score("C", -4));
	        scoresList.add(new Score("D", 0));
	        scoresList.add(new Score("E", 0));
	        scoresList.add(new Score("Reggi", 14));
	        scoresList.add(new Score("Bill", -100));
	        scoresList.add(new Score("Seven", 777));
	        scoresList.add(new Score("Wuss", 0));
	        scoresList.add(new Score("Bendr", 990));
	        
	        assertEquals(scoresList, this.hallOfFame.getHighScores(clusterfun.state.CFState.DifficultyType.HARD));
        } 
        catch (Exception e)
        {
            fail("Exception");
        }
    }

    public void testProposeNewHighScore_newScoreAddedToFullList()
    {
        try
        {
            this.hallOfFame.proposeNewHighScore(2, CFState.DifficultyType.HARD);
            this.hallOfFame.addProposedScoreToList("Jimmy");

            ArrayList<Score> returnedScores = hallOfFame.getHighScores(CFState.DifficultyType.HARD);

            assertTrue(returnedScores.contains(new Score("Jimmy", 2)));
            
            // Make sure the right message was sent
            ArrayList<GameMessage> uiMessages = 
                (ArrayList<GameMessage>) MessageManager.getMessages(MessageDest.CF_UI);
            ArrayList<GameMessage> soundMessages = 
                (ArrayList<GameMessage>) MessageManager.getMessages(MessageDest.CF_SOUND);
            
            // Check size
            assertEquals(10, returnedScores.size());
            
            assertEquals(uiMessages.get(0).getType(), GameMessage.MessageType.AFFIRM_HIGH_SCORE);
            assertEquals(soundMessages.get(0).getType(), GameMessage.MessageType.AFFIRM_HIGH_SCORE);
        }
        catch (Exception e)
        {
            fail("Exception was thrown.");
            System.out.print(e.toString());
        }
        
    }
    
    public void testProposeNewHighScore_newScoreAddedToNotFullList()
    {
        try
        {
            ArrayList<Score> testScores = new ArrayList<Score>();
            testScores.add(new Score("TS1", 5));
            testScores.add(new Score("TS2", 1));
            testScores.add(new Score("TS3", 3));
            
            HallOfFame testHall = new HallOfFame(testScores);
            
            testHall.proposeNewHighScore(-2, CFState.DifficultyType.HARD);
            testHall.addProposedScoreToList("Jimmy");

            ArrayList<Score> returnedScores = testHall.getHighScores(CFState.DifficultyType.HARD);

            // Check size
            assertEquals(4, returnedScores.size());
            // Check the score was entered
            assertTrue(returnedScores.contains(new Score("Jimmy", -2)));
            
            // Make sure the right message was sent
            ArrayList<GameMessage> uiMessages = 
                (ArrayList<GameMessage>) MessageManager.getMessages(MessageDest.CF_UI);
            ArrayList<GameMessage> soundMessages = 
                (ArrayList<GameMessage>) MessageManager.getMessages(MessageDest.CF_SOUND);
            
            assertEquals(uiMessages.get(0).getType(), GameMessage.MessageType.AFFIRM_HIGH_SCORE);
            assertEquals(soundMessages.get(0).getType(), GameMessage.MessageType.AFFIRM_HIGH_SCORE);
        }
        catch (Exception e)
        {
            fail("Exception was thrown.");
            System.out.print(e.toString());
        }
        
    }

    public void testProposeNewHighScore_newScoreAddedToListWith9Scores()
    {
        try
        {
            ArrayList<Score> testScores = new ArrayList<Score>();
            testScores.add(new Score("TS1",  5));
            testScores.add(new Score("TS2",  1));
            testScores.add(new Score("TS3",  3));
            testScores.add(new Score("TS4",  3));
            testScores.add(new Score("TS5",  7));
            testScores.add(new Score("TS6", -4));
            testScores.add(new Score("TS7", -3));
            testScores.add(new Score("TS8",  0));
            testScores.add(new Score("TS9",  2));
            
            HallOfFame testHall = new HallOfFame(testScores);            

            testHall.proposeNewHighScore(-20, CFState.DifficultyType.HARD);
            testHall.addProposedScoreToList("Jimmy");

            ArrayList<Score> returnedScores = testHall.getHighScores(CFState.DifficultyType.HARD);

            // Check size
            assertEquals(10, returnedScores.size());
            // Check the score was entered
            assertTrue(returnedScores.contains(new Score("Jimmy", -20)));
            
            // Make sure the right message was sent
            ArrayList<GameMessage> uiMessages = 
                (ArrayList<GameMessage>) MessageManager.getMessages(MessageDest.CF_UI);
            ArrayList<GameMessage> soundMessages = 
                (ArrayList<GameMessage>) MessageManager.getMessages(MessageDest.CF_SOUND);
            
            assertEquals(uiMessages.get(0).getType(), GameMessage.MessageType.AFFIRM_HIGH_SCORE);
            assertEquals(soundMessages.get(0).getType(), GameMessage.MessageType.AFFIRM_HIGH_SCORE);
        }
        catch (Exception e)
        {
            fail("Exception was thrown.");
            System.out.print(e.toString());
        }
        
    }
    
    public void testProposeNewHighScore_newScoreNotAdded()
    {
        try
        {
            this.hallOfFame.proposeNewHighScore(-50, CFState.DifficultyType.HARD);

            ArrayList<Score> returnedScores = hallOfFame.getHighScores(CFState.DifficultyType.HARD);

            assertFalse(returnedScores.contains(new Score("Jimmy", 2)));
        }
        catch (Exception e)
        {
            fail("Exception was thrown.");
            System.out.print(e.toString());
        }
        
    }

    public void testProposeNewHighScore_failedAddThenSuccessfulAdd()
    {
        try
        {
            this.hallOfFame.proposeNewHighScore(-50, CFState.DifficultyType.HARD);
            this.hallOfFame.proposeNewHighScore(2, CFState.DifficultyType.HARD);

            ArrayList<Score> returnedScores = hallOfFame.getHighScores(CFState.DifficultyType.HARD);

            assertFalse(returnedScores.contains(new Score("Jimmy", 2)));
        }
        catch (Exception e)
        {
            fail("Exception was thrown.");
            System.out.print(e.toString());
        }
        
    }
    
    public void testAddProposedScoreToList_noProposalMadeFirst()
    {
        try
        {
            this.hallOfFame.addProposedScoreToList("Jimmy");
        }
        catch (Exception e)
        {
            assertEquals("You must propose a score before adding it to the list", e.getMessage());
        }
        
    }
    
    public void testAddProposedScoreToList_newLastPlaceScore()
    {
        try
        {
            this.hallOfFame.proposeNewHighScore(-99, CFState.DifficultyType.HARD);
            this.hallOfFame.addProposedScoreToList("Jimmy");

            ArrayList<Score> returnedScores = hallOfFame.getHighScores(CFState.DifficultyType.HARD);
            
            assertEquals(10, returnedScores.size());

            assertTrue(returnedScores.contains(new Score("Jimmy", -99)));
        }
        catch (Exception e)
        {
            fail("Exception was thrown.");
            System.out.print(e.toString());
        }
        
    }

    public void testAddProposedScoreToList_newFirstPlaceScore()
    {
        try
        {
            this.hallOfFame.proposeNewHighScore(999, CFState.DifficultyType.HARD);
            this.hallOfFame.addProposedScoreToList("Jimmy");

            ArrayList<Score> returnedScores = hallOfFame.getHighScores(CFState.DifficultyType.HARD);

            assertEquals(10, returnedScores.size());

            assertTrue(returnedScores.contains(new Score("Jimmy", 999)));
        }
        catch (Exception e)
        {
            fail("Exception was thrown.");
            System.out.print(e.toString());
        }
        
    }

    
    public void testSerialization()
    {
        try
        {
            this.hallOfFame.proposeNewHighScore(7, CFState.DifficultyType.HARD);
            this.hallOfFame.addProposedScoreToList("Jimmy");
            
            // The scores after serializing
            ArrayList<Score> serializedScores = hallOfFame.getHighScores(CFState.DifficultyType.HARD);

            // The scores after reconstituting from the serialization file
            HallOfFame newHOF = new HallOfFame();

            ArrayList<Score> unserializedScores = newHOF.getHighScores(CFState.DifficultyType.HARD);
            assertEquals(serializedScores, unserializedScores);
            
        }
        catch (Exception e)
        {
            fail("Exception was thrown.");
            System.out.print(e.toString());
        }
        
    }
    
    


}












