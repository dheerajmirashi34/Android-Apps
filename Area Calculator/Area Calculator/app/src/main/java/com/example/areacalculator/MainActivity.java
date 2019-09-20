package com.example.areacalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Assignment Number: 2
 * File Name: MainActivity.java
 * Author Name: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 **/
public class MainActivity extends AppCompatActivity {

    private int selectedShapeId=0;
    private EditText l1text;
    private EditText l2text;
    private TextView tvShapeName;
    private TextView resultText;
    private TextView l2TextView;
    private final double PI = 3.14;
    private double area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing instance variables
        this.l1text = findViewById(R.id.etLength1);
        this.l2text = findViewById(R.id.etLength2);
        this.tvShapeName = findViewById(R.id.tvShapeName);
        this.resultText = findViewById(R.id.tvResult);
        this.l2TextView = findViewById(R.id.tvlength2);
    }

    public void clearCalculator(View view) {
        Log.d("MainActivity", "clearCalculator method called");
        l1text.setText(getString(R.string.blank_text));
        l2text.setText(getString(R.string.blank_text));
        tvShapeName.setText(R.string.shape_name_default);
        resultText.setText(R.string.zero_text);
        l2text.setVisibility(View.VISIBLE);
        l2TextView.setVisibility(View.VISIBLE);
        this.selectedShapeId=0;
    }

    public void selectShape(View view) {
        Log.d("MainActivity", "selectShape method called");
        if (view.getId() == R.id.imageViewCircle) {
            tvShapeName.setText(R.string.shape_circle_text);
            selectedShapeId = R.string.shape_circle_text;
            l2text.setVisibility(View.INVISIBLE);
            l2TextView.setVisibility(View.INVISIBLE);
        } else if (view.getId() == R.id.imageViewTriangle) {
            tvShapeName.setText(R.string.shape_triangle_text);
            selectedShapeId = R.string.shape_triangle_text;
            l2text.setVisibility(View.VISIBLE);
            l2TextView.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.imageViewSquare) {
            tvShapeName.setText(R.string.shape_square_text);
            selectedShapeId = R.string.shape_square_text;
            l2text.setVisibility(View.INVISIBLE);
            l2TextView.setVisibility(View.INVISIBLE);
        }
    }

    public void calculateArea(View view) {
        Log.d("MainActivity", "calculateArea method called");
        if (selectedShapeId == R.string.shape_circle_text) {
            EditText l1text = findViewById(R.id.etLength1);
            double length1 = l1text.getText().toString().trim().equals("") ? 0 : Float.parseFloat(l1text.getText().toString());
            this.area = length1 * length1 * PI;
        } else if (selectedShapeId == R.string.shape_triangle_text) {
            EditText l1text = findViewById(R.id.etLength1);
            double length1 = l1text.getText().toString().trim().equals("") ? 0 : Float.parseFloat(l1text.getText().toString());
            EditText l2text = findViewById(R.id.etLength2);
            double length2 = l2text.getText().toString().trim().equals("") ? 0 : Float.parseFloat(l2text.getText().toString());
            this.area = 0.5 * length1 * length2;
        } else if (selectedShapeId == R.string.shape_square_text) {
            EditText l1text = findViewById(R.id.etLength1);
            double length1 = l1text.getText().toString().trim().equals("") ? 0 : Float.parseFloat(l1text.getText().toString());
            this.area = length1 * length1;
        } else {
            this.area = 0.0;
        }
        resultText.setText(Double.toString(area));
    }
}
