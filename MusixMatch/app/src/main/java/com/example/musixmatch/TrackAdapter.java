package com.example.musixmatch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private ArrayList<Track> mData;

    TrackAdapter(ArrayList<Track> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = mData.get(position);
        holder.dateTv.setText(track.getDate().equals("null") ? "" : "Date: " + track.getDate());
        holder.albumTv.setText(track.getAlbumName().equals("null") ? "" : "Album: " + track.getAlbumName());
        holder.artistTv.setText(track.getArtistName().equals("null") ? "" : "Artist: " + track.getArtistName());
        holder.trackTv.setText((track.getTrackName().equals("null") ? "" : "Track: " + track.getTrackName()));
        holder.url = track.getUrl();

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trackTv;
        TextView artistTv;
        TextView albumTv;
        TextView dateTv;
        String url;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trackTv = itemView.findViewById(R.id.trackTv);
            artistTv = itemView.findViewById(R.id.artistTv);
            albumTv = itemView.findViewById(R.id.albumTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckInternet.isConnected(context)) {
                        if (!url.equals("")) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            context.startActivity(i);
                        }
                    } else {
                        Toast.makeText(context, "Internet is not connected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
