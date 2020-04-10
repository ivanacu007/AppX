package com.example.appx.promoclases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appx.R;
import com.example.appx.imgclases.ImageViewHolder;

public class PromoViewHolder extends RecyclerView.ViewHolder {
    TextView idImg;
    ImageView imgV;
    View myView;

    public PromoViewHolder(@NonNull View itemView) {
        super(itemView);
        myView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
//        itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mClickListener.onItemLongClick(v, getAdapterPosition());
//                return true;
//            }
//        });

        //initialize views with model layout
        idImg = itemView.findViewById(R.id.txtidA);
        imgV = itemView.findViewById(R.id.imgVA);
    }

    private PromoViewHolder.ClickListener mClickListener;

    //    //interface for clicklistener
    public interface ClickListener {
        void onItemClick(View view, int position);
        //void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(PromoViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
