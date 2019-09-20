package com.example.homework02;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
 * Homework Group: Group 50
 * students:
 * Dheeraj Sanjay Mirashi
 * Jatin Narayan Gupte
 *
 * */



public class MainActivity extends AppCompatActivity {

    private Button toppingB;
    private Button clearPizzaB;
    private Button checkoutB;
    private Builder builder;
    private String[] toppings;
    private ProgressBar limitOfToppingsPB;
    private int currentProgress;
    private List<String> toppingsList;
    //private float amount;
    private CheckBox checkBoxDelivery;
    private boolean delivery;
    private LinearLayout linearlayout;
    private LinearLayout linearLayout2;

    public static final String TOPPINGS  = "Toppings";
    //public static final String AMOUNT  = "Amount";
    public static final String DELIVERY  = "Delivery";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Pizza Store");
        toppingsList = new ArrayList<String>();
        //amount = 6.50f;
        toppings = new String[]{getResources().getString(R.string.topping_bacon),
                getResources().getString(R.string.topping_cheese),
                getResources().getString(R.string.topping_garlic),
                getResources().getString(R.string.topping_green_paper),
                getResources().getString(R.string.topping_mashroom),
                getResources().getString(R.string.topping_olives),
                getResources().getString(R.string.topping_onions),
                getResources().getString(R.string.topping_red_paper)
        };

        limitOfToppingsPB = findViewById(R.id.progressBarLimit);
        checkBoxDelivery = findViewById(R.id.checkBoxDelivery);
        clearPizzaB = findViewById(R.id.buttonClear);
        checkoutB = findViewById(R.id.buttonCheckout);

        builder = new Builder(this);
        builder.setTitle("Choose a Topping!")
                .setCancelable(true)
                .setItems(toppings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        currentProgress = limitOfToppingsPB.getProgress();
                        if(currentProgress==100){
                            Toast toast;
                            toast = Toast.makeText(getApplicationContext(),"Maximum Topping capacity reached!",Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        Log.d("topping added",toppings[i]);
                        toppingsList.add(toppings[i]);
                        limitOfToppingsPB.setProgress(currentProgress+10);

                        renderToppings(toppingsList);

                    }
                });
        final AlertDialog simpleAlert = builder.create();

        toppingB = (Button) findViewById(R.id.buttonTopping);
        toppingB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleAlert.show();
            }
        });

        checkBoxDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                delivery = b;
            }
        });

        clearPizzaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //amount = amount - 1.5f*(toppingsList.size());
                toppingsList.clear();

                limitOfToppingsPB.setProgress(0);
                renderToppings(toppingsList);

            }
        });

        checkoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(toppingsList.size()==0)
                {
                    Toast t = Toast.makeText(getApplicationContext(),"Please Select atleast one topping to continue",Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this,OrderActivity.class);

                String commaToppings = "";
                for(String top : toppingsList)
                {
                    if(commaToppings!="")
                        commaToppings =commaToppings+", "+ top;
                    else
                        commaToppings = top;
                }

                intent.putExtra(MainActivity.TOPPINGS,commaToppings);
                intent.putExtra(MainActivity.DELIVERY,delivery);

                startActivity(intent);
                finish();
            }
        });
    }

    private void removeTopping(int id)
    {
        this.toppingsList.remove(id);
        renderToppings(toppingsList);
        currentProgress -= 10;
        limitOfToppingsPB.setProgress(currentProgress);
    }

    private void renderToppings(List<String> toppings)
    {
        linearlayout = findViewById(R.id.LinearLayout);
        linearlayout.removeAllViews();

        linearLayout2 = findViewById(R.id.LinearLayout2);
        linearLayout2.removeAllViews();

        for(int i=0; i<toppings.size();i++)
        {
            ImageView toppingImageView = new ImageView(getApplicationContext());
            toppingImageView.setId(i);
            toppingImageView.setMinimumHeight(70);
            toppingImageView.setMinimumWidth(70);
            toppingImageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            toppingImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeTopping(v.getId());
                }
            });
            switch (toppings.get(i)){
                case "Bacon":
                    toppingImageView.setImageResource(R.drawable.bacon);
                    break;
                case "Cheese":
                    toppingImageView.setImageResource(R.drawable.cheese);
                    break;
                case "Garlic":
                    toppingImageView.setImageResource(R.drawable.garlic);
                    break;
                case "Green Paper":
                    toppingImageView.setImageResource(R.drawable.green_pepper);
                    break;
                case "Mashroom":
                    toppingImageView.setImageResource(R.drawable.mashroom);
                    break;
                case "Olives":
                    toppingImageView.setImageResource(R.drawable.olive);
                    break;
                case "Onions":
                    toppingImageView.setImageResource(R.drawable.onion);
                    break;
                case "Red Paper":
                    toppingImageView.setImageResource(R.drawable.red_pepper);
                    break;
            }
            if(i<5)
                linearlayout.addView(toppingImageView);
            else
                linearLayout2.addView(toppingImageView);
        }


    }
}

