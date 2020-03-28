package com.example.appx;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appx.models.FoodModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    FondasRes dataActivity;
    List<FoodModel> modelList;
    Context context;
    private FondasRes objs = new FondasRes();
    public boolean isClickable = true;

    public CustomAdapter(FondasRes fondasRes, List<FoodModel> modelList) {
        this.dataActivity = fondasRes;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_layout, parent, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //show data in toast

                String title = modelList.get(position).getName();
                String id = modelList.get(position).getId();
                Intent i = new Intent(view.getContext(), ScrollingActivity.class);
                i.putExtra("ID", id);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                view.getContext().startActivity(i);

                //objs.mRecyclerView.setEnabled(false);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Bind views / set data
        String im = modelList.get(position).getImg();
        holder.txName.setText(modelList.get(position).getName());
        holder.idS.setText(modelList.get(position).getId());
        if(im != ""){
            Picasso.get().load(modelList.get(position).getImg()).into(holder.imgV);
        }else{
            Picasso.get().load(R.drawable.noimg).into(holder.imgV);
            holder.imgV.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}