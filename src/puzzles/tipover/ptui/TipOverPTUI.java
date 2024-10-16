package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tipover.model.*;
import java.io.*;
import java.util.*;

/**
 * The TipOverPTUI is the text UI of the Tip Over Game. Creates a board in the terminal and takes in arguments
 * to run specific actions.
 *
 * @author Jaden Vo
 */

public class TipOverPTUI implements Observer<TipOverModel, String> {
    /** the mode of the PTUI */
    private TipOverModel model;
    /** Scanner for input #*/
    private Scanner in;
    /** boolean for game on */
    private boolean gameOn;
    /** String for the file */
    private String file;

    /**
     * The Text UI for Tip Over
     *
     * @param file the file to be used in the TipOverModel constructor
     */
    public TipOverPTUI(String file) throws IOException {
        this.model = new TipOverModel(file);
        this.model.addObserver(this);
        this.gameOn = false;
        this.file = file;
        this.in = new Scanner(System.in);
    }

    /**
     * Gets a filename from the user and attempts to load the file.
     *
     * @param filename the file to be loaded
     */
    public void loadFromFile(String filename){
        file = filename.substring(2);
        model.loadBoardFromFile(file);
    }

    /**
     * Displays the grid for the model
     */
    public void displayGrid(){
        System.out.println(model.toString());
    }

    /**
     * When the model announces anything, it is passed through the update and does the corresponding actions
     * SOLVED - Current board already solved
     * SOLUTION - I WON
     * LOAD - Loaded
     * LOAD_FAILED - Load Failed
     * HINT_PREFIX - Next Step
     * MESSAGE - No crate or tower
     * TIPMSG - Tower tipped over
     * OFFBOARDMSG - Move off the board
     * CANTIP - Cannot be tipped
     *
     * @param model   the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     */
    @Override
    public void update(TipOverModel model, String message) {
        if (message.equals(TipOverModel.SOLVED)){
            System.out.println(message);
            System.out.println();
            return;
        }
        else if (message.equals(TipOverModel.SOLUTION)){
            System.out.println(message);
            System.out.println();
            displayGrid();
            System.out.println();
            return;
        }
        else if (message.equals(TipOverModel.LOAD)){
            if (gameOn) {
                System.out.println(message + file);
            }
            System.out.println();
            displayGrid();
            if (gameOn){
                System.out.println("");
            }
            return;
        }
        else if (message.equals(TipOverModel.LOAD_FAILED)){
            System.out.println(message + file);
            System.out.println();
            displayGrid();
            return;
        }
        else if (message.equals(TipOverModel.HINT_PREFIX)){
            System.out.println(message);
            System.out.println();
            displayGrid();
            System.out.println();
            return;
        }
        else if(message.equals(TipOverModel.TIPMSG)){
            System.out.print(message);
            return;
        }
        else if (message.equals(TipOverModel.OFFBOARDMSG)){
            System.out.println(message);
            return;
        }
        else if (message.equals(TipOverModel.CANTIP)){
            System.out.println(message);
            System.out.println();
            return;
        }
        else if (message.equals(TipOverModel.MESSAGE)){
            System.out.println(message);
            return;
        }
        System.out.println(message);
    }

    /**
     * Initializes the game with a file.
     *
     * @param filename file to be loaded
     * @param reset boolean of if board was reset
     * @return
     */
    public boolean gameStart(String filename, boolean reset){
        File file = new File(filename);
        if (!file.exists()){
            System.out.println("Failed to load: " + filename);
            System.exit(0);
        }
        if (!reset){
            System.out.println("Loaded: " + filename + "\n");
        }
        displayGrid();
        gameOn = true;
        return true;
    }

    /**
     * runs the game until quit
     *
     * @param filename File to be passed into game start
     * @param reset checks to see if game was reset
     * @throws IOException
     */
    public void run(String filename, boolean reset) throws IOException {
        while (gameStart(filename, reset)){
            gameLoop(reset);
        }
    }

    /**
     * The main loop for the TipOverPTUI. Gets user input and runs commands based on them.
     *
     * @param reset Whether the game has been reset
     * @throws IOException
     */
    public void gameLoop(boolean reset) throws IOException {
        String msg;
        if (!reset){
            System.out.println("\nh(int)              -- hint next move\n" +
                    "l(oad) filename     -- load new puzzle file\n" +
                    "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                    "q(uit)              -- quit the game\n" +
                    "r(eset)             -- reset the current game");
        }
        while (gameOn){
            msg = "";
            String command = in.nextLine().strip();
            System.out.print("> ");
            if (command.equalsIgnoreCase("h") || command.equalsIgnoreCase("hint")){
                String hintmsg = this.model.getHint();
                if (!Objects.equals(hintmsg, "")) {
                    System.out.println(hintmsg);
                    displayGrid();
                    System.out.println();
                }
            }
            else if (command.toLowerCase().contains("l ") || command.toLowerCase().contains("load ")){
                this.loadFromFile(command);
            }
            else if (command.toLowerCase().contains("m ") || command.toLowerCase().contains("move ")){
                this.model.tipOver(command);
                System.out.println();
                displayGrid();
                System.out.println();
            }
            else if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("quit")){
                System.exit(0);
            }
            else if (command.equalsIgnoreCase("r") || command.equalsIgnoreCase("reset")){
                System.out.println("Puzzle reset!");
                gameOn = false;
                model.loadBoardFromFile(file);
                gameOn = true;
                System.out.println("");
            }
            else {
                msg = "Not a valid command";
            }
            if (!msg.isEmpty()) {
                if (reset) {
                    System.out.println("h(int)              -- hint next move\n" +
                            "l(oad) filename     -- load new puzzle file\n" +
                            "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                            "q(uit)              -- quit the game\n" +
                            "r(eset)             -- reset the current game");
                }
                gameLoop(reset);
            }
        }
    }

    /**
     * The main method. Creates a TipOverPTUI and runs the game if there is a filename.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        }
        else{
            TipOverPTUI game = new TipOverPTUI(args[0]);
            game.run(args[0], false);
        }
    }
}