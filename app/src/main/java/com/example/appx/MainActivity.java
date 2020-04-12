package com.example.appx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.appx.models.FoodModel;
import com.example.appx.models.SliderItem;
import com.example.appx.sliderclases.SliderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int tarjeta = 0;
    //    private FlipperLayout flipperLayout;
    private CardView card, cardServ, cardTax, cardPromo;
    private FirebaseFirestore db;
    private List<String> imagesSlide = new ArrayList<>();
    private List<SliderItem> modelList = new ArrayList<>();
    //List<FoodModel> modelList = new ArrayList<>();
    ViewFlipper flipperView;
    SliderView sliderView;
    //private SliderCustomAdapter adapter;
    //SliderItem sliderItem = new SliderItem();
    SliderAdapter adapter;
    int x;

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();


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
        viewPager2 = findViewById(R.id.viewPager2);
        cardPromo = findViewById(R.id.cardPromo);




        /*int img[] = {R.drawable.portadapp};
        flipperView = findViewById(R.id.flipper);
        for (int i = 0; i < img.length; i++) {
            setFlipperImage(img[i]);
        }
        flipperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnunciosActivity.class));
            }
        });*/
        //sliderView = findViewById(R.id.imageSlider);
        showAnuncios();
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

    private void showAnuncios() {
        db.collection("anuncios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    SliderItem model = new SliderItem(
                            doc.getString("id"),
                            doc.getString("name"),
                            doc.getString("desc"),
                            doc.getString("img")
                    );
                    modelList.add(model);
                    adapter = new SliderAdapter(modelList, viewPager2);
                    viewPager2.setAdapter(adapter);
    /*                 x = doc.getData().size();
                    SliderItem sliderItem = new SliderItem();
                    sliderItem.setImageUrl(doc.getString("img"));
                    sliderItem.setId(doc.getString("id"));
                    sliderItem.setDescription(doc.getString("desc"));
                    sliderItem.setName(doc.getString("name"));
                    sliderItems.add(sliderItem);

                    //adapter.addItem(sliderItem);
                    adapter = new SliderAdapter(sliderItems, viewPager2);
                    viewPager2.setAdapter(adapter);
*/
                }
                Toast.makeText(MainActivity.this, String.valueOf(x), Toast.LENGTH_LONG).show();
                //Log.d("TAG", sliderItems.toString());
                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);
                viewPager2.setOffscreenPageLimit(3);
                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(20));
                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.85f + r * 0.15f);
                    }
                });
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        sliderHandler.removeCallbacks(slideRunnable);
                        sliderHandler.postDelayed(slideRunnable, 3000); //duracion del slide 3 sec
                    }
                });
                viewPager2.setPageTransformer(compositePageTransformer);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


//        for (int i = 0; i < 3; i++) {
//            sliderItem.setDescription("Slider Item Added Manually");
//            sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//            adapter.addItem(sliderItem);
//        }
    }

    public void openActivities(Integer valor) {
        String valorTipo;
        valorTipo = valor.toString();
        if (valor == 1) {
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
        sliderHandler.postDelayed(slideRunnable, 3000);
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

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(slideRunnable);
    }

    public void cardPromoClick(View view) {
        startActivity(new Intent(MainActivity.this, AnunciosActivity.class));
    }
}
