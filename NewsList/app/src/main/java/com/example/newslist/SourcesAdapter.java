package com.example.newslist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * File Name: SourcesAdapter.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private ArrayList<Source> mData;

    public SourcesAdapter(ArrayList<Source> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sources_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Source source = mData.get(position);
        holder.nameTv.setText(source.getName());
        holder.id = source.getId();
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsImage;
        public TextView nameTv;
        public String id;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(id.equals("")){
                        Toast.makeText(context, "Error: ID Not Present", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (CheckInternet.isConnected(context)) {
                        Intent intent = new Intent("com.example.newslist.intent.action.VIEW");
                        intent.putExtra("id", id);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Internet is not connected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
