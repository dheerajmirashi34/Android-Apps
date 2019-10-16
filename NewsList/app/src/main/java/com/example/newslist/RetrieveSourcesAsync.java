package com.example.newslist;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * File Name: RetrieveSourcesAsync.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class RetrieveSourcesAsync extends AsyncTask<String, Void, ArrayList<Source>> {
    private RetrieveSourcesAsyncInterface retrieveSourcesAsyncInterface;
    private RequestParams requestParams;

    public RetrieveSourcesAsync(RetrieveSourcesAsyncInterface retrieveSourcesAsyncInterface, RequestParams requestParams) {
        this.retrieveSourcesAsyncInterface = retrieveSourcesAsyncInterface;
        this.requestParams = requestParams;
    }

    @Override
    protected void onPostExecute(ArrayList<Source> articles) {
        super.onPostExecute(articles);
        retrieveSourcesAsyncInterface.handleSourcesResponse(articles);
    }

    @Override
    protected ArrayList<Source> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Source> result = new ArrayList<>();
        try {
            URL url = new URL(this.requestParams.getEncodedUrl(params[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                JSONArray sources = root.getJSONArray("sources");
                // int arrayLength = (sources.length() >= 20) ? 20 : sources.length();
                for (int i = 0; i < sources.length(); i++) {
                    JSONObject articlesJson = sources.getJSONObject(i);
                    Source source = new Source();
                    source.setId((articlesJson.getString("id").equals("null") || articlesJson.getString("id").equals("")) ? "" : articlesJson.getString("id"));
                    source.setName((articlesJson.getString("name").equals("null") || articlesJson.getString("name").equals("")) ? "" : articlesJson.getString("name"));
                    result.add(source);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;

    }

    public interface RetrieveSourcesAsyncInterface {
        void handleSourcesResponse(ArrayList<Source> articles);
    }
}
