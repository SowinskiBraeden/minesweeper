package ca.bcit.comp2522.project;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main launcher for the games application.
 * Provides a CLI for selecting WordGame, NumberGame,
 * or Minesweeper, launching JavaFX games in separate
 * JVM processes for lifecycle stability.
 *
 * @author Braeden Sowinski
 * @version 1.0.1
 */
public class Main
{
    /**
     * Program entry point for the CLI menu.
     *
     * @param args command-line arguments. Optional:
     *             --jfxlib=/path/to/javafx-sdk/lib
     */
    public static void main(final String[] args)
    {
        final String javafxLib;
        javafxLib = System.getProperty("jdk.module.path");

        if (javafxLib == null)
        {
            System.err.println(
                    "JavaFX library path missing.\n" +
                    "Provide with:\n" +
                    "  1) VM option:  -Djfxlib=/path/to/javafx/lib\n" +
                    "  2) Or program arg: --jfxlib=/path/to/javafx/lib"
            );
            return;
        }

        final Scanner scanner;
        boolean       running;

        scanner = new Scanner(System.in);
        running = true;

        while (running)
        {
            System.out.println("Word Game (W)");
            System.out.println("Number Game (N)");
            System.out.println("Minesweeper (M)");
            System.out.println("Quit (Q)");
            System.out.print("> ");

            final String choice;

            choice = scanner.nextLine().trim().toUpperCase();

            switch (choice)
            {
                case "W" -> runWordGame(scanner);
                case "N" -> launchJavaFxApp("ca.bcit.comp2522.project.NumberGame", javafxLib);
                case "M" -> launchJavaFxApp("ca.bcit.comp2522.project.MyGame", javafxLib);
                case "Q" -> running = false;
                default -> System.out.println("Invalid selection.");
            }
        }
    }

    /**
     * Runs the WordGame inside the CLI environment.
     *
     * @param scanner shared scanner for System.in
     */
    private static void runWordGame(final Scanner scanner)
    {
        try
        {
            final WordGame game;
            game = new WordGame();
            game.runTrivia(scanner);
        }
        catch (final IOException e)
        {
            System.err.println("Unable to start WordGame.");
        }
    }

    /**
     * Launches a JavaFX Application subclass in a new
     * JVM process.
     *
     * @param mainClass fully-qualified class name
     * @param javafxLib path to JavaFX lib directory
     */
    private static void launchJavaFxApp(
        final String mainClass,
        final String javafxLib
    ) {
        final String         javaBin;
        final String         cp;
        final ProcessBuilder pb;

        javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        cp      = System.getProperty("java.class.path");

        pb = new ProcessBuilder(
            javaBin,
            "--module-path", javafxLib,
            "--add-modules", "javafx.controls,javafx.fxml",
            "--enable-native-access=javafx.graphics",
            "-cp", cp,
            mainClass
        );

        pb.inheritIO();

        try
        {
            final Process p;
            p = pb.start();
            p.waitFor();
        }
        catch (final IOException | InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
