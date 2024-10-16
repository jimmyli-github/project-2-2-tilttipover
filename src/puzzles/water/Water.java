package puzzles.water;
import java.util.*;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.*;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Jaden Vo
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            int amount = Integer.parseInt(args[0]);
            List<Integer> buckets = new ArrayList<>();
            for (int i = 1; i < args.length; i++){
                buckets.add(Integer.parseInt(args[i]));
            }
            List<Integer> values = new ArrayList<>();
            for (int i = 0; i < buckets.size(); i++){
                values.add(i, 0);
            }
            Solver solve = new Solver();
            WaterConfig water = new WaterConfig(amount, buckets, values);
            System.out.println("Amount: " + amount + ", Buckets: " + buckets);
            List<Configuration> path = solve.solve(water);
            System.out.println("Total configs: " + solve.getTotal());
            System.out.println("Unique configs: " + solve.getUnique());
            if (path.isEmpty()){
                System.out.println("No Solution");
            }
            else {
                int i = 0;
                for (Configuration element : path) {
                    WaterConfig e = (WaterConfig) element;
                    System.out.println("Step " + i++ + ": " + e.getBuckets());
                }
            }
        }
    }
}