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
 * File Name: TopNewsActivity.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class TopNewsActivity extends AppCompatActivity implements RetrieveTopNewsAsync.RetrieveTopNewsAsyncInterface {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private final String URL_TOP_HEADLINE = "https://newsapi.org/v2/top-headlines";
    private final String API_KEY = "95dd0e6e194a44b197dec7fedea99606";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_news);
        progressBar = findViewById(R.id.topNewsProgress);

        if (getIntent() != null) {
            this.id = getIntent().getStringExtra("id");
        }

        recyclerView = findViewById(R.id.top_news_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RequestParams requestParams = new RequestParams();
        requestParams.addParams("apiKey", API_KEY).addParams("sources", id);
        if (CheckInternet.isConnected(TopNewsActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            new RetrieveTopNewsAsync(TopNewsActivity.this, requestParams).execute(URL_TOP_HEADLINE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(TopNewsActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleTopNewsResponse(ArrayList<News> news) {
        progressBar.setVisibility(View.INVISIBLE);
        if (!news.isEmpty()) {
            mAdapter = new NewsAdapter(news);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
