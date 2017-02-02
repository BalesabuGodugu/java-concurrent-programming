import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Farbod_Salimi on 11/22/2015.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        // create new thread pool with two threads
        ExecutorService executorService = Executors.newCachedThreadPool();

        // create BoardBuffer
        BoardBuffer sharedLocation = new BoardBuffer();

        // show the initial board
        sharedLocation.Show();

        Producer referee = new Producer(sharedLocation);
        Consumer player1 = new Consumer(sharedLocation, 1);
        Consumer player2 = new Consumer(sharedLocation, 2);

        executorService.execute(referee);
        executorService.execute(player1);
        executorService.execute(player2);

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.print(e.getMessage());
        }
    }
} // end class Main