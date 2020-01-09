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
import com.example.emailappnew.Utils.CheckInternet;
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
public class MainActivity extends AppCompatActivity implements PostAsync.PostAsyncInterface {

    EditText email;
    EditText password;
    Button login;
    Button signup;
    private final String SIMPLE_VALIDATION_REGEX = "^(.+)@(.+)$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.emailEt);
        password = findViewById(R.id.passwordEt);
        login = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.signupBtn);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternet.isConnected(MainActivity.this)) {

                    String emailText = email.getText().toString();
                    String passwordText = password.getText().toString();
                    boolean validEmail = (!emailText.equals("") && Pattern.compile(SIMPLE_VALIDATION_REGEX,Pattern.CASE_INSENSITIVE).matcher(emailText).matches()) ? true : false;
                    boolean validPass = !passwordText.equals("");
                    if(validEmail && validPass){
                        HashMap<String, String> formBodyMap = new HashMap<>();
                        formBodyMap.put(AppConstants.FORM_PARAM_EMAIL, email.getText().toString());
                        formBodyMap.put(AppConstants.FORM_PARAM_PASSWORD, password.getText().toString());

                        new PostAsync(MainActivity.this, formBodyMap, null).execute(AppConstants.LOGIN_URL);
                    }
                    else{
                        if(validEmail)
                            email.setError("Enter valid email address.");
                        else
                            password.setError("Enter valid password");
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(i);
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
            editor.putString("token", token);
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
