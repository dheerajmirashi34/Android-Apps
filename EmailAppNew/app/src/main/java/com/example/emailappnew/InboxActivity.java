package com.example.emailappnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailappnew.Utils.AppConstants;
import com.example.emailappnew.Utils.CheckInternet;
import com.example.emailappnew.Utils.ResponseWrapper;
import com.example.emailappnew.asynctasks.GetAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Jatin N Gupte, Dheeraj Mirashi
 * Group No: 50
 */
public class InboxActivity extends AppCompatActivity implements GetAsync.GetAsyncInterface {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageView logoutIv;
    private ImageView createNewIv;
    private TextView nameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");

        logoutIv = findViewById(R.id.logoutIv);
        createNewIv = findViewById(R.id.createNewIv);
        nameTv = findViewById(R.id.nameTv);

        recyclerView = findViewById(R.id.inbox_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        logoutIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("my_pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(view.getContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        createNewIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),CreateAndDisplayActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.AUTH_TOKEN_KEY, "abc");
        String name = sharedPreferences.getString(AppConstants.USER_NAME_KEY, "John Doe");

        nameTv.setText(name);

        if (CheckInternet.isConnected(this)) {
            HashMap<String, String> headersMap = new HashMap<>();
            headersMap.put(AppConstants.AUTHORIZATION_HEADER, "bearer " + token);

            new GetAsync(this, headersMap).execute(AppConstants.INBOX_GET_URL);
        } else {
            Toast.makeText(InboxActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void handleResponse(ResponseWrapper wrapper) {
        if (wrapper.isSuccess()) {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            ArrayList<Email> emailList = getEmailList(wrapper.getResponse());
            mAdapter = new EmailAdapter(emailList);
            recyclerView.setAdapter(mAdapter);
        } else {
            Toast.makeText(this, getErrorMsg(wrapper.getResponse()), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Email> getEmailList(String respString) {
        ArrayList<Email> eList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(respString);
            JSONArray emailArray = root.getJSONArray("messages");
            for (int i = 0; i < emailArray.length(); i++) {
                JSONObject obj = emailArray.getJSONObject(i);
                Email email = new Email();
                email.setCreatedAt(obj.getString("created_at").equals("null") ? null : obj.getString("created_at"));
                email.setDate(obj.getString("created_at").equals("null") ? null : obj.getString("created_at"));
                email.setFname(obj.getString("sender_fname").equals("null") ? null : obj.getString("sender_fname"));
                email.setId(obj.getString("id").equals("null") ? null : obj.getString("id"));
                email.setLname(obj.getString("sender_lname").equals("null") ? null : obj.getString("sender_lname"));
                email.setMessage(obj.getString("message").equals("null") ? null : obj.getString("message"));
                email.setReceiverId(obj.getString("receiver_id").equals("null") ? null : obj.getString("receiver_id"));
                email.setSenderId(obj.getString("sender_id").equals("null") ? null : obj.getString("sender_id"));
                email.setSubject(obj.getString("subject").equals("null") ? null : obj.getString("subject"));
                email.setUpdatedAt(obj.getString("updated_at").equals("null") ? null : obj.getString("updated_at"));
                eList.add(email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eList;
    }

    private String getErrorMsg(String respString) {
        try {
            JSONObject root = new JSONObject(respString);
            String message = root.getString("message");
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
