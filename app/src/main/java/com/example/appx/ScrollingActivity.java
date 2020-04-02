package com.example.appx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;
import android.net.Uri;
import android.os.Bundle;

import com.example.appx.imgclases.CustomImageAdapter;
import com.example.appx.models.FoodModel;
import com.example.appx.models.ImageModel;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScrollingActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private FirebaseFirestore db;
    private DocumentReference documentReference, documentReferenceM;
    private CollectionReference collectionReference, imgCollectionReference;
    private TextView txdesc, tvtitle;
    private ImageView imgV;
    private FloatingActionButton fMsg, fCall;
    private LinearLayout linearLayout;
    List<ImageModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    CustomImageAdapter adapter;
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
        //String aux2 = "negocios/"+id;
        documentReference  = db.document(aux);
        collectionReference = db.document(aux).collection("Menu");
        imgCollectionReference = db.document(aux).collection("Imagenes");
        pd.setTitle(R.string.pdload);
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        showData(documentReference, collectionReference, imgCollectionReference);
        txdesc = findViewById(R.id.txDesc);
        imgV = findViewById(R.id.imgVS);
        fMsg = findViewById(R.id.floatMsg);
        fCall = findViewById(R.id.floatCall);
        tvtitle = findViewById(R.id.txTitle2);
        linearLayout = findViewById(R.id.linearMenu);
        mRecyclerView = findViewById(R.id.recVIMG);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        fCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:4435981907"));
                startActivity(intent);
            }
        });

        fMsg.setOnClickListener(new View.OnClickListener() {
            String number = "4435981907";
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + number);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
            }
        });
    }

    private void showData(DocumentReference ref, CollectionReference refM, CollectionReference refImg) {
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
                            //String location = documentSnapshot.getString("direc");
                            setActionBarTitle(name);
                            Picasso.get().load(documentSnapshot.getString("imgL")).into(imgV);
                            txdesc.setText(documentSnapshot.getString("desc"));
                            //txdir.setText(documentSnapshot.getString("direc"));
//                            lat = Double.parseDouble(documentSnapshot.getString("lat"));
//                            lon = Double.parseDouble(documentSnapshot.getString("lon"));
                            //map = "http://maps.google.com/maps?q=loc:" + location;
//                            onMapReady(map, lat, lon);
                            int tipoD = documentSnapshot.getLong("tipo").intValue();
                            txTitle(tipoD);
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

        refM.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            String id = doc.getId();
                            String index;
                            int size = doc.getData().size();
                            for (int i = 1; i <= size; i++) {
                                index = String.valueOf(i);
                                String dbtext = doc.getData().get(index).toString();
                                TextView textView = new TextView(ScrollingActivity.this);
                                textView.setText(dbtext);
                                textView.setTextSize(16);
                                linearLayout.addView(textView);
                                Log.d("TAG", dbtext);
                            }
                            //Log.d("TAG", String.valueOf(size));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        refImg.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            ImageModel model = new ImageModel(
                                    doc.getString("img"));

                            modelList.add(model);
                            adapter = new CustomImageAdapter(ScrollingActivity.this, modelList);
                            mRecyclerView.setAdapter(adapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void txTitle(int tipo) {
        if (tipo == 1) {
            tvtitle.setText("Menu");
        }
        if (tipo == 2) {
            tvtitle.setText("Nuestros servicios");
        }
        if (tipo == 3) {
            tvtitle.setText("Ofrecemos");
        }
    }

    public void setActionBarTitle(String title) {
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(title);
        //toolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        toolbarLayout.setExpandedTitleMarginStart(30);
        toolbarLayout.setExpandedTitleMarginBottom(30);
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}