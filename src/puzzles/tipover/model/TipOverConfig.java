package puzzles.tipover.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;
import java.io.*;
import java.util.*;

/**
 * The TipOverConfig implements the configuration interface in order to instantiate a new TipOverConfig object
 * in the main to be passed into the solver class.
 *
 * @author Jaden Vo
 */

public class TipOverConfig implements Configuration {
    /** Number of rows for the board */
    private int rows;
    /** Number of columns for the board */
    private int cols;
    /** The position of the tipper */
    private Coordinates tipperPos;
    /** The position of the goal */
    private Coordinates goal;
    /** Grid for the tip over puzzle */
    private int[][] grid;
    /** set of neighbors */
    private Set<Configuration> neighbors;
    /** boolean holding tipped tower */
    private boolean tipped;
    /** boolean holding off or on board */
    private boolean offBoard;
    /** boolean of whether a tower CAN be tipped */
    private boolean canTip;
    /** boolean containing if a config is a solution */
    public boolean solution;
    /** boolean containing if a config has already been solved */
    public boolean solved;
    private boolean isZero;

    /**
     * The constructor for the tip over puzzle
     *
     * @param tipperPos The initial position of the tipper
     * @param goal The position of the goal.
     */
    public TipOverConfig(Coordinates tipperPos, Coordinates goal, int[][] grid) {
        this.tipperPos = tipperPos;
        this.goal = goal;
        this.grid = grid;
        this.rows = this.grid.length;
        this.cols = this.grid[0].length;
        this.neighbors = new LinkedHashSet<>();
        this.tipped = false;
        this.offBoard = false;
        this.canTip = true;
        this.solution = false;
        this.solved = false;
        this.isZero = false;
    }

