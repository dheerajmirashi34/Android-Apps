package com.example.emailappnew;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Author: Jatin N Gupte, Dheeraj Mirashi
 * Group No: 50
 */
public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {


    private ArrayList<Email> mData;

    public EmailAdapter(ArrayList<Email> mData) {
        this.mData = mData;
    }
    public static final String EMAIL ="EMAIL_INTENT_EXTRAS";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_layout_template, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Email email = mData.get(position);
        holder.subjectTv.setText(email.getSubject());
        holder.dateTv.setText(email.getDate());
        holder.email = email;
    }


    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView subjectTv;
        public TextView dateTv;
        public ImageView deleteIv;
        public Email email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectTv = itemView.findViewById(R.id.subjTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            deleteIv = itemView.findViewById(R.id.deleteIv);

            deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(),CreateAndDisplayActivity.class);
                    i.putExtra(EMAIL,email);
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
