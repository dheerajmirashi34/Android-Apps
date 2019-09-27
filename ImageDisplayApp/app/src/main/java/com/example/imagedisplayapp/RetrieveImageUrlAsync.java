package com.example.imagedisplayapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @File: MainActivity.java
 * @Author: Jatin Gupte, Dheeraj Mirashi
 * @Group: 50
 **/
public class RetrieveImageUrlAsync extends AsyncTask<String, Void, String> {
    RetrieveImageUrlAsyncInterface retrieveImageUrlAsyncInterface;
    RequestParams params;

    public RetrieveImageUrlAsync(RetrieveImageUrlAsyncInterface retrieveImageUrlAsyncInterface, RequestParams params) {
        this.retrieveImageUrlAsyncInterface = retrieveImageUrlAsyncInterface;
        this.params = params;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        retrieveImageUrlAsyncInterface.handleUrlListResp(s);

    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String result = null;
        try {
            Log.d("NWDEMO", strings[0]);
            URL url = new URL(this.params.getEncodedUrl(strings[0]));
            conn = (HttpURLConnection) url.openConnection();
            result = IOUtils.toString(conn.getInputStream(), "UTF8");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static interface RetrieveImageUrlAsyncInterface {
        public void handleUrlListResp(String resp);
    }
}
