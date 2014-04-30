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
public class GSMServiceHelper {

    /**
     * Class TAG
     */
    public final String TAG = "GSMServiceHelper";

    /**
     * Context
     */
    Context context;

    /**
     * Service intent
     */
    private Intent gsmServiceIntent;

    /**
     * Flag whether is the service bound
     */
    private boolean isGsmServiceBound;
    /**
     * Bounded service
     */
    private GSMService boundedGsmService;
    /**
     * Service connection
     */
    private ServiceConnection gsmServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG, "ServiceConnection onServiceConnected");
            boundedGsmService = ((LocalBinder<GSMService>) service).getService();
            isGsmServiceBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG, "ServiceConnection onServiceDisconnected");
            boundedGsmService = null;
            isGsmServiceBound = false;
        }
    };

    public GSMServiceHelper(Context context) {
        this.context = context;
        gsmServiceIntent = new Intent(context, GSMService.class);
    }

    /**
     * Start and bind service
     */
    public void startAndBind() {
        Log.v(TAG, "startAndBind");
        context.startService(gsmServiceIntent);
        bind();
    }

    /**
     * Stop and unbind service
     */
    public void stopAndUnbind() {
        Log.v(TAG, "stopAndUnbind");
        unbind();
        context.stopService(gsmServiceIntent);
    }

    /**
     * Bind service
     */
    public void bind() {
        Log.v(TAG, "bind");
        context.bindService(this.gsmServiceIntent, gsmServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbind service
     */
    public void unbind() {
        Log.v(TAG, "unbind");
        context.unbindService(gsmServiceConnection);
        isGsmServiceBound = false;
    }

    /**
     * Whether is the service bound or not
     *
     * @return
     */
    public boolean isBound() {
        return isGsmServiceBound;
    }
}
