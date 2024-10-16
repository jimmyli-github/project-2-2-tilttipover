package puzzles.tilt.model;

// TODO: implement your TiltConfig for the common solver

import puzzles.common.solver.Configuration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The TiltConfig for the Tilt class. It provides all
 * the information needed to find a solution with solver.
 */
public class TiltConfig implements Configuration {
    /** The green slider symbol **/
    private final static char GREEN = 'G';
    /** The blue slider symbol **/
    private final static char BLUE = 'B';
    /** The empty symbol **/
    private final static char EMPTY = '.';
    /** The blocker symbol **/
    private final static char BLOCKER = '*';
    /** The hole symbol **/
    private final static char HOLE = 'O';

    /** The amount of size/column on the grid **/
    private int size;
    /** The grid representing the board **/
    private char[][] grid;
    /** A set of valid moves/neighbors **/
    private Set<Configuration> neighbors;

    /**
     * The TiltConfig which creates the grid from the given filename
     * and sets it to a private variable. Size is also set to a private
     * variable and neighbors is initialized.
     * @param filename The filename of the puzzle
     * @throws IOException Handles a FileNotFound exception
     */
    public TiltConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            size = Integer.parseInt(in.readLine());
            grid = new char[size][size];
            String[] fields = in.readLine().split("\\s+");
            int count = 0;
            while (true) {
                char[] temp = new char[size];
                for (int i = 0; i < fields.length; i++) {
                    temp[i] = fields[i].charAt(0);
                }
                grid[count] = temp;
                count++;
                if (count != size) {
                    fields = in.readLine().split("\\s+");
                }
                else {
                    break;
                }
            }
        }
        this.neighbors = new LinkedHashSet<>();
    }


    /**
     * The TiltConfig which sets the given grid to a private variable.
     * Size is also set to a private variable and neighbors is initialized.
     * @param grid
     */
    public TiltConfig(char[][] grid) {
        this.grid = grid;
        this.size = grid.length;
        this.neighbors = new LinkedHashSet<>();
    }

    /**
     * The getter method for grid.
     * @return The grid representing the board
     */
    public String getGrid() {
        return "\n" + this;
    }

    /**
     * The getter method for a specific grid
     * value on a given row and column.
     * @param row The specific row
     * @param col The specific column
     * @return The grid value
     */
    public char getGridValue(int row, int col) {
        return grid[row][col];
    }

    /**
     * The getter method for size.
     * @return The amount of size/column on the grid
     */
    public int getSize() {
        return size;
    }

    /**
     * Checks to see if a solution is found by seeing if
     * there is a green slider on the grid. If there is
     * a green slider, it returns false. Otherwise,
     * it returns true.
     * @return boolean for whether current grid is a solution
     */
    @Override
    public boolean isSolution() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (Objects.equals(grid[row][col], GREEN)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get the neighbor moves using helper methods that
     * check for the four directions. If the helper methods
     * return null, it means that config is not value, and
     * it isn't added to neighbors.
     * @return a collection full of neighbor moves
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        TiltConfig upConfig = (TiltConfig) up();
        if (upConfig != null) {
            neighbors.add(upConfig);
        }

        TiltConfig downConfig = (TiltConfig) down();
        if (downConfig != null) {
            neighbors.add(downConfig);
        }

        TiltConfig leftConfig = (TiltConfig) left();
        if (leftConfig != null) {
            neighbors.add(leftConfig);
        }

        TiltConfig rightConfig = (TiltConfig) right();
        if (rightConfig != null) {
            neighbors.add(rightConfig);
        }

        return neighbors;
    }

    /**
     * Deep copies the current grid to a new grid
     * @return the new copied grid
     */
    private char[][] copyGrid() {
        char[][] copyGrid = new char[size][size];
        for (int row = 0; row < size; row++) {
            copyGrid[row] = grid[row].clone();
        }
        return copyGrid;
    }

    /**
     * Creates a new config that represents the user tilting
     * up on the current board. It checks it by using a nested
     * loop that checks the highest to lowest row for sliders
     * and creates the new variation of the grid. If a blue
     * slider goes into the hole, the method returns null,
     * representing that the move was invalid.
     * @return a new Config after the puzzle is tilted up
     */
    public Configuration up() {
        char[][] upGrid = copyGrid();
        for (int row = 1; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (upGrid[row][col] == GREEN || upGrid[row][col] == BLUE) {
                    int tempRow = row;
                    int rowAbove = row - 1;
                    while (upGrid[rowAbove][col] == EMPTY) {
                        char tempChar = upGrid[tempRow][col];
                        upGrid[tempRow][col] = EMPTY;
                        upGrid[rowAbove][col] = tempChar;
                        rowAbove--;
                        tempRow--;
                        if (rowAbove == -1) {
                            rowAbove++;
                            break;
                        }
                    }
                    if (upGrid[rowAbove][col] == HOLE) {
                        if (upGrid[tempRow][col] == BLUE) {
                            return null;
                        }
                        upGrid[tempRow][col] = EMPTY;
                    }
                }
            }
        }
        return new TiltConfig(upGrid);
    }

    /**
     * Creates a new config that represents the user tilting
     * down on the current board. It checks it by using a nested
     * loop that checks the lowest to highest row for sliders
     * and creates the new variation of the grid. If a blue
     * slider goes into the hole, the method returns null,
     * representing that the move was invalid.
     * @return a new Config after the puzzle is tilted down
     */
    public Configuration down() {
        //look lowest row
        char[][] downGrid = copyGrid();
        for (int row = size - 2; row > -1; row--) {
            for (int col = 0; col < size; col++) {
                if (downGrid[row][col] == GREEN || downGrid[row][col] == BLUE) {
                    int tempRow = row;
                    int rowBelow = row + 1;
                    while (downGrid[rowBelow][col] == EMPTY) {
                        char tempChar = downGrid[tempRow][col];
                        downGrid[tempRow][col] = EMPTY;
                        downGrid[rowBelow][col] = tempChar;
                        rowBelow++;
                        tempRow++;
                        if (rowBelow == size) {
                            rowBelow--;
                            break;
                        }
                    }
                    if (downGrid[rowBelow][col] == HOLE) {
                        if (downGrid[tempRow][col] == BLUE) {
                            return null;
                        }
                        downGrid[tempRow][col] = EMPTY;
                    }
                }
            }
        }
        return new TiltConfig(downGrid);
    }

    /**
     * Creates a new config that represents the user tilting
     * left on the current board. It checks it by using a nested
     * loop that checks the leftmost to rightmost column for sliders
     * and creates the new variation of the grid. If a blue
     * slider goes into the hole, the method returns null,
     * representing that the move was invalid.
     * @return a new Config after the puzzle is tilted left
     */
    public Configuration left() {
        //look leftest col
        char[][] leftGrid = copyGrid();
        for (int col = 1; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (leftGrid[row][col] == GREEN || leftGrid[row][col] == BLUE) {
                    int tempCol = col;
                    int colLeft = col - 1;
                    while (leftGrid[row][colLeft] == EMPTY) {
                        char tempChar = leftGrid[row][tempCol];
                        leftGrid[row][tempCol] = EMPTY;
                        leftGrid[row][colLeft] = tempChar;
                        colLeft--;
                        tempCol--;
                        if (colLeft == -1) {
                            colLeft++;
                            break;
                        }
                    }
                    if (leftGrid[row][colLeft] == HOLE) {
                        if (leftGrid[row][tempCol] == BLUE) {
                            return null;
                        }
                        leftGrid[row][tempCol] = EMPTY;
                    }
                }
            }
        }
        return new TiltConfig(leftGrid);
    }

    /**
     * Creates a new config that represents the user tilting
     * right on the current board. It checks it by using a nested
     * loop that checks the rightmost to leftmost column for sliders
     * and creates the new variation of the grid. If a blue
     * slider goes into the hole, the method returns null,
     * representing that the move was invalid.
     * @return a new Config after the puzzle is tilted right
     */
    public Configuration right() {
        //look rightest col
        char[][] rightGrid = copyGrid();
        for (int col = size - 2; col > -1; col--) {
            for (int row = 0; row < size; row++) {
                if (rightGrid[row][col] == GREEN || rightGrid[row][col] == BLUE) {
                    int tempCol = col;
                    int colRight = col + 1;
                    while (rightGrid[row][colRight] == EMPTY) {
                        char tempChar = rightGrid[row][tempCol];
                        rightGrid[row][tempCol] = EMPTY;
                        rightGrid[row][colRight] = tempChar;
                        colRight++;
                        tempCol++;
                        if (colRight == size) {
                            colRight--;
                            break;
                        }
                    }
                    if (rightGrid[row][colRight] == HOLE) {
                        if (rightGrid[row][tempCol] == BLUE) {
                            return null;
                        }
                        rightGrid[row][tempCol] = EMPTY;
                    }
                }
            }
        }
        return new TiltConfig(rightGrid);
    }

    /**
     * Checks to see if this is equal to another TiltConfig. It
     * first checks if other is an instanceof TiltConfig. It returns
     * true if both clockConfigs have the size and grid.
     * @param other the object being compared to with this
     * @return boolean that shows if this is equal to other
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof TiltConfig newOther) {
            if (size != newOther.size) {
                return false;
            }
            for (int row = 0; row < size; row++) {
                if (!Arrays.equals(grid[row], newOther.grid[row])) {
                    return false;
                };
            }
            return true;
        }
        return false;
    }

    /**
     * Creates a hashcode by adding the hash code of size
     * and the deep hash code of grid.
     * @return an int representing a hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(size) + Arrays.deepHashCode(grid);
    }

    /**
     * A string representation of tiltConfig which is
     * its grid.
     * @return a string that represents the tiltConfig object
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                result.append(grid[row][col]);
                if (col != size - 1) {
                    result.append(" ");
                }
            }
            if (row != size - 1) {
                result.append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
