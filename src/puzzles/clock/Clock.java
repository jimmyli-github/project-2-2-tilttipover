package puzzles.clock;

import puzzles.common.solver.*;

import java.util.*;

/**
 * Main class for the clock puzzle.
 *
 * @author Jaden Vo
 */
public class Clock{

    /**
     * Run an instance of the clock puzzle.
     *
     * @param args [0]: the number of hours in the clock;
     *             [1]: the starting hour;
     *             [2]: the finish hour.
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(("Usage: java Clock hours start finish"));
        } else {
            // Gets the arguments from the command line and makes a new ClockConfig
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);
            Solver solve = new Solver();
            Configuration clock = new ClockConfig(hours, start, end);
            System.out.println(clock);
            List<Configuration> path = solve.solve(clock);
            System.out.println("Total configs: " + solve.getTotal());
            System.out.println("Unique configs: " + solve.getUnique());
            if (path.isEmpty()){
                System.out.println("No Solution");
            }
            else{
                int i = 0;
                for (Configuration element:path){
                    ClockConfig e = (ClockConfig) element;
                    System.out.println("Step " + i++ + ": " + e.getStart());
                }
            }
        }
    }
}

