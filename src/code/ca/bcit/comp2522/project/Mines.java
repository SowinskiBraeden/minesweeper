package ca.bcit.comp2522.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

public class Mines extends GameBoard
{
    private static final int MINE               = -1;
    private static final int NO_MINE            = 0;

    private static final int MIN_OFFSET         = -1;
    private static final int MAX_OFFSET         = 1;
    private static final int SELF_OFFSET        = 0;
    private static final int FIRST_ROW          = 0;
    private static final int FIRST_COL          = 0;

    public static final int NO_FLAG            = 0;
    public static final int FLAG               = 1;
    public static final int FLAG_QUESTION      = 2;

    private static final int INITIAL_BEST_SCORE = Integer.MAX_VALUE;

    private static final String SCORE_FILE_NAME = "scores.txt";

    private static final Random RANDOM_GENERATOR;

    static
    {
        RANDOM_GENERATOR = new Random();
    }

    private final int totalMines;
    private boolean   randomMode;

    private int[]     field;
    private boolean[] revealed;
    private int[]     flagged;

    private int bestScoreSeconds;

    public Mines(
        final int width,
        final int height,
        final int mines,
        final boolean randomMode
    ) {
        super(width, height);
        this.totalMines = mines;
        this.randomMode = randomMode;
        this.bestScoreSeconds = INITIAL_BEST_SCORE;
        reset();
        loadBestScore();
    }

    public void startNewGame()
    {
        reset();
    }

    public void startNewGame(final boolean randomMode)
    {
        this.randomMode = randomMode;
        reset();
    }

    public void setRandomMode(final boolean randomMode)
    {
        this.randomMode = randomMode;
    }

    public boolean isRandomMode()
    {
        return this.randomMode;
    }

    @Override
    public void reset()
    {
        final int totalCells;

        totalCells = this.width * this.height;

        this.field    = new int[totalCells];
        this.revealed = new boolean[totalCells];
        this.flagged  = new int[totalCells];

        generateField();
    }

    private void generateField()
    {
        int placedMines;
        final int totalCells;

        placedMines = NO_MINE;
        totalCells  = this.width * this.height;

        while (placedMines < totalMines)
        {
            final int index;

            index = RANDOM_GENERATOR.nextInt(totalCells);

            if (this.field[index] != MINE)
            {
                this.field[index] = MINE;
                placedMines++;
            }
        }

        for (int i = 0; i < this.field.length; i++)
        {
            if (this.field[i] == MINE)
            {
                continue;
            }

            final int[] count;

            count = new int[] { NO_MINE };

            forEachNeighbor(i, neighborIndex -> {
                if (this.field[neighborIndex] == MINE)
                {
                    count[SELF_OFFSET]++;
                }
            });

            this.field[i] = count[SELF_OFFSET];
        }
    }

    private void forEachNeighbor(
        final int               index,
        final Consumer<Integer> action
    ) {
        final int row;
        final int col;

        row = index / this.width;
        col = index % this.width;

        for (int dr = MIN_OFFSET; dr <= MAX_OFFSET; dr++)
        {
            for (int dc = MIN_OFFSET; dc <= MAX_OFFSET; dc++)
            {
                if (dr == SELF_OFFSET && dc == SELF_OFFSET)
                {
                    continue;
                }

                final int nr;
                final int nc;

                nr = row + dr;
                nc = col + dc;

                if (nr < FIRST_ROW || nr >= this.height ||
                    nc < FIRST_COL || nc >= this.width)
                {
                    continue;
                }

                final int neighborIndex;

                neighborIndex = nr * this.width + nc;
                action.accept(neighborIndex);
            }
        }
    }

    private void popFieldVoid(final int index)
    {
        forEachNeighbor(index, neighborIndex -> {

            if (!this.revealed[neighborIndex])
            {
                try
                {
                    reveal(neighborIndex);
                }
                catch (final InvalidMoveException e)
                {
                    // revealing neighbors should never be invalid
                }

                if (this.field[neighborIndex] == NO_MINE)
                {
                    popFieldVoid(neighborIndex);
                }
            }
        });
    }

