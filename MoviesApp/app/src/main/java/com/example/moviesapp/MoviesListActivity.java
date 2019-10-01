package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * File Name: Movie.java
 * Authors: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 * Group No: 50
 */
public class MoviesListActivity extends AppCompatActivity {

    private TextView screenTitle;
    private TextView titleValueTv;
    private TextView descValueTv;
    private TextView genreValaueTv;
    private TextView ratingsValueTv;
    private TextView yearValueTv;
    private TextView imdbValueTv;
    private ImageView firstIv;
    private ImageView prevIv;
    private ImageView nextIv;
    private ImageView lastIv;
    private Button finishBtn;

    private ArrayList<Movie> movies;
    private int currentSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_by_year);
        setTitle("Movies By Year");

        screenTitle = findViewById(R.id.screenTitleTv);
        titleValueTv = findViewById(R.id.titleValue);
        descValueTv = findViewById(R.id.descValue);
        genreValaueTv = findViewById(R.id.genreValue);
        ratingsValueTv = findViewById(R.id.ratingsValue);
        yearValueTv = findViewById(R.id.yearValue);

        imdbValueTv = findViewById(R.id.imdbValue);
        firstIv = findViewById(R.id.firstButton);
        prevIv = findViewById(R.id.prevButton);
        nextIv = findViewById(R.id.nextButton);
        lastIv = findViewById(R.id.lastButton);
        finishBtn = findViewById(R.id.finishButton);

        if (getIntent() != null && getIntent().getExtras() != null) {
            movies = (ArrayList<Movie>) getIntent().getSerializableExtra(MainActivity.MOVIES_LIST);
            String sortBy = getIntent().getExtras().getString(MainActivity.SORT_BY);

            //Toast.makeText(this, sortBy, Toast.LENGTH_SHORT).show();

            if (sortBy.equalsIgnoreCase("year")) {
                screenTitle.setText(getResources().getText(R.string.Movies_By_Year_Title));
                movies.sort(new Comparator<Movie>() {
                    @Override
                    public int compare(Movie m1, Movie m2) {
                        return m1.getYear() - m2.getYear();
                    }
                });
            } else if (sortBy.equalsIgnoreCase("rating")) {
                screenTitle.setText(getResources().getText(R.string.Movies_By_Rating_Title));
                movies.sort(new Comparator<Movie>() {
                    @Override
                    public int compare(Movie m1, Movie m2) {
                        return m2.getRating() - m1.getRating();
                    }
                });
            }

            setContents(movies.get(currentSelectedIndex));
        }

        firstIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelectedIndex = 0;
                setContents(movies.get(currentSelectedIndex));
            }
        });

        prevIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSelectedIndex == 0) {
                    currentSelectedIndex = movies.size() - 1;
                    setContents(movies.get(currentSelectedIndex));
                } else {
                    currentSelectedIndex--;
                    setContents(movies.get(currentSelectedIndex));
                }
            }
        });

        nextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelectedIndex = (currentSelectedIndex + 1) % movies.size();
                setContents(movies.get(currentSelectedIndex));
            }
        });

        lastIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelectedIndex = movies.size() - 1;
                setContents(movies.get(currentSelectedIndex));
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setContents(Movie m) {
        titleValueTv.setText(m.getName());
        descValueTv.setText(m.getDescription());
        genreValaueTv.setText(m.getGenre().toString());
        ratingsValueTv.setText(m.getRating() + "/5");
        yearValueTv.setText(m.getYear() + "");
        imdbValueTv.setText(m.getImdb());
    }

}
