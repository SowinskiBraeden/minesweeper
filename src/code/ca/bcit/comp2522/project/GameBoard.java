package ca.bcit.comp2522.project;

/**
 * GameBoard is a simple abstract class
 * to represent some two-dimensional board,
 * it stores the width and height of board
 * and must have a reset method.
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public abstract class GameBoard
{
    protected int width;
    protected int height;

    /**
     * GameBoard constructor creates the board
     * @param width of board
     * @param height of board
     */
    protected GameBoard(final int width, final int height)
    {
        this.width = width;
        this.height = height;
    }

    /**
     * getWidth of game board
     * @return width of game board
     */
    public final int getWidth()
    {
        return this.width;
    }

    /**
     * getHeight of game board
     * @return height of game board
     */
    public final int getHeight()
    {
        return this.height;
    }

    /**
     * reset game board
     */
    public abstract void reset();
}
