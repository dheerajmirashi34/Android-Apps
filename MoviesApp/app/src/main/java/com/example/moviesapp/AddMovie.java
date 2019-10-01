package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.moviesapp.R.*;

/**
 * File Name: Movie.java
 * Authors: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 * Group No: 50
 */
public class AddMovie extends AppCompatActivity {

    private Genre genre;
    private List<String> genreList;
    private ArrayAdapter<String> genreArrayAdapter;
    private ArrayList<Movie> moviesList;
    private TextView ratingLabel;
    private Movie movie;
    private int genreIndex;
    private Spinner genreDropdwon;
    private Genre[] genres = {Genre.Action, Genre.Animation, Genre.Comedy, Genre.Documentary, Genre.Family, Genre.Horror, Genre.Crime, Genre.Others};
    private SeekBar seekbar;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText yearEditText;
    private EditText imdbLinkEditText;
    private Button addbutton;

    private boolean fromEditScreen = false;
    private final String SIMPLE_VALIDATION_REGEX = "[a-zA-Z0-9\\-!\\.\\'\\?\\s]{0,1000}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_add_movie);
        movie = new Movie(null, null, null, 0, 0, null);

        nameEditText = findViewById(id.nameEditText);
        descriptionEditText = findViewById(id.descriptionEditText);
        yearEditText = findViewById(id.yearEditText);
        ratingLabel = findViewById(id.rating_display_label);
        imdbLinkEditText = findViewById(id.editText2);
        seekbar = findViewById(id.seekBar);
        seekbar.setMax(5);
        addbutton = findViewById(id.button);

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().get(MainActivity.MOVIE_OBJECT) instanceof Movie) {
            Movie m = (Movie) getIntent().getExtras().get(MainActivity.MOVIE_OBJECT);
            nameEditText.setText(m.getName());
            descriptionEditText.setText(m.getDescription());
            seekbar.setProgress(m.getRating());
            yearEditText.setText(m.getYear() + "");
            ratingLabel.setText(m.getRating() + "");
            imdbLinkEditText.setText(m.getImdb());
            addbutton.setText("Save Changes");
            fromEditScreen = true;
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            moviesList = (ArrayList<Movie>) getIntent().getSerializableExtra(MainActivity.MOVIES_LIST);
        }


        ratingLabel = findViewById(id.rating_display_label);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ratingLabel.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        genreDropdwon = (Spinner) findViewById(id.genreDropdown);
        genreList = new ArrayList<String>();
        for (Genre g : Genre.values()) {
            genreList.add(g.toString());
        }
        genreArrayAdapter = new ArrayAdapter<String>(AddMovie.this, layout.support_simple_spinner_dropdown_item, genreList);
        genreDropdwon.setAdapter(genreArrayAdapter);

        genreDropdwon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genreIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nameEditText = findViewById(id.nameEditText);
        descriptionEditText = findViewById(id.descriptionEditText);
        yearEditText = findViewById(id.yearEditText);
        ratingLabel = findViewById(id.rating_display_label);
        imdbLinkEditText = findViewById(id.editText2);

    }


    public void addMovieButtonClick(View view) {
        String nameText = nameEditText.getText().toString().trim();
        String descText = descriptionEditText.getText().toString().trim();
        int yearValue = (yearEditText.getText().toString().trim().equals("")) ? 0 : Integer.parseInt(yearEditText.getText().toString().trim());
        String imdbText = imdbLinkEditText.getText().toString().trim();

        boolean nameValidation = (!nameText.equals("") && Pattern.compile(SIMPLE_VALIDATION_REGEX).matcher(nameText).matches()) ? true : false;
        boolean imdbValidation = (!imdbText.equals("") && Patterns.WEB_URL.matcher(imdbText).matches()) ? true : false;
        boolean descValidation = (!descText.equals("") && Pattern.compile(SIMPLE_VALIDATION_REGEX).matcher(descText).matches()) ? true : false;
        boolean yearValidation = (yearValue <= Calendar.getInstance().get(Calendar.YEAR) && yearValue > 1874) ? true : false;


        if (nameValidation && imdbValidation && descValidation && yearValidation) {

            movie.setName(nameEditText.getText().toString());
            movie.setDescription(descriptionEditText.getText().toString());
            movie.setGenre(genres[genreIndex]);
            movie.setRating(Integer.parseInt(ratingLabel.getText().toString()));
            movie.setYear(Integer.parseInt(yearEditText.getText().toString()));
            movie.setImdb(imdbLinkEditText.getText().toString());

            if (!fromEditScreen && moviesList.contains(movie)) {
                Toast.makeText(this, "Movie already exists!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent();
            intent.putExtra(MainActivity.MOVIE_OBJECT, movie);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (!nameValidation) {
                nameEditText.setError("Invalid Input");
            }

            if (!imdbValidation) {
                imdbLinkEditText.setError("Invalid Input");
            }

            if (!descValidation) {
                descriptionEditText.setError("Invalid Input");
            }

            if (!yearValidation) {
                yearEditText.setError("Invalid Input");
            }
        }
    }
}
