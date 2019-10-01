package com.example.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RetrieveNewsAsync extends AsyncTask<String, Void, List<Article>> {
    private RetrieveNewsAsyncInterface retrieveNewsAsyncInterface;
    private RequestParams requestParams;

    public RetrieveNewsAsync(RetrieveNewsAsyncInterface retrieveNewsAsyncInterface, RequestParams requestParams) {
        this.retrieveNewsAsyncInterface = retrieveNewsAsyncInterface;
        this.requestParams = requestParams;
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
        retrieveNewsAsyncInterface.handleResponse(articles);
    }

    @Override
    protected List<Article> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Article> result = new ArrayList<>();
        try {
            Log.d("NewsAppLog", this.requestParams.getEncodedUrl(params[0]));
            URL url = new URL(this.requestParams.getEncodedUrl(params[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                Log.d("NewsAppLog", json);
                JSONObject root = new JSONObject(json);
                JSONArray articles = root.getJSONArray("articles");
                int arrayLength = (articles.length() >= 20) ? 20 : articles.length();
                for (int i = 0; i < arrayLength; i++) {
                    JSONObject articlesJson = articles.getJSONObject(i);
                    Article article = new Article();
                    article.setAuthor(articlesJson.getString("author"));
                    article.setContent(articlesJson.getString("content"));
                    article.setDescription(articlesJson.getString("description"));
                    article.setPublishedAt(articlesJson.getString("publishedAt"));
                    article.setTitle(articlesJson.getString("title"));
                    article.setUrlToImage(articlesJson.getString("urlToImage"));
                    result.add(article);
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

    public static interface RetrieveNewsAsyncInterface {
        void handleResponse(List<Article> articles);
    }
}
