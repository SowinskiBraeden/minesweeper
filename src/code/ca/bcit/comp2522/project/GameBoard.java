package ca.bcit.comp2522.project;

public abstract class GameBoard
{
    protected int width;
    protected int height;

    protected GameBoard(final int width, final int height)
    {
        this.width = width;
        this.height = height;
    }

    public final int getWidth()
    {
        return this.width;
    }

    public final int getHeight()
    {
        return this.height;
    }

    public abstract void reset();
}
