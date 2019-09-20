package com.example.inclass04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Group 50
 * Dheeraj Mirashi
 * Jatin Gupte
 */
public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private Button generateButton;
    private TextView minValueTV;
    private TextView avgValueTV;
    private TextView maxValueTV;
    private TextView complexityValue;
    private LinearLayout progressBarOuter;

    int complexity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateButton = findViewById(R.id.generateBtn);
        complexityValue = findViewById(R.id.complexityValue);
        minValueTV = findViewById(R.id.minimumValue);
        maxValueTV = findViewById(R.id.maxValue);
        avgValueTV = findViewById(R.id.averageValue);
        progressBarOuter = findViewById(R.id.ProgressBarOuter);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoWorkAsync doWork = new DoWorkAsync();
                doWork.execute(complexity);
            }
        });

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                complexity=progress;
                complexityValue.setText(Integer.toString(progress)+" Times");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    class DoWorkAsync extends AsyncTask<Integer,Boolean, List<Double>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(MainActivity.this,"started",Toast.LENGTH_SHORT).show();
            progressBarOuter.setVisibility(View.VISIBLE);
            minValueTV.setText("");
            maxValueTV.setText("");
            avgValueTV.setText("");
        }

        @Override
        protected void onPostExecute(List<Double> doubles) {
            super.onPostExecute(doubles);
            //Toast.makeText(MainActivity.this, "min:"+doubles.get(0)+"max:"+doubles.get(1)+"avg:"+doubles.get(2), Toast.LENGTH_LONG).show();

            minValueTV.setText(doubles.get(0).toString());
            maxValueTV.setText(doubles.get(1).toString());
            avgValueTV.setText(doubles.get(2).toString());
            progressBarOuter.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);

            //TODO: Display or hide progressbar
            // false means display
        }

        @Override
        protected List<Double> doInBackground(Integer... integers) {
            publishProgress(false);
            List<Double> results = new ArrayList<Double>();
            ArrayList<Double> returnedArray = HeavyWork.getArrayNumbers(integers[0]);
            double sum=0.0;
            double max = 0.0;
            double min=returnedArray.size()==0?0.0:returnedArray.get(0);
            for(Double returnedElement : returnedArray)
            {
                if(returnedElement>max)
                    max = returnedElement;
                if(returnedElement<min)
                    min = returnedElement;
                sum += returnedElement;
            }
            results.add(min);
            results.add(max);
            results.add(sum/(returnedArray.size()==0?1:returnedArray.size()));
            publishProgress(true);
            return results;
        }
    }
}
