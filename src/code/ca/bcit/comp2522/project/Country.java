package ca.bcit.comp2522.project;

import java.util.Random;

/**
 * Country class stores a country's
 * name, capital city name
 * about the country.
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public class Country
{
    private final String   name;
    private final String   capitalCityName;
    private final String[] facts;

    /**
     * validateString ensures an input String is
     * not null and not blank
     * @param str to validate
     */
    private static void validateString(final String str)
    {
        if (str == null || str.isBlank())
        {
            throw new IllegalArgumentException("Invalid string");
        }
    }

    /**
     * Country constructor takes in the
     * name, capital, and facts of a country
     * @param name of country
     * @param capitalCityName of country
     * @param facts about the country
     */
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

    /**
     * getName of the country
     * @return name of the country
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * getCapital of country
     * @return capital of country
     */
    public String getCapital()
    {
        return this.capitalCityName;
    }

    /**
     * getRandomFact of country
     * @return random fact from the list of facts of the country
     */
    public String getRandomFact()
    {
        final Random rand;

        rand = new Random();

        return this.facts[rand.nextInt(this.facts.length)];
    }
}
