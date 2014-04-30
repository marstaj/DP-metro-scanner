package cz.marstaj.metroscanner.data;

import android.content.Context;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import cz.marstaj.metroscanner.MainActivity;
import cz.marstaj.metroscanner.OnDataReceivedListener;

public class GSM {

    /**
     * Handler for timing
     */
    private final Handler handler;

    /**
     * Telephony manager
     */
    private final TelephonyManager tm;

    /**
     * Context
     */
    private Context context;

    /**
     * Last value of signal straight
     */
    private int lastSignalStrenght = 0;
    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            lastSignalStrenght = signalStrength.getGsmSignalStrength();
            getGSMinfo();
        }
    };

    /**
     * Data received listener
     */
    private OnDataReceivedListener onDataReceivedListener;

    public GSM(Context context) {
        handler = new Handler();
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * Start receiving GSM data
     */
    public void start() {
        Log.v("GSM", "start");

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, MainActivity.GSM_INTERVAL);
        tm.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    /**
     * Stop receiving GSM data
     */
    public void stop() {
        Log.v("GSM", "stop");
        handler.removeCallbacks(runnable);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * Create and print GSM info
     */
    private void getGSMinfo() {
        GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
//        String out = "CID: " + cellLocation.getCid() + ", ";
//        out += "CIDs: " + (short) cellLocation.getCid() + ", ";
//        out += "LAC: " + cellLocation.getLac() + ", ";
//        out += "SIGNAL: " + ((lastSignalStrenght * 2) - 113) + ", ";
//        out += "TYPE: " + getType(tm.getNetworkType()) + ", ";
//        out += "TIME: " + System.currentTimeMillis();
//        out += "\n";

        if (cellLocation != null) {
            String out = "" + cellLocation.getCid() + ", ";
            out += "" + (short) cellLocation.getCid() + ", ";
            out += "" + cellLocation.getLac() + ", ";
            out += "" + ((lastSignalStrenght * 2) - 113) + ", ";
            out += "" + tm.getNetworkType() + ", "; //  out += "" + getType(tm.getNetworkType()) + ", ";
            out += "" + System.currentTimeMillis();
            out += "\n";
            print(out);
        }
    }

    /**
     * Get network type from id
     *
     * @param networkType
     * @return
     */
    private String getType(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "eHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO-0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO-A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO-B";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDen";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "UNKNOWN";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * Timer for getting strength signal
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(runnable);
            getGSMinfo();
            handler.postDelayed(runnable, MainActivity.GSM_INTERVAL);
        }
    };

    /**
     * Print GSM info
     *
     * @param info
     */
    private void print(String info) {
        onDataReceivedListener.onDataReceived(info);
    }

    public void setOnDataReceivedListener(OnDataReceivedListener onDataReceivedListener) {
        this.onDataReceivedListener = onDataReceivedListener;
    }


}
