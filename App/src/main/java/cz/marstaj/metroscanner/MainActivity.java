package cz.marstaj.metroscanner;

import android.app.Activity;
import android.app.ActivityManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends Activity {

    /**
     * Name of the folder for saving data
     */
    public static final String FOLDER_NAME = "MetroScanner";

    /**
     * GSM strength getting interval
     */
    public static final int GSM_INTERVAL = 100; // WAS 1000

    /**
     * Accelerator data getting interval
     */
    public static final int SENSOR_INTERVAL = SensorManager.SENSOR_DELAY_FASTEST; // WAS SENSOR_DELAY_NORMAL

    //Service IDs
    public static final int GSM_SERVICE_ID = 1;
    public static final int SENSORS_SERVICE_ID = 2;
    public static final int USER_SERVICE_ID = 3;

    /**
     * Prefix for filename
     */
    public static String partFileName = "";


    /**
     * Section button click listener
     */
    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (userHelper.isBound()) {
                String text = ((Button) view).getText().toString();
                textView.setText(text + "\n" + textView.getText().toString());
                String tag = (String) view.getTag();
                userHelper.passUserCommand(tag);
            }
        }
    };

    // Views
    private Button stopService;
    private Button startService;
    private TextView textView;

    /**
     * GSM service helper class
     */
    private GSMServiceHelper gsmHelper;

    /**
     * Accelerometer service helper class
     */
    private SENSORServiceHelper sensorHelper;

    /**
     * User service helper class
     */
    private USERServiceHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keep the screen on
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Set view
        setContentView(R.layout.activity_main);

        // Init buttons
        handleUserButtons();

        // Init views
        startService = (Button) findViewById(R.id.buttonStart);
        stopService = (Button) findViewById(R.id.buttonStop);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                startAllServices();
                startService.setVisibility(View.GONE);
                stopService.setVisibility(View.VISIBLE);
            }
        });
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                stopAllServices();
                stopService.setVisibility(View.GONE);
                startService.setVisibility(View.VISIBLE);
            }
        });

        // Init services
        gsmHelper = new GSMServiceHelper(this);
        sensorHelper = new SENSORServiceHelper(this);
        userHelper = new USERServiceHelper(this);
    }

    @Override
    protected void onStart() {
        // Bind all running services and adjust buttons visibility accordingly
        boolean somethigRunning = bindAllServices();
        if (somethigRunning) {
            startService.setVisibility(View.GONE);
            stopService.setVisibility(View.VISIBLE);
        } else {
            stopService.setVisibility(View.GONE);
            startService.setVisibility(View.VISIBLE);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        unbindAllServices();
        super.onStop();
    }

    /**
     * Start all services
     */
    private void startAllServices() {
        //Generate file name
        Calendar cal = Calendar.getInstance();
        partFileName = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.HOUR_OF_DAY) + "-" + cal.get(Calendar.MINUTE) + "-" + cal.get(Calendar.SECOND);

        gsmHelper.startAndBind();
        sensorHelper.startAndBind();
        userHelper.startAndBind();
    }

    /**
     * Start all services
     */
    private void stopAllServices() {
        gsmHelper.stopAndUnbind();
        sensorHelper.stopAndUnbind();
        userHelper.stopAndUnbind();
    }

    /**
     * Bind all services
     */
    private boolean bindAllServices() {
        boolean somethingRunning = false;
        if (isServiceRunning(GSMService.class)) {
            gsmHelper.bind();
            somethingRunning = true;
        }

        if (isServiceRunning(SENSORService.class)) {
            sensorHelper.bind();
            somethingRunning = true;
        }

        if (isServiceRunning(USERService.class)) {
            userHelper.bind();
            somethingRunning = true;
        }

        return somethingRunning;
    }

    /**
     * Unbind all services
     */
    private void unbindAllServices() {
        if (gsmHelper.isBound()) {
            gsmHelper.unbind();
        }
        if (sensorHelper.isBound()) {
            sensorHelper.unbind();
        }
        if (userHelper.isBound()) {
            userHelper.unbind();
        }
    }

    /**
     * Find out whether service is running
     *
     * @param className
     * @return True or False
     */
    private boolean isServiceRunning(Class className) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Init buttons views
     */
    private void handleUserButtons() {
        textView = (TextView) findViewById(R.id.textView);

        Button regularUp = (Button) findViewById(R.id.regularUp);
        Button regularDown = (Button) findViewById(R.id.regularDown);
        Button movingUp = (Button) findViewById(R.id.movingUp);
        Button movingDown = (Button) findViewById(R.id.movingDown);
        Button stopPlatform = (Button) findViewById(R.id.stopPlatform);
        Button metroGo = (Button) findViewById(R.id.metroGo);
        Button metroStop = (Button) findViewById(R.id.metroStop);

        regularUp.setTag("1"); // regularUp
        regularDown.setTag("2"); // regularDown
        movingUp.setTag("3"); // movingUp
        movingDown.setTag("4"); // movingDown
        stopPlatform.setTag("5"); // stopPlatform
        metroGo.setTag("6"); // metroGo
        metroStop.setTag("7"); // metroStop

        regularUp.setOnClickListener(buttonClickListener);
        regularDown.setOnClickListener(buttonClickListener);
        movingUp.setOnClickListener(buttonClickListener);
        movingDown.setOnClickListener(buttonClickListener);
        stopPlatform.setOnClickListener(buttonClickListener);
        metroGo.setOnClickListener(buttonClickListener);
        metroStop.setOnClickListener(buttonClickListener);
    }
}
