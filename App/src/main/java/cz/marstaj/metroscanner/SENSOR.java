package cz.marstaj.metroscanner;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SENSOR {

    private final SensorManager mSensorManager;
    private Context context;

    private int lastSignalStrenght = 0;
    private Sensor mAccelerometer;
    private OnDataReceivedListener onDataReceivedListener;

    public SENSOR(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void start() {
        Log.v("SENSOR", "start");
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(listener, mAccelerometer, MainActivity.SENSOR_INTERVAL);
    }

    public void stop() {
        Log.v("SENSOR", "stop");
        mSensorManager.unregisterListener(listener);
    }

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            getSENSORinfo(x, y, z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void getSENSORinfo(float x, float y, float z) {
//        String out = "X: " + x + ", ";
//        out += "Y: " + y + ", ";
//        out += "Z: " + z + ", ";
//        out += "TIME: " + System.currentTimeMillis();
//        out += "\n";

        String out = x + ", ";
        out += y + ", ";
        out += z + ", ";
        out += "" + System.currentTimeMillis();
        out += "\n";
        print(out);
    }

    private void print(String str) {
//        Log.v("GSM", "GSM - " + str);
        onDataReceivedListener.onDataReceived(str);
    }

    public void setOnDataReceivedListener(OnDataReceivedListener onDataReceivedListener) {
        this.onDataReceivedListener = onDataReceivedListener;
    }
}
