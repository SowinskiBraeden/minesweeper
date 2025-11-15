package ca.bcit.comp2522.project;

import javafx.application.Application;
import javafx.stage.Stage;

public class Mines extends Application
{
    @Override
    public void start(final Stage s)
    {
        s.setTitle("MineSweeper Random");
        s.show();
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}
