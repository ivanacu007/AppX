package com.example.appx;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView idS, txName;
    ImageView imgV;
    View myView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        myView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        //initialize views with model layout
        txName = itemView.findViewById(R.id.txtF);
        imgV = itemView.findViewById(R.id.imgV);
        idS = itemView.findViewById(R.id.idS);
    }
    private ViewHolder.ClickListener mClickListener;
    //interface for clicklistener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}