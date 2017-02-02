/**
 * Created by Farbod_Salimi on 11/22/2015.
 */
public class Consumer implements Runnable {

    boolean isRunning = true;

    private final BoardBuffer sharedLocation; // reference to shared object
    private int player;

    // constructor
    public Consumer(BoardBuffer sharedLocation, int player) {
        this.sharedLocation = sharedLocation;
        this.player = player;
    }

    public void run() {
        while (isRunning) {
            try {
                int winner = sharedLocation.CheckWinner();
                if (winner == 1 || winner == 2) {
                    this.kill();
                }else {
                    sharedLocation.blockingPut(player);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void kill() {
        isRunning = false;
    }
}
