package com.example.moviesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * File Name: Movie.java
 * Authors: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 * Group No: 50
 */
public class MainActivity extends AppCompatActivity {

    private static final int ADD_MOVIE_REQUEST = 1000;
    private static final int EDIT_MOVIE_REQUEST = 1001;
    public static final String MOVIE_OBJECT = "movie_object";
    private ArrayList<Movie> movies = new ArrayList<>();
    public static final String MOVIES_LIST = "movies_list";
    private int movieIndex = -1;
    public static final String SORT_BY = "sort_by";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addMoviesClicked(View v) {
        Intent intent = new Intent(this, AddMovie.class);
        intent.putExtra(MOVIES_LIST, movies);
        startActivityForResult(intent, ADD_MOVIE_REQUEST);
    }

    public void editMoviesClicked(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pick a movie");
        if (movies.size() > 0) {
            final String[] movieNames = new String[movies.size()];
            for (int i = 0; i < movies.size(); i++) {
                movieNames[i] = movies.get(i).getName();
            }
            builder.setItems(movieNames, new DialogInterface.OnClickListener() {
                Intent intent = new Intent(getApplicationContext(),AddMovie.class);
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intent.putExtra(MOVIE_OBJECT,movies.get(which));
                    intent.putExtra(MOVIES_LIST, movies);
                    movieIndex = which;
                    startActivityForResult(intent,MainActivity.EDIT_MOVIE_REQUEST);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(MainActivity.this, "No movies available", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteMoviesClicked(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pick a movie");
        if (movies.size() > 0) {
            String[] movieNames = new String[movies.size()];
            for (int i = 0; i < movies.size(); i++) {
                movieNames[i] = movies.get(i).getName();
            }
            builder.setItems(movieNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,"Movies Deleted",Toast.LENGTH_SHORT).show();
                    movies.remove(which);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(MainActivity.this, "No movies available", Toast.LENGTH_SHORT).show();
        }
    }

    public void showListByYearClicked(View v) {
        if (!movies.isEmpty()) {
            Intent intent = new Intent("com.example.moviesapp.intent.action.VIEW");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(MOVIES_LIST, movies);
            intent.putExtra(SORT_BY, "year");
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "No movies available", Toast.LENGTH_SHORT).show();
        }
    }

    public void showListByRatingClicked(View v) {
        if (!movies.isEmpty()) {
            Intent intent = new Intent("com.example.moviesapp.intent.action.VIEW");
            intent.putExtra(MOVIES_LIST, movies);
            intent.putExtra(SORT_BY, "rating");
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "No movies available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Movie movie;
        switch (requestCode) {
            case ADD_MOVIE_REQUEST:
                assert data != null;
                if(resultCode==RESULT_OK && (Objects.requireNonNull(data.getExtras()).get(MOVIE_OBJECT)!=null) && (data.getExtras().get(MOVIE_OBJECT) instanceof Movie)) {
                    movies.add(movie=(Movie) data.getExtras().get(MOVIE_OBJECT));
                    Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show();
                }
                break;
            case EDIT_MOVIE_REQUEST:
                if(resultCode==RESULT_OK && (data.getExtras().get(MOVIE_OBJECT)!=null) && (data.getExtras().get(MOVIE_OBJECT) instanceof Movie) && movieIndex!=-1) {
                    movies.set(movieIndex,movie=(Movie) data.getExtras().get(MOVIE_OBJECT));
                    Toast.makeText(this, "Movie edited", Toast.LENGTH_SHORT).show();
                    movieIndex = -1;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
}
