package com.example.appx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ActivityDisplay extends AppCompatActivity {
    private ImageView imgDis;
    private ImageButton btnClose;
    private PhotoView imgDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String imgURL = intent.getExtras().getString("URL");
        imgDisp = findViewById(R.id.imgVDis);
        Picasso.get().load(imgURL).into(imgDisp);

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
