package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Handle empty list
 */

public class MainActivity extends AppCompatActivity implements RetrieveNewsAsync.RetrieveNewsAsyncInterface {
    private final String API_KEY = "95dd0e6e194a44b197dec7fedea99606";
    private final String URL = "https://newsapi.org/v2/top-headlines";
    private final String DEFAULT_IMAGE_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTs2MLrotUUZh7DoUoWoz5qHayN5YepArgvkSgzw2D0YhWUlbOU";

    private TextView categoryTV;
    private TextView newsTitleTV;
    private TextView newsPublishedAtTV;
    private TextView newsDescriptionTV;
    private TextView resultCountTV;
    private Button selectButton;
    private ProgressBar progressBar;
    private ImageView newsImageIV;
    private ImageView nextImageIV;
    private ImageView prevImageIV;

    private List<Article> articles = null;
    private boolean progressbarVisible = false;
    private int currentSelectedIndex = 0;

    private final String[] categories = {
            "Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"
    };
    private String selectedCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News App");


        categoryTV = findViewById(R.id.categoryTV);
        newsTitleTV = findViewById(R.id.newsTitleTV);
        newsPublishedAtTV = findViewById(R.id.newsPublishedAtTV);
        newsDescriptionTV = findViewById(R.id.newsDescriotionTV);
        resultCountTV = findViewById(R.id.resultCountTV);
        selectButton = findViewById(R.id.selectButton);
        progressBar = findViewById(R.id.progressBar);
        newsImageIV = findViewById(R.id.newsImageIV);
        nextImageIV = findViewById(R.id.next_image);
        prevImageIV = findViewById(R.id.prev_image);

        progressbarVisible = false;
        setVisibilityOfProgressBar();

        nextImageIV.setImageResource(R.drawable.next);
        prevImageIV.setImageResource(R.drawable.prev);


        nextImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected() && articles != null && articles.size() > 1) {
                    if (isConnected()) {
                        clearUI();
                        renderUI(currentSelectedIndex = (currentSelectedIndex + 1) % articles.size());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "news not avaliable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        prevImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (articles != null && articles.size() > 1) {
                    if (isConnected()) {
                        clearUI();
                        if (currentSelectedIndex == 0) {
                            renderUI(currentSelectedIndex = articles.size() - 1);
                        } else {
                            renderUI(currentSelectedIndex = currentSelectedIndex - 1);                        }
                    } else {
                        Toast.makeText(MainActivity.this, "news not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onClickSelect(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Choose a Category");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCategory = categories[which];
                categoryTV.setText(selectedCategory);
                progressbarVisible = true;
                setVisibilityOfProgressBar();
                clearUI();
                RequestParams requestParams = new RequestParams();
                requestParams.addParams("apiKey", API_KEY).addParams("category", selectedCategory);
                if (isConnected()) {
                    new RetrieveNewsAsync(MainActivity.this, requestParams).execute(URL);
                } else {
                    Toast.makeText(MainActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setVisibilityOfProgressBar() {
        if (progressbarVisible == true) {
            progressBar.setVisibility(View.VISIBLE);
            disableButtonClick();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            enableButtonClick();
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


    @Override
    public void handleResponse(List<Article> articles) {
        progressbarVisible = false;
        setVisibilityOfProgressBar();
        if (articles.isEmpty()) {
            Toast.makeText(MainActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
        } else {
            this.articles = articles;
            currentSelectedIndex = 0;
            renderUI(currentSelectedIndex);
        }
    }

    private void clearUI() {
        newsTitleTV.setText("");
        newsPublishedAtTV.setText("");
        newsDescriptionTV.setText("");
        Picasso.get().load("http://jjajd.kjndkj").into(this.newsImageIV);
        resultCountTV.setText("");
    }

    private void renderUI(int index) {
        Article article = this.articles.get(index);
        newsTitleTV.setText((article.getTitle() != null) ? article.getTitle() : "");
        newsPublishedAtTV.setText((article.getPublishedAt() != null) ? article.getPublishedAt() : "");
        newsDescriptionTV.setText((article.getDescription() != null) ? article.getDescription() : "");
        Picasso.get().load((article.getUrlToImage() != null) ? article.getUrlToImage() : DEFAULT_IMAGE_URL).into(this.newsImageIV);
        resultCountTV.setText((index + 1) + " out of " + this.articles.size());
    }

    private void enableButtonClick() {
        nextImageIV.setClickable(true);
        prevImageIV.setClickable(true);
        selectButton.setClickable(true);
    }

    private void disableButtonClick() {
        nextImageIV.setClickable(false);
        prevImageIV.setClickable(false);
        selectButton.setClickable(false);
    }


}
