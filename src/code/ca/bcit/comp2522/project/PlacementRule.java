package ca.bcit.comp2522.project;

/**
 * PlacementRule defines abstract methods
 * that must be implemented to validate positions
 * in a number game.
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public abstract class PlacementRule
{
    /**
     * isValidPlacement abstract method
     * @param positions represents a 2d int array in 1d array
     * @param index to check if value can be placed here
     * @param value to be placed
     * @return if value can be placed at index
     */
    public abstract boolean isValidPlacement(
        final int[] positions,
        final int   index,
        final int   value
    );

    /**
     * canPlaceNext abstract method
     * @param positions represents a 2d int array in 1d array
     * @param nextValue to place
     * @return if next value to place can be placed at all
     */
    public abstract boolean canPlaceNext(
        final int[] positions,
        final int   nextValue
    );
}
