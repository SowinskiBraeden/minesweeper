package ca.bcit.comp2522.project;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

/**
 * NumberGame is a GUI game with JavaFX
 * where a user has to place randomly generated
 * numbers into a 5x4 grid of buttons. Numbers
 * must be played in order to win.
 *
 * Extends JavaFX Application
 *
 * @author Braeden Sowinski
 * @version 1.0.0
 */
public class NumberGame
    extends Application
{
    private static final int  STARTING_NUMBERS_PLACED = 0;
    private static final int  INDEX_OFFSET   = 1;
    private static final int  FONT_SIZE      = 24;
    private static final int  WINDOW_WIDTH   = 700;
    private static final int  WINDOW_HEIGHT  = 600;
    private static final int  GRID_PADDING   = 10;
    private static final int  GRID_WIDTH     = 5;
    private static final int  GRID_HEIGHT    = 4;
    private static final int  BUTTON_WIDTH   = 200;
    private static final int  BUTTON_HEIGHT  = 160;
    private static final int  TOTAL_NUMBERS  = 20;
    private static final int  MIN_RAND_NUM   = 1;
    private static final int  MAX_RAND_NUM   = 1001;
    private static final Font FONT           = Font.font("Arial", FontWeight.BOLD, FONT_SIZE);

    private int numbersPlaced;
    private Label numberLabel;
    private int currentNumber;
    private int[] positions;
    private RandomNumberGenerator generator;
    private AscendingPlacement placementValidator;

    /**
     * start NumberGame GUI
     * @param stage to show
     */
    @Override
    public void start(final Stage stage)
    {
        this.placementValidator = new AscendingPlacement();
        this.generator          = new RandomNumberGenerator(MIN_RAND_NUM, MAX_RAND_NUM);
        this.numbersPlaced      = STARTING_NUMBERS_PLACED;
        this.currentNumber      = this.generator.generate();
        this.positions          = new int[GRID_WIDTH * GRID_HEIGHT];

        this.numberLabel = new Label("Next number: " + this.currentNumber + " - Select a slot.");
        this.numberLabel.setFont(FONT);
        this.numberLabel.setMaxWidth(Double.MAX_VALUE);
        this.numberLabel.setAlignment(Pos.CENTER);

        final VBox root;
        final GridPane grid;
        final Scene scene;

        root = new VBox();
        grid = createGrid();
        root.getChildren().add(this.numberLabel);
        root.getChildren().add(grid);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(scene);
        stage.setTitle("20 Number Challenge");
        stage.setResizable(false);
        stage.show();
    }

    /*
     * createGrid is used to generate the
     * button grid for users to play in.
     * @return GridPane with Buttons
     */
    private GridPane createGrid()
    {
        final GridPane grid;
        grid = new GridPane();

        grid.setPadding(new Insets(GRID_PADDING));
        grid.setHgap(GRID_PADDING);
        grid.setVgap(GRID_PADDING);

        for (int i = 0; i < GRID_HEIGHT; i++)
        {
            for (int j = 0; j < GRID_WIDTH; j++)
            {
                final Button button;
                final int    index;

                button = new Button("[]");
                index = (j * GRID_HEIGHT) + i;

                button.setFont(FONT);
                button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
                button.setOnAction(e -> handlePress(button, index));

                grid.add(button, i, j);
            }
        }

        return grid;
    }

    /*
     * triggerFailed handles stopping the game
     * when lost, i.e. impossible to place next
     * number
     */
    private void triggerFailed(final String message)
    {
        this.numberLabel.setText("Next number: " + this.currentNumber + " - " + message);
    }

    /*
     * handlePress of button to place number
     * in that cell
     * @param button pressed
     * @param index of button to place value
     */
    private void handlePress(final Button button, final int index)
    {
        button.setText("" + this.currentNumber);
        button.setDisable(true);
        this.numbersPlaced++;

        this.positions[index] = this.currentNumber;

        // Detect bad placement
        final boolean isValidPlacement;
        isValidPlacement = this.placementValidator.isValidPlacement(
                this.positions,
                index,
                this.currentNumber
        );

        if (!isValidPlacement)
        {
            triggerFailed("Placed number incorrectly.");
            return;
        }

        if (this.numbersPlaced >= TOTAL_NUMBERS)
        {
            this.numberLabel.setText("All numbers placed!");
            return;
        }

        this.currentNumber = this.generator.generate();

        final boolean canPlaceNext;
        canPlaceNext = this.placementValidator.canPlaceNext(this.positions, this.currentNumber);

        if (!canPlaceNext)
        {
            triggerFailed("Impossible to place next number.");
            return;
        }

        this.numberLabel.setText("Next number: " + this.currentNumber + " - Select a slot.");
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
