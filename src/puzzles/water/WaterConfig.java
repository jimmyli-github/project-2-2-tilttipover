package puzzles.water;
import puzzles.common.solver.*;

import java.sql.SQLOutput;
import java.util.*;
import java.util.Collection;

/**
 * The WaterConfig implements the configuration interface in order to instantiate a new WaterConfig object
 *  * in the main to be passed into the solver class.9
 *
 * @author Jaden Vo
 */
public class WaterConfig implements Configuration{
    /** the desired amount of water */
    private int amount;
    /** the max bucketsize */
    private List<Integer> cap;
    /** the starting buckets */
    private List<Integer> buckets;
    /** a set a neighbors */
    private Set<Configuration> neighbors;

    public WaterConfig(int amount, List<Integer> cap, List<Integer> buckets){
        this.amount = amount;
        this.cap = cap;
        this.buckets = buckets;
        this.neighbors = new LinkedHashSet<>();
    }
    /**
     * Is the current configuration a solution?
     *
     * @return true if the configuration is a puzzle's solution; false, otherwise
     */
    @Override
    public boolean isSolution() {
        for (int val:buckets){
            if (val == this.amount){
                return true;
            }
        }
        return false;
    }

    /**
     * Get the collection of neighbors from the current configuration.
     *
     * @return All the neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        neighbors.add(new WaterConfig(amount, cap, buckets));
        for (int i = 0; i < buckets.size(); i++){
            //filled completely from source
            if (!Objects.equals(buckets.get(i), cap.get(i))){
                List<Integer> newBucket = new ArrayList<>(buckets);
                newBucket.set(i, cap.get(i));
                Configuration water = new WaterConfig(amount, cap, newBucket);
                neighbors.add(water);
            }
            //dumped completely from source
            if (buckets.get(i) != 0){
                List<Integer> newBucket = new ArrayList<>(buckets);
                newBucket.set(i, 0);
                Configuration water = new WaterConfig(amount, cap, newBucket);
                neighbors.add(water);
            }
            //pour into other buckets
            if (buckets.get(i) != 0){
                int nextBucket = 0;
                if (i < buckets.size() - 1){
                    nextBucket = i + 1;
                }
                while (nextBucket != i){
                    if(!Objects.equals(buckets.get(nextBucket), cap.get(nextBucket))){
                        List<Integer> newBucket = new ArrayList<>(buckets);
                        int diff = cap.get(nextBucket) - newBucket.get(nextBucket);
                        int vol = newBucket.get(i);
                        if (diff <= vol){
                            newBucket.set(i, newBucket.get(i) - diff);
                            newBucket.set(nextBucket, newBucket.get(nextBucket) + diff);
                            Configuration water = new WaterConfig(amount, cap, newBucket);
                            neighbors.add(water);
                        }
                        else{
                            newBucket.set(i, newBucket.get(i) - vol);
                            newBucket.set(nextBucket, newBucket.get(nextBucket) + vol);
                            Configuration water = new WaterConfig(amount, cap, newBucket);
                            neighbors.add(water);
                        }
                    }
                    //nextBucket++;
                    if (nextBucket + 1 == buckets.size()){
                        nextBucket = 0;
                    }
                    else{
                        nextBucket++;
                    }
                }
            }
        }
        return neighbors;
    }

    public List<Integer> getBuckets(){
        return buckets;
    }

    /**
     * Checks to see if two instances of WaterConfigs are equals
     *
     * @param other the WaterConfig to be compared
     * @return A boolean statement of whether two or equal
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof WaterConfig newOther)
            for (int i = 0; i < buckets.size(); i++){
                if(Objects.equals(this.buckets.get(i), newOther.getBuckets().get(i))){
                    return true;
                }
            }
        return false;
    }

    /**
     * Generates a unique hashcode based on the total amount of each bucket and the amount.
     *
     * @return an integer of the amount and total of all buckets added.
     */
    @Override
    public int hashCode(){
        return cap.hashCode() + buckets.hashCode() + amount;
    }

    /**
     * Creates a string with the amount and buckets
     *
     * @return a string in the following format
     * Amount: x, Buckets: [...]
     */
    @Override
    public String toString(){
        return String.format("%S", buckets);
    }
}
