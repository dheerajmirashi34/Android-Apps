package com.example.newslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * File Name: MainActivity.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class MainActivity extends AppCompatActivity implements RetrieveSourcesAsync.RetrieveSourcesAsyncInterface {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private final String API_KEY = "95dd0e6e194a44b197dec7fedea99606";
    private final String URL_SOURCES = "https://newsapi.org/v2/sources";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.sources_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RequestParams requestParams = new RequestParams();
        requestParams.addParams("apiKey", API_KEY);
        if (CheckInternet.isConnected(MainActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            new RetrieveSourcesAsync(MainActivity.this, requestParams).execute(URL_SOURCES);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleSourcesResponse(ArrayList<Source> sources) {
        progressBar.setVisibility(View.INVISIBLE);
        if (!sources.isEmpty()) {
            mAdapter = new SourcesAdapter(sources);
            recyclerView.setAdapter(mAdapter);
        }
    }

}
