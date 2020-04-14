package com.example.appx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.example.appx.models.SliderItem;
import com.example.appx.sliderclases.SliderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int tarjeta = 0;
    Context context;
    //    private FlipperLayout flipperLayout;
    private CardView card, cardServ, cardTax, cardPromo;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private List<String> imagesSlide = new ArrayList<>();
    private List<SliderItem> modelList = new ArrayList<>();
    private CollectionReference collectionReference;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    String pdString = "Cargando datos...";
    SliderAdapter adapter;
    int x;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("anuncios");
        pd = new ProgressDialog(this);
        //flipperView = new FlipperView(getBaseContext());
        card = findViewById(R.id.cardFood);
        cardServ = findViewById(R.id.cardServices);
        cardTax = findViewById(R.id.cardTaxi);
        viewPager2 = findViewById(R.id.viewPager2);
        cardPromo = findViewById(R.id.cardPromo);
        progressBar = findViewById(R.id.progressBar);
        viewPager2.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                progressBar.setVisibility(View.GONE);
                viewPager2.setVisibility(View.VISIBLE);
            }
        }, 5000);
        //loadingImages();
        getAnuncios(collectionReference);
        showAnuncios();
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

    public void loadingImages() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        viewPager2.setVisibility(View.VISIBLE);
                    }
                },
                5000
        );
    }

    private void getAnuncios(CollectionReference refM) {
        pd.setTitle(pdString);
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        refM.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    //adapter.notifyDataSetChanged();
                    viewPager2.setAdapter(adapter);
                }
                //Toast.makeText(MainActivity.this, String.valueOf(x), Toast.LENGTH_LONG).show();
                //Log.d("TAG", sliderItems.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showAnuncios() {
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
        pd.dismiss();
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

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onStart() {
        refreshData();
        card.setClickable(true);
        sliderHandler.postDelayed(slideRunnable, 3000);
        super.onStart();
    }
    @Override
    protected void onResume() {
        //showAnuncios();
        card.setClickable(true);
        sliderHandler.postDelayed(slideRunnable, 3000);
        super.onResume();
        //refreshData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(slideRunnable);
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

    public void refreshData() {
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error getting data", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    int oldIndex = dc.getOldIndex();
                    int newIndex = dc.getNewIndex();
                    int size = queryDocumentSnapshots.getDocumentChanges().size();
                    switch (dc.getType()) {
                        case ADDED:
                            if (size == 1) {
                                pdString = "Nuevos datos agregados";
                                modelList.clear();
                                getAnuncios(collectionReference);
                                pd.dismiss();
                                Toast.makeText(MainActivity.this, "Documento agregado", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case MODIFIED:
                            pdString = "Actualizando datos...";
                            modelList.clear();
                            if (oldIndex == newIndex) {
                                getAnuncios(collectionReference);
                                pd.dismiss();
                            }
                            Toast.makeText(MainActivity.this, "Documentos actualizados", Toast.LENGTH_SHORT).show();
                            break;
                        case REMOVED:
                            Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }
}
