package com.example.moviesappfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * File Name: Movie.java
 * Authors: Dheeraj Sanjay Mirashi
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
    private List<Movie> movies;

    private int currentSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_by_year);
        setTitle("Display Movies");

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
        movies = new ArrayList<>();
        

        if (getIntent() != null && getIntent().getExtras() != null) {
            String sortBy = getIntent().getExtras().getString(MainActivity.SORT_BY);

            if (sortBy.equalsIgnoreCase("year")) {
                screenTitle.setText(getResources().getText(R.string.Movies_By_Year_Title));

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if(CheckInternet.isConnected(this)) {
                    db.collection("movies")
                            .orderBy("year")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            Movie m = new Movie();
                                            m.setName(queryDocumentSnapshot.getData().get("name").toString());
                                            m.setDescription(queryDocumentSnapshot.getData().get("description").toString());
                                            m.setGenre(Genre.get(queryDocumentSnapshot.getData().get("genre").toString()));
                                            m.setRating(Integer.parseInt(queryDocumentSnapshot.getData().get("rating").toString()));
                                            m.setYear(Integer.parseInt(queryDocumentSnapshot.getData().get("year").toString()));
                                            m.setImdb(queryDocumentSnapshot.getData().get("imdb").toString());
                                            movies.add(m);
                                        }
                                        if(movies.size()==0){
                                            Toast.makeText(MoviesListActivity.this, "No movies to display", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else{
                                            setContents(movies.get(currentSelectedIndex));
                                        }

                                    }

                                }
                            });
                }
                else{
                    Toast.makeText(this, "Internet not connected", Toast.LENGTH_SHORT).show();
                }

            } else if (sortBy.equalsIgnoreCase("rating")) {
                if(CheckInternet.isConnected(this)) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("movies")
                            .orderBy("rating")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            Movie m = new Movie();
                                            m.setName(queryDocumentSnapshot.getData().get("name").toString());
                                            m.setDescription(queryDocumentSnapshot.getData().get("description").toString());
                                            m.setGenre(Genre.get(queryDocumentSnapshot.getData().get("genre").toString()));
                                            m.setRating(Integer.parseInt(queryDocumentSnapshot.getData().get("rating").toString()));
                                            m.setYear(Integer.parseInt(queryDocumentSnapshot.getData().get("year").toString()));
                                            m.setImdb(queryDocumentSnapshot.getData().get("imdb").toString());
                                            movies.add(m);
                                        }
                                        if(movies.size()==0){
                                            Toast.makeText(MoviesListActivity.this, "No movies to display", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else {
                                            setContents(movies.get(currentSelectedIndex));
                                        }

                                    }

                                }
                            });
                }
                else{
                    Toast.makeText(this, "Internet not connected", Toast.LENGTH_SHORT).show();
                }
            }


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
