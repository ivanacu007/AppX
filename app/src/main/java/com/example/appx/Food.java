package com.example.appx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Food extends AppCompatActivity {
    private int tarjeta = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView cvFonda = findViewById(R.id.cVm1);
        cvFonda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarjeta = 1;
                openAct(tarjeta);
            }
        });
    }

    public void openAct(Integer op){
        if (op == 1){
            startActivity(new Intent(this, FondasRes.class));
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
