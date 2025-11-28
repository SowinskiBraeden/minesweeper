package ca.bcit.comp2522.project;

import java.util.Random;

/**
 * RandomNumberGenerator is a helper class
 * that generates random numbers within a
 * defined range
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public class RandomNumberGenerator
    implements Generator
{
    private final Random random;
    private final int min;
    private final int max;

    /**
     * RandomNumberGenerator constructor
     * @param min number that can be randomly generated inclusive
     * @param max number that can be randomly generated inclusive
     */
    public RandomNumberGenerator(
        final int min,
        final int max)
    {
        this.random = new Random();

        this.min = min;
        this.max = max;
    }

    /**
     * generate a random number between
     * inclusive min and inclusive max value
     * @return random number between min and max inclusive
     */
    @Override
    public int generate()
    {
        return this.random.nextInt(this.max - this.min) + this.min;
    }
}
