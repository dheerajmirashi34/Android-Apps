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
 * File Name: RetrieveTopNewsAsync.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class RetrieveTopNewsAsync extends AsyncTask<String, Void, ArrayList<News>> {
    private RetrieveTopNewsAsyncInterface retrieveTopNewsAsyncInterface;
    private RequestParams requestParams;

    public RetrieveTopNewsAsync(RetrieveTopNewsAsyncInterface retrieveTopNewsAsyncInterface, RequestParams requestParams) {
        this.retrieveTopNewsAsyncInterface = retrieveTopNewsAsyncInterface;
        this.requestParams = requestParams;
    }

    @Override
    protected void onPostExecute(ArrayList<News> news) {
        super.onPostExecute(news);
        retrieveTopNewsAsyncInterface.handleTopNewsResponse(news);
    }

    @Override
    protected ArrayList<News> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<News> result = new ArrayList<>();
        try {
            URL url = new URL(this.requestParams.getEncodedUrl(params[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                JSONArray articles = root.getJSONArray("articles");
                //int arrayLength = (articles.length() >= 20) ? 20 : articles.length();
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject newsJson = articles.getJSONObject(i);
                    News news = new News();
                    news.setAuthor((newsJson.getString("author").equals("null") ? "" : newsJson.getString("author")));
                    news.setPublishedAt((newsJson.getString("publishedAt").equals("null")  ? "" : newsJson.getString("publishedAt")));
                    news.setTitle((newsJson.getString("title").equals("null")) ? "" : newsJson.getString("title"));
                    news.setUrl((newsJson.getString("url").equals("null")) ? "" : newsJson.getString("url"));
                    news.setUrlToImage(( newsJson.getString("urlToImage") == null || newsJson.getString("urlToImage").equals("null") || (newsJson.getString("urlToImage").equals("")) ? "http://abbcb.dd" : newsJson.getString("urlToImage")));
                    result.add(news);
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

    public interface RetrieveTopNewsAsyncInterface {
        void handleTopNewsResponse(ArrayList<News> articles);
    }
}
