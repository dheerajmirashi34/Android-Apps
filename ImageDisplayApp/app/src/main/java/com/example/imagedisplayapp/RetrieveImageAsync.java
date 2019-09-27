package com.example.imagedisplayapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @File: MainActivity.java
 * @Author: Jatin Gupte, Dheeraj Mirashi
 * @Group: 50
 **/
public class RetrieveImageAsync extends AsyncTask<String, Void, Bitmap> {
    RetrieveImageAsyncInterface retrieveImageAsyncInterface;

    public RetrieveImageAsync(RetrieveImageAsyncInterface retrieveImageAsyncInterface) {
        this.retrieveImageAsyncInterface = retrieveImageAsyncInterface;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        retrieveImageAsyncInterface.handleBitmapImage(bitmap);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap image = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(strings[0]);
            conn = (HttpURLConnection) url.openConnection();
            image = BitmapFactory.decodeStream(conn.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (image != null) {
                //TODO: close image
            }
        }

        return image;
    }

    public static interface RetrieveImageAsyncInterface {
        void handleBitmapImage(Bitmap image);
    }
}
