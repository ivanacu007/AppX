package com.example.appx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        String imgURL = intent.getExtras().getString("URL");
        btnClose = findViewById(R.id.btnClose);
        //imgDis = findViewById(R.id.imgVDis);
        imgDisp = findViewById(R.id.imgVDis);
        Picasso.get().load(imgURL).into(imgDisp);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
