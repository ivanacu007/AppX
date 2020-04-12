package com.example.appx.sliderclases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appx.MainActivity;
import com.example.appx.R;
import com.example.appx.models.SliderItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderViewHolder> {
    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    MainActivity dataActivity;
    private Context context;

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide_item_container, parent, false);
        context = parent.getContext();
        SliderViewHolder sliderViewHolder = new SliderViewHolder(itemView);
        sliderViewHolder.setOnClickListener(new SliderViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = sliderItems.get(position).getId();
                int size = sliderItems.size();
                Toast.makeText(context, id, Toast.LENGTH_LONG).show();
            }
        });

        return sliderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Picasso.get().load(sliderItems.get(position).getImageUrl()).into(holder.imageView);
//        holder.setImage(sliderItems.get(position));
        if (position == sliderItems.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

}

