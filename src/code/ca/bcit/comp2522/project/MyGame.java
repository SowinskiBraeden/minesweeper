package ca.bcit.comp2522.project;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * MyGame manager for Minesweeper application
 * in JavaFX.
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public class MyGame extends Application
{
    /**
     * start JavaFX application by creating
     * the MinesUI handler to show main menu.
     * @param primaryStage to show minesweeper menu UI
     */
    @Override
    public void start(final Stage primaryStage)
    {
        final MinesUI ui;

        ui = new MinesUI();
        ui.showMainMenu(primaryStage);
    }

    /**
     * main program entry for quick testing
     * @param args from command line
     */
    public static void main(final String[] args)
    {
        launch(args);
    }
}
