package com.example.appx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    CollectionReference collectionRef;
    String pdString = "Cargando datos...";
    int tipoD, numberOfColumns = 2;

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
        collectionRef = db.collection("negocios");
        mRecyclerView = findViewById(R.id.recV);
        mRecyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation ==
                this.getResources().getConfiguration()
                        .ORIENTATION_LANDSCAPE) {
            numberOfColumns = 3;
        }
        if (tipoD == 3) {
            gridLayoutManager = new GridLayoutManager(this, 1);
        } else {
            gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        }
        mRecyclerView.setLayoutManager(gridLayoutManager);

        pd = new ProgressDialog(this);

        //modelList.clear();
        changeTitle();
        getData(collectionRef);
    }

    private void getData(CollectionReference refColl) {
        mRecyclerView.removeAllViews();
        pd.setTitle(pdString);
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        refColl.orderBy("name").get()
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
                                adapter.notifyDataSetChanged();
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

    @Override
    protected void onStart() {
        refreshData();
        super.onStart();
    }

    @Override
    protected void onResume() {
        //showAnuncios();
        super.onResume();
        //refreshData();
    }

    public void refreshData() {
        collectionRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(FondasRes.this, "Error getting data", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    int oldIndex = dc.getOldIndex();
                    int newIndex = dc.getNewIndex();
                    int size = queryDocumentSnapshots.getDocumentChanges().size();
                    switch (dc.getType()) {
                        case ADDED:
                            if (size == 1) {
                                modelList.clear();
                                pdString = "Nuevos datos agregados";
                                getData(collectionRef);
                                pd.dismiss();
                                Toast.makeText(FondasRes.this, "Documento agregado", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case MODIFIED:
                            pdString = "Actualizando datos...";
                            modelList.clear();
                            if (oldIndex == newIndex) {
                                getData(collectionRef);
                                pd.dismiss();
                            }
                            Toast.makeText(FondasRes.this, "Documentos actualizados", Toast.LENGTH_SHORT).show();
                            break;
                        case REMOVED:
                            Toast.makeText(FondasRes.this, "Removed", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }
}
