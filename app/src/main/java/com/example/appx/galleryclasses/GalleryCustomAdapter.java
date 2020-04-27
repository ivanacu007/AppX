package com.example.appx.galleryclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appx.R;
import com.example.appx.models.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryCustomAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    private List<ImageModel> sliderItems;
    private ViewPager2 viewPager2;
    //MainActivity dataActivity;
    private Context context;

    public GalleryCustomAdapter(List<ImageModel> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide_item_gallery, parent, false);
        context = parent.getContext();
        GalleryViewHolder sliderViewHolder = new GalleryViewHolder(itemView);
        sliderViewHolder.setOnClickListener(new GalleryViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return sliderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Picasso.get().load(sliderItems.get(position).getIdImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
}
