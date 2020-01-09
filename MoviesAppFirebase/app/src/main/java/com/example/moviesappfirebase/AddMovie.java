package com.example.moviesappfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;


/**
 * File Name: AddMovie.java
 * Author: Dheeraj Sanjay Mirashi
 * Group No: 50
 */
public class AddMovie extends AppCompatActivity {

    FirebaseFirestore db;
    String key;
    private ArrayAdapter<String> genreArrayAdapter;
    private TextView ratingLabel;
    private Movie movie;
    private int genreIndex = 0;
    private Spinner genreDropdwon;
    private List<Genre> genreList = new ArrayList<Genre>();
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
        setContentView(R.layout.activity_add_movie);

        movie = new Movie(null, null, null, 0, 0, null);

        genreList = Arrays.asList(new Genre[]{Genre.Action, Genre.Animation, Genre.Comedy, Genre.Documentary, Genre.Family, Genre.Horror, Genre.Crime, Genre.Others});
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        yearEditText = findViewById(R.id.yearEditText);
        ratingLabel = findViewById(R.id.rating_display_label);
        imdbLinkEditText = findViewById(R.id.editText2);
        seekbar = findViewById(R.id.seekBar);
        seekbar.setMax(5);
        addbutton = findViewById(R.id.button);
        db = FirebaseFirestore.getInstance();
        genreDropdwon = (Spinner) findViewById(R.id.genreDropdown);

        genreArrayAdapter = new ArrayAdapter<String>(AddMovie.this, R.layout.support_simple_spinner_dropdown_item, Genre.getGenreList());
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


        if (getIntent() != null && getIntent().getExtras() != null && !getIntent().getExtras().get(MainActivity.MOVIE_ID_Label).equals("")) {
            key = (String) getIntent().getExtras().get(MainActivity.MOVIE_ID_Label);

            if(CheckInternet.isConnected(this)) {
                DocumentReference docRef = db.collection("movies").document(key);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Movie m = new Movie();

                                m.setName(document.getData().get("name").toString());
                                m.setDescription(document.getData().get("description").toString());
                                m.setGenre(Genre.get(document.getData().get("genre").toString()));
                                //genreIndex =
                                m.setRating(Integer.parseInt(document.getData().get("rating").toString()));
                                m.setYear(Integer.parseInt(document.getData().get("year").toString()));
                                m.setImdb(document.getData().get("imdb").toString());

                                nameEditText.setText(m.getName());
                                descriptionEditText.setText(m.getDescription());
                                seekbar.setProgress(m.getRating());
                                yearEditText.setText(m.getYear() + "");
                                ratingLabel.setText(m.getRating() + "");
                                imdbLinkEditText.setText(m.getImdb());
                                addbutton.setText("Save Changes");
                                genreDropdwon.setSelection(genreList.indexOf(m.genre));
                                fromEditScreen = true;

                            } else {
                                Log.d("data", "No such document");
                            }
                        } else {
                            Log.d("data", "get failed with ", task.getException());
                        }
                    }
                });
            }
            else{
                Toast.makeText(this, "Internet not connected", Toast.LENGTH_SHORT).show();
            }
        }


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
            movie.setGenre(genreList.get(genreIndex));
            movie.setRating(Integer.parseInt(ratingLabel.getText().toString()));
            movie.setYear(Integer.parseInt(yearEditText.getText().toString()));
            movie.setImdb(imdbLinkEditText.getText().toString());

            if(CheckInternet.isConnected(this)) {
                if (fromEditScreen) {
                    db.collection("movies").document(key).set(movie);
                } else {
                    db.collection("movies")
                            .add(movie).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Log.d("demo", "Success");
                            } else {
                                Log.e("demo", task.getException().toString());
                            }
                        }
                    });

                }
                Intent intent = new Intent(this,MainActivity.class);
                finish();
            }
            else{
                Toast.makeText(this, "Internet not connected", Toast.LENGTH_SHORT).show();
            }

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
