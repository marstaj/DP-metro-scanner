package cz.marstaj.metroscanner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import cz.marstaj.metroscanner.util.LocalBinder;

/**
 * Created by mastajner on 12/12/13.
 */
public class SENSORServiceHelper {

    /**
     * Class TAG
     */
    public final String TAG = "SENSORServiceHelper";

    /**
     * Context
     */
    Context context;

    /**
     * Service intent
     */
    private Intent sensorServiceIntent;

    /**
     * Flag whether is the service bound
     */
    private boolean isSensorServiceBound;
    /**
     * Bounded service
     */
    private SENSORService boundedSensorService;
    /**
     * Service connection
     */
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

    public SENSORServiceHelper(Context context) {
        this.context = context;
        sensorServiceIntent = new Intent(context, SENSORService.class);
    }

    /**
     * Start and bind service
     */
    public void startAndBind() {
        Log.v(TAG, "startAndBind");
        context.startService(sensorServiceIntent);
        bind();
    }

    /**
     * Stop and unbind service
     */
    public void stopAndUnbind() {
        Log.v(TAG, "stopAndUnbind");
        unbind();
        context.stopService(sensorServiceIntent);
    }

    /**
     * Bind service
     */
    public void bind() {
        Log.v(TAG, "bind");
        context.bindService(this.sensorServiceIntent, SENSORServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbind service
     */
    public void unbind() {
        Log.v(TAG, "unbind");
        context.unbindService(SENSORServiceConnection);
        isSensorServiceBound = false;
    }

    /**
     * Whether is the service bound or not
     *
     * @return
     */
    public boolean isBound() {
        return isSensorServiceBound;
    }
}