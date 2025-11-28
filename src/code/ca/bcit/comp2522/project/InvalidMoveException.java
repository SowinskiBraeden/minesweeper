package ca.bcit.comp2522.project;

/**
 * InvalidMoveException is a custom error
 * to convey that an attempted move is invalid.
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public class InvalidMoveException extends Exception
{
    /**
     * InvalidMoveException constructor
     * @param message of InvalidMoveException
     */
    public InvalidMoveException(final String message)
    {
        super(message);
    }
}
