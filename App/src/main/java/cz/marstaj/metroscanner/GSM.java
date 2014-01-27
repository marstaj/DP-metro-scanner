package cz.marstaj.metroscanner;

import android.content.Context;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class GSM {

    private final Handler handler;
    private final TelephonyManager tm;
    private Context context;

    private int lastSignalStrenght = 0;
    private OnDataReceivedListener onDataReceivedListener;

    public GSM(Context context) {
        handler = new Handler();
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            lastSignalStrenght = signalStrength.getGsmSignalStrength();
            getGSMinfo();
        }
    };

    public void start() {
        Log.v("GSM", "start");

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, MainActivity.GSM_INTERVAL);
        tm.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public void stop() {
        Log.v("GSM", "stop");
        handler.removeCallbacks(runnable);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(runnable);
            getGSMinfo();
            handler.postDelayed(runnable, MainActivity.GSM_INTERVAL);
        }
    };

    private void getGSMinfo() {
        GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
//        String out = "CID: " + cellLocation.getCid() + ", ";
//        out += "CIDs: " + (short) cellLocation.getCid() + ", ";
//        out += "LAC: " + cellLocation.getLac() + ", ";
//        out += "SIGNAL: " + ((lastSignalStrenght * 2) - 113) + ", ";
//        out += "TYPE: " + getType(tm.getNetworkType()) + ", ";
//        out += "TIME: " + System.currentTimeMillis();
//        out += "\n";

        String out = "" + cellLocation.getCid() + ", ";
        out += "" + (short) cellLocation.getCid() + ", ";
        out += "" + cellLocation.getLac() + ", ";
        out += "" + ((lastSignalStrenght * 2) - 113) + ", ";
        out += "" + tm.getNetworkType() + ", "; //  out += "" + getType(tm.getNetworkType()) + ", ";
        out += "" + System.currentTimeMillis();
        out += "\n";
        print(out);
    }

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


    private void print(String str) {
        Log.v("GSM", str);
        onDataReceivedListener.onDataReceived(str);
    }

    public void setOnDataReceivedListener(OnDataReceivedListener onDataReceivedListener) {
        this.onDataReceivedListener = onDataReceivedListener;
    }


