import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Farbod_Salimi on 11/22/2015.
 */
public class BoardBuffer implements Buffer {
    int ROW = 6;
    int COLUMN = 7;
    public final int[][] lastMove = new int[ROW][COLUMN];

    // lock to control synchronization with this buffer
    public final Lock accessLock = new ReentrantLock();

    // conditions to control reading and writing
    public final Condition canWrite = accessLock.newCondition();
    public final Condition canRead = accessLock.newCondition();

    private int[][] buffer = new int[ROW][COLUMN]; // shared with producer and consumer
    private boolean occupied = false;

    @Override
    public void blockingPut(int value) throws InterruptedException {

        //output thread information and buffer information, then wait
        try {
            accessLock.lock(); // lock this object

            //while buffer is not empty, place thread in waiting state
            while (IsFull()) {
                //System.out.println("Producer tries to write.");
                canWrite.await();
            }

            int rndCol = Utility.MyRand(0, 6);
            // Check row if empty
            for (int i = 0; i < ROW; i++) {
                if (buffer[i][rndCol] == 0) {
                    buffer[i][rndCol] = value; // set new buffer value
                    break;
                }
            } // end for loop

            // show the board
            Show();

            // signal any threads waiting to read from buffer
            canRead.signalAll();

        } finally {
            accessLock.unlock();
        }
    }

    @Override
    public int[][] blockingGet() throws InterruptedException {
        accessLock.lock(); // lock this object

        //output thread information and buffer information, then wait
        try {
            //if there is no data to read, place thread in waiting state
            while (IsEmpty()) {
                //System.out.println("Consumer tries to write.");
                canRead.await(); // wait until buffer is full
            }

            // signal any threads waiting for buffer to be empty
            canWrite.signalAll();
        } finally {
            accessLock.unlock(); // unlock this object
        }

        return buffer;
    }

    //
    public boolean IsEmpty() {
        boolean flag = true;
        int i, j;
        for (i = 0; i < ROW; i++) {
            for (j = 0; j < COLUMN; j++) {
                if (buffer[i][j] != 0)
                    flag = false;
            }
        }

        return flag;
    }

    //
    public boolean IsFull() {
        boolean flag = true;
        int i, j;
        for (i = 0; i < ROW; i++) {
            for (j = 0; j < COLUMN; j++) {
                if (buffer[i][j] == 0)
                    flag = false;
            }
        }

        return flag;
    }


    //
    public synchronized void Show() {

        int[][] board_tmp = new int[ROW][COLUMN];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                board_tmp[ROW - i - 1][j] = buffer[i][j];
            }
        }

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++)
                System.out.print(board_tmp[i][j] + "\t");
            System.out.print("\n");
        }

        System.out.print("\n");
    }

    //
    public synchronized int CheckWinner() throws InterruptedException {
        int result = 0;

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {

                if (buffer[i][j] != 0) {
                    // Check line
                    if (CheckLine(i, j) != 0) {
                        result = CheckLine(i, j);
                        break;
                        // Check Diagonal
                    } else if (CheckDiagonal(i, j) != 0) {
                        result = CheckDiagonal(i, j);
                        break;
                    }
                }
            }
        } // end for loop

        return result;
    }

    private synchronized int CheckLine(int _i, int _j) {

        if ((_j + 3) <= (COLUMN - 1)) {
            int cell = buffer[_i][_j];
            int cell_1 = buffer[_i][_j + 1];
            int cell_2 = buffer[_i][_j + 2];
            int cell_3 = buffer[_i][_j + 3];

            if (cell_1 == cell && cell_2 == cell && cell_3 == cell) {
                // Return the winner number
                return cell;
            }
        }

        return 0;
    }

    private synchronized int CheckDiagonal(int _i, int _j) {

        if (((_i + 3) <= (ROW - 1)) && ((_j + 3) <= (COLUMN - 1))) {
            int cell = buffer[_i][_j];
            int cell_1 = buffer[_i + 1][_j + 1];
            int cell_2 = buffer[_i + 2][_j + 2];
            int cell_3 = buffer[_i + 3][_j + 3];

            if (cell_1 == cell && cell_2 == cell && cell_3 == cell) {
                // Return the winner number
                return cell;
            }
        }

        return 0;
    }
}
