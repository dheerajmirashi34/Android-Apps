package com.example.newslist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * File Name: WebViewActivity.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class WebViewActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        this.webView = findViewById(R.id.webview);
        if (getIntent() != null) {
            String url = getIntent().getStringExtra("url");
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);

        }
    }
}
