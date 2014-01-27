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
public class USERServiceHelper {

    public final String TAG = "USERServiceHelper";

    Context context;
    private Intent userServiceIntent;
    private boolean isUserServiceBound;

    public USERServiceHelper(Context context) {
        this.context = context;
        userServiceIntent = new Intent(context, USERService.class);
    }

    private USERService boundedUserService;

    private ServiceConnection USERServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG, "ServiceConnection onServiceConnected");
            boundedUserService = ((LocalBinder<USERService>) service).getService();
            isUserServiceBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG, "ServiceConnection onServiceDisconnected");
            boundedUserService = null;
            isUserServiceBound = false;
        }
    };

    public void startAndBind() {
        Log.v(TAG, "startAndBind");
        context.startService(userServiceIntent);
        bind();
    }

    public void stopAndUnbind() {
        Log.v(TAG, "stopAndUnbind");
        unbind();
        context.stopService(userServiceIntent);
    }

    public void bind() {
        Log.v(TAG, "bind");
        context.bindService(this.userServiceIntent, USERServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        Log.v(TAG, "unbind");
        context.unbindService(USERServiceConnection);
        isUserServiceBound = false;
    }

    public boolean isBound() {
        return isUserServiceBound;
    }

    public void passUserCommand(String tag) {
        if (boundedUserService != null) {
            boundedUserService.printUserCommand(tag);
        }
    }
}