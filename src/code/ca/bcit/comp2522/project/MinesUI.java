package ca.bcit.comp2522.project;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MinesUI
{
    private static final int  FONT_SIZE               = 18;
    private static final Font FONT                    = Font.font("Arial", FontWeight.BOLD, FONT_SIZE);

    private static final int WINDOW_WIDTH             = 900;
    private static final int WINDOW_HEIGHT            = 700;
    private static final int BUTTON_WIDTH             = 40;
    private static final int BUTTON_HEIGHT            = 40;
    private static final int MENU_BUTTON_WIDTH        = 200;
    private static final int MENU_BUTTON_HEIGHT       = 60;

    private static final int PADDING                  = 20;
    private static final int GAME_PADDING             = 5;
    private static final int VERTICAL_MARGIN          = 15;
    private static final int TOPBAR_SPACING           = 5;

    private static final int EASY_WIDTH               = 8;
    private static final int EASY_HEIGHT              = 8;
    private static final int EASY_MINES               = 10;
    private static final int HARD_WIDTH               = 36;
    private static final int HARD_HEIGHT              = 16;
    private static final int HARD_MINES               = 99;

    private static final int STARTING_FLAGS           = 0;
    private static final int STARTING_SECONDS         = 0;
    private static final int TIMER_TICK_SECONDS       = 1;

    private static final int DEFAULT_BUTTON_KEY       = -2;
    private static final int MINE_KEY                 = -1;
    private static final int ZERO_KEY                 = 0;

    private static final String TITLE_TEXT            = "Random Mines - A Minesweeper Game";

    private static final Map<Integer, String> BUTTON_THEMES;

    static
    {
        BUTTON_THEMES = new java.util.HashMap<>();
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

    private final List<Button> buttons;

    private Mines game;

    private Label flagLabel;
    private Label timerLabel;
    private Label bestLabel;

    private boolean randomMode;
    private int     flagsPlaced;
    private int     seconds;

    private Timeline timer;
    private boolean  timerRunning;

    public MinesUI()
    {
        this.buttons = new ArrayList<>();
        this.randomMode   = false;
        this.timerRunning = false;
    }

    public void showMainMenu(final Stage primaryStage)
    {
        final Label  titleLabel;
        final Button easyButton;
        final Button hardButton;
        final Label  modeLabel;
        final Button toggleModeButton;
        final VBox   root;
        final Scene  scene;

        titleLabel = new Label("Select Game Size");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        easyButton = new Button("8 x 8 (Beginner)");
        hardButton = new Button("36 x 16 (Expert)");

        easyButton.setPrefWidth(MENU_BUTTON_WIDTH);
        easyButton.setPrefHeight(MENU_BUTTON_HEIGHT);
        hardButton.setPrefWidth(MENU_BUTTON_WIDTH);
        hardButton.setPrefHeight(MENU_BUTTON_HEIGHT);

        modeLabel = new Label("Random Mode: OFF");
        toggleModeButton = new Button("Toggle Random Mode");

        toggleModeButton.setOnAction(e -> {
            this.randomMode = !this.randomMode;
            modeLabel.setText("Random Mode: " + (this.randomMode ? "ON" : "OFF"));
        });

        easyButton.setOnAction(e -> startGame(EASY_WIDTH, EASY_HEIGHT, EASY_MINES, primaryStage));
        hardButton.setOnAction(e -> startGame(HARD_WIDTH, HARD_HEIGHT, HARD_MINES, primaryStage));

        root = new VBox(VERTICAL_MARGIN);
        root.setPadding(new Insets(PADDING));
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, easyButton, hardButton, modeLabel, toggleModeButton);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle(TITLE_TEXT);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGame(
        final int   width,
        final int   height,
        final int   mines,
        final Stage ownerStage
    ) {
        final Stage    gameStage;
        final VBox     root;
        final VBox     topBar;
        final GridPane grid;
        final Scene    scene;

        this.flagsPlaced = STARTING_FLAGS;
        this.seconds     = STARTING_SECONDS;
        this.timerRunning = false;

        this.game = new Mines(width, height, mines, this.randomMode);

        this.buttons.clear();

        this.flagLabel  = new Label("Flags: " + this.flagsPlaced + " / " + this.game.getTotalMines());
        this.timerLabel = new Label("Time: " + this.seconds);

        final int bestScore;
        bestScore = this.game.getBestScoreSeconds();

        if (bestScore == Integer.MAX_VALUE)
        {
            this.bestLabel = new Label("Best: -");
        }
        else
        {
            this.bestLabel = new Label("Best: " + bestScore + "s");
        }

        this.flagLabel.setFont(FONT);
        this.timerLabel.setFont(FONT);
        this.bestLabel.setFont(FONT);

        topBar = new VBox(
            TOPBAR_SPACING,
            this.flagLabel,
            this.timerLabel,
            this.bestLabel
        );
        topBar.setAlignment(Pos.CENTER);

        grid = createGrid(width, height);

        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(GAME_PADDING));
        root.getChildren().addAll(topBar, grid);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        gameStage = new Stage();
        gameStage.initOwner(ownerStage);
        gameStage.setTitle("Random Mines " + width + "x" + height);
        gameStage.setScene(scene);
        gameStage.show();
    }

    private GridPane createGrid(final int width, final int height)
    {
        final GridPane grid;

        grid = new GridPane();
        grid.setPadding(new Insets(GAME_PADDING));
        grid.setHgap(GAME_PADDING);
        grid.setVgap(GAME_PADDING);
        grid.setAlignment(Pos.CENTER);

        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                final int index;
                final Button button;

                index  = (j * width) + i;
                button = new Button();

                button.setFont(FONT);
                button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
                button.setStyle(BUTTON_THEMES.get(DEFAULT_BUTTON_KEY));

                button.setOnMouseEntered(e -> button.setCursor(javafx.scene.Cursor.HAND));
                button.setOnMouseExited(e -> button.setCursor(javafx.scene.Cursor.DEFAULT));

                button.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY)
                    {
                        handleReveal(index);
                    }
                    else if (e.getButton() == MouseButton.SECONDARY)
                    {
                        handleFlag(index);
                    }
                });

                grid.add(button, i, j);
                this.buttons.add(button);
            }
        }

        return grid;
    }

    private void handleReveal(final int index)
    {
        if (!timerRunning)
        {
            startTimer();
        }

        try
        {
            final boolean hitMine;
            hitMine = this.game.reveal(index);

            if (hitMine)
            {
                handleLoss();
                return;
            }

            if (this.game.isRandomMode())
            {
                this.game.randomizeRemaining();
            }

            refreshAllButtons();

            if (this.game.hasWon())
            {
                handleWin();
                return;
            }

        }
        catch (final InvalidMoveException e)
        {
            final Alert warn = new Alert(Alert.AlertType.WARNING);
            warn.setHeaderText("Invalid move");
            warn.setContentText(e.getMessage());
            warn.showAndWait();
        }
    }

    private void handleFlag(final int index)
    {
        final int newState;

        newState = this.game.toggleFlag(index);

        if (newState == Mines.FLAG)
        {
            this.flagsPlaced++;
        }
        else if (newState == Mines.FLAG_QUESTION)
        {
            this.flagsPlaced--;
        }

        this.flagLabel.setText("Flags: " +
                               this.flagsPlaced +
                               " / " +
                               this.game.getTotalMines());

        updateButtonDisplay(index);
    }

    private void updateButtonDisplay(final int index)
    {
        final Button button;

        button = this.buttons.get(index);

        if (this.game.isFlagged(index))
        {
            button.setText("F");
            button.setStyle(BUTTON_THEMES.get(DEFAULT_BUTTON_KEY));
            return;
        }

        if (this.game.isQuestionMarked(index))
        {
            button.setText("?");
            button.setStyle(BUTTON_THEMES.get(DEFAULT_BUTTON_KEY));
            return;
        }

        if (!this.game.isRevealed(index))
        {
            button.setText("");
            button.setStyle(BUTTON_THEMES.get(DEFAULT_BUTTON_KEY));
            return;
        }

        final int value;

        value = this.game.getFieldValue(index);

        if (value == MINE_KEY)
        {
            button.setText("*");
            button.setStyle(BUTTON_THEMES.get(MINE_KEY));
        }
        else if (value == ZERO_KEY)
        {
            button.setText(" ");
            button.setStyle(BUTTON_THEMES.get(ZERO_KEY));
        }
        else
        {
            button.setText(Integer.toString(value));
            button.setStyle(BUTTON_THEMES.get(value));
        }
    }

    private void refreshAllButtons()
    {
        for (int i = 0; i < this.buttons.size(); i++)
        {
            updateButtonDisplay(i);
            if (this.game.isRevealed(i))
            {
                disableButton(this.buttons.get(i));
            }
        }
    }

    private void startTimer()
    {
        final KeyFrame tick;

        seconds = STARTING_SECONDS;
        this.timerLabel.setText("Time: " + seconds);

        tick = new KeyFrame(Duration.seconds(TIMER_TICK_SECONDS), e -> {
            this.seconds++;
            this.timerLabel.setText("Time: " + this.seconds + "s");
        });

        this.timer = new Timeline(tick);
        this.timer.setCycleCount(Animation.INDEFINITE);
        this.timer.play();

        this.timerRunning = true;
    }

    private void stopTimer()
    {
        if (this.timer != null)
        {
            this.timer.stop();
        }
        this.timerRunning = false;
    }

    private void handleWin()
    {
        final Alert winAlert;

        stopTimer();
        this.game.saveScore(this.seconds);
        this.bestLabel.setText("Best: " + this.game.getBestScoreSeconds() + "s");

        disableAllButtons();

        winAlert = new Alert(Alert.AlertType.INFORMATION);
        winAlert.setHeaderText("You Win!");
        winAlert.setContentText("You successfully cleared all safe squares!");
        winAlert.showAndWait();
    }

    private void handleLoss()
    {
        final Alert lossAlert;

        stopTimer();
        disableAllButtons();
        showAllMines();

        lossAlert = new Alert(Alert.AlertType.ERROR);
        lossAlert.setHeaderText("You lost...");
        lossAlert.setContentText("You dug up a mine. Better luck next time.");
        lossAlert.showAndWait();
    }

    private void showAllMines()
    {
        for (int i = 0; i < this.buttons.size(); i++)
        {
            if (this.game.isMine(i))
            {
                final Button button;
                button = this.buttons.get(i);

                button.setText("*");
                button.setStyle(BUTTON_THEMES.get(MINE_KEY));
            }
        }
    }

    private void disableAllButtons()
    {
        final Iterator<Button> iterator;

        iterator = this.buttons.iterator();

        while (iterator.hasNext())
        {
            final Button button;

            button = iterator.next();
            disableButton(button);
        }
    }

    private void disableButton(final Button button)
    {
        button.setMouseTransparent(true);
        button.setFocusTraversable(false);
    }
}
