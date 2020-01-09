package com.example.emailappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailappnew.R;
import com.example.emailappnew.Utils.AppConstants;
import com.example.emailappnew.Utils.CheckInternet;
import com.example.emailappnew.Utils.ResponseWrapper;
import com.example.emailappnew.asynctasks.GetAsync;
import com.example.emailappnew.asynctasks.PostAsync;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Author: Jatin N Gupte, Dheeraj Mirashi
 * Group No: 50
 */
public class CreateAndDisplayActivity extends AppCompatActivity implements GetAsync.GetAsyncInterface, PostAsync.PostAsyncInterface {

    TextView personNameTV;
    Spinner spinner;
    TextView senderName;
    EditText subjectET;
    EditText messageET;
    Button sendBtn;
    Button cancelBtn;

    ArrayList<User> users = new ArrayList<>();
    String receiver_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_display);

        personNameTV = findViewById(R.id.personName_label);
        spinner = findViewById(R.id.spinnerUsersList);
        senderName = findViewById(R.id.SenderName);
        subjectET = findViewById(R.id.subject);
        messageET = findViewById(R.id.mailBodyET);
        sendBtn = findViewById(R.id.button);
        cancelBtn = findViewById(R.id.button2);

        Intent i = getIntent();
        if (i != null && i.getExtras() != null && i.getExtras().get(EmailAdapter.EMAIL) != null) {
            Email email = (Email) i.getExtras().get(EmailAdapter.EMAIL);
            spinner.setVisibility(View.INVISIBLE);
            personNameTV.setText("Sender:");
            senderName.setText(email.fname + " " + email.lname);
            subjectET.setText(email.subject);
            subjectET.setInputType(InputType.TYPE_NULL);
            messageET.setText(email.message);
            messageET.setInputType(InputType.TYPE_NULL);
            sendBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setVisibility(View.INVISIBLE);
        } else {

            SharedPreferences sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE);
            String token = sharedPreferences.getString(AppConstants.AUTH_TOKEN_KEY, "abc");

            if (CheckInternet.isConnected(this)) {
                HashMap<String, String> headersMap = new HashMap<>();
                headersMap.put(AppConstants.AUTHORIZATION_HEADER, "bearer " + token);

                new GetAsync(this, headersMap).execute(AppConstants.USERS_GET_URL);
            } else {
                Toast.makeText(this, "Internet is not connected", Toast.LENGTH_SHORT).show();
            }

            senderName.setVisibility(View.INVISIBLE);
            personNameTV.setText("Send To:");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.AUTH_TOKEN_KEY, "abc");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternet.isConnected(CreateAndDisplayActivity.this)) {

                    SharedPreferences sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE);
                    String token = sharedPreferences.getString(AppConstants.AUTH_TOKEN_KEY, "abc");

                    HashMap<String, String> headersMap = new HashMap<>();
                    headersMap.put(AppConstants.AUTHORIZATION_HEADER, "bearer " + token);

                    HashMap<String, String> bodyParams = new HashMap<>();
                    bodyParams.put(AppConstants.MESSAGE_KEY, messageET.getText().toString());
                    bodyParams.put(AppConstants.SUBJECT_KEY, subjectET.getText().toString());
                    bodyParams.put(AppConstants.RECEIVER_ID_KEY, receiver_id);

                    new PostAsync(CreateAndDisplayActivity.this, bodyParams, headersMap).execute(AppConstants.INBOX_ADD_URL);

                } else {
                    Toast.makeText(CreateAndDisplayActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void handleResponse(ResponseWrapper result) {
        if (result.isSuccess()) {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            getUsers(result.getResponse());
        } else {
            Toast.makeText(this, getErrorMsg(result.getResponse()), Toast.LENGTH_SHORT).show();
        }
    }

    private void getUsers(String response) {
        try {
            JSONObject root = new JSONObject(response);
            JSONArray data = root.getJSONArray("users");
            for (int i = 0; i < data.length(); i++) {
                JSONObject user1 = data.getJSONObject(i);
                String id = user1.getString("id");
                String fName = user1.getString("fname");
                String lName = user1.getString("lname");
                User user = new User(fName, lName, id);
                users.add(user);
            }
            receiver_id = users.get(0).id;
            ArrayAdapter<User> adapter = new ArrayAdapter<User>(this,
                    android.R.layout.simple_spinner_item, users);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    receiver_id = users.get(i).id;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
//            spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    receiver_id = users.get(i).id;
//                }
//            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private int getErrorMsg(String response) {
        return 0;
    }

    @Override
    public void handlePostResponse(ResponseWrapper result) {
        finish();
    }
}
