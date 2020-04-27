package com.example.appx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;

import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appx.galleryclasses.GalleryCustomAdapter;
import com.example.appx.imgclases.CustomImageAdapter;
import com.example.appx.models.ImageModel;
import com.example.appx.models.SliderItem;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityDisplay extends AppCompatActivity {
    private ImageView imgDis;
    private ImageButton btnClose;
    private PhotoView imgDisp;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    private ScrollingActivity dataAct = new ScrollingActivity();
    private List<ImageModel> sliderItems = new ArrayList<>();
    private List<SliderItem> modelList = new ArrayList<>();
    private TextView txv;
    private FirebaseFirestore db;
    private int pos;
    private CollectionReference imgCollectionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        txv = findViewById(R.id.txcount);
        viewPager2 = findViewById(R.id.viewPagerG);
        Intent intent = getIntent();
        final String data = intent.getExtras().getString("POS");
        final String id = intent.getExtras().getString("ID");
        imgCollectionReference = db.document(id).collection("Imagenes");
        pos = Integer.parseInt(data);
        getImgData(imgCollectionReference);
    }

    public void getImgData(CollectionReference ref) {
        ref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            String path = doc.getString("img");
                            ImageModel model = new ImageModel(
                                    doc.getString("img"));
                            sliderItems.add(model);
                            viewPager2.setAdapter(new GalleryCustomAdapter(sliderItems, viewPager2));
                        }
                        viewPager2.setClipToPadding(false);
                        viewPager2.setClipChildren(false);
                        //viewPager2.setOffscreenPageLimit(3);
                        viewPager2.setCurrentItem(pos);
                        //viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                txv.setText(position + 1 + " de " + sliderItems.size());
                                //Toast.makeText(GallerySlide.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityDisplay.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
