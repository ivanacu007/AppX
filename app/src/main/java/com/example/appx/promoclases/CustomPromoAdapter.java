package com.example.appx.promoclases;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.appx.ContactActivity;
import com.example.appx.R;
import com.example.appx.ScrollingActivity;
import com.example.appx.imgclases.ImageViewHolder;
import com.example.appx.models.AnunciosModel;
import com.example.appx.models.ImageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomPromoAdapter extends RecyclerView.Adapter<PromoViewHolder> {
    AnunciosActivity anunciosActivity;
    List<AnunciosModel> modelList;
    private Context context;
    private String id;
    private CollectionReference contactReference;
    private FirebaseFirestore db;
    private String number, number2, smstext, numberWhats;

    public CustomPromoAdapter(AnunciosActivity anunciosActivity, List<AnunciosModel> modelList) {
        this.anunciosActivity = anunciosActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_an, parent, false);
        context = parent.getContext();
        db = FirebaseFirestore.getInstance();
        contactReference = db.collection("contacto");
        getContactData(contactReference);
        final PromoViewHolder viewHolder = new PromoViewHolder(itemView);
        viewHolder.setOnClickListener(new PromoViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                id = modelList.get(position).getId();
                String aux = "1234";
                String name = modelList.get(position).getName();
                //String sms = modelList.get(position).getSms();
                smstext = modelList.get(position).getDesc();
                if (id.equals(aux)) {
                    context.startActivity(new Intent(context, ContactActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    //Toast.makeText(anunciosActivity, "AQUI VOY A MANDAR A OTRA COSA PARA QUE NOS CONTACTEN", Toast.LENGTH_LONG).show();
                } else {
                    showDialog(id);
                    //openWhatsApp(itemView, sms, smstext);
                }
                //Toast.makeText(anunciosActivity, String.valueOf(id), Toast.LENGTH_SHORT).show();
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

    private void openDialer() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    private void openWhatsApp(Context context, String smsto, String smstext) {
        numberWhats = "+521" + smsto;
        PackageManager pm = context.getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
        if (isInstalled) {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + numberWhats + "&text=" + smstext;
            sendIntent.setData(Uri.parse(url));
            if (sendIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(sendIntent);
            }
        } else {
            Toast.makeText(context, "No se encontró Whatsapp en tu dispositivo, contactanos por llamada.", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void showDialog(final String id) {
        CharSequence options[] = new CharSequence[]{"¡Pedir ahora por llamada!", "¡Pedir ahora por mensaje!", "Ver menú completo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(anunciosActivity);
        builder.setCancelable(false);
        builder.setTitle(anunciosActivity.getString(R.string.optiondialog));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                switch (which) {
                    case 0:
                        openDialer();
                        //Toast.makeText(anunciosActivity, "Picaste llamar", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        openWhatsApp(context, number, smstext);
                        //Toast.makeText(anunciosActivity, "Picaste mensaje", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Intent i = new Intent(context, ScrollingActivity.class);
                        i.putExtra("ID", id);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(i);
                }
            }
        });
        builder.setNegativeButton(anunciosActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
            }
        });
        builder.show();
    }

    private void getContactData(CollectionReference refCon) {
        refCon.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            number = doc.getString("numuno");
                            number2 = doc.getString("numdos");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error al obtener datos de contacto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
