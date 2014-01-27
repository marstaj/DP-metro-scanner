package cz.marstaj.metroscanner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by mastajner on 12/12/13.
 */
public class SENSORServiceHelper {

    public final String TAG = "SENSORServiceHelper";

    Context context;
    private Intent sensorServiceIntent;
    private boolean isSensorServiceBound;

    public SENSORServiceHelper(Context context) {
        this.context = context;
        sensorServiceIntent = new Intent(context, SENSORService.class);
    }

    private SENSORService boundedSensorService;

    private ServiceConnection SENSORServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG, "ServiceConnection onServiceConnected");
            boundedSensorService = ((LocalBinder<SENSORService>) service).getService();
            isSensorServiceBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG, "ServiceConnection onServiceDisconnected");
            boundedSensorService = null;
            isSensorServiceBound = false;
        }
    };

    public void startAndBind() {
        Log.v(TAG, "startAndBind");
        context.startService(sensorServiceIntent);
        bind();
    }

    public void stopAndUnbind() {
        Log.v(TAG, "stopAndUnbind");
        unbind();
        context.stopService(sensorServiceIntent);
    }

    public void bind() {
        Log.v(TAG, "bind");
        context.bindService(this.sensorServiceIntent, SENSORServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        Log.v(TAG, "unbind");
        context.unbindService(SENSORServiceConnection);
        isSensorServiceBound = false;
    }

    public boolean isBound() {
        return isSensorServiceBound;
    }
}