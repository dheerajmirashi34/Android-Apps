package com.example.threadbasedconcurrency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @File: MainActivity.java
* @Author: Jatin Gupte, Dheeraj Mirashi
* @Group: 50
**/
public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private Button generateButton;
    private TextView minValueTV;
    private TextView avgValueTV;
    private TextView maxValueTV;
    private TextView complexityValue;
    private LinearLayout progressBarOuter;

    private ExecutorService threadPool;
    private Handler handler;

    int complexity;
    boolean seekbarEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Thread-based Concurrency");

        threadPool = Executors.newFixedThreadPool(2);

        generateButton = findViewById(R.id.generateBtn);
        complexityValue = findViewById(R.id.complexityValue);
        minValueTV = findViewById(R.id.minimumValue);
        maxValueTV = findViewById(R.id.maxValue);
        avgValueTV = findViewById(R.id.averageValue);
        progressBarOuter = findViewById(R.id.ProgressBarOuter);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                progressBarOuter.setVisibility(View.INVISIBLE);
                double[] result = message.getData().getDoubleArray(DoWork.RESULT);
                minValueTV.setText(Double.toString(result[0]));
                maxValueTV.setText(Double.toString(result[1]));
                avgValueTV.setText(Double.toString(result[2]));
                seekbarEnabled = false;
                generateButton.setClickable(true);
                return false;
            }
        });

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarOuter.setVisibility(View.VISIBLE);
                minValueTV.setText("");
                maxValueTV.setText("");
                avgValueTV.setText("");
                seekbarEnabled = true;
                generateButton.setClickable(false);
                threadPool.execute(new DoWork());
            }
        });

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return seekbarEnabled;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                complexity = progress;
                complexityValue.setText(progress + " Times");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    class DoWork implements Runnable {
        public static final String RESULT = "RESULT";

        @Override
        public void run() {

            double[] results = new double[3];
            ArrayList<Double> returnedArray = HeavyWork.getArrayNumbers(complexity);
            double sum = 0.0;
            double max = 0.0;
            double min = returnedArray.size() == 0 ? 0.0 : returnedArray.get(0);
            for (Double returnedElement : returnedArray) {
                if (returnedElement > max)
                    max = returnedElement;
                if (returnedElement < min)
                    min = returnedElement;
                sum += returnedElement;
            }
            results[0] = min;
            results[1] = max;
            results[2] = (sum / (returnedArray.size() == 0 ? 1 : returnedArray.size()));

            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putDoubleArray(RESULT, results);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }
}
