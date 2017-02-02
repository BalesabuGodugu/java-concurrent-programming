/**
 * Created by Farbod_Salimi on 11/21/2015.
 */

import java.util.Random;

public class Utility {
    public static int MyRand(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
