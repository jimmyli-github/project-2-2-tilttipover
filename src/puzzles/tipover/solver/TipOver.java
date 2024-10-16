package puzzles.tipover.solver;

import puzzles.common.solver.*;
import puzzles.tipover.model.TipOverConfig;
import java.io.*;
import java.util.*;

/**
 * The main class for the Tip Over puzzle
 *
 * @author Jaden Vo
 */

public class TipOver {
    // Initializes
    public static void main(String[] args) throws IOException {
        try {
            if (args.length != 1) {
                System.out.println("Usage: java TipOver filename");
            } else {
                TipOverConfig tipOver = new TipOverConfig(args[0]);
                System.out.println("File: " + args[0]);
                System.out.println(tipOver);
                Solver solve = new Solver();
                List<Configuration> path = solve.solve(tipOver);
                System.out.println("Total configs: " + solve.getTotal());
                System.out.println("Unique configs: " + solve.getUnique());
                if (path.isEmpty()){
                    System.out.println("No Solution");
                }
                else{
                    int i = 0;
                    for (Configuration element : path){
                        TipOverConfig e = (TipOverConfig) element;
                        System.out.println("Step " + i++ + ":\n" + e + "\n");
                    }
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }
}
