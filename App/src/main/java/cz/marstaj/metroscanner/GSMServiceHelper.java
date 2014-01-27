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
public class GSMServiceHelper {

    public final String TAG = "GSMServiceHelper";

    Context context;
    private Intent gsmServiceIntent;
    private boolean isGsmServiceBound;

    public GSMServiceHelper(Context context) {
        this.context = context;
        gsmServiceIntent = new Intent(context, GSMService.class);
    }

    private GSMService boundedGsmService;

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

    public void startAndBind() {
        Log.v(TAG, "startAndBind");
        context.startService(gsmServiceIntent);
        bind();
    }

    public void stopAndUnbind() {
        Log.v(TAG, "stopAndUnbind");
        unbind();
        context.stopService(gsmServiceIntent);
    }

    public void bind() {
        Log.v(TAG, "bind");
        context.bindService(this.gsmServiceIntent, gsmServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        Log.v(TAG, "unbind");
        context.unbindService(gsmServiceConnection);
        isGsmServiceBound = false;
    }

    public boolean isBound() {
        return isGsmServiceBound;
    }
}
