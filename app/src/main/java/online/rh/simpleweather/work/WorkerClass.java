package online.rh.simpleweather.work;

import android.content.Context;
import android.util.Log;

import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkerClass extends Worker {

    public static final String TASK_DESC = "task_desc";


    public WorkerClass(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public ListenableWorker.Result doWork() {

        Log.d("wzsb", "oooooooooooooo");
        return null;

    }

}
