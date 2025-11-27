package ca.bcit.comp2522.project;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the Minesweeper game.
 * Lesson 9: JavaFX GUI entry class.
 */
public class MyGame extends Application
{
    @Override
    public void start(final Stage primaryStage)
    {
        final MinesUI ui;

        ui = new MinesUI();
        ui.showMainMenu(primaryStage);
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}
