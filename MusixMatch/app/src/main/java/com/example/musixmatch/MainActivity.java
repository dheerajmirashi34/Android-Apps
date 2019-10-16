package com.example.musixmatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.widget.TextView;
import android.widget.Toast;

/**
 * File Name: RetrieveTracksAsync.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class MainActivity extends AppCompatActivity implements RetrieveTracksAsync.RetrieveTracksAsyncInterface {

    private EditText songsEditText;
    private SeekBar limitSeekBar;
    private Button searchButton;
    private RadioGroup sortByRg;
    private RadioButton trackRatingRb;
    private RadioButton artistRatingRb;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView limitTv;

    private int limit = 5;
    private int seekbarMinValue = 5;
    private final String SORT_BY_VALUE = "desc";
    private final String SORT_BY_ARTIST_KEY = "s_artist_rating";
    private final String SORT_BY_TRACK_KEY = "s_track_rating";
    private final String PAGE_SIZE_KEY = "page_size";
    private String sortByKey = SORT_BY_TRACK_KEY;
    private final String API_KEY_VALUE = "e7ece7fff118129f8374d73c027dc978";
    private final String API_KEY_KEY = "apikey";
    private final String URL_SOURCES = "http://api.musixmatch.com/ws/1.1/track.search";
    private final String SEARCH_KEY = "q";
    private final String SIMPLE_VALIDATION_REGEX = "[a-zA-Z0-9\\-!\\.\\'\\?\\s]{0,50}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.track_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        songsEditText = findViewById(R.id.songTextField);
        limitSeekBar = findViewById(R.id.limitSeekBar);
        searchButton = findViewById(R.id.searchButton);
        sortByRg = findViewById(R.id.sortByRg);
        trackRatingRb = findViewById(R.id.trackRatingRb);
        artistRatingRb = findViewById(R.id.artistRatingRb);
        progressBar = findViewById(R.id.progressBar);
        limitTv = findViewById(R.id.limitTv);

        limitTv.setText("Limit: "+String.valueOf(limit));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeApiCall();
            }
        });

        limitSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                limit = seekbarMinValue + i;
                limitTv.setText("Limit: "+String.valueOf(limit));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sortByRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.artistRatingRb) {
                    sortByKey = SORT_BY_ARTIST_KEY;
                } else if (i == R.id.trackRatingRb) {
                    sortByKey = SORT_BY_TRACK_KEY;
                }
                makeApiCall();
            }
        });

    }

    private void makeApiCall() {
        RequestParams requestParams = new RequestParams();
        String searchQuery = songsEditText.getText().toString();

        boolean nameValidation = (!searchQuery.equals("") && Pattern.compile(SIMPLE_VALIDATION_REGEX).matcher(searchQuery).matches()) ? true : false;


        if (nameValidation) {
            progressBar.setVisibility(View.VISIBLE);
            requestParams.addParams(PAGE_SIZE_KEY, String.valueOf(limit)).addParams(API_KEY_KEY, API_KEY_VALUE).addParams(sortByKey, SORT_BY_VALUE).addParams(SEARCH_KEY, searchQuery);
            if (CheckInternet.isConnected(MainActivity.this)) {
                new RetrieveTracksAsync(MainActivity.this, requestParams).execute(URL_SOURCES);
            } else {
                Toast.makeText(MainActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
            }
        } else {
            songsEditText.setError("Enter valid query");
        }
    }


    @Override
    public void handleSourcesResponse(ArrayList<Track> tracks) {
        progressBar.setVisibility(View.INVISIBLE);
        if (!tracks.isEmpty()) {
            mAdapter = new TrackAdapter(tracks);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter = new TrackAdapter(new ArrayList<Track>());
            recyclerView.setAdapter(mAdapter);
            Toast.makeText(this, "Track :" + tracks.size(), Toast.LENGTH_SHORT).show();
        }
    }
}
