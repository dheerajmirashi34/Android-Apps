package com.example.emailappnew.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.emailappnew.Utils.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: Jatin N Gupte, Dheeraj Mirashi
 * Group No: 50
 */
public class PostAsync extends AsyncTask<String, Void, ResponseWrapper> {

    private final OkHttpClient client = new OkHttpClient();
    HashMap<String, String> formParamsMap;
    HashMap<String, String> headers;
    PostAsyncInterface postAsyncInterface;

    public PostAsync(PostAsyncInterface loginAsyncInterFace, HashMap<String, String> formParamsMap, HashMap<String, String> headers) {
        this.postAsyncInterface = loginAsyncInterFace;
        this.formParamsMap = formParamsMap;
        this.headers = headers;
    }

    @Override
    protected void onPostExecute(ResponseWrapper result) {
        super.onPostExecute(result);
        postAsyncInterface.handlePostResponse(result);

    }

    @Override
    protected ResponseWrapper doInBackground(String... params) {

        String responseString = "";

        Request.Builder requestBuilder = new Request.Builder()
                .url(params[0]);

        //Add form body
        if (formParamsMap != null) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Map.Entry param : formParamsMap.entrySet()) {
                formBuilder.add(param.getKey().toString(), param.getValue().toString());
            }
            requestBuilder.post(formBuilder.build());
        }

        //Add headers
        if (this.headers != null) {
            Headers.Builder hBuilder = new Headers.Builder();
            for (Map.Entry header : this.headers.entrySet()) {
                hBuilder.add(header.getKey().toString(), header.getValue().toString());
            }
            requestBuilder.headers(hBuilder.build());
        }


        Log.d("login rq", params[0]);

        Request request = requestBuilder.build();

        try {
            Response response = client.newCall(request).execute();
            responseString = response.body().string();
            if (response.isSuccessful()) {
                Log.d("resppppp", responseString);
                return new ResponseWrapper(responseString, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseWrapper(responseString, false);
    }

    public interface PostAsyncInterface {
        void handlePostResponse(ResponseWrapper result);
    }
}
