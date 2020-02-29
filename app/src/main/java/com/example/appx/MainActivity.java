package com.example.appx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import technolifestyle.com.imageslider.FlipperLayout;

public class MainActivity extends AppCompatActivity {
    private int tarjeta = 0;
    private FlipperLayout flipperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView card = findViewById(R.id.cardFood);
        CardView cardServ = findViewById(R.id.cardServices);
        CardView cardTax = findViewById(R.id.cardTaxi);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarjeta = 1;
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


//        Intent intent2 = new Intent(this, ServicesMenu.class);
//        Intent intent3 = new Intent(this, CarriageMenu.class);

        if(valor == 1){
            startActivity(new Intent(this, Food.class));
            finish();
        }
//        if(valor == 2){
//            startActivity(intent2);
//        }
//        if(valor == 3){
//            startActivity(intent3);
//        }

    }
}
