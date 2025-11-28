package ca.bcit.comp2522.project;

import ca.bcit.comp2522.project.Mines;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lesson 10: unit tests for MinesGame.
 */
public class MinesTest
{
    private static final int TEST_WIDTH  = 5;
    private static final int TEST_HEIGHT = 5;
    private static final int TEST_MINES  = 3;

    @Test
    public void newGameHasCorrectMineCount()
    {
        final Mines game;
        int             mineCount;

        game = new Mines(TEST_WIDTH, TEST_HEIGHT, TEST_MINES, false);
        mineCount = 0;

        for (int i = 0; i < TEST_WIDTH * TEST_HEIGHT; i++)
        {
            if (game.isMine(i))
            {
                mineCount++;
            }
        }

        assertEquals(TEST_MINES, mineCount);
    }

    @Test
    public void revealNonMineDoesNotLose()
            throws Exception
    {
        final Mines game;
        int         safeIndex;

        game = new Mines(TEST_WIDTH, TEST_HEIGHT, TEST_MINES, false);
        safeIndex = -1;

        for (int i = 0; i < TEST_WIDTH * TEST_HEIGHT; i++)
        {
            if (!game.isMine(i))
            {
                safeIndex = i;
                break;
            }
        }

        assertNotEquals(-1, safeIndex);

        assertFalse(game.reveal(safeIndex));
        assertTrue(game.isRevealed(safeIndex));
    }
}
