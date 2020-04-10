package com.example.appx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class MainActivity extends AppCompatActivity {
    private int tarjeta = 0;
    //    private FlipperLayout flipperLayout;
    private CardView card, cardServ, cardTax;
    private FirebaseFirestore db;
    private List<String> imagesSlide = new ArrayList<String>();
    ViewFlipper flipperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        //flipperView = new FlipperView(getBaseContext());
        card = findViewById(R.id.cardFood);
        cardServ = findViewById(R.id.cardServices);
        cardTax = findViewById(R.id.cardTaxi);
        int img[] = {R.drawable.portadapp};
        flipperView = findViewById(R.id.flipper);
        for (int i = 0; i < img.length; i++) {
            setFlipperImage(img[i]);
        }
        flipperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnunciosActivity.class));
            }
        });

        //showAnuncios();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarjeta = 1;
                card.setClickable(false);
                openActivities(tarjeta);
            }
        });
        cardServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarjeta = 2;
                openActivities(tarjeta);
            }
        });
        cardTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarjeta = 3;
                openActivities(tarjeta);
            }
        });
    }

    public void openActivities(Integer valor) {
        String valorTipo;
        valorTipo = valor.toString();
        if(valor == 1){
            //startActivity(new Intent(this, Food.class));
            Intent i = new Intent(this, FondasRes.class);
            i.putExtra("TIPO", valorTipo);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        if (valor == 2) {
            Intent i = new Intent(this, FondasRes.class);
            i.putExtra("TIPO", valorTipo);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        if (valor == 3) {
            Intent i = new Intent(this, FondasRes.class);
            i.putExtra("TIPO", valorTipo);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
    }

    public void showAnuncios() {
        db.collection("anuncios").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            int size = doc.getData().size();
                            String url = doc.getString("img");
                            imagesSlide.add(url);
                            Log.i("TAG", imagesSlide.toString());
                        }
                        int size = imagesSlide.size();
                        //cargaAnuncios(size);
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void setFlipperImage(int res) {
        Log.i("Set Filpper Called", res + "");
        ImageView image = new ImageView(getApplicationContext());
        image.setBackgroundResource(res);
        flipperView.addView(image);
    }

/*
    private void cargaAnuncios(int size) {
        String myArray[] = new String[size];

        for(int i = 0; i < myArray.length; i++){
            myArray[i] = imagesSlide.get(i);
            //FlipperView flipperView = new FlipperView(getBaseContext());
            flipperView.setImageUrl(myArray[i]);
            Picasso.get().load((myArray[i])).into((Target) flipperLayout);
            //flipperLayout.addFlipperView(flipperView);
        }
    }
*/

    @Override
    protected void onResume() {
        card.setClickable(true);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                startActivity(new Intent(this, SettingsActivity.class));
                // do something
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



}
