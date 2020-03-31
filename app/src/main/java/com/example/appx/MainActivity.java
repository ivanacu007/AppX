package com.example.appx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import technolifestyle.com.imageslider.FlipperLayout;

public class MainActivity extends AppCompatActivity {
    private int tarjeta = 0;
    private FlipperLayout flipperLayout;
    private CardView card, cardServ, cardTax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card = findViewById(R.id.cardFood);
        cardServ = findViewById(R.id.cardServices);
        cardTax = findViewById(R.id.cardTaxi);
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
