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
public class USERServiceHelper {

    /**
     * Class TAG
     */
    public final String TAG = "USERServiceHelper";

    /**
     * Context
     */
    Context context;

    /**
     * Service intent
     */
    private Intent userServiceIntent;

    /**
     * Flag whether is the service bound
     */
    private boolean isUserServiceBound;
    /**
     * Bounded service
     */
    private USERService boundedUserService;
    /**
     * Service connection
     */
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

    public USERServiceHelper(Context context) {
        this.context = context;
        userServiceIntent = new Intent(context, USERService.class);
    }

    /**
     * Start and bind service
     */
    public void startAndBind() {
        Log.v(TAG, "startAndBind");
        context.startService(userServiceIntent);
        bind();
    }

    /**
     * Stop and unbind service
     */
    public void stopAndUnbind() {
        Log.v(TAG, "stopAndUnbind");
        unbind();
        context.stopService(userServiceIntent);
    }

    /**
     * Bind service
     */
    public void bind() {
        Log.v(TAG, "bind");
        context.bindService(this.userServiceIntent, USERServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbind service
     */
    public void unbind() {
        Log.v(TAG, "unbind");
        context.unbindService(USERServiceConnection);
        isUserServiceBound = false;
    }

    /**
     * Whether is the service bound or not
     *
     * @return
     */
    public boolean isBound() {
        return isUserServiceBound;
    }

    /**
     * Passes user command to the service
     *
     * @param command User command
     */
    public void passUserCommand(String command) {
        if (boundedUserService != null) {
            boundedUserService.printUserCommand(command);
        }
    }
}