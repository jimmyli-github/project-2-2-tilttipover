package puzzles.tipover.gui;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 *  The GUI for the Tip over game. Creates a board with towers and crates in the representation of numbers. Highlights
 *  the goal as red and the tipper as pink. Takes a cardinal direction and moves in that place, tipping over towers
 *  if possible until the puzzle is solved.
 *
 * @author Jaden Vo
 */

public class TipOverGUI extends Application implements Observer<TipOverModel, String> {
    /** Holds the background color for the goal */
    private static String GOAL = "-fx-background-color: CRIMSON; -fx-font-size: 50;";
    /** Holds the background color for the tipper */
    private static String TIPPER = "-fx-background-color: PINK; -fx-font-size: 50;";
    /** Holds the background color for everything else */
    private static String DEFAULT = "-fx-background-color: WHITE; -fx-font-size: 50;";
    /** the tip over model */
    private TipOverModel model;
    /** the gridpane containing the board */
    private GridPane board;
    /** holds the message */
    private Label message;
    /** stage */
    private Stage stage;
    /** the main border pane */
    private BorderPane borderPane;

    /**
     * Initializes the GUI
     *
     * @throws IOException
     */
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new TipOverModel(filename);
        this.model.addObserver(this);
    }

    /**
     * Creates the GUI
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        //Initialize a borderpane and stage
        this.stage = stage;
        stage.setTitle("Tip Over");
        borderPane = new BorderPane();

        //Setting up message
        message = new Label("Loaded: " + model.getFile());
        message.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(message, Pos.CENTER);
        message.setStyle("-fx-font-size: 15;");
        message.setPadding(new Insets(15));
        borderPane.setTop(message);

        //GridPane
        board = new GridPane();
        for(int i = 0; i < model.getRows(); i++){
            for (int g = 0; g < model.getCols(); g++){
                int num = model.gridValue(i, g);
                Coordinates thisNum = new Coordinates(i, g);
                Label numbers = new Label(String.valueOf(num));
                if (thisNum.equals(this.model.getTipper())){
                    numbers.setStyle(TIPPER);
                }
                else if (thisNum.equals(this.model.getGoal())){
                    numbers.setStyle(GOAL);
                }
                else{
                    numbers.setStyle(DEFAULT);
                }
                numbers.setAlignment(Pos.CENTER);
                numbers.setMinSize(40, 75);
                board.add(numbers, g, i);
            }
        }
        board.setAlignment(Pos.CENTER);
        borderPane.setCenter(board);

        //Border pane containing buttons on the right
        BorderPane buttons = new BorderPane();

        //HBox of directional buttons
        BorderPane dirButtons = new BorderPane();
        //UP
        Button up = new Button("↑");
        up.setStyle("-fx-font-size: 15; -fx-background-color: LIGHTCYAN; -fx-borderwidth 1; -fx-border-color: BLACK;");
        up.setMinSize(50, 50);
        BorderPane.setAlignment(up, Pos.CENTER);
        dirButtons.setTop(up);
        //DOWN
        Button down = new Button("↓");
        down.setStyle("-fx-font-size: 15; -fx-background-color: LIGHTCYAN; -fx-borderwidth 1; -fx-border-color: BLACK;");
        down.setMinSize(50, 50);
        BorderPane.setAlignment(down, Pos.CENTER);
        dirButtons.setBottom(down);
        //LEFT
        Button left = new Button("←");
        left.setStyle("-fx-font-size: 15; -fx-background-color: LIGHTCYAN; -fx-borderwidth 1; -fx-border-color: BLACK;");
        left.setMinSize(50, 50);
        dirButtons.setLeft(left);
        //RIGHT
        Button right = new Button("→");
        right.setStyle("-fx-font-size: 15; -fx-background-color: LIGHTCYAN; -fx-borderwidth 1; -fx-border-color: BLACK;");
        right.setMinSize(50, 50);
        dirButtons.setRight(right);
        //EMPTY SPACE IN THE MIDDLE
        Label empty = new Label("");
        empty.setMinSize(50, 50);
        dirButtons.setCenter(empty);
        dirButtons.setPadding(new Insets(15));

        buttons.setTop(dirButtons);
        //HBox of reset, loadgame, and hint buttons
        VBox uiButtons = new VBox();

        Button load = new Button("Load");
        load.setMinSize(100, 35);
        load.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-background-color: LIGHTSTEELBLUE; -fx-borderwidth 1; -fx-border-color: BLACK;");

        Button reset = new Button("Reset");
        reset.setMinSize(100, 35);
        reset.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-background-color: LIGHTSTEELBLUE; -fx-borderwidth 1; -fx-border-color: BLACK;");

        Button hint = new Button("Hint");
        hint.setMinSize(100, 35);
        hint.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-background-color: LIGHTSTEELBLUE; -fx-borderwidth 1; -fx-border-color: BLACK;");

        uiButtons.getChildren().addAll(load, reset, hint);
        uiButtons.setSpacing(15);
        uiButtons.setPadding(new Insets(15));
        uiButtons.setAlignment(Pos.CENTER);
        buttons.setCenter(uiButtons);
        //Add all to the main borderPane
        borderPane.setRight(buttons);

        //EVENTS
        load.setOnAction((event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load a game board.");
            File selectedFile = fileChooser.showOpenDialog(stage);
            model.loadBoardFromFile(selectedFile);
        }));

        reset.setOnAction((event -> {
            model.loadBoardFromFile("data/tipover/" + model.getFile());
            this.message.setText("Puzzle reset!");
            displayGrid();
        }));

        hint.setOnAction((event -> {
            String message = model.getHint();
            if (!Objects.equals(message, "")) {
                this.message.setText(message);
                displayGrid();
            }
        }));

        up.setOnAction((event -> {
            model.tipOver("m n");
        }));

        down.setOnAction((event -> {
            model.tipOver("m s");
        }));

        left.setOnAction((event -> {
            model.tipOver("m w");
        }));

        right.setOnAction((event -> {
            model.tipOver("m e");
        }));

        //Sets the scene and shows the stage
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setMinHeight(300);
        stage.setWidth(40 * model.getCols() + 250);
        stage.setHeight(75 * model.getRows() + 155);
        borderPane.setStyle("-fx-background-color: LAVENDERBLUSH;");
        stage.show();
    }


    /**
     * When the model announces anything, it is passed through the update and does the corresponding actions
     *
     * @param tipOverModel the object that wishes to inform this object
     *                     about something that has happened.
     * @param message      optional data the server.model can send to the observer
     */
    @Override
    public void update(TipOverModel tipOverModel, String message) {
        if (message.equals(TipOverModel.SOLVED)){
            this.message.setText(message);
            displayGrid();
            return;
        }
        else if (message.equals(TipOverModel.SOLUTION)){
            this.message.setText(message);
            displayGrid();
            return;
        }
        else if (message.equals(TipOverModel.LOAD)){
            stage.setWidth(40 * model.getCols() + 250);
            stage.setHeight(Math.max(75 * model.getRows() + 155, 450));
            this.message.setText(message + this.model.getFile());
            displayGrid();
            return;
        }
        else if (message.equals(TipOverModel.LOAD_FAILED)){
            this.message.setText(message);
            displayGrid();
            return;
        }
        else if (message.equals(TipOverModel.HINT_PREFIX)){
            this.message.setText(message);
            displayGrid();
            return;
        }
        else if(message.equals(TipOverModel.TIPMSG)){
            this.message.setText(message);
            return;
        }
        else if (message.equals(TipOverModel.OFFBOARDMSG)){
            this.message.setText(message);
            displayGrid();
            return;
        }
        else if (message.equals(TipOverModel.CANTIP)){
            this.message.setText(message);
            displayGrid();
            return;
        }
        else if (message.equals(TipOverModel.MESSAGE)){
            this.message.setText(message);
            displayGrid();
            return;
        }
        displayGrid();
        this.message.setText(message);
    }

    /**
     * Displays the grid to the user
     */
    public void displayGrid(){
        board = new GridPane();
        for (int i = 0; i < model.getRows(); i++){
            for (int g = 0; g < model.getCols(); g++){
                Coordinates current = new Coordinates (i, g);
                int num = model.gridValue(i, g);
                Label numbers = new Label(String.valueOf(num));
                if (model.getTipper().equals(current)){
                    numbers.setStyle(TIPPER);
                }
                else if (model.getGoal().equals(current)){
                    numbers.setStyle(GOAL);
                }
                else{
                    numbers.setStyle(DEFAULT);
                }
                numbers.setAlignment(Pos.CENTER);
                numbers.setMinSize(40, 75);
                board.add(numbers, g, i);
            }
        }
        board.setAlignment(Pos.CENTER);
        borderPane.setCenter(board);
    }

    /**
     * The main function that checks if there is a valid argument, if there is then runs the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