    /**
     * Constructor to take in a filename
     *
     * @param filename File to be read
     * @throws IOException
     */
    public TipOverConfig(String filename) throws IOException {
        File file = new File(filename);
        Scanner scan = new Scanner(file);
        String[] initials = scan.nextLine().split(" ");
        this.rows = Integer.parseInt(initials[0]);
        this.cols = Integer.parseInt(initials[1]);
        this.tipperPos = new Coordinates(Integer.parseInt(initials[2]),
                Integer.parseInt(initials[3]));
        this.goal = new Coordinates(Integer.parseInt(initials[4]),
                Integer.parseInt(initials[5]));
        this.grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            String[] boardVals = scan.nextLine().split(" ");
            for (int g = 0; g < boardVals.length; g++) {
                this.grid[i][g] = Integer.parseInt(boardVals[g]);
            }
        }
        this.neighbors = new LinkedHashSet<>();
        this.tipped = false;
        this.offBoard = false;
        this.canTip = true;
        this.solution = false;
        this.solved = false;
        this.isZero = false;
    }

    /**
     * Is the current configuration a solution?
     *
     * @return true if the configuration is a puzzle's solution; false, otherwise
     */
    @Override
    public boolean isSolution() {
        return this.tipperPos.equals(this.goal);
    }

    /**
     * Get the collection of neighbors from the current configuration.
     *
     * @return All the neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        neighbors.add(new TipOverConfig(tipperPos, goal, grid));
        TipOverConfig north = (TipOverConfig) northCase();
        if (north != null){
            neighbors.add(north);
        }
        TipOverConfig south = (TipOverConfig) southCase();
        if (south != null){
            neighbors.add(south);
        }
        TipOverConfig west = (TipOverConfig) westCase();
        if (west != null){
            neighbors.add(west);
        }
        TipOverConfig east = (TipOverConfig) eastCase();
        if (east != null){
            neighbors.add(east);
        }
        return neighbors;
    }

    /**
     * Helper method to get the north configuration
     *
     * @return the north configuration
     */
    public Configuration northCase(){
        if (this.isSolution()){
            this.solution = true;
        }
        if (solution){
            this.solved = true;
            return this;
        }
        TipOverConfig tip = null;
        int curRow = this.tipperPos.row();
        int curCol = this.tipperPos.col();
        //Cases for towers
        if (grid[curRow][curCol] > 1) {
            int height = grid[curRow][curCol];
            if (curRow - height >= 0) {
                int[][] newGrid = gridCopy();
                int tempHeight = height;
                for (int i = height; i > 0; i--) {
                    if (newGrid[curRow - i][curCol] != 0) {
                        break;
                    } else {
                        tempHeight--;
                    }
                }
                if (tempHeight == 0) {
                    int north = curRow - 1;
                    for (int i = height; i > 0; i--) {
                        newGrid[curRow - i][curCol] = 1;
                    }
                    newGrid[curRow][curCol] = 0;
                    if (newGrid[north][curCol] >= 1) {
                        this.tipped = true;
                        Coordinates northPos = new Coordinates(north, curCol);
                        tip = new TipOverConfig(northPos, this.goal, newGrid);
                        if (tip.isSolution()){
                            tip.solution = true;
                        }
                    }
                    else{
                        isZero = true;
                    }
                } else {
                    canTip = false;
                    if (curRow - 1 >= 0) {
                        int north = curRow - 1;
                        if (grid[north][curCol] >= 1) {
                            Coordinates northPos = new Coordinates(north, curCol);
                            tip = new TipOverConfig(northPos, this.goal, newGrid);
                            if (tip.isSolution()){
                                tip.solution = true;
                            }
                        }
                        else{
                            isZero = true;
                        }
                    }
                    else{
                        offBoard = true;
                        isZero = false;
                        canTip = true;
                    }
                }
            }
            else {
                canTip = false;
                if (curRow - 1 >= 0) {
                    int[][] newGrid = gridCopy();
                    int north = curRow - 1;
                    if (grid[north][curCol] >= 1) {
                        Coordinates northPos = new Coordinates(north, curCol);
                        tip = new TipOverConfig(northPos, this.goal, newGrid);
                        if (tip.isSolution()){
                            tip.solution = true;
                        }
                    }
                    else{
                        isZero = true;
                    }
                }
                else{
                    offBoard = true;
                    isZero = false;
                    canTip = true;
                }
            }
        }
        //Cases for crates
        else{
            if (curRow - 1 >= 0){
                int north = curRow - 1;
                int[][] newGrid = gridCopy();
                if (newGrid[north][curCol] >= 1) {
                    Coordinates northPos = new Coordinates(north, curCol);
                    tip = new TipOverConfig(northPos, this.goal, newGrid);
                    if (tip.isSolution()){
                        tip.solution = true;
                    }
                }
                else{
                    isZero = true;
                }
            }
            else{
                offBoard = true;
                isZero = false;
                canTip = true;
            }
        }
        return tip;
    }

    /**
     * Helper method to get the south configuration
     *
     * @return the south configuration
     */
    public Configuration southCase(){
        if (this.isSolution()){
            this.solution = true;
        }
        if (solution){
            this.solved = true;
            return this;
        }
        TipOverConfig tip = null;
        int curRow = this.tipperPos.row();
        int curCol = this.tipperPos.col();
        if (grid[curRow][curCol] > 1) {
            int height = grid[curRow][curCol];
            //Cases for towers
            if (curRow + height < rows) {
                int[][] newGrid = gridCopy();
                int tempHeight = height;
                for (int i = height; i > 0; i--) {
                    if (newGrid[curRow + i][curCol] != 0) {
                        break;
                    } else {
                        tempHeight--;
                    }
                }
                if (tempHeight == 0) {
                    int south = curRow + 1;
                    for (int i = height; i > 0; i--) {
                        newGrid[curRow + i][curCol] = 1;
                    }
                    newGrid[curRow][curCol] = 0;
                    if (newGrid[south][curCol] >= 1) {
                        this.tipped = true;
                        Coordinates southPos = new Coordinates(south, curCol);
                        tip = new TipOverConfig(southPos, this.goal, newGrid);
                        if (tip.isSolution()){
                            tip.solution = true;
                        }
                    }
                    else{
                        isZero = true;
                    }
                }
                else {
                    canTip = false;
                    if (curRow + 1 < rows) {
                        int south = curRow + 1;
                        if (grid[south][curCol] >= 1) {
                            Coordinates southPos = new Coordinates(south, curCol);
                            tip = new TipOverConfig(southPos, this.goal, newGrid);
                            if (tip.isSolution()){
                                tip.solution = true;
                            }
                        }
                        else{
                            isZero = true;
                        }
                    }
                    else{
                        offBoard = true;
                        isZero = false;
                        canTip = true;
                    }
                }
            }
            else {
                canTip = false;
                if (curRow + 1 < rows) {
                    int[][] newGrid = gridCopy();
                    int south = curRow + 1;
                    if (grid[south][curCol] >= 1) {
                        Coordinates southPos = new Coordinates(south, curCol);
                        tip = new TipOverConfig(southPos, this.goal, newGrid);
                        if (tip.isSolution()){
                            tip.solution = true;
                        }
                    }
                    else{
                        isZero = true;
                    }
                }
                else{
                    offBoard = true;
                    isZero = false;
                    canTip = true;
                }
            }
        }
        //Cases for crates
        else{
            if (curRow + 1 < rows){
                int south = curRow + 1;
                int[][] newGrid = gridCopy();
                if (newGrid[south][curCol] >= 1) {
                    Coordinates southPos = new Coordinates(south, curCol);
                    tip = new TipOverConfig(southPos, this.goal, newGrid);
                    if (tip.isSolution()){
                        tip.solution = true;
                    }
                }
                else{
                    isZero = true;
                }
            }
            else{
                offBoard = true;
                isZero = false;
                canTip = true;
            }
        }
        return tip;
    }

    /**
     * Helper method to get the west configuration
     *
     * @return the west configuration
     */
    public Configuration westCase(){
        if (this.isSolution()){
            this.solution = true;
        }
        if (solution){
            this.solved = true;
            return this;
        }
        TipOverConfig tip = null;
        int curRow = this.tipperPos.row();
        int curCol = this.tipperPos.col();
        //Cases for towers
        if (grid[curRow][curCol] > 1) {
            int height = grid[curRow][curCol];
            if (curCol - height >= 0) {
                int[][] newGrid = gridCopy();
                int tempHeight = height;
                for (int i = height; i > 0; i--) {
                    if (newGrid[curRow][curCol - i] != 0) {
                        break;
                    }
                    else{
                        tempHeight--;
                    }
                }
                if (tempHeight == 0){
                    int west = curCol - 1;
                    for (int i = height; i > 0; i--){
                        newGrid[curRow][curCol - i] = 1;
                    }
                    newGrid[curRow][curCol] = 0;
                    if (newGrid[curRow][west] >= 1) {
                        this.tipped = true;
                        Coordinates westPos = new Coordinates(curRow, west);
                        tip = new TipOverConfig(westPos, this.goal, newGrid);
                        if (tip.isSolution()){
                            tip.solution = true;
                        }
                    }
                    else{
                        isZero = true;
                    }
                }
                else{
                    canTip = false;
                    if (curCol - 1 >= 0) {
                        int west = curCol - 1;
                        if (newGrid[curRow][west] >= 1) {
                            Coordinates westPos = new Coordinates(curRow, west);
                            tip = new TipOverConfig(westPos, this.goal, newGrid);
                            if (tip.isSolution()){
                                tip.solution = true;
                            }
                        }
                        else{
                            isZero = true;
                        }
                    }
                    else{
                        offBoard = true;
                        isZero = false;
                        canTip = true;
                    }
                }
            }
            else{
                canTip = false;
                if (curCol - 1 >= 0) {
                    int[][] newGrid = gridCopy();
                    int west = curCol - 1;
                    if (newGrid[curRow][west] >= 1) {
                        Coordinates westPos = new Coordinates(curRow, west);
                        tip = new TipOverConfig(westPos, this.goal, newGrid);
                        if (tip.isSolution()){
                            tip.solution = true;
                        }
                    }
                    else{
                        isZero = true;
                    }
                }
                else{
                    offBoard = true;
                    isZero = false;
                    canTip = true;
                }
            }
        }
        //Cases for crates
        else{
            if (curCol - 1 >= 0) {
                int west = curCol - 1;
                int[][] newGrid = gridCopy();
                if (newGrid[curRow][west] >= 1) {
                    Coordinates westPos = new Coordinates(curRow, west);
                    tip = new TipOverConfig(westPos, this.goal, newGrid);
                    if (tip.isSolution()){
                        tip.solution = true;
                    }
                }
                else{
                    isZero = true;
                }
            }
            else{
                offBoard = true;
                isZero = false;
                canTip = true;
            }
        }
        return tip;
    }

    /**
     * Helper method to get the east configuration
     *
     * @return the east configuration
     */
    public Configuration eastCase(){
        if (this.isSolution()){
            this.solution = true;
        }
        if (solution){
            this.solved = true;
            return this;
        }
        TipOverConfig tip = null;
        int curRow = this.tipperPos.row();
        int curCol = this.tipperPos.col();
        //Cases for towers
        if (grid[curRow][curCol] > 1){
            int height = grid[curRow][curCol];
            if (curCol + height < cols){
                int[][] newGrid = gridCopy();
                int tempHeight = height;
                for (int i = height; i > 0; i--) {
                    if (newGrid[curRow][curCol + i] != 0) {
                        break;
                    }
                    else{
                        tempHeight--;
                    }
                }
                if (tempHeight == 0){
                    int east = curCol + 1;
                    for (int i = height; i > 0; i--){
                        newGrid[curRow][curCol + i] = 1;
                    }
                    newGrid[curRow][curCol] = 0;
                    if (newGrid[curRow][east] >= 1) {
                        this.tipped = true;
                        Coordinates eastPos = new Coordinates(curRow, east);
                        tip = new TipOverConfig(eastPos, this.goal, newGrid);
                        if (tip.isSolution()){
                            tip.solution = true;
                        }
                    }
                    else{
                        isZero = true;
                    }
                }
                else{
                    canTip = false;
                    if (curCol + 1 < cols){
                        int east = curCol + 1;
                        if (newGrid[curRow][east] >= 1) {
                            Coordinates eastPos = new Coordinates(curRow, east);
                            tip = new TipOverConfig(eastPos, this.goal, newGrid);
                            if (tip.isSolution()){
                                tip.solution = true;
                            }
                        }
                         else{
                             isZero = true;
                        }
                    }
                    else{
                        offBoard = true;
                        isZero = false;
                        canTip = true;
                    }
                }
            }
            else{
                canTip = false;
                if (curCol + 1 < cols){
                    int[][] newGrid = gridCopy();
                    int east = curCol + 1;
                    if (newGrid[curRow][east] >= 1) {
                        Coordinates eastPos = new Coordinates(curRow, east);
                        tip = new TipOverConfig(eastPos, this.goal, newGrid);
                        if (tip.isSolution()){
                            tip.solution = true;
                        }
                    }
                    else{
                        isZero = true;
                    }
                }
                else{
                    offBoard = true;
                    isZero = false;
                    canTip = true;
                }
            }
        }
        //Cases for crates
        else{
            if (curCol + 1 < cols){
                int east = curCol + 1;
                int[][] newGrid = gridCopy();
                if (newGrid[curRow][east] >= 1) {
                    Coordinates eastPos = new Coordinates(curRow, east);
                    tip = new TipOverConfig(eastPos, this.goal, newGrid);
                    if (tip.isSolution()){
                        tip.solution = true;
                    }
                }
                else{
                    isZero = true;
                }
            }
            else{
                offBoard = true;
                isZero = false;
                canTip = true;
            }
        }
        return tip;
    }

    /**
     * Makes a copy of the grids
     *
     * @return a copy of the grid to be changed.
     */
    public int[][] gridCopy(){
        int newGrid[][] = new int[rows][cols];
        for (int i = 0; i < rows; i++){
            for (int g = 0; g < cols; g++){
                newGrid[i][g] = this.grid[i][g];
            }
        }
        return newGrid;
    }

    /**
     * gets a boolean of tipped tower
     *
     * @return boolean of whether a tower has been tipped
     */
    public boolean isTipped(){
        return tipped;
    }

    /**
     * gets a boolean of off board
     *
     * @return a boolean of whether the placement is off the board
     */
    public boolean offBoard(){
        return offBoard;
    }

    /**
     * gets a boolean of tippability
     *
     * @return a boolean of whether the tower can be tipped
     */
    public boolean canTip(){
        return canTip;
    }

    /**
     * gets a boolean of if the config is on a zero
     *
     * @return a boolean of whether the config is on a zero
     */
    public boolean isZero(){
        return isZero;
    }

    /**
     * gets the number of rows
     *
     * @return the number of rows
     */
    public int getRows(){
        return rows;
    }

    /**
     * gets the number of cols
     *
     * @return the number of cols
     */
    public int getCols(){
        return cols;
    }

    /**
     * Gets the value on the grid
     *
     * @param row the accessed row
     * @param col the accessed column
     * @return
     */
    public int getValue(int row, int col){
        return grid[row][col];
    }

    /**
     * gets the tipper position
     *
     * @return the tipper position
     */
    public Coordinates getTipper(){
        return tipperPos;
    }

    /**
     * gets the goal position
     *
     * @return the goal position
     */
    public Coordinates getGoal(){
        return goal;
    }

    /**
     * gets a boolean of if the config is solved
     *
     * @return a boolean of whether the config is solved
     */
    public boolean isAlreadySolved(){
        return solved;
    }

    /**
     * Checks to see whether two TipOverConfigs are equals to eachother.
     *
     * @param other the object to be compared
     * @return a boolean for the equality.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof TipOverConfig){
            TipOverConfig newOther = (TipOverConfig) other;
            if (this.rows == newOther.rows && this.cols == newOther.cols &&
                    this.tipperPos.equals(newOther.tipperPos) && this.goal.equals(newOther.goal) &&
                    this.gridCheck(newOther)){
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to check if all elements of two grids are equal to eachother.
     *
     * @param other the grid to be compared
     * @return a boolean of whether the grids are equal.
     */
    public boolean gridCheck(TipOverConfig other){
        for (int i = 0; i < rows; i++){
            for (int g = 0; g < cols; g++){
                if (this.grid[i][g] != other.grid[i][g]){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates a unique hashcode for each TipOverConfig object
     *
     * @return an integer representing the hashcode of the object.
     */
    @Override
    public int hashCode() {
        return this.rows + this.cols + this.tipperPos.hashCode() + this.goal.hashCode();
    }

    /**
     *
     *
     * @return the board with its special characters
     */
    @Override
    public String toString() {
        String board = "\t";
        String under = "\t";
        for (int i = 0; i < cols; i++){
            board += "  " + i;
            under += "___";
        }
        board += "\n" + under;
        for (int i = 0; i < rows; i++){
            board += "\n " + i + " |";
            for (int g = 0; g < cols; g++){
                Coordinates newCoord = new Coordinates(i, g);
                if (tipperPos.equals(newCoord)){
                    board += " *" + this.grid[i][g];
                }
                else if (goal.equals(newCoord)){
                    board += " !" + this.grid[i][g];
                }
                else if (this.grid[i][g] > 0){
                    board += "  " + this.grid[i][g];
                }
                else{
                    board += "  _";
                }
            }
        }
        return board;
    }
}
