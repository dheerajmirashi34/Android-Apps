package com.example.newslist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * File Name: NewsAdapter.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<News> mData;

    public NewsAdapter(ArrayList<News> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_news_layout, parent, false);

        NewsAdapter.ViewHolder viewHolder = new NewsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News news = mData.get(position);
        holder.titleTv.setText(news.getTitle());
        holder.authorTv.setText(news.getAuthor());
        holder.dateTv.setText(news.getPublishedAt());
        Picasso.get().load(news.getUrlToImage()).into(holder.newsImage);
        holder.url = news.getUrl();
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTv;
        public TextView authorTv;
        public TextView dateTv;
        public ImageView newsImage;
        public String url;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.newsTitle);
            authorTv = itemView.findViewById(R.id.newsAuthor);
            dateTv = itemView.findViewById(R.id.newsDate);
            newsImage = itemView.findViewById(R.id.newsImage);
            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (url.equals("")) {
                        Toast.makeText(context, "Error: URL Not Present", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (CheckInternet.isConnected(context)) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("url", url);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Internet is not connected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
