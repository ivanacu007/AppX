package com.example.appx.imgclases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appx.ActivityDisplay;
import com.example.appx.R;
import com.example.appx.ScrollingActivity;
import com.example.appx.models.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    ScrollingActivity dataActivity;
    List<ImageModel> modelList;
    private static Context context;

    public CustomImageAdapter(ScrollingActivity scrollingActivity, List<ImageModel> modelList) {
        this.dataActivity = scrollingActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_gallery, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(itemView);
        viewHolder.setOnClickListener(new ImageViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //show data in toast
//                String title = modelList.get(position).getName();
//                String id = modelList.get(position).getId();
                String imgUrl = modelList.get(position).getIdImage();
                String pos = String.valueOf(position);
                String aux = dataActivity.aux;
                //Toast.makeText(dataActivity, title+"\n"+id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), ActivityDisplay.class);
                i.putExtra("POS", pos);
                i.putExtra("ID", aux);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                view.getContext().startActivity(i);
                //view.getContext().startActivity(new Intent(view.getContext(), ScrollingActivity.class));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String im = modelList.get(position).getIdImage();
        //holder.txName.setText(modelList.get(position).getName());
        holder.idImg.setText(modelList.get(position).getIdImage());
        if (im != "") {
            Picasso.get().load(modelList.get(position).getIdImage()).into(holder.imgV);
        } else {
            Picasso.get().load(R.drawable.noimg).into(holder.imgV);
            holder.imgV.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
