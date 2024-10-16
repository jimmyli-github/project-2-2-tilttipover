package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/** Implements the game rules for Tilt.
 */
public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();
    public static String LOADED = "Loaded: ";
    /**
     * Message sent when a board has failed to load.
     */
    public static String LOAD_FAILED = "Failed to load";
    /**
     * The message that will precede a hint.
     */
    public static String HINT_PREFIX = "Next step!";

    /** the current configuration */
    private TiltConfig currentConfig;
    /** the name of the current file **/
    private String file;


    /**
     * Creates a new TiltConfig from the given filename.
     * It also sets the filename to a private variable.
     * @param filename The name of the current file
     * @throws IOException Handles the exception for creating a TiltConfig
     */
    public TiltModel(String filename) throws IOException {
        currentConfig = new TiltConfig(filename);
        this.file = filename;
    }

    /**
     * The getter method for grid of the current config.
     * @return The grid of the current config
     */
    public String getGrid() {
        return currentConfig.getGrid().strip();
    }

    /**
     * The getter method for the grid value on a given
     * row and column.
     * @param row The specific row
     * @param col The specific column
     * @return The grid value
     */
    public char getGridValue(int row, int col) {
        return currentConfig.getGridValue(row, col);
    }

    /**
     * The getter method for the size of the current grid.
     * @return The grid value
     */
    public int getSize() {
        return currentConfig.getSize();
    }

    /**
     * The getter method for the filename.
     * @return The filename
     */
    public String getFile() {
        file = file.replace("data/tilt/", "");
        file = file.replace("data\\tilt\\", "");
        return file;
    }

    /**
     * Checks if the game is over.
     * @return boolean of whether the game is over or not
     */
    public boolean gameOver(){
        return currentConfig.isSolution();
    }

    /**
     * Loads a board from a file
     * @param file the file of the tilt puzzle
     */
    public void loadBoardFromFile(File file) {
        loadBoardFromFile(String.valueOf(file));
    }

    /**
     * Loads a board from a filename by doing a
     * try to set currentConfig to a new TiltConfig
     * with the filename. If successful, it will
     * announce LOADED. Otherwise, it will announce
     * LOAD_FAILED.
     * @param filename the filename of the tilt puzzle
     */
    public void loadBoardFromFile(String filename) {
        try {
            if (!Objects.equals(filename, "null")) {
                file = filename;
                int index;
                if (file.contains("data/tilt/")) {
                    index = file.indexOf("data/tilt/");
                }
                else {
                    index = file.indexOf("data\\tilt\\");
                }
                file = file.substring(index);
            }
        } catch (Exception ignored) {

        }
        try {
            currentConfig = new TiltConfig(filename);
            announce(LOADED);
        }
        catch (IOException e) {
            announce(LOAD_FAILED);
        }
    }

    /**
     * If solvable, the puzzle advances to the next step in the
     * solution and announces HINT_PREFIX to show that it was
     * successful. This is done by using the solver and setting
     * currentConfig to index 1 of the path. If the path is empty,
     * then it will return a string saying that there was no solution.
     * If the path is one, then it will return a string saying that
     * the puzzle was already solved.
     * @return a message corresponding to if the board has no solution,
     *          was already solved, or that hint was successful
     */
    public String getHint() {
        Solver solver = new Solver();
        List<Configuration> path = solver.solve(currentConfig);
        String msg = "";
        if (path.isEmpty()) {
            msg = "No solution!";
        }
        else if (path.size() == 1) {
            msg = "Already solved!";
        }
        else {
            currentConfig = (TiltConfig) path.get(1);
            announce(HINT_PREFIX);
        }
        return msg;
    }

    /**
     * Tilts the board by using the methods up(), down(),
     * left(), and right() in the TiltConfig class.
     * If the move is valid, it will update currentConfig
     * and return true. Otherwise, it will return false.
     * @param direction The tilt direction
     * @return boolean for whether the tilt move in the given
     *          direction was successful
     */
    public boolean tilt(String direction) {
        if (direction.toLowerCase().contains("n")) {
            TiltConfig upConfig = (TiltConfig) currentConfig.up();
            if (upConfig != null) {
                currentConfig = upConfig;
            }
            else {
                return false;
            }
        }
        else if (direction.toLowerCase().contains("s")) {
            TiltConfig downConfig = (TiltConfig) currentConfig.down();
            if (downConfig != null) {
                currentConfig = downConfig;
            }
            else {
                return false;
            }
        }
        else if (direction.toLowerCase().contains("e")) {
            TiltConfig rightConfig = (TiltConfig) currentConfig.right();
            if (rightConfig != null) {
                currentConfig = rightConfig;
            }
            else {
                return false;
            }
        }
        else {
            TiltConfig leftConfig = (TiltConfig) currentConfig.left();
            if (leftConfig != null) {
                currentConfig = leftConfig;
            }
            else {
                return false;
            }
        }
        alertObservers("");
        return true;
    }

    /**
     * Announce to observers the model has changed
     * @param arg A message
     */
    private void announce(String arg) {
        for ( var obs : this.observers ) {
            obs.update( this, arg );
        }
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
