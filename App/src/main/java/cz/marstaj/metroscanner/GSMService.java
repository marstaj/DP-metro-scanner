package cz.marstaj.metroscanner;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by mastajner on 12/12/13.
 */
public class GSMService extends Service {

    private final String TAG = "GSMService";
    private GSM gsm;
    private boolean isRunning = false;
    private FileWriter writer;

    private final String restFileName = "_gsm_log.txt";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        // Make sure service will not get killed
        Notification notification = new Notification(R.drawable.ic_launcher, TAG + " is Running", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // ???? needed?
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, TAG, "is running", pendingIntent);
        startForeground(MainActivity.GSM_SERVICE_ID, notification);

        gsm = new GSM(getApplicationContext());
        gsm.setOnDataReceivedListener(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String str) {
                try {
                    writer.append(str);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");

        if (!isRunning) {
            isRunning = true;
            start();
        }

        return START_STICKY;
    }

    private void start() {
        writer = createLogFileWriter(MainActivity.partFileName + restFileName);
        gsm.start();
    }

    private void stop() {
        gsm.stop();
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileWriter createLogFileWriter(String logFileName) {
        FileWriter fileWriter = null;
        File root = new File(Environment.getExternalStorageDirectory(), MainActivity.FOLDER_NAME);
        if (!root.exists()) {   //Create the subfolder if it does not exist
            root.mkdirs();
        }
        File file = new File(root, logFileName);    //Create the file object using the provided file name
        //Open the writer
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            Log.v("LOG FILE", "Failed to open file for '" + logFileName + "'");
            e.printStackTrace();
        }
        return fileWriter;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Returns binder for communication with activity
        Log.v(TAG, "onBind");
        return new LocalBinder<GSMService>(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        stop();
        stopForeground(true);
    }


}