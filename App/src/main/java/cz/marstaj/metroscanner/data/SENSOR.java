package cz.marstaj.metroscanner.data;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import cz.marstaj.metroscanner.MainActivity;
import cz.marstaj.metroscanner.OnDataReceivedListener;

public class SENSOR {

    /**
     * Sensot manager
     */
    private final SensorManager mSensorManager;
    /**
     * Listener for accelerometer changes
     */
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
    /**
     * Context
     */
    private Context context;
    /**
     * Accelerometer sensor
     */
    private Sensor mAccelerometer;
    /**
     * Data received listener
     */
    private OnDataReceivedListener onDataReceivedListener;

    public SENSOR(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    /**
     * Start receiving accelerometer data
     */
    public void start() {
        Log.v("SENSOR", "start");
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(listener, mAccelerometer, MainActivity.SENSOR_INTERVAL);
    }

    /**
     * Stop receiving accelerometer data
     */
    public void stop() {
        Log.v("SENSOR", "stop");
        mSensorManager.unregisterListener(listener);
    }

    /**
     * Create and print accelerometer data information
     *
     * @param x
     * @param y
     * @param z
     */
    private void getSENSORinfo(float x, float y, float z) {
        String out = x + ", ";
        out += y + ", ";
        out += z + ", ";
        out += "" + System.currentTimeMillis();
        out += "\n";
        print(out);
    }

    /**
     * Print accelerometer info
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
