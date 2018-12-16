package com.example.mobeensohail.firstproject;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.hardware.Sensor;

import android.hardware.SensorManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import mr.go.sgfilter.SGFilter;
import mr.go.sgfilter.ZeroEliminator;


public class SecondActivity extends AppCompatActivity implements SensorEventListener {


    private FirebaseAuth firebaseAuth;
    private TextView tvXaxis, tvYaxis, tvZaxis;
    private Button btnStart, btnStop, btnFft;
    private Sensor mySensor;
    private SensorManager SM;

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private double acc_x = 0;
    private double acc_y = 0;
    private double acc_z = 0;
    private int count = 0;
    private int localcount = 0;
    private int state = 1;

    private LineGraphSeries<DataPoint> series_x;
    private LineGraphSeries<DataPoint> series_y;
    private LineGraphSeries<DataPoint> series_z;

    private final int nl = 5;
    private final int nr = 5;
    private final int degree = 3;
    private SGFilter sgFilter;

    private double[] data_x = new double[200];
    private double[] data_y = new double[200];
    private double[] data_z = new double[200];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Assign Button
        btnFft = (Button) findViewById(R.id.btnFft);
        // action listener for next activity
        btnFft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View f) {

                Intent FFTIntent = new Intent(SecondActivity.this, FftActivity.class);
                SecondActivity.this.startActivity((FFTIntent));

            }
        });

        // Create our Sensor Manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Assign TextView
        tvXaxis = (TextView) findViewById(R.id.tvXaxis);
        tvYaxis = (TextView) findViewById(R.id.tvYaxis);
        tvZaxis = (TextView) findViewById(R.id.tvZaxis);

   /* btnStart = (Button) findViewById(R.id.btnStart);
    btnStop = (Button) findViewById(R.id.btnStop);
    btnStart.setOnClickListener(this);
    btnStop.setOnClickListener(this);
    btnStart.setEnabled(true);
    btnStop.setEnabled(false); */

        // Initializing Graph series for 3 axis
        series_x = new LineGraphSeries<>();
        series_y = new LineGraphSeries<>();
        series_z = new LineGraphSeries<>();
        series_x.appendData(new DataPoint(0, 0), true, 200);
        series_y.appendData(new DataPoint(0, 0), true, 200);
        series_z.appendData(new DataPoint(0, 0), true, 200);
        GraphView graph = (GraphView) findViewById(R.id.graph);

        // add series to graph
        graph.addSeries(series_x);
        graph.addSeries(series_y);
        graph.addSeries(series_z);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(200);

        // heading style lable and design colors legend
        graph.setTitle("Real time Accelrometer Data");
        graph.setTitleTextSize(40);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time(instance)");
        gridLabel.setVerticalAxisTitle("g-Force");
        graph.setTitleColor(Color.WHITE);
        series_x.setColor(Color.RED);
        series_y.setColor(Color.GREEN);
        series_z.setColor(Color.BLUE);
        //graph.getLegendRenderer().setVisible(true);


        // initializa sgFilter
        sgFilter = new SGFilter(nl, nr);
        sgFilter.appendPreprocessor(new ZeroEliminator());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }


    // get axis values
    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        //acc = Math.sqrt(x*x+y*y+z*z);
        acc_x = x;
        acc_y = y;
        acc_z = z;
        tvXaxis.setText("X: " + x);
        tvYaxis.setText("Y: " + y);
        tvZaxis.setText("Z: " + z);
        int i = 0;

        // counter generation and algo for data plotting
        if (state == 1) {
            count++;

            if (count > 200) {
                for (i = 0; i < 199; i++) {
                    data_x[i] = data_x[i + 1];
                    data_y[i] = data_y[i + 1];
                    data_z[i] = data_z[i + 1];

                }
                localcount = 200;
                data_x[199] = acc_x;
                data_y[199] = acc_y;
                data_z[199] = acc_z;


            } else {
                localcount = count % 200;
                if (count == 200) localcount = 200;
                data_x[count - 1] = acc_x;
                data_y[count - 1] = acc_y;
                data_z[count - 1] = acc_z;


            }

            // apply filter on data and plot series in graph
            if (count > nl + nr) {
                double[] smooth_x = sgFilter.smooth(data_x, SGFilter.computeSGCoefficients(nl, nr, degree));
                series_x.appendData(new DataPoint(count, smooth_x[localcount - 1 - nl]), true, 200);
                double[] smooth_y = sgFilter.smooth(data_y, SGFilter.computeSGCoefficients(nl, nr, degree));
                series_y.appendData(new DataPoint(count, smooth_y[localcount - 1 - nl]), true, 200);
                double[] smooth_z = sgFilter.smooth(data_z, SGFilter.computeSGCoefficients(nl, nr, degree));
                series_z.appendData(new DataPoint(count, smooth_z[localcount - 1 - nl]), true, 200);

            }


        }
    }

    public void startfn(View v) {
        state = 1;

    }

    public void stopfn(View v) {

        state = 0;
    }


//firebaseAuth = FirebaseAuth.getInstance();

    //logout = (Button) findViewById(R.id.btnLogout);
    // logout.setOnClickListener(new View.OnClickListener() {
    // @Override
    // public void onClick(View v) {
    //   Logout();

    //  }
    //   });


    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.logoutMenu: {
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}