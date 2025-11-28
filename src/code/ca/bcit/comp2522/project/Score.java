package ca.bcit.comp2522.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Score manager for WordGame, writes and reads
 * scores from file, get and validate high-scores,
 * calculate score averages and provides useful
 * toString methods to summarize scores.
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public class Score
{
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final int    FIRST_GUESS_POINTS = 2;
    private static final int    SECOND_GUESS_POINTS = 1;
    private static final int    SPLIT_VALUE = 1;

    private final String dateTimePlayed;
    private final int    numGamesPlayed;
    private final int    numCorrectFirstAttempt;
    private final int    numCorrectSecondAttempt;
    private final int    numIncorrectTwoAttempts;
    private final int    score;

    /**
     * appendScoreToFile takes in a Score object
     * and filepath, appends Score values to file
     * accordingly.
     * @param score to append to filepath
     * @param scoreFilePath filepath to append Score to
     */
    public static void appendScoreToFile(
        final Score score,
        final String scoreFilePath
    ) {
        try
        {
            final BufferedWriter writer;

            writer = new BufferedWriter(new FileWriter(scoreFilePath, true));

            writer.write(score.toString());
            writer.newLine();

            writer.close();
        }
        catch (final IOException e)
        {
            System.err.println("Failed to write score.txt");
        }
    }

    /**
     * readScoresFromFile returns a history of all Scores
     * read from the given filepath.
     * @param scoreFilePath to read scores from
     * @return a list of all Scores read from given filepath
     */
    public static List<Score> readScoresFromFile(final String scoreFilePath)
    {
        final List<Score> scores;

        scores = new ArrayList<>();

        try
        {
            final BufferedReader reader;

            reader = new BufferedReader(new FileReader(scoreFilePath));

            String line;
            while ((line = reader.readLine()) != null)
            {

                if (!line.isBlank() && line.contains("Date and Time: "))
                {
                    final DateTimeFormatter formatter;
                    final String            dateTime;
                    final Score             score;
                    final String            games;
                    final String            correctFirst;
                    final String            correctSecond;
                    final String            incorrect;

                    formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

                    dateTime      = line.split(": ")[SPLIT_VALUE];
                    games         = reader.readLine().split(": ")[SPLIT_VALUE];
                    correctFirst  = reader.readLine().split(": ")[SPLIT_VALUE];
                    correctSecond = reader.readLine().split(": ")[SPLIT_VALUE];
                    incorrect     = reader.readLine().split(": ")[SPLIT_VALUE];

                    reader.readLine(); // Skip over score line

                    score = new Score(
                        LocalDateTime.parse(dateTime, formatter),
                        Integer.parseInt(games),
                        Integer.parseInt(correctFirst),
                        Integer.parseInt(correctSecond),
                        Integer.parseInt(incorrect)
                    );

                    scores.add(score);
                }
            }

            reader.close();
        }
        catch (final IOException e)
        {
            System.err.println("Failed to open score log file.");
        }

        return scores;
    }

    /**
     * getHighScore from a given List of Scores
     * @param scores list to find high-score from
     * @return high Score from list of scores
     */
    public static Score getHighScore(final List<Score> scores)
    {
        return scores.stream()
                .min(Comparator.comparingInt(Score::getScore))
                .orElse(null);
    }

    /**
     * isHighScore takes a List of Scores and
     * new Score to check if the new Score is
     * a high-score in the given scores list.
     * @param score to check if is high score
     * @param scores list to compare Score to
     * @return if score is a new high-score in list
     */
    public static boolean isHighScore(
        final Score score,
        final List<Score> scores
    ) {
        final Score highScore;

        highScore = Score.getHighScore(scores);

        return (highScore == null ||
                highScore.getScore() < score.getScore());
    }

    /**
     * Score constructor saves score dateTime,
     * number of games played for this score,
     * and relevant scores based on number of
     * guesses.
     * @param dateTime the score was recorded
     * @param numGamesPlayed is the number of WordGames played
     * @param numCorrectFirstAttempt is the number of times a guess was correct first try
     * @param numCorrectSecondAttempt is the number of times a guess was correct on second try
     * @param numIncorrectTwoAttempts is the number of times both guesses were incorrect
     */
    public Score(
        final LocalDateTime dateTime,
        final int numGamesPlayed,
        final int numCorrectFirstAttempt,
        final int numCorrectSecondAttempt,
        final int numIncorrectTwoAttempts
    ) {
        final DateTimeFormatter formatter;

        formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        this.dateTimePlayed = dateTime.format(formatter);

        this.numGamesPlayed          = numGamesPlayed;
        this.numCorrectFirstAttempt  = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;

        this.score = this.numCorrectFirstAttempt * FIRST_GUESS_POINTS +
                     this.numCorrectSecondAttempt * SECOND_GUESS_POINTS;
    }

    /**
     * getDateTimePlayed of Score
     * @return dateTimePlayed as a String
     */
    public String getDateTimePlayed()
    {
        return this.dateTimePlayed;
    }

    /**
     * toString neatly presents this Score
     * in a String format
     * @return formatted String of Score details
     */
    @Override
    public String toString()
    {
        final StringBuilder log;

        log = new StringBuilder();

        log.append("Date and Time: ");
        log.append(this.dateTimePlayed);
        log.append("\n");
        log.append("Games Played: ");
        log.append(this.numGamesPlayed);
        log.append("\n");
        log.append("Correct First Attempts: ");
        log.append(this.numCorrectFirstAttempt);
        log.append("\n");
        log.append("Correct Second Attempts: ");
        log.append(this.numCorrectSecondAttempt);
        log.append("\n");
        log.append("Incorrect Attempts: ");
        log.append(this.numIncorrectTwoAttempts);
        log.append("\n");
        log.append("Score: ");
        log.append(this.score);
        log.append(" points\n");

        return log.toString();
    }

    /**
     * getScore returns total points of this Score
     * @return total points score
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * calculateAverage points per round
     * @return average points scored per round
     */
    public float calculateAverage()
    {
        return (float) this.score / (float) this.numGamesPlayed;
    }
}
