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
public class RetriveKeysAsync extends AsyncTask<String, Void, String> {
    RetriveKeysAsyncInterface retriveKeysAsyncInterface;

    public RetriveKeysAsync(RetriveKeysAsyncInterface retriveKeysAsyncInterface){
        this.retriveKeysAsyncInterface = retriveKeysAsyncInterface;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        retriveKeysAsyncInterface.handleKeysResp(s);
    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder sb = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String result = null;
        try {
            Log.d("NWDEMO", strings[0]);
            URL url = new URL(strings[0]);
            conn = (HttpURLConnection) url.openConnection();
//            conn.connect();
//            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("NWDEMO","Connection OK");
                result = IOUtils.toString(conn.getInputStream(), "UTF8");
                Log.d("NWDEMO",result);
           // }

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

    public static interface RetriveKeysAsyncInterface{
        public void handleKeysResp(String resp);
    }
}
