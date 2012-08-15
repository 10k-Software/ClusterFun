package clusterfun.state;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import clusterfun.message.GameMessage;
import clusterfun.message.MessageManager;
import clusterfun.state.CFState.DifficultyType;
import clusterfun.state.Score;

/**
* HallOfFame is a score tracking module that receives players'
* scores after each game and determines if they make it into the
* top 10 highest scores for the difficulty they were playing on
*
* @author Nick Artman
* @version 2.0
* @.date 2009-2-23
*/
//  Version History
//       2/22/09 Nick Artman added class skeleton
//       2/22/09 Nick Artman added methods to serialize and unserialize the high score information

public class HallOfFame
{
    // A list of the easy mode high scores
    private ArrayList<Score> easyScores;

    // A list of the medium mode high scores
    private ArrayList<Score> mediumScores;

    // A list of the hard mode high scores
    private ArrayList<Score> hardScores;

    // The most recently proposed score
    private int proposedScore;

    // The most recently proposed score's difficulty
    private clusterfun.state.CFState.DifficultyType proposedDifficulty = null;

    // The default high score on the easy high scores list
    private final int kDefaultEasyScore = -999;
    
    // The default high score on the medium high scores list
    private final int kDefaultMediumScore = 0;
    
    // The default high score on the hard high scores list
    private final int kDefaultHardScore = 17;
    
    // The number of high scores to keep on the list
    private final int kMaxNumberOfHighScores = 10;

    // The directory that the score data files are stored in
    private final String kDataFileLocation = "data/";

    /**
     * Creates a new hall of fame to keep track of high scores. The HallOfFame will automatically
     * read in serialized hall of fame data from the appropriate file or, if no file has been
     * created yet, will create and serialize the default hall of fame
    */
    public HallOfFame()
    {
        // TRY to read in the serialized high scores
        try
        {
            // Read in the easy scores
            FileInputStream easyFile = new FileInputStream(kDataFileLocation + "EASYScores.hsl");
            ObjectInputStream easyScoresStream = new ObjectInputStream(easyFile);
            easyScores = (ArrayList<Score>) easyScoresStream.readObject();
            easyScoresStream.close();

            // Read in the medium scores
            FileInputStream mediumFile = new FileInputStream(kDataFileLocation + "MEDIUMScores.hsl");
            ObjectInputStream mediumScoresStream = new ObjectInputStream(mediumFile);
            mediumScores = (ArrayList<Score>) mediumScoresStream.readObject();
            mediumScoresStream.close();

            // Read in the hard scores
            FileInputStream hardFile = new FileInputStream(kDataFileLocation + "HARDScores.hsl");
            ObjectInputStream hardScoresStream = new ObjectInputStream(hardFile);
            hardScores = (ArrayList<Score>) hardScoresStream.readObject();
            hardScoresStream.close();
        }
        // CATCH no file was found and instantiate new lists
        catch (FileNotFoundException fnf)
        {
            // CREATE new internal lists
            this.easyScores = new ArrayList<Score>();
            this.mediumScores = new ArrayList<Score>();
            this.hardScores = new ArrayList<Score>();

            // POPULATE them with the default high scores
            this.easyScores.add(new Score("Bub", kDefaultEasyScore));
            this.mediumScores.add(new Score("Jimbo", kDefaultMediumScore));
            this.hardScores.add(new Score("Seven", kDefaultHardScore));

            // SERIALIZE the default scores
            this.serializeScores(CFState.DifficultyType.EASY);
            this.serializeScores(CFState.DifficultyType.MEDIUM);
            this.serializeScores(CFState.DifficultyType.HARD);
        }
        // Any other exception means something actually went wrong
        catch (Exception e)
        {
            System.out.println("Critical error reading high score files!\n Your high score files may have" + 
                " been tampered with or become corrupted and cannot be repaired. Please remove the three score" + 
                " files (EASYScores.hsl, MEDIUMScores.hsl, and HARDScores.hsl) in the \"data\" folder of your" +
                " ClusterFun installation and relaunch the game. Your high scores will be cleared and cannot be" +
                " recovered. We apologize for this inconvenience. If you do not follow this procedure" + 
                " ClusterFun may malfunction. If you receive this message repeatedly, there may be a problem" +
                " with your computer. Please contact 10k Software for support and we will do our best to assist" +
                " you.");
        }
    }

    /**
     * Creates a new hall of fame to keep track of high scores. This constructor uses the provided scores
     * list (it sets it as the scores for hard) and is intended for debug and testing purposes
     *
     * @param scoresList A list of all the scores to use
    */
    public HallOfFame(ArrayList<Score> scoresList)
    {


        // CREATE new internal lists
        this.easyScores = new ArrayList<Score>();
        this.mediumScores = new ArrayList<Score>();
        this.hardScores = new ArrayList<Score>();

        // POPULATE them with the default high scores
        this.easyScores.add(new Score("Bubba", kDefaultEasyScore));
        this.mediumScores.add(new Score("Forest Gump", kDefaultMediumScore));
        this.hardScores.add(new Score("Gordon Freeman", kDefaultHardScore));

        // SERIALIZE the default scores
        this.serializeScores(CFState.DifficultyType.EASY);
        this.serializeScores(CFState.DifficultyType.MEDIUM);
        this.serializeScores(CFState.DifficultyType.HARD);

        this.hardScores = scoresList;
    }

