package puzzles.common.solver;


import java.util.*;

/**
 * Ths solver class creates a predecessor HashMap in order to access individual configurations. It then
 * uses its helper method, pathConstruct, in order to create the path that is associated with the map.
 *
 * @author Jaden Vo
 */
public class Solver {
    private int totalConfig;
    private int uniqueConfig;
    /**
     * The solve method first creates a queue which is a LinkedList and a HashMap of predecessors.
     * It iterates through the queue until it is empty, searching for the solution using a BFS. Once
     * the solution is found, the loop will break and create a path using the predecessor values.
     *
     * @param c The inputted Configuration to find the solution of
     * @return The fastest possible path to get to the desired solution.
     */
    public List<Configuration> solve(Configuration c){
        int total = 0;
        Configuration end = null;
        List<Configuration> queue = new LinkedList<>();
        queue.add(c);
        total++;
        Map<Configuration, Configuration> predecessor = new HashMap<>();
        predecessor.put(c, null);
        while (!queue.isEmpty()){
            Configuration current = queue.remove(0);
            if (current.isSolution()){
                end = current;
                break;
            }
            for (Configuration nbr : current.getNeighbors()){
                total++;
                if(!predecessor.containsKey(nbr)){
                    predecessor.put(nbr, current);
                    queue.add(nbr);
                }
            }
        }
        totalConfig = total;
        uniqueConfig = predecessor.size();
        return pathConstruct(predecessor, c, end);
    }

    /**
     * A helper method for the solve class to create a path based on the predecessor map.
     *
     * @param predecessor The hashmap of all the  predecessors.
     * @param start The start configuration
     * @param end The solution configuration
     * @return
     */
    private List<Configuration> pathConstruct(Map<Configuration, Configuration> predecessor,
                                              Configuration start, Configuration end){
        List<Configuration> path = new LinkedList<>();
        if (predecessor.containsKey(end)){
            Configuration curr = end;
            while (curr != start){
                path.add(0, curr);
                curr = predecessor.get(curr);
            }
            path.add(0, start);
        }
        return path;
    }

    public int getTotal(){
        return totalConfig;
    }

    public int getUnique(){
        return uniqueConfig;
    }

}
