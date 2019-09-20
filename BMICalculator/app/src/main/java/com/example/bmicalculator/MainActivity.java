package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText weightET;
    private EditText heightInFeet;
    private EditText heightInInches;
    private TextView resultBmiTV;
    private TextView resultMsgTV;
    private double weightValue;
    private double heightInFeetValue;
    private double heightInInchesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.weightET = findViewById(R.id.weightET);
        this.heightInFeet = findViewById(R.id.feetET);
        this.heightInInches = findViewById(R.id.inchesET);
        this.resultBmiTV = findViewById(R.id.bmiResultTV);
        this.resultMsgTV = findViewById(R.id.resultMsgTV);

        weightET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                resetResult();
            }
        });

        heightInFeet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                double value = editable.toString().trim().equals("") ? 0.0 : Double.parseDouble(editable.toString());
//                if (value != heightInFeetValue)
                resetResult();
            }
        });

        heightInInches.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                double value = editable.toString().trim().equals("") ? 0.0 : Double.parseDouble(editable.toString());
//                if (value != heightInInchesValue)
                resetResult();
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void resetResult() {
        TextView resultMsgTV = findViewById(R.id.resultMsgTV);
        resultMsgTV.setText("");
        TextView bmiResultTV = findViewById(R.id.bmiResultTV);
        bmiResultTV.setText("");
    }


    public void calculateBMI(View view) {
        String weightETStr = this.weightET.getText().toString().trim();
        String heightInFeetStr = this.heightInFeet.getText().toString();
        String heightInInchesStr = this.heightInInches.getText().toString().trim();

        boolean wightETValid = weightETStr.equals("") || weightETStr.equals(".") || (Double.parseDouble(weightETStr) == 0) ? false : true;
        boolean heightInFeetValid = heightInFeetStr.equals("") ? false : true;
        boolean heightInInchesValid = heightInInchesStr.equals("") || heightInInchesStr.equals(".") || (Double.parseDouble(heightInInchesStr) >= 12) ? false : true;
        boolean totalHeightValid = ((Double.parseDouble(heightInFeetStr) != 0) || (Double.parseDouble(heightInInchesStr) != 0)) ? true : false;

        if (wightETValid && heightInFeetValid && heightInInchesValid && totalHeightValid) {
            Log.d("calculateBMI", "Validation Successful");
            this.weightValue = Double.parseDouble(this.weightET.getText().toString());
            this.heightInFeetValue = Double.parseDouble(this.heightInFeet.getText().toString());
            this.heightInInchesValue = Double.parseDouble(this.heightInInches.getText().toString());

            double totalHeightInInches = (this.heightInFeetValue * 12) + heightInInchesValue;

            Double bmiValue = (this.weightValue / (totalHeightInInches * totalHeightInInches)) * 703;

            this.resultBmiTV.setText(String.format(getResources().getString(R.string.bmi_result), bmiValue));

            if (bmiValue < 18.5) {
                resultMsgTV.setText(R.string.underweight);
            } else if ((bmiValue >= 18.5) && (bmiValue < 25)) {
                resultMsgTV.setText(R.string.normal_weight);
            } else if ((bmiValue >= 25) && (bmiValue < 30)) {
                resultMsgTV.setText(R.string.overweight);
            } else if (bmiValue >= 30) {
                resultMsgTV.setText(R.string.obesity);
            }

            hideSoftKeyboard(this);

            Toast.makeText(getApplicationContext(), "BMI Calculated", Toast.LENGTH_SHORT).show();

        } else {
            if (!wightETValid) {
                weightET.setError("Invalid Input");
            }
            if (!heightInFeetValid) {
                heightInFeet.setError("Invalid Input");
            }
            if (!heightInInchesValid) {
                heightInInches.setError("Invalid Input");
            }

            if(!totalHeightValid){
                heightInFeet.setError("Invalid Input");
                heightInInches.setError("Invalid Input");
            }
        }

    }
}
