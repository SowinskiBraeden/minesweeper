package ca.bcit.comp2522.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Random;
import java.util.stream.Stream;

/**
 * World class reads countries and their facts
 * from a file and stores the Country in a Map.
 * Also provides useful functions to get a random
 * Country from the Map.
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public class World
{
    private static final String BASE_PATH       = "./data/facts";
    private static final int    COUNTRY_PARAM   = 0;
    private static final int    CAPITAL_PARAM   = 1;
    private static final int    NUMBER_OF_FACTS = 3;

    private final Map<String, Country> countries;

    /*
     * readCuntryData from a given line
     * @param line to parse
     * @param reader to read line
     * @return Country with name and facts
     * @throws IOException
     */
    private static Country readCountryData(
        final String         line,
        final BufferedReader reader
    )
        throws IOException
    {
        final String   countryName;
        final String   countryCapital;
        final String[] facts;

        countryName    = line.split(":")[COUNTRY_PARAM];
        countryCapital = line.split(":")[CAPITAL_PARAM];

        facts = new String[NUMBER_OF_FACTS];

        for (int i = 0; i < NUMBER_OF_FACTS; i++)
        {
            facts[i] = reader.readLine();
        }

        return new Country(countryName, countryCapital, facts);
    }

    /**
     * World constructor creates a hashmap
     * where the key is a Country name, and
     * the value is the Country object.
     * @throws IOException
     */
    public World()
        throws IOException
    {
        this.countries = new HashMap<>();

        try (final Stream<Path> walk = Files.walk(Paths.get(BASE_PATH)))
        {
            final List<Path> filesInFolder;
            filesInFolder = walk
                            .filter(Files::isRegularFile)
                            .toList();

            for (final Path file : filesInFolder)
            {
                final BufferedReader reader;

                reader = new BufferedReader(new FileReader(file.toString()));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (line.isBlank())
                    {
                        continue;
                    }

                    final Country country;

                    country = readCountryData(line, reader);

                    this.countries.put(country.getName(), country);
                }
            }
        }
        catch (final IOException e)
        {
            System.err.println("Error accessing the folder: " + e.getMessage());
        }
    }

    /**
     * getRandomCountry returns a random
     * Country from this countries Map.
     * @return random Country
     */
    public Country getRandomCountry()
    {
        final Random       rand;
        final String       key;
        final List<String> keys;

        rand = new Random();

        keys = new ArrayList<>(this.countries.keySet());
        key  = keys.get(rand.nextInt(keys.size()));

        return this.countries.get(key);
    }
}
