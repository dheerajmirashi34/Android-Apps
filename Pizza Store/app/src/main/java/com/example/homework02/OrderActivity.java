package com.example.homework02;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends AppCompatActivity {
    private String[] toppings;
    private boolean delivery;
    private float amount;
    private float toppingsBill;
    private TextView toppingsCost;
    private TextView deliveryCost;
    private TextView totalCost;
    private TextView listOfToppings;
    private static final float BASE_AMOUNT=6.5f;
    private static final float PER_TOPPING_COST=1.50f;
    private static final float DELIVERY_COST=4.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle("Pizza Store");
        amount = BASE_AMOUNT;

        if(getIntent() != null && getIntent().getExtras() != null) {
            toppings = getIntent().getExtras().getString(MainActivity.TOPPINGS).split(",");

            toppingsBill = (toppings.length)*PER_TOPPING_COST;
            toppingsCost = findViewById(R.id.toppingsCost);
            toppingsCost.setText(toppingsBill+"$");
            amount += toppingsBill;

            delivery = getIntent().getExtras().getBoolean(MainActivity.DELIVERY);
            deliveryCost = findViewById(R.id.deliveryCost);
            if(delivery)
            {
                amount += DELIVERY_COST;
                deliveryCost.setText(DELIVERY_COST+"$");//R.string.delivery_cost_true+"$");
            }

            totalCost = findViewById(R.id.totalCost);
            totalCost.setText(amount+"$");

            listOfToppings = findViewById(R.id.toppingsList);
            listOfToppings.setText(getIntent().getExtras().getString(MainActivity.TOPPINGS));

//            Toast t1 = Toast.makeText(getApplicationContext(),""+amount,Toast.LENGTH_SHORT);
//            t1.show();


        }

    }

}
