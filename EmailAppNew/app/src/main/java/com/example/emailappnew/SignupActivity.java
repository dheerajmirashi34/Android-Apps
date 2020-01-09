package com.example.emailappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emailappnew.Utils.AppConstants;
import com.example.emailappnew.Utils.ResponseWrapper;
import com.example.emailappnew.asynctasks.PostAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Author: Jatin N Gupte, Dheeraj Mirashi
 * Group No: 50
 */
public class SignupActivity extends AppCompatActivity implements PostAsync.PostAsyncInterface {

    EditText firstNameEt;
    EditText lastNameEt;
    EditText emailEt;
    EditText passwordEt;
    EditText retypePasswordEt;
    Button signup;
    Button cancel;
    private final String SIMPLE_VALIDATION_NAME_REGEX = "[a-zA-Z0-9\\-!\\.\\'\\?\\s]{0,10}";
    private final String SIMPLE_VALIDATION_PASSWORD_REGEX = "^(.+)@(.+)$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstNameEt = findViewById(R.id.firstNameEt);
        lastNameEt = findViewById(R.id.lastNameEt);
        emailEt = findViewById(R.id.emailSignupEt);
        passwordEt = findViewById(R.id.passwordSignupEt);
        retypePasswordEt = findViewById(R.id.reEnterPasswordEt);
        signup = findViewById(R.id.signupBtn);
        cancel = findViewById(R.id.cancelBtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = firstNameEt.getText().toString();
                String lastName = lastNameEt.getText().toString();
                String email = emailEt.getText().toString();
                boolean validFirstname = (!firstName.equals("") && Pattern.compile(SIMPLE_VALIDATION_NAME_REGEX,Pattern.CASE_INSENSITIVE).matcher(firstName).matches()) ? true : false;
                boolean validLastname = (!lastName.equals("") && Pattern.compile(SIMPLE_VALIDATION_NAME_REGEX,Pattern.CASE_INSENSITIVE).matcher(lastName).matches()) ? true : false;
                boolean validEmail = (!email.equals("") && Pattern.compile(SIMPLE_VALIDATION_PASSWORD_REGEX,Pattern.CASE_INSENSITIVE).matcher(email).matches()) ? true : false;
                if (!passwordEt.getText().toString().equals(retypePasswordEt.getText().toString())) {
                    Toast.makeText(SignupActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(validFirstname&& validLastname && validEmail){
                    HashMap<String, String> formBodyMap = new HashMap<>();
                    formBodyMap.put(AppConstants.FORM_PARAM_EMAIL, emailEt.getText().toString());
                    formBodyMap.put(AppConstants.FORM_PARAM_PASSWORD, passwordEt.getText().toString());
                    formBodyMap.put(AppConstants.FORM_PARAM_FNAME, firstNameEt.getText().toString());
                    formBodyMap.put(AppConstants.FORM_PARAM_LNAME, lastNameEt.getText().toString());
                    new PostAsync(SignupActivity.this, formBodyMap,null).execute(AppConstants.SIGNUP_URL);
                }
                else{
                    if(!validFirstname)firstNameEt.setError("Enter valid text");
                    if(!validLastname) lastNameEt.setError("Enter valid text");
                    if(!validEmail) emailEt.setError("Enter valid text");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void handlePostResponse(ResponseWrapper wrapper) {
        if (wrapper.isSuccess()) {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            saveJwt(wrapper.getResponse());
        } else {
            Toast.makeText(this, getErrorMsg(wrapper.getResponse()), Toast.LENGTH_SHORT).show();
        }
    }

    private String getErrorMsg(String response) {
        try {
            JSONObject root = new JSONObject(response);
            String message = root.getString("message");
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void saveJwt(String resp) {
        try {
            JSONObject root = new JSONObject(resp);
            String token = root.getString("token");
            String fName = root.getString("user_fname");
            String lName = root.getString("user_lname");
            Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPref = getSharedPreferences("my_pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(AppConstants.AUTH_TOKEN_KEY, token);
            editor.putString(AppConstants.USER_NAME_KEY, fName + " " + lName);
            editor.commit();
            Intent i = new Intent(this, InboxActivity.class);
            startActivity(i);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
