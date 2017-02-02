import javax.swing.*;

/**
 * Created by Farbod_Salimi on 12/9/2015.
 */
public class GUI extends SwingWorker<Long, Object> {

    @Override
    protected Long doInBackground() throws Exception {
        setProgress();

        publish();
        return null;
    }

    protected void process(){

    }

    public void test(){
        GUI task = new GUI();
        task.execute();
        task.cancel(true;
    }
}