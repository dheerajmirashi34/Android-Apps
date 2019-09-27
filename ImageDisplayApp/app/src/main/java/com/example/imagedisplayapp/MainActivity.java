package com.example.imagedisplayapp;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @File: MainActivity.java
 * @Author: Jatin Gupte, Dheeraj Mirashi
 * @Group: 50
 **/
public class MainActivity extends AppCompatActivity implements RetriveKeysAsync.RetriveKeysAsyncInterface, RetrieveImageUrlAsync.RetrieveImageUrlAsyncInterface, RetrieveImageAsync.RetrieveImageAsyncInterface {
    private Button goButton;
    private ImageView imageView;
    private TextView searchKeywordTV;
    private ProgressBar loadingPB;
    private ImageView nextButton;
    private ImageView prevButton;

    public static final String GET_KEYWORDS_URL = "http://dev.theappsdr.com/apis/photos/keywords.php";
    public static final String GET_URL_LIST_URL = "http://dev.theappsdr.com/apis/photos/index.php";

    String[] listItems;
    String[] urlList;
    int currentImage = 0;
    boolean progressbarVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Image App");
        loadKeyWordList();

        goButton = findViewById(R.id.go_button);
        imageView = findViewById(R.id.imageView);
        searchKeywordTV = findViewById(R.id.search_keyword);
        loadingPB = findViewById(R.id.progressBar);
        nextButton = findViewById(R.id.next_image);
        prevButton = findViewById(R.id.prev_image);

        nextButton.setImageResource(R.drawable.next);
        prevButton.setImageResource(R.drawable.prev);
        //imageView.setImageResource(R.drawable.defaultimage);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlList != null && urlList.length > 1) {
                    if (isConnected()) {
                        progressbarVisible = true;
                        setVisibilityOfProgressBar();
                        imageView.setImageURI(Uri.parse("http://jsjss"));
                        if (currentImage == 0) {
                            new RetrieveImageAsync(MainActivity.this).execute(urlList[currentImage = urlList.length - 1]);
                        } else {
                            new RetrieveImageAsync(MainActivity.this).execute(urlList[currentImage = currentImage - 1]);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Image not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected() && urlList != null && urlList.length > 1) {
                    if (isConnected()) {
                        progressbarVisible = true;
                        setVisibilityOfProgressBar();
                        imageView.setImageURI(Uri.parse("http://jsjss"));
                        new RetrieveImageAsync(MainActivity.this).execute(urlList[currentImage = (currentImage + 1) % urlList.length]);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Image not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItems != null && listItems.length > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Choose a keyword");

                    builder.setItems(listItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isConnected()) {
                                searchKeywordTV.setText(listItems[which]);
                                RequestParams params = new RequestParams();
                                params.addParams("keyword", listItems[which]);
                                imageView.setImageURI(Uri.parse("http://jsjss"));
                                progressbarVisible = true;
                                setVisibilityOfProgressBar();
                                new RetrieveImageUrlAsync(MainActivity.this, params).execute(GET_URL_LIST_URL);
                            } else {
                                Toast.makeText(MainActivity.this, "Internet not connected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    loadKeyWordList();
                }
            }
        });
    }

    public void loadKeyWordList(){
        if (isConnected()) {
            new RetriveKeysAsync(MainActivity.this).execute(GET_KEYWORDS_URL);
        } else {
            Toast.makeText(MainActivity.this, "Internet not connected", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableButtonClick() {
        nextButton.setClickable(true);
        prevButton.setClickable(true);
        goButton.setClickable(true);
    }

    private void disableButtonClick() {
        nextButton.setClickable(false);
        prevButton.setClickable(false);
        goButton.setClickable(false);
    }

    public void handleKeysResp(String resp) {
        this.listItems = resp.split(";");
    }

    public void handleUrlListResp(String resp) {
        if (resp != null && !resp.trim().equals("")) {
            this.urlList = resp.split("\n");
            currentImage = 0;
            if (urlList.length > 0) {
                if (isConnected()) {
                    new RetrieveImageAsync(MainActivity.this).execute(this.urlList[0]);
                } else {
                    Toast.makeText(MainActivity.this, "Internet not connected", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleBitmapImage(Bitmap image) {
        if (image != null) {
            imageView.setImageBitmap(image);
            progressbarVisible = false;
            setVisibilityOfProgressBar();
        } else {
            Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVisibilityOfProgressBar() {
        if (progressbarVisible == true) {
            loadingPB.setVisibility(View.VISIBLE);
        } else {
            loadingPB.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != connectivityManager.TYPE_WIFI) &&
                        (networkInfo.getType() != connectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
