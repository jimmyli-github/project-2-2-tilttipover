package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.IOException;
import java.util.List;

/**
 * Main class for the tilt puzzle.
 */
public class Tilt {
    /**
     * Run an instance of the tilt puzzle.
     *
     * @param args the filename of the tilt puzzle
     * @throws IOException handles an exception when making a TiltConfig
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else {
            Configuration tilt = new TiltConfig(args[0]);
            System.out.println(tilt);
            Solver solver = new Solver();
            List<Configuration> path = solver.solve(tilt);
            if (path.isEmpty()){
                System.out.println("No solution");
            }
            else {
                System.out.println("Total configs: " + solver.getTotal() + "\nUnique configs: " + solver.getUnique());
                int i = 0;
                for (Configuration element : path) {
                    TiltConfig e = (TiltConfig) element;
                    System.out.println("Step " + i++ + ":" + e.getGrid() + "\n");
                }
            }
        }
    }
}