//
///* first you wanna get a telephony manager by asking the system service */
//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//
///* then you can query for all the neighborhood cells */
//        List<NeighboringCellInfo> neighbors = tm.getNeighboringCellInfo();
//
///* here's something you can get from NeighboringCellInfo */
//        for (NeighboringCellInfo n : neighbors) {
//            Log.v("CellInfo", "" + n.getCid());
//            Log.v("CellInfo", "" + n.getLac());
//            Log.v("CellInfo", "" + n.getPsc());
//            Log.v("CellInfo", "" + n.getRssi());
//
//
//        }
//
//        List<CellInfo> celInfo = tm.getAllCellInfo();
//        if (celInfo != null) {
//            for (CellInfo n : celInfo) {
//                CellInfoGsm cellInfoGSM = (CellInfoGsm) n;
//                Log.v("CellInfo", "CID" + cellInfoGSM.getCellIdentity().getCid());
//                Log.v("CellInfo", "LAC" + cellInfoGSM.getCellIdentity().getLac());
//                Log.v("CellInfo", "MCC" + cellInfoGSM.getCellIdentity().getMcc());
//                Log.v("CellInfo", "MNC" + cellInfoGSM.getCellIdentity().getMnc());
//                Log.v("CellInfo", "PSC" + cellInfoGSM.getCellIdentity().getPsc());
//                Log.v("CellInfo", "ASU" + cellInfoGSM.getCellSignalStrength().getAsuLevel());
//                Log.v("CellInfo", "DBM" + cellInfoGSM.getCellSignalStrength().getDbm());
//                Log.v("CellInfo", "Level" + cellInfoGSM.getCellSignalStrength().getLevel());
//                Log.v("CellInfo", "Level" + cellInfoGSM.getCellSignalStrength().describeContents());
////            Log.v("CellInfo", "" + n.getPsc());
////            Log.v("CellInfo", "" + n.getRssi());
//            }
//        }
//
//        GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
//        Log.v("CellInfo", "CID " + cellLocation.getCid());
//        Log.v("CellInfo", "CID SHORT" + ((short) cellLocation.getCid()));
//        Log.v("CellInfo", "LAC " + cellLocation.getLac());
//        Log.v("CellInfo", "PSC " + cellLocation.getPsc());
//
//        Log.v("CellInfo", "getCallState " + tm.getCallState());
//        Log.v("CellInfo", "getDataActivity " + tm.getDataActivity());
//        Log.v("CellInfo", "getDataState " + tm.getDataState());
//        Log.v("CellInfo", "getDeviceId " + tm.getDeviceId());
//        Log.v("CellInfo", "getDeviceSoftwareVersion " + tm.getDeviceSoftwareVersion());
//        Log.v("CellInfo", "getGroupIdLevel1 " + tm.getGroupIdLevel1());
//        Log.v("CellInfo", "getLine1Number " + tm.getLine1Number());
////        Log.v("CellInfo", "getMmsUAProfUrl " + tm.getMmsUAProfUrl());
////        Log.v("CellInfo", "getMmsUserAgent " + tm.getMmsUserAgent());
//        Log.v("CellInfo", "getNetworkCountryIso " + tm.getNetworkCountryIso());
//        Log.v("CellInfo", "getNetworkOperator " + tm.getNetworkOperator());
//        Log.v("CellInfo", "getNetworkOperatorName " + tm.getNetworkOperatorName());
//        Log.v("CellInfo", "getNetworkType " + tm.getNetworkType());
//        Log.v("CellInfo", "getSimOperatorName " + tm.getSimOperatorName());
//        Log.v("CellInfo", "getSimCountryIso " + tm.getSimCountryIso());
//        Log.v("CellInfo", "getSimOperator " + tm.getSimOperator());
//        Log.v("CellInfo", "getSimSerialNumber " + tm.getSimSerialNumber());
//        Log.v("CellInfo", "getSimState " + tm.getSimState());
//
//        tm.listen(new PhoneStateListener() {
//            @Override
//            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
//                Log.v("CellInfo", "getCdmaDbm " + signalStrength.getCdmaDbm());
//                Log.v("CellInfo", "getCdmaEcio " + signalStrength.getCdmaEcio());
//                Log.v("CellInfo", "getEvdoDbm " + signalStrength.getEvdoDbm());
//                Log.v("CellInfo", "getEvdoEcio " + signalStrength.getEvdoEcio());
//                Log.v("CellInfo", "getGsmSignalStrength " + signalStrength.getGsmSignalStrength());
//                Log.v("CellInfo", "getEvdoSnr " + signalStrength.getEvdoSnr());
//                super.onSignalStrengthsChanged(signalStrength);
//            }
//        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
//
//        tm.listen(new PhoneStateListener() {
//            @Override
//            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
//                Log.v("CellInfo", "getCdmaDbm " + signalStrength.getCdmaDbm());
//                Log.v("CellInfo", "getCdmaEcio " + signalStrength.getCdmaEcio());
//                Log.v("CellInfo", "getEvdoDbm " + signalStrength.getEvdoDbm());
//                Log.v("CellInfo", "getEvdoEcio " + signalStrength.getEvdoEcio());
//                Log.v("CellInfo", "getGsmSignalStrength " + signalStrength.getGsmSignalStrength());
//                Log.v("CellInfo", "getEvdoSnr " + signalStrength.getEvdoSnr());
//                super.onSignalStrengthsChanged(signalStrength);
//            }
//
//            @Override
//            public void onSignalStrengthChanged(int asu) {
//                Log.v("CellInfo", "ASU " + asu);
//            }
//        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTH);


}
