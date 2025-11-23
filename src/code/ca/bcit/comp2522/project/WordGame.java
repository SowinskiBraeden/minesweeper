package ca.bcit.comp2522.project;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordGame
{
    private static final String SCORE_PATH           = "./data/score.txt";
    private static final int    NUMBER_OF_QUESTIONS  = 10;
    private static final int    QUESTION_NUM_OFFSET  = 1;
    private static final int    QUEST_ASK_CAPITAL    = 0;
    private static final int    QUEST_ASK_COUNTRY    = 1;
    private static final int    QUEST_ASK_FACT       = 2;
    private static final int    QUESTION_TYPES       = 3;
    private static final int    MAX_ATTEMPTS         = 2;
    private static final int    FIRST_ATTEMPT        = 0;
    private static final int    SPLIT_DATE           = 0;
    private static final int    SPLIT_TIME           = 1;
    private static final int    FIRST_ATTEMPT_SCORE  = 0;
    private static final int    SECOND_ATTEMPT_SCORE = 1;
    private static final int    INCORRECT_SCORE      = 2;
    private static final int    DEFAULT_SCORE        = 0;

    private final World world;

    public WordGame()
        throws IOException
    {
        this.world = new World();
    }

    private static void checkAnswer(
        final String  correctAnswer,
        final Scanner scanner,
        final int[]   scores
    ) {
        for (int i = 0; i < MAX_ATTEMPTS; i++)
        {
            final String answer;

            System.out.print("> ");
            answer = scanner.nextLine();

            if (answer.equalsIgnoreCase(correctAnswer))
            {
                if (i == FIRST_ATTEMPT)
                {
                    scores[FIRST_ATTEMPT_SCORE]++;
                }
                else
                {
                    scores[SECOND_ATTEMPT_SCORE]++;
                }

                System.out.println("CORRECT!");
                return;
            }

            System.out.println("INCORRECT!");
        }

        scores[INCORRECT_SCORE]++;
        System.out.printf("The correct answer was %s\n", correctAnswer);
    }

    private void startTrivia(
        final Scanner scanner,
        final int[]   scores
    ) {
        for (int i = 0; i < NUMBER_OF_QUESTIONS; i++)
        {
            System.out.printf("Question %d:\n", i + QUESTION_NUM_OFFSET);

            final Random rand;
            final Country      country;
            final int          questionType;

            rand = new Random();

            country = this.world.getRandomCountry();
            questionType = rand.nextInt(QUESTION_TYPES);

            switch (questionType)
            {
                case QUEST_ASK_CAPITAL:
                    System.out.println("What is the country of the following capital?");
                    System.out.println(country.getCapital());

                    checkAnswer(country.getName(), scanner, scores);
                    break;

                case QUEST_ASK_COUNTRY:
                    System.out.println("What is the capital of the following country?");
                    System.out.println(country.getName());

                    checkAnswer(country.getCapital(), scanner, scores);
                    break;

                case QUEST_ASK_FACT:
                    System.out.println("What is the country of the following fact?");
                    System.out.println(country.getRandomFact());

                    checkAnswer(country.getName(), scanner, scores);
                    break;
            }
        }
    }

    public void runTrivia()
    {
        final Scanner scanner;
        Score         score;
        boolean       continuePlaying;

        continuePlaying = true;
        scanner = new Scanner(System.in);

        final int[] scores;
        int gamesPlayed;

        scores      = new int[]{DEFAULT_SCORE,DEFAULT_SCORE,DEFAULT_SCORE};
        gamesPlayed = DEFAULT_SCORE;

        do
        {
            gamesPlayed++;

            this.startTrivia(scanner, scores);

            score = new Score(
                LocalDateTime.now(),
                gamesPlayed,
                scores[FIRST_ATTEMPT_SCORE],
                scores[SECOND_ATTEMPT_SCORE],
                scores[INCORRECT_SCORE]
            );

            System.out.println(score);

            do
            {
                System.out.println("\nDo you want to play again?");
                System.out.print("> ");

                final String playAgain;
                playAgain = scanner.nextLine();

                if (playAgain.equalsIgnoreCase("no"))
                {
                    continuePlaying = false;
                    break;
                }
                else if (playAgain.equalsIgnoreCase("yes"))
                {
                    // break inner do-while loop
                    break;
                }
                else
                {
                    System.out.printf("Invalid option \"%s\"\n",  playAgain);
                }
            } while (true);

        } while (continuePlaying);

        scanner.close();

        final List<Score> history;

        history = Score.readScoresFromFile(SCORE_PATH);
        Score.appendScoreToFile(score, SCORE_PATH);

        if (Score.isHighScore(score, history))
        {
            System.out.printf(
                "CONGRATULATIONS! You have a new high score with an average of %.2f points per game; ",
                score.calculateAverage()
            );

            final Score prevHighScore;
            prevHighScore = Score.getHighScore(history);

            if (prevHighScore == null)
            {
                return;
            }

            System.out.printf(
                "the previous record was %.2f points per game ",
                prevHighScore.calculateAverage()
            );

            System.out.printf(
                "on %s at %s.\n",
                prevHighScore.getDateTimePlayed().split(" ")[SPLIT_DATE],
                prevHighScore.getDateTimePlayed().split(" ")[SPLIT_TIME]
            );
        }
    }

    public static void main(final String[] args)
    {
        final WordGame test;

        try
        {
            test = new WordGame();
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

        test.runTrivia();
    }
}
