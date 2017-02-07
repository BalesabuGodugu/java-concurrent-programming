/**
 * Created by Farbod_Salimi on 11/21/2015.
 */
public interface Buffer {

    // place int value into Buffer
    public void blockingPut(int value) throws InterruptedException;

    // return int value from Buffer
    public int[][] blockingGet() throws InterruptedException;
} // end interface Buffer