    /**
     * Determines of a high score makes the cut to get into the high scores list. This must be called before
     * using addProposedScoreToList. This function will cache the score and difficulty proposed in preparation
     * for the addProposedScoreToList call
     *
     * @param score The player's score
     * @param difficulty The difficulty the player was playing on
     *
     * @throws Exception Throws an exception with an "invalid difficulty" message
     * if a difficulty is requestsed that does not exist.
    */
    public void proposeNewHighScore(int score, CFState.DifficultyType difficulty) throws Exception
    {
        // If the score made the list
        boolean scoreMakesTheList = false;

        // The proper list for the given difficulty
        ArrayList<Score> scores = null;

        // SET the proposed difficulty and score
        this.proposedDifficulty = difficulty;
        this.proposedScore = score;

        // DETERMINE the proper list to use
        switch (difficulty)
        {
            case EASY: scores = this.easyScores; break;
            case MEDIUM: scores = this.mediumScores; break;
            case HARD: scores = this.hardScores; break;
            default: throw new Exception("Invalid difficulty provided");
        }

        // IF the list is empty, the score automatically makes the list
        if (scores.size() < kMaxNumberOfHighScores)
        {
            scoreMakesTheList = true;
        }
        else
        {
            // DETERMINE if the score makes the list by being greater than any one of the current scores
            for (Score currScore : scores)
            {
                scoreMakesTheList |= score > currScore.getScore();
            }
        }

        // IF the score made the high scores list
        if (scoreMakesTheList)
        {
            // SEND an AFFIRM_HIGH_SCORE message
            MessageManager.sendMessage(new GameMessage(GameMessage.MessageType.AFFIRM_HIGH_SCORE, null),
                GameMessage.MessageDest.CF_UI, GameMessage.MessageDest.CF_SOUND);
        }
    }

    /**
     * Adds the most recently proposed score to the high scores list; proposeNewHighScore must be called
     * before this or an exception will be thrown.
     *
     * @param playerName The name of the player who attained the score
     * @throws Exception Throws an exception with an "invalid difficulty" message
     * if a difficulty is requestsed that does not exist. Throws an exception with
     * the message "You must propose a score before adding it to the list" if this method
     * is called without first calling proposeNewHighScore.
    */
    public void addProposedScoreToList(String playerName) throws Exception
    {
        // The proper list for the given difficulty
        ArrayList<Score> scores = null;

        // ENSURE the proposal was made first
        if (this.proposedDifficulty == null)
        {
            throw new Exception("You must propose a score before adding it to the list");
        }

        // DETERMINE the proper list to use
        switch (this.proposedDifficulty)
        {
            case EASY: scores = this.easyScores; break;
            case MEDIUM: scores = this.mediumScores; break;
            case HARD: scores = this.hardScores; break;
            default: throw new Exception("Invalid difficulty provided");
        }

        // ADD the new score to the list
        scores.add(new Score(playerName, this.proposedScore));

        // SORT the list so the lowest score is at the head
        Collections.sort(scores);

        // TRIM the list if necessary by removing the lowest element
        if (scores.size() > kMaxNumberOfHighScores)
        {
            scores.remove(0);
        }

        // SERIALIZE the scores to maintain the list between runs
        this.serializeScores(this.proposedDifficulty);

        // SET the proposed difficulty back to null to gaurd against
        // other classes adding a score without proposing one first
        this.proposedDifficulty = null;


    }

    /**
     * Returns the high scores list for the requested difficulty.
     *
     * @param difficulty The difficulty
     * @return An ascending order list of Scores for the requested difficulty
     * @throws Exception Throws an exception with an invalid difficulty provided message if the difficulty
     * requested does not exist
    */
    public ArrayList<Score> getHighScores(CFState.DifficultyType difficulty) throws Exception
    {
        // The proper list for the given difficulty
        ArrayList<Score> scores = null;

        // DETERMINE the proper list to use
        switch (difficulty)
        {
            case EASY: scores = this.easyScores; break;
            case MEDIUM: scores = this.mediumScores; break;
            case HARD: scores = this.hardScores; break;
            default: throw new Exception("Invalid difficulty provided");
        }

        // RETURN the table for the specified difficulty
        return scores;
    }

    // Writes the score table of the given difficulty to a high score list (hsl) file
    private void serializeScores(DifficultyType difficulty)
    {
        // SERIALIZE the table for the given difficulty
        try
        {
            // SET UP the output stream
            FileOutputStream fout = new FileOutputStream(kDataFileLocation + difficulty + "Scores.hsl");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            ArrayList<Score> scores = null;

            // DETERMINE the proper list to use
            switch (difficulty)
            {
                case EASY: scores = this.easyScores; break;
                case MEDIUM: scores = this.mediumScores; break;
                case HARD: scores = this.hardScores; break;
                default: throw new Exception("Invalid difficulty provided");
            }

            // WRITE the high score table
            oos.writeObject(scores);

            // CLOSE the output stream
            oos.close();
        }
        // CATCH any errors - this means something important went wrong
        catch (Exception e)
        {
            System.out.println("Error serializing" + difficulty + "Scores");
            e.printStackTrace();
        }

    }

}
