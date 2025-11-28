package ca.bcit.comp2522.project;

/**
 * AscendingPlacement contains the logical and
 * validation of placing an integer in an array
 * of integers following an ascending order.
 */
public class AscendingPlacement
    extends PlacementRule
{
    private static final int FIRST = 0;
    private static final int NEXT  = 1;

    /**
     * isValidPlacement ensures that a value placed at a given index
     * is in the correct position, no number greater than the value
     * comes before and no number smaller than the value comes after
     * within the positions array.
     * @param positions represents a 2d int array in 1d array
     * @param index to check if value can be placed here
     * @param value to be placed
     * @return if the value is in a valid placement
     */
    @Override
    public boolean isValidPlacement(int[] positions, int index, int value)
    {
        for (int i = 0; i < positions.length; i++)
        {
            if (positions[i] == FIRST)
            {
                continue;
            }

            boolean invalidBefore;
            boolean invalidAfter;

            invalidBefore = i < index && positions[i] > value;
            invalidAfter  = i > index && positions[i] < value;

            if (invalidBefore || invalidAfter)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * canPlaceNext detects if a given value
     * has a valid position to be placed.
     * @param positions represents a 2d int array in 1d array
     * @param nextValue to place
     * @return if the value can be placed in the positions array
     * without violating the ascending rule
     */
    @Override
    public boolean canPlaceNext(int[] positions, int nextValue)
    {
        for (int i = 0; i < positions.length; i++)
        {
            if (positions[i] != FIRST)
            {
                continue;
            }

            int left;
            int right;

            left = Integer.MIN_VALUE;
            right = Integer.MAX_VALUE;

            // scan left
            for (int l = i - NEXT; l >= FIRST; l--)
            {
                if (positions[l] != FIRST)
                {
                    left = positions[l];
                    break;
                }
            }

            // scan right
            for (int r = i + NEXT; r < positions.length; r++)
            {
                if (positions[r] != FIRST)
                {
                    right = positions[r];
                    break;
                }
            }

            if (left < nextValue && nextValue < right)
            {
                return true;
            }
        }
        return false;
    }
}
