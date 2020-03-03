package com.example.appx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.appx.models.FoodModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ScrollingActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private TextView txdesc;
    private ImageView imgV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String id = intent.getExtras().getString("ID");
        String aux = "negocios/"+id;
        documentReference  = db.document(aux);
        pd.setTitle("Cargando...");
        pd.show();
        showData(documentReference);
        txdesc = findViewById(R.id.txDesc);
        imgV = findViewById(R.id.imgVS);
        //Toast.makeText(ScrollingActivity.this, "ID: "+id, Toast.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void showData(DocumentReference ref) {
        //String docid = id;
        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //Toast.makeText(ScrollingActivity.this, documentSnapshot.getString("name"), Toast.LENGTH_LONG).show();
                        if(documentSnapshot.exists()){
                            //display attributes
                            String name = documentSnapshot.
                                    getString("name");
                            String imgPath = documentSnapshot.getString("imgL");
                            setActionBarTitle(name, imgPath);
                            Picasso.get().load(documentSnapshot.getString("imgL")).into(imgV);
                            txdesc.setText(documentSnapshot.getString("desc"));
                            pd.dismiss();
                        }else{
                            Toast.makeText(ScrollingActivity.this, "El documento no existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void setActionBarTitle(String title, String img) {
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(title);
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}