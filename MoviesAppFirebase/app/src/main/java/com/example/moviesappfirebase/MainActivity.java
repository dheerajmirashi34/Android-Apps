package com.example.moviesappfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String MOVIE_ID_Label ="MOVIE_ID_LABEL";
    public static final String SORT_BY ="SORT_BY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void addMoviesClicked(View v) {
        Intent intent = new Intent(this , AddMovie.class);
        startActivity(intent);
    }

    public void editMoviesClicked(View v) {

        if(CheckInternet.isConnected(this)) {
            //Toast.makeText(this, "YES pahije", Toast.LENGTH_SHORT).show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            final Map<String, String> moviesMap = new HashMap<>();
            db.collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId() != null && document.get("name") != null) {
                                list.add(document.getId());
                                moviesMap.put(document.getId(), document.get("name").toString());
                            }

                        }
                        Log.d("yes", list.toString());

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Pick a movie");
                        if (moviesMap.size() > 0) {

                            Set<String> keys = moviesMap.keySet();
                            final String[] keysArray = new String[keys.size()];
                            final String[] valsArray = new String[keys.size()];
                            int index = 0;
                            for (String element : keys) {
                                keysArray[index] = element;
                                valsArray[index++] = moviesMap.get(element);
                            }


                            builder.setItems(valsArray, new DialogInterface.OnClickListener() {
                                Intent intent = new Intent(getApplicationContext(), AddMovie.class);

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), AddMovie.class);
                                    intent.putExtra(MOVIE_ID_Label, keysArray[which]);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Editing Movie:" + valsArray[which], Toast.LENGTH_SHORT).show();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        } else {
                            Toast.makeText(MainActivity.this, "No movies available", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d("yes", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Internet not connected", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteMoviesClicked(View v) {
        if(CheckInternet.isConnected(this)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            final Map<String, String> moviesMap = new HashMap<>();
            db.collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId() != null && document.get("name") != null) {
                                list.add(document.getId());
                                moviesMap.put(document.getId(), document.get("name").toString());
                            }

                        }
                        Log.d("yes", list.toString());

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Pick a movie");
                        if (moviesMap.size() > 0) {

                            Set<String> keys = moviesMap.keySet();
                            final String[] keysArray = new String[keys.size()];
                            final String[] valsArray = new String[keys.size()];
                            int index = 0;
                            for (String element : keys) {
                                keysArray[index] = element;
                                valsArray[index++] = moviesMap.get(element);
                            }


                            builder.setItems(valsArray, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("movies").document(keysArray[which])
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("data", "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("data", "Error deleting document", e);
                                                }
                                            });

                                    Toast.makeText(MainActivity.this, "Movie Deleted:" + valsArray[which], Toast.LENGTH_SHORT).show();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        } else {
                            Toast.makeText(MainActivity.this, "No movies available", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d("yes", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Internet not connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void showListByYearClicked(View v) {
            Intent intent = new Intent(this,MoviesListActivity.class);
            intent.putExtra(SORT_BY, "year");
            startActivity(intent);
    }

    public void showListByRatingClicked(View v) {
            Intent intent = new Intent(this,MoviesListActivity.class);
            intent.putExtra(SORT_BY, "rating");
            startActivity(intent);
    }

}
