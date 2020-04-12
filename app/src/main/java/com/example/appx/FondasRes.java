package com.example.appx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appx.models.FoodModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FondasRes extends AppCompatActivity {
    List<FoodModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;
    CustomAdapter adapter;
    ProgressDialog pd;
    GridLayoutManager gridLayoutManager;
    int tipoD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fondas_res);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final String typeB = intent.getExtras().getString("TIPO");
        tipoD = Integer.parseInt(typeB);
        db = FirebaseFirestore.getInstance();
        mRecyclerView = findViewById(R.id.recV);
        mRecyclerView.setHasFixedSize(true);
        if (tipoD == 3) {
            gridLayoutManager = new GridLayoutManager(this, 1);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 2);
        }
        mRecyclerView.setLayoutManager(gridLayoutManager);

        pd = new ProgressDialog(this);

        changeTitle();
        showData();
    }

    private void showData() {
        pd.setTitle("Cargando datos...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        db.collection("negocios").orderBy("name").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            //Toast.makeText(FondasRes.this, String.valueOf(doc.getData().size()), Toast.LENGTH_LONG).show();
                            int status = doc.getLong("estatus").intValue();
                            int tipo = doc.getLong("tipo").intValue();
                            String id = doc.getId();
                            //int typeB = Integer.parseInt(type);
                            if (status > 0 && tipo == tipoD) {
                                FoodModel model = new FoodModel(
                                        doc.getString("id"),
                                        doc.getString("name"),
                                        doc.getString("desc"),
                                        doc.getString("img"),
                                        doc.getLong("estatus").intValue());
                                //txID.setText(id);
                                modelList.add(model);
                                adapter = new CustomAdapter(FondasRes.this, modelList);
                                mRecyclerView.setAdapter(adapter);
                            } /*else {
                                Toast.makeText(FondasRes.this, "No se encontraron datos disponibles", Toast.LENGTH_SHORT).show();
                            }*/
                        }

                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(FondasRes.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void changeTitle() {
        if (tipoD == 1) {
            this.setTitle(R.string.tipo1);
        }
        if (tipoD == 2) {
            this.setTitle(R.string.tipo2);
        }
        if (tipoD == 3) {
            this.setTitle(R.string.tipo3);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
