/**
 * Created by Farbod_Salimi on 11/20/2015.
 */
public class Producer implements Runnable {

    boolean isRunning = true;
    private final BoardBuffer sharedLocation; // reference to shared object

    //constructor
    public Producer(BoardBuffer sharedLocation) {
        this.sharedLocation = sharedLocation;
    }

    //
    public void run() {
        while (isRunning) {
            try {
                sharedLocation.accessLock.lock();

                int winner = sharedLocation.CheckWinner();
                if (winner == 1 || winner == 2) {
                    System.out.print("Winner : " + winner);
                    this.kill();
                    System.exit(0);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                sharedLocation.accessLock.unlock();
            }
        }
    }

    private void kill() {
        isRunning = false;
    }

} // end classProducer
