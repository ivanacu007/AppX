package com.example.appx.promoclases;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appx.AnunciosActivity;
import com.example.appx.R;
import com.example.appx.ScrollingActivity;
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
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_an, parent, false);
        final PromoViewHolder viewHolder = new PromoViewHolder(itemView);
        viewHolder.setOnClickListener(new PromoViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = modelList.get(position).getName();
                String sms = modelList.get(position).getSms();
                String smstext = modelList.get(position).getDesc();
                openWhatsApp(itemView, sms, smstext);
                Toast.makeText(anunciosActivity, name, Toast.LENGTH_SHORT).show();
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

    private void openWhatsApp(View view, String smsto, String smstext) {
        PackageManager pm = view.getContext().getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
        if (isInstalled) {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + smsto + "&text=" + smstext;
            sendIntent.setData(Uri.parse(url));
            if (sendIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
                view.getContext().startActivity(sendIntent);
            }
        } else {
            Toast.makeText(view.getContext(), "No se encontró Whatsapp en tu dispositivo, contactanos por llamada.", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
