package com.example.photoalbum;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @Author: Jatin Gupte, Dheeraj Mirashi
 * @Group No: 50
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private static ArrayList<String> mData;

    public ImageAdapter(ArrayList<String> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String url = mData.get(position);
        Picasso.get().load(url).into(holder.itemIV);
        holder.itemIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                String imageUrlTempValue = mData.get(position);
                int indexStart = imageUrlTempValue.indexOf("%");
                int indexEnd = imageUrlTempValue.indexOf("png");

                String path = "images/"+ imageUrlTempValue.substring(indexStart+3,indexEnd) +"png";
                Log.d("demo",path);
                assert path != null;
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageRef = firebaseStorage.getReference(path);
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "ERROR " +e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                MainActivity.urlList.remove(position);
                MainActivity.mAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemIV = itemView.findViewById(R.id.itemImageView);
        }
    }
}
