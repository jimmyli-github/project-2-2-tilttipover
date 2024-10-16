package puzzles.tipover.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import puzzles.common.Coordinates;

/**
 * The TipOverModel implements the game rules for the Tip Over Game.
 *
 * @author Jaden Vo
 */

public class TipOverModel {
    /** String holding hint prefixes */
    public static String HINT_PREFIX = "Next step!";
    /** String holding load failed */
    public static String LOAD_FAILED = "Failed to load ";
    /** String holding load */
    public static String LOAD = "Loaded: ";
    /** String holding no crates or towers in location */
    public static String MESSAGE = "No crate or tower there.";
    /** String holding a tipped over tower */
    public static String TIPMSG= "A tower has been tipped over.";
    /** String holding a move off the board */
    public static String OFFBOARDMSG = "Move goes off the board.";
    /** A string holding if a tower can be tipped */
    public static String CANTIP = "Tower cannot be tipped over.";
    /** String holding if the current board is solved */
    public static String SOLVED = "Current board is already solved.";
    /** String holding if user won */
    public static String SOLUTION = "I WON!";
    /** the collection of observers of this model */
    private final List<Observer<TipOverModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    private TipOverConfig currentConfig;
    private String file;

    /**
     * Constructer for the TipOverModel
     *
     * @param filename the file to be put into the config
     * @throws IOException
     */
    public TipOverModel(String filename) throws IOException{
        currentConfig = new TipOverConfig(filename);
        this.file = filename;
    }
    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TipOverModel, String> observer) {
        this.observers.add(observer);
    }

    public void loadBoardFromFile(File file){
        loadBoardFromFile(String.valueOf(file));
    }

    /**
     * Attempts to load a board from a file object. It will announce to the observers if it was loaded successfully or not.
     * @param filename The file to load
     * @return True iff loaded successfully
     */
    public void loadBoardFromFile(String filename)  {
        try {
            if (!Objects.equals(filename, "null")) {
                file = filename;
                int index;
                if (file.contains("data/tipover/")) {
                    index = file.indexOf("data/tipover/");
                }
                else {
                    index = file.indexOf("data\\tipover\\");
                }
                file = file.substring(index);
            }
        }
        catch (Exception ignore) {
        }
        try {
            currentConfig = new TipOverConfig(filename);
            announce(LOAD);
        }catch (Exception e) {
            announce(LOAD_FAILED);
        }
    }


    /**
     * Prints a hint if the path is not empty
     *
     * @return a boolean
     */
    public String getHint(){
        if (currentConfig.isAlreadySolved()){
            announce(SOLVED);
            return "";
        }
        Solver solve = new Solver();
        List<Configuration> path = solve.solve(currentConfig);
        String msg = "";
        if (path.isEmpty()){
            msg = "No Solution";
        }
        else if (path.size() == 1){
            if (currentConfig.isSolution()){
                announce(SOLVED);
                currentConfig.solution = true;
                currentConfig.solved = true;
            }
        }
        else{
            currentConfig = (TipOverConfig) path.get(1);
            if (currentConfig.isSolution()){
                announce(SOLUTION);
                currentConfig.solution = true;
                currentConfig.solved = true;
            }
            else {
                announce(HINT_PREFIX);
            }
        }
        return msg;
    }


    /**
     * Announce to observers the model has changed
     *
     * @param arg String to be announced
     */
    private void announce(String arg){
        for (var obs : this.observers){
            obs.update(this, arg);
        }
    }

    /**
     * sets the current configuration as the location the player wants to move.
     *
     * @param direction where player wants to move
     */
    public void tipOver(String direction){
        if (direction.toLowerCase().contains("n")){
            TipOverConfig north = (TipOverConfig) this.currentConfig.northCase();
            if (currentConfig.isSolution()){
                if (currentConfig.isAlreadySolved()){
                    announce(SOLVED);
                }
                else {
                    announce(SOLUTION);
                }
            }
            else if (currentConfig.isAlreadySolved()){
                announce(SOLVED);
            }
            else if (north != null){
                if (currentConfig.isTipped()){
                    announce(TIPMSG);
                }
                currentConfig = north;
                if (currentConfig.isSolution()){
                    announce(SOLUTION);
                }
                else{
                    announce("");
                }
            }
            else if (!this.currentConfig.canTip()){
                announce(CANTIP);
            }
            else if (this.currentConfig.isZero()){
                announce(MESSAGE);
            }
            else if (this.currentConfig.offBoard()){
                announce(OFFBOARDMSG);
            }
        }
        else if (direction.toLowerCase().contains("s")) {
            TipOverConfig south = (TipOverConfig) this.currentConfig.southCase();
            if (currentConfig.isSolution()){
                if (currentConfig.isAlreadySolved()){
                    announce(SOLVED);
                }
                else {
                    announce(SOLUTION);
                }
            }
            else if (currentConfig.isAlreadySolved()){
                announce(SOLVED);
            }
            else if (south != null){
                if (currentConfig.isTipped()){
                    announce(TIPMSG);
                }
                currentConfig = south;
                if (currentConfig.isSolution()){
                    announce(SOLUTION);
                }
                else{
                    announce("");
                }
            }
            else if (!this.currentConfig.canTip()){
                announce(CANTIP);
            }
            else if (this.currentConfig.isZero()){
                announce(MESSAGE);
            }
            else if (this.currentConfig.offBoard()){
                announce(OFFBOARDMSG);
            }
        }
        else if (direction.toLowerCase().contains("w")) {
            TipOverConfig west = (TipOverConfig) this.currentConfig.westCase();
            if (currentConfig.isSolution()){
                if (currentConfig.isAlreadySolved()){
                    announce(SOLVED);
                }
                else {
                    announce(SOLUTION);
                }
            }
            else if (currentConfig.isAlreadySolved()){
                announce(SOLVED);
            }
            else if (west != null){
                if (currentConfig.isTipped()){
                    announce(TIPMSG);
                }
                currentConfig = west;
                if (currentConfig.isSolution()){
                    announce(SOLUTION);
                }
                else{
                    announce("");
                }
            }
            else if (!this.currentConfig.canTip()){
                announce(CANTIP);
            }
            else if (this.currentConfig.isZero()){
                announce(MESSAGE);
            }
            else if (this.currentConfig.offBoard()){
                announce(OFFBOARDMSG);
            }
        }
        else if (direction.toLowerCase().contains("e")) {
            TipOverConfig east = (TipOverConfig) this.currentConfig.eastCase();
            if (currentConfig.isSolution()){
                if (currentConfig.isAlreadySolved()){
                    announce(SOLVED);
                }
                else {
                    announce(SOLUTION);
                }
            }
            else if (currentConfig.isAlreadySolved()){
                announce(SOLVED);
            }
            else if (east != null){
                if (currentConfig.isTipped()){
                    announce(TIPMSG);
                }
                currentConfig = east;
                if (currentConfig.isSolution()){
                    announce(SOLUTION);
                }
                else{
                    announce("");
                }
            }
            else if (!this.currentConfig.canTip()){
                announce(CANTIP);
            }
            else if (this.currentConfig.isZero()){
                announce(MESSAGE);
            }
            else if (this.currentConfig.offBoard()){
                announce(OFFBOARDMSG);
            }
        }
    }

    /**
     * gets the current configs rows
     *
     * @return the current configs rows
     */
    public int getRows(){
        return currentConfig.getRows();
    }

    /**
     * gets the current configs cols
     *
     * @return the current configs cols
     */
    public int getCols(){
        return currentConfig.getCols();
    }

    /**
     * gets the tipper position
     *
     * @return the tipper position
     */
    public Coordinates getTipper(){
        return currentConfig.getTipper();
    }

    /**
     * gets the goal position
     *
     * @return the goal position
     */
    public Coordinates getGoal(){
        return currentConfig.getGoal();
    }
    /**
     * Gets the value of the grid given a row and column
     *
     * @param row the row to be accessed
     * @param col the col to be accessed
     * @return the value on the grid
     */
    public int gridValue(int row, int col){
        return currentConfig.getValue(row, col);
    }

    public String getFile(){
        file = file.replace("data/tipover/", "");
        file = file.replace("data\\tipover\\", "");
        return file;
    }


    /**
     * the same toString as the currentConfig
     *
     * @return The current configurations grid
     */
    @Override
    public String toString(){
        return this.currentConfig.toString();
    }
}
