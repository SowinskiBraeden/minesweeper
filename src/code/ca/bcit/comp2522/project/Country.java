package ca.bcit.comp2522.project;

import java.util.Random;

/**
 * Country class
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public class Country
{
    private static final int  NUMBER_OF_FACTS = 3;

    private final String   name;
    private final String   capitalCityName;
    private final String[] facts;

    private static void validateString(final String str)
    {
        if (str == null || str.isBlank())
        {
            throw new IllegalArgumentException("Invalid string");
        }
    }

    public Country(
        final String name,
        final String capitalCityName,
        final String[] facts
    ) {
        validateString(name);
        validateString(capitalCityName);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    public String getName()
    {
        return this.name;
    }

    public String getCapital()
    {
        return this.capitalCityName;
    }

    public String getRandomFact()
    {
        final Random rand;

        rand = new Random();

        return this.facts[rand.nextInt(this.facts.length)];
    }
}
