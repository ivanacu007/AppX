package com.example.appx.galleryclasses;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appx.R;
import com.github.chrisbanes.photoview.PhotoView;

public class GalleryViewHolder extends RecyclerView.ViewHolder {
    PhotoView imageView;
    //ImageView imageView;
    View myView;

    GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        myView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        imageView = itemView.findViewById(R.id.imageSliderGallery);
    }

    /*    void setImage(SliderItem sliderItem) {
            //Para mostrar imagenes desde url, es aqui !
            Picasso.get().load(sliderItem.getImageUrl()).into(imageView);
            //imageView.setImageResource(sliderItem.getImageUrl());
        }*/
    private GalleryViewHolder.ClickListener mClickListener;

    //private SliderViewHolder.ScrollListener mScrollListener;
    //    //interface for clicklistener
    public interface ClickListener {
        void onItemClick(View view, int position);
        //void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(GalleryViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
