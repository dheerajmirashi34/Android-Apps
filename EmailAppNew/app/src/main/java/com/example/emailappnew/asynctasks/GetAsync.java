package com.example.emailappnew.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.emailappnew.Utils.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: Jatin N Gupte, Dheeraj Mirashi
 * Group No: 50
 */
public class GetAsync extends AsyncTask<String, Void, ResponseWrapper> {

    private final OkHttpClient client = new OkHttpClient();
    HashMap<String, String> headers;
    GetAsyncInterface getAsyncInterface;


    public GetAsync(GetAsyncInterface getAsyncInterface, HashMap<String, String> headers) {
        this.headers = headers;
        this.getAsyncInterface = getAsyncInterface;
    }

    @Override
    protected void onPostExecute(ResponseWrapper result) {
        super.onPostExecute(result);
        getAsyncInterface.handleResponse(result);
    }

    @Override
    protected ResponseWrapper doInBackground(String... params) {

        String responseSring = null;

        Headers.Builder hBuilder = new Headers.Builder();

        if (this.headers != null) {
            for (Map.Entry header : this.headers.entrySet()) {
                hBuilder.add(header.getKey().toString(), header.getValue().toString());
            }
        }

        Headers reqHeader = hBuilder.build();

        Request request = new Request.Builder()
                .url(params[0])
                .headers(reqHeader)
                .build();

        try {
            Response response = client.newCall(request).execute();
            responseSring = response.body().string();
            Log.d("Response String", responseSring);
            if (response.isSuccessful()) {
                return new ResponseWrapper(responseSring, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return new ResponseWrapper(responseSring, false);
    }


    public interface GetAsyncInterface {
        void handleResponse(ResponseWrapper result);
    }
}