    public boolean reveal(final int index)
        throws InvalidMoveException
    {
        if (this.flagged[index] == FLAG)
        {
            throw new InvalidMoveException("Cannot reveal a flagged cell.");
        }

        this.revealed[index] = true;

        if (this.field[index] == NO_MINE)
        {
            popFieldVoid(index);
        }

        return this.field[index] == MINE;
    }

    public int toggleFlag(final int index)
    {
        final int nextState;

        nextState = (this.flagged[index] + FLAG) % (FLAG_QUESTION + FLAG);
        this.flagged[index] = nextState;
        return nextState;
    }

    public int getFieldValue(final int index)
    {
        return this.field[index];
    }

    public boolean isRevealed(final int index)
    {
        return this.revealed[index];
    }

    public boolean isMine(final int index)
    {
        return this.field[index] == MINE;
    }

    public boolean isFlagged(final int index)
    {
        return this.flagged[index] == FLAG;
    }

    public boolean isQuestionMarked(final int index)
    {
        return this.flagged[index] == FLAG_QUESTION;
    }

    public int getTotalMines()
    {
        return this.totalMines;
    }

    public boolean hasWon()
    {
        int revealedCount;

        revealedCount = NO_MINE;

        for (int i = 0; i < this.field.length; i++)
        {
            if (this.revealed[i])
            {
                revealedCount++;
            }
            else if (this.field[i] != MINE)
            {
                return false;
            }
        }

        return revealedCount == (this.field.length - this.totalMines);
    }

    public void randomizeRemaining()
    {
        if (!this.randomMode)
        {
            return;
        }

        int minesToReplant;

        minesToReplant = NO_MINE;

        for (int i = 0; i < this.field.length; i++)
        {
            if (this.field[i] == MINE && this.flagged[i] == FLAG)
            {
                continue;
            }

            if (this.field[i] == MINE)
            {
                minesToReplant++;
            }

            this.field[i] = NO_MINE;
        }

        while (minesToReplant > NO_MINE)
        {
            final int index;

            index = RANDOM_GENERATOR.nextInt(this.width * this.height);

            if (this.field[index] == NO_MINE && !this.revealed[index])
            {
                this.field[index] = MINE;
                minesToReplant--;
            }
        }

        for (int i = 0; i < this.field.length; i++)
        {
            if (this.field[i] == MINE)
            {
                continue;
            }

            final int[] count;

            count = new int[] { NO_MINE };

            forEachNeighbor(i, neighborIndex -> {
                if (this.field[neighborIndex] == MINE)
                {
                    count[SELF_OFFSET]++;
                }
            });

            this.field[i] = count[SELF_OFFSET];
        }

        for (int i = 0; i < this.field.length; i++)
        {
            if (this.revealed[i] && this.field[i] == NO_MINE)
            {
                popFieldVoid(i);
            }
        }

    }

    public void saveScore(final int seconds)
    {
        try
        {
            final BufferedWriter writer;

            writer = new BufferedWriter(new FileWriter(SCORE_FILE_NAME));

            writer.write(Integer.toString(seconds));
            writer.newLine();
        }
        catch (final IOException e)
        {
            // ignore failure
        }

        if (seconds < this.bestScoreSeconds)
        {
            this.bestScoreSeconds = seconds;
        }
    }

    private void loadBestScore()
    {
        final File scoreFile;

        scoreFile = new File(SCORE_FILE_NAME);

        if (!scoreFile.exists())
        {
            return;
        }

        int best;
        best = INITIAL_BEST_SCORE;

        try
        {
            final Scanner scanner;
            scanner = new Scanner(new BufferedReader(new FileReader(scoreFile)));

            while (scanner.hasNextInt())
            {
                final int candidate;

                candidate = scanner.nextInt();

                if (candidate < best)
                {
                    best = candidate;
                }
            }
        }
        catch (final IOException e)
        {
            // ignore failure
        }

        this.bestScoreSeconds = best;
    }

    public int getBestScoreSeconds()
    {
        return this.bestScoreSeconds;
    }
}
