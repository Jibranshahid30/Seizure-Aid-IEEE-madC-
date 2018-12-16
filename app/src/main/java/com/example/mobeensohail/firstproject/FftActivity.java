package com.example.mobeensohail.firstproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mobeensohail.firstproject.Emergency;
import com.example.mobeensohail.firstproject.R;

import org.jtransforms.fft.DoubleFFT_1D;
import static java.lang.Math.abs;
public class FftActivity extends AppCompatActivity implements SensorEventListener {

    private TextView time;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    // epoch time since last file write
    char buttonPressed = 'z';
    private long lastTime = 0;
    private long startTime = 0;
    private long endTime = 0;
    private long totalTime = 0;
    double add=0;
    // minimum time in seconds to write to file after previous write
    private double period = 50;
    float[] n_mag = new float[8];
    double[] Data = new double[500];
    int size = 0;
    int i= 0;
    int f_one=0; int f_two=0; int f_three=0; int f_four=0; int f_five=0; int f_six=0; int f_seven=0; int f_eight=0;
    float add_one=0; float add_two=0; float add_three=0; float add_four=0; float add_five=0; float add_six=0;
    float add_seven=0; float add_eight=0;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fft);

        // Assign TextView
        time = (TextView) findViewById(R.id.time);
        // registering accelrometer sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        // getting start time of process
        startTime = System.currentTimeMillis();
    }
    // this function is called when sensor value changed
    @Override
    public void onSensorChanged (SensorEvent event){
        // getting values of 3 axis of sensor
        double x_axis = event.values[0];
        double y_axis = event.values[1];
        double z_axis = event.values[2];
        long tsLong = System.currentTimeMillis();
        // storing values of sensor after required delay in ARRAY
        if (tsLong > lastTime + period) {
            Data[size] = Math.sqrt((x_axis * x_axis) + (y_axis * y_axis) + (z_axis * z_axis));
            size++;
            // if required size 500 points achived, it will take 30 seconds
            if (size == 500) {
                // call fft function
                calculateFft(Data);
                // after completion of fft fuction the program returns here
                // here calculate total time sent in process in cluding data storage and processing
                endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;
                totalTime = totalTime / 1000;
                // show time in text view
                time.setText("time(sec): " + totalTime);
                onPause();
                if(add>3.5 && add<5.5) {
                    Intent intent = new Intent(FftActivity.this, Emergency.class); //for next activity
                    startActivity(intent);  //code (to do)
                }
            }
            lastTime = tsLong;
        }
    }

    @Override
    public void onAccuracyChanged (Sensor sensor,int accuracy){
    }
    protected void onResume () {
        super.onResume();
    }
    protected void onPause () {
        super.onPause();
        // unregister sensor end results
        mSensorManager.unregisterListener(this);
    }
    protected void onDestroy () {
        super.onDestroy();
    }
    // this fuction apply FFT on double arrays using the JTransform library which is included
    public void calculateFft ( double[] Data){
        final DoubleFFT_1D FFT = new DoubleFFT_1D(size);
        FFT.realForward(Data);
        for (i = 0; i < Data.length / 2; i++) {
            double re = Data[2 * i];
            double im = Data[2 * i + 1];
            double mag = Math.sqrt(re * re + im * im);
            //calculate related frequncies
            double freq = i * (16 / (double) size);
            // round up to nearest integer frequency
            freq = freq + 0.5;
            int frequency = (int) freq;
            // getting out the required frequencies
            if (frequency == 1) {
                f_one++;
                add_one = (float) (add_one + mag);
            } else if (frequency == 2) {
                f_two++;
                add_two = (float) (add_two + mag);
            } else if (frequency == 3) {
                f_three++;
                add_three = (float) (add_three + mag);
            } else if (frequency == 4) {
                f_four++;
                add_four = (float) (add_four + mag);
            } else if (frequency == 5) {
                f_five++;
                add_five = (float) (add_five + mag);
            } else if (frequency == 6) {
                f_six++;
                add_six = (float) (add_six + mag);
            } else if (frequency == 7) {
                f_seven++;
                add_seven = (float) (add_seven + mag);
            } else if (frequency == 8) {
                f_eight++;
                add_eight = (float) (add_eight + mag);
            }
        }
        float[] add_mag = {add_one, add_two, add_three, add_four, add_five, add_six, add_seven, add_eight};
        //find max magnitude in each axis
        float max_mag = add_mag[0];
        for (i = 0; i <= 7; i++) {
            if (add_mag[i] > max_mag) {
                max_mag = add_mag[i];
            }
        }
        for (int i = 0; i <= 7; i++) {
            n_mag[i] = add_mag[i] / max_mag;
        }
        for (int i = 0; i <= 7; i++) {
            add = add+n_mag[i];
        }
    }//fft
}


