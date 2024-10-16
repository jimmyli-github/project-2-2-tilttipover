package puzzles.tilt.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * A GUI interface for Tilt
 */
public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /** The green disk/slider image **/
    private Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    /** The blue disk/slider image **/
    private Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));
    /** The blocker image **/
    private Image blocker = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    /** The hole image **/
    private Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));
    /** The model for Tilt **/
    private TiltModel model;
    /** The text-field of the GUI on top of the stage**/
    private TextField textField;
    /** The grid pane of the GUI that represents the board **/
    private GridPane gridPane;
    /** The border of the GUI at the center of the stage that includes the board and buttons **/
    private BorderPane gridWithButtons;
    /** The stage of the GUI **/
    private Stage stage;

    /**
     * Initializes the model and adds this as an observer.
     * @throws IOException handles for exception for creating a tiltModel
     */
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new TiltModel(filename);
        this.model.addObserver(this);
    }

    /**
     * Creates the GUI for the puzzle with all the buttons being set
     * to a specific action. The stage's title is named Tilt and
     * a BorderPane is created. The top of the borderPane is a textField
     * which displays the amount of moves and specific messages. The center
     * of the borderPane is another borderpane that consists of a gridPane and buttons
     * that is used for the puzzle. On the right, there is a vbox that consists of
     * more buttons such as load, reset, and hint.
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception handles exceptions
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Button button = new Button();
        button.setGraphic(new ImageView(greenDisk));
        Scene scene = new Scene(button);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Tilt GUI");

        BorderPane borderPane = new BorderPane();
        textField = new TextField("Loaded: " + model.getFile());
        textField.setEditable(false);
        textField.setAlignment(Pos.CENTER);
        textField.setStyle("-fx-font-size: 15;");
        borderPane.setTop(textField);

        gridWithButtons = new BorderPane();
        gridPane = new GridPane();
        for (int row = 0; row < model.getSize(); row++) {
            for (int col = 0; col < model.getSize(); col++) {
                char symbol = model.getGridValue(row, col);
                ImageView imageView;
                if (symbol == 'G') {
                    imageView = new ImageView(greenDisk);
                }
                else if (symbol == 'B') {
                    imageView = new ImageView(blueDisk);
                }
                else if (symbol == 'O') {
                    imageView = new ImageView(hole);
                }
                else if (symbol == '*') {
                    imageView = new ImageView(blocker);
                }
                else {
                    imageView = new ImageView();
                }
                imageView.setFitWidth(75);
                imageView.setFitHeight(75);
                gridPane.add(imageView, col, row);
            }
        }
        gridPane.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
        gridPane.setPadding(new Insets(20));
        gridWithButtons.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridWithButtons.setCenter(gridPane);

        Button upButton = new Button("^");
        upButton.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        gridWithButtons.setTop(upButton);
        upButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        BorderPane.setAlignment(upButton, Pos.CENTER);

        Button downButton = new Button("v");
        downButton.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        gridWithButtons.setBottom(downButton);
        downButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        BorderPane.setAlignment(downButton, Pos.CENTER);

        Button leftButton = new Button("<");
        leftButton.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        gridWithButtons.setLeft(leftButton);
        leftButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        BorderPane.setAlignment(leftButton, Pos.CENTER);

        Button rightButton = new Button(">");
        rightButton.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        gridWithButtons.setRight(rightButton);
        rightButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        BorderPane.setAlignment(rightButton, Pos.CENTER);

        gridWithButtons.setPadding(new Insets(20));
        borderPane.setCenter(gridWithButtons);

        VBox vBox = new VBox();
        Button load = new Button("Load");
        load.textFillProperty();
        load.setMinWidth(150);
        load.setMinHeight(75);
        load.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        vBox.getChildren().add(load);

        Button reset = new Button("Reset");
        reset.setMinWidth(150);
        reset.setMinHeight(75);
        reset.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        vBox.getChildren().add(reset);

        Button hint = new Button("Hint");
        hint.setMinWidth(150);
        hint.setMinHeight(75);
        vBox.getChildren().add(hint);
        vBox.setAlignment(Pos.CENTER);
        hint.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));
        borderPane.setRight(vBox);

        stage.setScene(new Scene(borderPane));

        load.setOnAction((event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load a game board.");
            File selectedFile = fileChooser.showOpenDialog(stage);
            model.loadBoardFromFile(selectedFile);
        }));

        hint.setOnAction((event -> {
            String message = model.getHint();
            if (!Objects.equals(message, "")) {
                textField.setText(message);
                displayBoard();
            }
        }));

        reset.setOnAction((event -> {
            model.loadBoardFromFile("data/tilt/" + model.getFile());
            textField.setText("Puzzle reset!");
            displayBoard();
        }));

        upButton.setOnAction((event -> {
            if (model.gameOver()) {
                textField.setText("Already solved!");
            } else {
                if (!model.tilt("t N")) {
                    textField.setText("Illegal move. A blue slider will fall through the hole");
                }
            }

        }));

        downButton.setOnAction((event -> {
            if (model.gameOver()) {
                textField.setText("Already solved!");
            }
            else {
                if (!model.tilt("t S")) {
                    textField.setText("Illegal move. A blue slider will fall through the hole");
                }
            }
        }));

        leftButton.setOnAction((event -> {
            if (model.gameOver()) {
                textField.setText("Already solved!");
            }
            else {
                if (!model.tilt("t W")) {
                    textField.setText("Illegal move. A blue slider will fall through the hole");
                }
            }
        }));

        rightButton.setOnAction((event -> {
            if (model.gameOver()) {
                textField.setText("Already solved!");
            }
            else {
                if (!model.tilt("t E")) {
                    textField.setText("Illegal move. A blue slider will fall through the hole");
                }
            }
        }));
    }

    /**
     * Uses two for loops to search through each gridValue on the grid.
     * It checks for the symbol and updates the specific tile with the
     * corresponding image. It will also adjust the GUI width and height
     * based on the size of the board.
     */
    public void displayBoard() {
        gridPane = new GridPane();
        for (int row = 0; row < model.getSize(); row++) {
            for (int col = 0; col < model.getSize(); col++) {
                char symbol = model.getGridValue(row, col);
                ImageView imageView;
                if (symbol == 'G') {
                    imageView = new ImageView(greenDisk);
                }
                else if (symbol == 'B') {
                    imageView = new ImageView(blueDisk);
                }
                else if (symbol == 'O') {
                    imageView = new ImageView(hole);
                }
                else if (symbol == '*') {
                    imageView = new ImageView(blocker);
                }
                else {
                    imageView = new ImageView();
                }
                imageView.setFitWidth(75);
                imageView.setFitHeight(75);
                gridPane.add(imageView, col, row);
                stage.setWidth(75 * model.getSize() + 400);
                stage.setHeight(75 * model.getSize() + 275);
            }
        }
        gridPane.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
        gridPane.setPadding(new Insets(20));
        gridWithButtons.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridWithButtons.setCenter(gridPane);
    }

    /**
     * Updates the message in the textField with a specific message.
     * It also displays the updated board.
     * @param tiltModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel tiltModel, String message) {
        if (message.equals(TiltModel.LOADED)) {
            textField.setText(message + model.getFile());
            displayBoard();
            return;
        }
        else if (message.equals(TiltModel.LOAD_FAILED)) {
            textField.setText(message);
            displayBoard();
            return;
        }
        else if (message.startsWith(TiltModel.HINT_PREFIX)) {
            textField.setText(message);
            displayBoard();
            if (model.gameOver()) {
                displayBoard();
                textField.setText("Congratulations");
            }
            return;
        }
        else {
            displayBoard();
            textField.setText("");
        }

        if (model.gameOver()) {
            displayBoard();
            textField.setText("Congratulations");
        }
    }

    /**
     * Launches the application.
     * @param args argument commands
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
