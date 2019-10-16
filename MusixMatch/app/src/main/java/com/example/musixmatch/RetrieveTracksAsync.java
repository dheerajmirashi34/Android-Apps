package com.example.musixmatch;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * File Name: RetrieveTracksAsync.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class RetrieveTracksAsync extends AsyncTask<String, Void, ArrayList<Track>> {
    private RetrieveTracksAsyncInterface retrieveTracksAsyncInterface;
    private RequestParams requestParams;

    public RetrieveTracksAsync(RetrieveTracksAsyncInterface retrieveTracksAsyncInterface, RequestParams requestParams) {
        this.retrieveTracksAsyncInterface = retrieveTracksAsyncInterface;
        this.requestParams = requestParams;
    }


    @Override
    protected void onPostExecute(ArrayList<Track> tracks) {
        super.onPostExecute(tracks);
        retrieveTracksAsyncInterface.handleSourcesResponse(tracks);
    }

    @Override
    protected ArrayList<Track> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Track> result = new ArrayList<>();
        try {
            URL url = new URL(this.requestParams.getEncodedUrl(params[0]));
            Log.d("URL ::: ",this.requestParams.getEncodedUrl(params[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                JSONObject message = root.getJSONObject("message");
                JSONObject body = message.getJSONObject("body");
                JSONArray track_list = body.getJSONArray("track_list");

                // int arrayLength = (sources.length() >= 20) ? 20 : sources.length();
                for (int i = 0; i < track_list.length(); i++) {

                    JSONObject articlesJson = track_list.getJSONObject(i);
                    JSONObject trackJason = articlesJson.getJSONObject("track");
                    Track track = new Track();
                    track.setTrackName((trackJason.getString("track_name").equals("null") || trackJason.getString("track_name").equals("")) ? "" : trackJason.getString("track_name"));
                    track.setAlbumName((trackJason.getString("album_name").equals("null") || trackJason.getString("album_name").equals("")) ? "" : trackJason.getString("album_name"));
                    track.setArtistName((trackJason.getString("artist_name").equals("null") || trackJason.getString("artist_name").equals("")) ? "" : trackJason.getString("artist_name"));
                    track.setDate((trackJason.getString("updated_time").equals("null") || trackJason.getString("updated_time").equals("")) ? "" : trackJason.getString("updated_time"));
                    track.setUrl((trackJason.getString("track_share_url").equals("null") || trackJason.getString("track_share_url").equals("")) ? "" : trackJason.getString("track_share_url"));
                    result.add(track);
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

    public interface RetrieveTracksAsyncInterface {
        void handleSourcesResponse(ArrayList<Track> articles);
    }
}
