package com.example.appx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appx.models.AnunciosModel;
import com.example.appx.models.FoodModel;
import com.example.appx.promoclases.CustomPromoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AnunciosActivity extends AppCompatActivity {
    List<AnunciosModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;
    CustomPromoAdapter adapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(R.string.anuncios);
        db = FirebaseFirestore.getInstance();
        mRecyclerView = findViewById(R.id.recyA);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        pd = new ProgressDialog(this);
        showA();
    }

    private void showA() {
        pd.setTitle("Cargando datos...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        db.collection("anuncios").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            AnunciosModel anunciosModel = new AnunciosModel(
                                    doc.getString("name"),
                                    doc.getString("desc"),
                                    doc.getString("img"),
                                    doc.getString("id")
                            );
                            modelList.add(anunciosModel);
                            adapter = new CustomPromoAdapter(AnunciosActivity.this, modelList);
                            mRecyclerView.setAdapter(adapter);
                        }
                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(AnunciosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
