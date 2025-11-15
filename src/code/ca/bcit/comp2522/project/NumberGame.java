package ca.bcit.comp2522.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NumberGame extends Application
{
    @Override
    public void start(final Stage stage)
    {
        final VBox layout;
        final Scene scene;

        layout = createLayout(); // see below
        scene = new Scene(layout, 300, 200);

        stage.setScene(scene);
        stage.setTitle("20 Number Challenge");
        stage.show();
    }

    private VBox createLayout()
    {
        final Label greeting;
        final TextField textField;
        final Button button;
        greeting = new Label("Enter your name:");
        textField = new TextField();
        button = new Button("Click me!");
        button.setOnAction(e -> greeting.setText("Hello, " + textField.getText()
                                                 + "!"));
        // or
        //button.setOnAction(event -> assignAction(greeting, textField, button));
        return new VBox(greeting, textField, button);
    }
    void assignAction(final Label l, final TextField t, final Button b)
    {
        l.setText("Hello, " + t.getText() + "!");
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}
