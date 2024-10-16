package puzzles.clock;

import puzzles.common.solver.*;

import java.util.Collection;
import java.util.*;

/**
 * The ClockConfig implements the configuration interface in order to instantiate a new ClockConfig object
 * in the main to be passed into the solver class.
 *
 * @author Jaden Vo
 */
public class ClockConfig implements Configuration {
    /** total number of hours */
    private int hours;
    /** the start time */
    private int start;
    /** the end time */
    private int end;
    /** set of neighbors */
    private Set<Configuration> neighbors;

    /**
     * Constructor for the ClockConfig
     *
     * @param hours the total number of hours
     * @param start the start number of hours
     * @param end the end number of hours
     */
    public ClockConfig(int hours, int start, int end){
        this.hours = hours;
        this.start = start;
        this.end = end;
        this.neighbors = new LinkedHashSet<>();
    }

    public int getHours(){
        return hours;
    }

    public int getStart(){
        return start;
    }

    public int getEnd(){
        return end;
    }
    /**
     * Is the current configuration a solution?
     *
     * @return true if the configuration is a puzzle's solution; false, otherwise
     */
    @Override
    public boolean isSolution() {
        return this.start == end;
    }

    /**
     * Get the collection of neighbors from the current configuration.
     *
     * @return All the neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        int minCurrent = start;
        int addCurrent = start;
        minCurrent = minCurrent - 1;
        if (minCurrent == 0){
            minCurrent = hours;
        }
        ClockConfig min = new ClockConfig(this.hours, minCurrent, this.end);
        neighbors.add(min);
        addCurrent = addCurrent + 1;
        if (addCurrent > hours){
            addCurrent = 1;
        }
        ClockConfig add = new ClockConfig(this.hours, addCurrent, this.end);
        neighbors.add(add);
        return neighbors;
    }

    /**
     * The equals method compares the hours, start and end time in order to see if two ClockConfigs are
     * equal to one another.
     *
     * @param other The object that is being compared to an instance of ClockConfig.
     * @return a boolean whether two ClockConfigs are equal.
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof ClockConfig newOther){
            return (this.hours == newOther.getHours()) &&
                    (this.start == newOther.getStart()) &&
                    (this.end == newOther.getEnd());
        }
        return false;
    }

    /**
     * Generates a unique hashcode for each clockConfig using hours start and end.
     *
     * @return hours start and end added.
     */
    @Override
    public int hashCode(){
        return hours + start + end;
    }

    /**
     * Generates a string containing hours, the start time, and the end time.
     *
     * @return A string in the format
     * Hours: x, Start: y, End: z
     */
    @Override
    public String toString(){
        return "Hours: " + hours + ", Start: " + start + ", End: " + end;
    }
}
