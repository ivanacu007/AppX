package com.example.appx.promoclases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appx.AnunciosActivity;
import com.example.appx.R;
import com.example.appx.imgclases.ImageViewHolder;
import com.example.appx.models.AnunciosModel;
import com.example.appx.models.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomPromoAdapter extends RecyclerView.Adapter<PromoViewHolder> {
    AnunciosActivity anunciosActivity;
    List<AnunciosModel> modelList;

    public CustomPromoAdapter(AnunciosActivity anunciosActivity, List<AnunciosModel> modelList) {
        this.anunciosActivity = anunciosActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_an, parent, false);
        PromoViewHolder viewHolder = new PromoViewHolder(itemView);
        viewHolder.setOnClickListener(new PromoViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder holder, int position) {
        String im = modelList.get(position).getImgurl();
        //holder.txName.setText(modelList.get(position).getName());
        holder.idImg.setText(modelList.get(position).getImgurl());
        if (im != "") {
            Picasso.get().load(modelList.get(position).getImgurl()).into(holder.imgV);
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
