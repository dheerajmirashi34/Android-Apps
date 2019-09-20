package com.example.myprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditProfileActivity extends AppCompatActivity {

    private EditText firstNameET;
    private EditText lastNameET;
    private ImageView genderIV;
    private RadioGroup genderRG;
    private RadioButton maleRB;
    private RadioButton femaleRB;
    private Button saveBtn;
    private String selectedGender;
    private boolean fromPrevActivity = false;

    public final static String USER_KEY = "user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        this.firstNameET = findViewById(R.id.firstNameET);
        this.lastNameET = findViewById(R.id.lastNameET);
        this.genderIV = findViewById(R.id.genderIV);
        this.genderRG = findViewById(R.id.gender_rg);
        this.maleRB = findViewById(R.id.male_rb);
        this.femaleRB = findViewById(R.id.female_rb);
        this.saveBtn = findViewById(R.id.saveBtn);
        if (getIntent() != null && getIntent().getExtras() != null) {
            User user = (User) getIntent().getExtras().getSerializable(DisplayProfileActivity.USER_EDIT_KEY);
            if (user != null) {
                firstNameET.setText(user.getFirstName());
                lastNameET.setText(user.getLastName());
                if (user.getGender().equalsIgnoreCase("Male")) {
                    maleRB.setChecked(true);
                    genderIV.setImageDrawable(getDrawable(R.drawable.male));
                    selectedGender = "Male";
                } else if (user.getGender().equalsIgnoreCase("Female")) {
                    femaleRB.setChecked(true);
                    genderIV.setImageDrawable(getDrawable(R.drawable.female));
                    selectedGender = "Female";
                }

                fromPrevActivity = true;
            }
        }

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("radio button", "radio button changed");
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.male_rb:
                        genderIV.setImageDrawable(getDrawable(R.drawable.male));
                        selectedGender = "Male";
                        break;
                    case R.id.female_rb:
                        genderIV.setImageDrawable(getDrawable(R.drawable.female));
                        selectedGender = "Female";
                        break;
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleRB.setError(null);
                lastNameET.setError(null);
                firstNameET.setError(null);

                String firstNameText = firstNameET.getText().toString().trim();
                String lastNameText = lastNameET.getText().toString().trim();

                boolean validFirstName = firstNameText.equals("") ? false : true;
                boolean validLastName = lastNameText.equals("") ? false : true;
                boolean validGender = (selectedGender != null) ? true : false;

                if (validFirstName && validLastName && validGender) {
                    User user = new User(firstNameText, lastNameText, selectedGender);

                    if (fromPrevActivity) {
                        Intent intent = new Intent();
                        intent.putExtra(USER_KEY, user);
                        setResult(RESULT_OK, intent);
                    } else {
                        Intent intent = new Intent(EditProfileActivity.this, DisplayProfileActivity.class);
                        intent.putExtra(USER_KEY, user);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    if (!validGender)
                        femaleRB.setError("Invalid Input");

                    if (!validLastName)
                        lastNameET.setError("Invalid Input");

                    if (!validFirstName)
                        firstNameET.setError("Invalid Input");
                }
            }
        });
    }
}
