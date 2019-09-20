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
import android.widget.TextView;
import android.widget.Toast;

public class DisplayProfileActivity extends AppCompatActivity {

    private TextView nameTV;
    private TextView genderTV;
    private ImageView imageIV;
    private Button editB;
    private User user;
    public final int REQ_CODE = 100;
    public static final String USER_EDIT_KEY="editUser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);
      //  setTitle("Display Profile Activity");

        this.nameTV = findViewById(R.id.nameText);
        this.genderTV = findViewById(R.id.genderText);
        this.imageIV = findViewById(R.id.imageView);
        this.editB = findViewById(R.id.button);

        if (getIntent() != null && getIntent().getExtras() != null) {
            this.user = (User) getIntent().getExtras().getSerializable(EditProfileActivity.USER_KEY);
            setUserInfo(this.user);
        }

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //user = (User) getIntent().getExtras().getSerializable(EditProfileActivity.USER_KEY);

                Intent intent = new Intent(DisplayProfileActivity.this,EditProfileActivity.class);
                intent.putExtra(USER_EDIT_KEY,user);
                startActivityForResult(intent,REQ_CODE);
            }
        });
//        Toast.makeText(this, user.getFirstName(), Toast.LENGTH_SHORT).show();
    }

    private void setUserInfo(User user){
        nameTV.setText("Name : " + user.getFirstName().toString() + " " + user.getLastName().toString());
        genderTV.setText(user.getGender().toString());
        switch (user.getGender().toString().toLowerCase()) {
            case "male":
                imageIV.setImageDrawable(getDrawable(R.drawable.male));
                break;
            case "female":
                imageIV.setImageDrawable(getDrawable(R.drawable.female));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQ_CODE){
            Log.d("result","done");
            if(resultCode == RESULT_OK){
                user = (User) data.getExtras().getSerializable(EditProfileActivity.USER_KEY);
                setUserInfo(user);
            }
        }
    }
}
