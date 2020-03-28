package com.example.appx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
