package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * A text user interface for Tilt
 */
public class TiltPTUI implements Observer<TiltModel, String> {
    /** The tilt model **/
    private TiltModel model;
    /** The scanner that takes in user inputs **/
    private static Scanner in;
    /** The boolean for if the game is on **/
    private boolean gameOn;
    /** The file of the tilt puzzle **/
    private String file;

    /**
     * Takes in messages from model and prints out the
     * corresponding message. It will also display the updated
     * board.
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel model, String message) {
        if (message.equals(TiltModel.LOADED)) {
            if (gameOn) {
                System.out.println(message + file);
            }
            displayBoard();
            if (gameOn) {
                System.out.println();
            }
            return;
        }
        else if (message.equals(TiltModel.LOAD_FAILED)) {
            if (gameOn) {
                System.out.println(message + ": " + file);
            }
            displayBoard();
            if (gameOn) {
                System.out.println();
            }
            return;
        }
        else if (message.startsWith(TiltModel.HINT_PREFIX)) {
            System.out.println(message);
            displayBoard();
            System.out.println();
            return;
        }

        if (model.gameOver()) {
            displayBoard();
            System.out.println("You win!");
            return;
        }
        displayBoard();
        System.out.print(message);
    }

    /**
     * The Text UI for Tilt
     * @param argsFile the initial puzzle file
     * @throws IOException handles the exception for when TiltModel is made
     */
    public TiltPTUI(String argsFile) throws IOException {
        model = new TiltModel(argsFile);
        file = argsFile;
        model.addObserver(this);
        gameOn = false;
        in = new Scanner(System.in);
    }

    /**
     * The main program loop. Keeps getting user input until
     * the user quits
     * @param argsFile the initial puzzle file
     * @param reset boolean for whether the puzzle has been reset before
     */
    public void run(String argsFile, boolean reset) {
        while (gameStart(argsFile, reset)) {
            gameLoop(reset);
        }
    }

    /**
     * Checks if the file exists and prints a
     * loaded message if the puzzle has never been reset.
     * If file exists, it returns true.
     * @param argsFile the initial puzzle file
     * @param reset boolean for whether the puzzle has been reset before
     * @return boolean for whether the game can start
     */
    public boolean gameStart(String argsFile, boolean reset) {
        File file = new File(argsFile);
        if (!file.exists()) {
            System.out.println("File Not Found!");
            System.exit(0);
        }
        if (!reset) {
            System.out.println("Loaded: " + file);
        }
        displayBoard();
        System.out.println();
        gameOn = true;
        return true;
    }

    /**
     * Handles the actual game play. It gets user input in order to tell
     * if the next move is hint, load file, tilt, quit, or reset. It calls
     * the corresponding method in tiltModel and prints corresponding messages
     * based on the input.
     * @param reset boolean for whether the puzzle has been reset before
     */
    public void gameLoop(boolean reset) {
        String msg;
        if (!reset) {
            System.out.println("h(int)              -- hint next move\n" +
                    "l(oad) filename     -- load new puzzle file\n" +
                    "t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                    "q(uit)              -- quit the game\n" +
                    "r(eset)             -- reset the current game");
        }
        while (gameOn) {
            msg = "";
            System.out.print("> ");
            String command = "";
            command = in.nextLine().strip();

            if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("quit")) {
                System.exit(0);
            }
            else if (command.equalsIgnoreCase("h") || command.equalsIgnoreCase("hint")) {
                String message = model.getHint();
                if (!Objects.equals(message, "")) {
                    System.out.println(message);
                    displayBoard();
                    System.out.println();
                }
            }
            else if (command.toLowerCase().contains("l ") || command.equalsIgnoreCase("load")) {
                loadFromFile(command);
            }
            else if (command.toLowerCase().contains("t ") || command.toLowerCase().contains("tilt")) {
                if (model.gameOver()) {
                    System.out.println("Already solved!");
                    displayBoard();
                }
                else if (!model.tilt(command)) {
                    System.out.println("Illegal move. A blue slider will fall through the hole!");
                    displayBoard();
                }
                System.out.println();
            }
            else if (command.equalsIgnoreCase("r") || command.toLowerCase().contains("reset")) {
                System.out.println("Puzzle reset!");
                gameOn = false;
                model.loadBoardFromFile(file);
                gameOn = true;
                System.out.println();
            }
            else {
                msg = "Enter a valid command";
            }

            if (!msg.isEmpty()) {
                if (reset) {
                    System.out.println("h(int)              -- hint next move\n" +
                            "l(oad) filename     -- load new puzzle file\n" +
                            "t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                            "q(uit)              -- quit the game\n" +
                            "r(eset)             -- reset the current game");
                }
                gameLoop(reset);
            }
        }
    }

    /**
     * Gets a filename from the user's load command and attempts to load the file.
     * @param command the load command which includes the filename
     */
    public void loadFromFile(String command){
        file = command.substring(2);
        model.loadBoardFromFile(file);
    }

    /**
     * Displays the current board.
     */
    public void displayBoard() {
        System.out.println(model.getGrid());
    }

    /**
     * Runs the Text UI for Tilt
     * @param args the command line args
     * @throws IOException handles the exception for when TiltPTUI is made
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        TiltPTUI tiltPTUI = new TiltPTUI(args[0]);
        tiltPTUI.run(args[0], false);
    }
}