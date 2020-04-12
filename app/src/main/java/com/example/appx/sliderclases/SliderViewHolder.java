package com.example.appx.sliderclases;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appx.R;
import com.example.appx.models.SliderItem;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

class SliderViewHolder extends RecyclerView.ViewHolder {
    RoundedImageView imageView;
    //ImageView imageView;
    View myView;

    SliderViewHolder(@NonNull View itemView) {
        super(itemView);
        myView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        imageView = itemView.findViewById(R.id.imageSlider);
    }

    /*    void setImage(SliderItem sliderItem) {
            //Para mostrar imagenes desde url, es aqui !
            Picasso.get().load(sliderItem.getImageUrl()).into(imageView);
            //imageView.setImageResource(sliderItem.getImageUrl());
        }*/
    private SliderViewHolder.ClickListener mClickListener;

    //    //interface for clicklistener
    public interface ClickListener {
        void onItemClick(View view, int position);
        //void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(SliderViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
