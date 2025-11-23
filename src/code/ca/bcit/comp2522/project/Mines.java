package ca.bcit.comp2522.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Mines extends Application
{
    private static final int  FONT_SIZE      = 18;
    private static final Font FONT           = Font.font("Arial", FontWeight.BOLD, FONT_SIZE);

    private static final int WINDOW_WIDTH    = 1920;
    private static final int WINDOW_HEIGHT   = 1080;
    private static final int BUTTON_WIDTH    = 60;
    private static final int BUTTON_HEIGHT   = 60;
    private static final int EASY_WIDTH      = 8;
    private static final int EASY_HEIGHT     = 8;
    private static final int HARD_WIDTH      = 36;
    private static final int HARD_HEIGHT     = 16;
    private static final int PADDING         = 20;
    private static final int GAME_PADDING    = 5;
    private static final int VERTICAL_MARGIN = 15;

    private static final int EASY_MINES      = 10;
    private static final int HARD_MINES      = 99;
    private static final int MINE            = -1;
    private static final int NO_MINE         = 0;

    private static final int MIN_OFFSET      = -1;
    private static final int MAX_OFFSET      = 1;
    private static final int SELF_OFFSET     = 0;
    private static final int FIRST_ROW       = 0;
    private static final int FIRST_COL       = 0;

    private static final int NO_FLAG         = 0;
    private static final int FLAG            = 1;
    private static final int FLAG_QUESTION   = 2;

    private static final int DEFAULT_BUTTON = -2;
    private static final Map<Integer, String> BUTTON_THEMES = new HashMap<>();

    static {
        BUTTON_THEMES.put(-2, "-fx-background-color: #b3b3b3; -fx-text-fill: black;");
        BUTTON_THEMES.put(-1, "-fx-background-color: #d9d9d9; -fx-text-fill: black;");
        BUTTON_THEMES.put(0, "-fx-background-color: #d9d9d9; -fx-text-fill: black;");
        BUTTON_THEMES.put(1, "-fx-background-color: #c8ffbf; -fx-text-fill: black;");
        BUTTON_THEMES.put(2, "-fx-background-color: #edfc9f; -fx-text-fill: black;");
        BUTTON_THEMES.put(3, "-fx-background-color: #fad08c; -fx-text-fill: black;");
        BUTTON_THEMES.put(4, "-fx-background-color: #f59338; -fx-text-fill: black;");
        BUTTON_THEMES.put(5, "-fx-background-color: #ff644d; -fx-text-fill: black;");
        BUTTON_THEMES.put(6, "-fx-background-color: #ff644d; -fx-text-fill: black;");
        BUTTON_THEMES.put(7, "-fx-background-color: #ff644d; -fx-text-fill: black;");
        BUTTON_THEMES.put(8, "-fx-background-color: #ff644d; -fx-text-fill: black;");
    }

    private int[]     field;
    private boolean[] revealed;
    private int[]     flagged;
    private Button[]  buttons;
    private int       width;
    private int       height;
    private int       flags;

    @Override
    public void start(final Stage stage)
    {
        final Label title;
        title = new Label("Select Game Size");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        final Button smallBtn;
        final Button largeBtn;

        smallBtn = new Button("8 × 8 (Beginner)");
        largeBtn = new Button("36 × 16 (Expert)");

        smallBtn.setPrefWidth(BUTTON_WIDTH);
        largeBtn.setPrefWidth(BUTTON_WIDTH);

        smallBtn.setOnAction(e -> startGame(EASY_WIDTH, EASY_HEIGHT, EASY_MINES));
        largeBtn.setOnAction(e -> startGame(HARD_WIDTH, HARD_HEIGHT, HARD_MINES));

        final VBox root;
        root = new VBox(VERTICAL_MARGIN);

        root.setPadding(new Insets(PADDING));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, smallBtn, largeBtn);

        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Random Mines - A Minesweeper Game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void forEachNeighbor(final int index, final java.util.function.IntConsumer action)
    {
        final int row = index / width;
        final int col = index % width;

        for (int dr = MIN_OFFSET; dr <= MAX_OFFSET; dr++)
        {
            for (int dc = MIN_OFFSET; dc <= MAX_OFFSET; dc++)
            {
                if (dr == SELF_OFFSET && dc == SELF_OFFSET)
                {
                    continue;
                }

                final int nr = row + dr;
                final int nc = col + dc;

                if (nr < FIRST_ROW || nr >= height ||
                    nc < FIRST_COL || nc >= width)
                {
                    continue;
                }

                final int neighborIndex = nr * width + nc;
                action.accept(neighborIndex);
            }
        }
    }

    private void generateField(final int mines)
    {
        this.revealed = new boolean[width * height];
        this.field    = new int[width * height];
        this.flagged  = new int[width * height];

        final Random rand;
        int placed;

        rand = new Random();
        placed = NO_MINE;

        while (placed < mines)
        {
            final int index;
            index = rand.nextInt(width * height);

            if (this.field[index] != MINE)
            {
                this.field[index] = MINE;
                placed++;
            }
        }

        for (int i = 0; i < field.length; i++)
        {
            if (this.field[i] == MINE)
            {
                continue;
            }

            int[] count = { NO_MINE };

            forEachNeighbor(i, neighborIndex -> {
                if (this.field[neighborIndex] == MINE)
                {
                    count[SELF_OFFSET]++;
                }
            });

            this.field[i] = count[SELF_OFFSET];
        }
    }

    private void popFieldVoid(final int index)
    {
        forEachNeighbor(index, neighborIndex -> {

            if (!revealed[neighborIndex])
            {
                reveal(neighborIndex);

                if (field[neighborIndex] == NO_MINE)
                {
                    popFieldVoid(neighborIndex);
                }
            }
        });
    }

    private void startGame(
        final int width,
        final int height,
        final int mines
    ) {
        this.flags   = NO_FLAG;
        this.buttons = new Button[width * height];
        this.width  = width;
        this.height = height;

        generateField(mines);

        final Stage    gameStage;
        final VBox     box;
        final GridPane grid;

        gameStage = new Stage();
        box       = new VBox();
        grid      = createGrid(width, height);

        box.getChildren().add(grid);
        box.setAlignment(Pos.CENTER);

        final Scene scene;
        scene = new Scene(box, WINDOW_WIDTH, WINDOW_HEIGHT);

        gameStage.setScene(scene);
        gameStage.setTitle("Random Mines " + width + "x" + height);
        gameStage.show();
    }

    private void flag(final int index)
    {
        this.flagged[index] = (this.flagged[index] + FLAG) % (FLAG_QUESTION + FLAG);

        final String buttonText;

        buttonText = this.flagged[index] == FLAG ? "F" :
                     this.flagged[index] == FLAG_QUESTION ? "?" : "";

        this.buttons[index].setText(buttonText);

        this.flags = this.flagged[index] == FLAG ? this.flags + FLAG : this.flags - FLAG;
    }

    private GridPane createGrid(final int width, final int height)
    {
        final GridPane grid;
        grid = new GridPane();

        grid.setPadding(new Insets(GAME_PADDING));
        grid.setHgap(GAME_PADDING);
        grid.setVgap(GAME_PADDING);

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                final Button button;
                final int    index;

                index = (j * width) + i;
                button = new Button();

                button.setFont(FONT);
                button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
                button.setStyle(BUTTON_THEMES.get(DEFAULT_BUTTON));
                button.setOnMouseEntered(e -> button.setCursor(javafx.scene.Cursor.HAND));
                button.setOnMouseExited(e -> button.setCursor(javafx.scene.Cursor.DEFAULT));
                button.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        reveal(index);
                    }
                    else if (e.getButton() == MouseButton.SECONDARY) {
                        flag(index);
                    }
                });

                grid.add(button, i, j);
                this.buttons[index] = button;
            }
        }

        return grid;
    }

    private void reveal(final int index)
    {
        if (this.revealed[index])
        {
            return;
        }

        final String buttonText;
        final Button button;

        button = this.buttons[index];

        buttonText = "" + (this.field[index] == MINE ? "*" :
                           this.field[index] == NO_MINE ? " " :
                           this.field[index]);

        button.setText(buttonText);
        button.setStyle(BUTTON_THEMES.get(this.field[index]));
        button.setMouseTransparent(true);
        button.setFocusTraversable(false);

        this.revealed[index] = true;

        if (this.field[index] == NO_MINE)
        {
            popFieldVoid(index);
        }
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}
