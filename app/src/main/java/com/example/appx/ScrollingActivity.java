package com.example.appx;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.PhoneNumberUtils;
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

import io.opencensus.internal.Utils;

public class ScrollingActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private FirebaseFirestore db;
    private DocumentReference documentReference, documentReferenceM;
    private CollectionReference collectionReference, imgCollectionReference;
    private TextView txdesc, tvtitle;
    private ImageView imgV;
    private FloatingActionButton fMsg, fCall;
    private LinearLayout linearLayout;
    private String number = "+5214435981907";
    private String wMessage = "Mensaje de prueba perro";
    //private Context context;
    private List<ImageModel> modelList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CustomImageAdapter adapter;
    private String pdString = "Cargando datos...";

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
        String aux = "negocios/" + id;
        //String aux2 = "negocios/"+id;
        documentReference = db.document(aux);
        collectionReference = db.document(aux).collection("Menu");
        imgCollectionReference = db.document(aux).collection("Imagenes");
        pd.setTitle(R.string.pdload);
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
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

        getData(documentReference);
        getMenuData(collectionReference);
        getImgData(imgCollectionReference);

        fCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(number));
                startActivity(intent);
            }
        });

        fMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });
    }

    private void openWhatsApp() {
        PackageManager pm = ScrollingActivity.this.getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
        if (isInstalled) {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + wMessage;
            sendIntent.setData(Uri.parse(url));
            if (sendIntent.resolveActivity(ScrollingActivity.this.getPackageManager()) != null) {
                startActivity(sendIntent);
            }
        } else {
            Toast.makeText(ScrollingActivity.this, "No se encontró Whatsapp en tu dispositivo, contactanos por llamada.", Toast.LENGTH_LONG).show();
        }
    }

    private void getData(DocumentReference ref) {
        //String docid = id;
        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //Toast.makeText(ScrollingActivity.this, documentSnapshot.getString("name"), Toast.LENGTH_LONG).show();
                        if (documentSnapshot.exists()) {
                            //display attributes
                            String name = documentSnapshot.
                                    getString("name");
                            setActionBarTitle(name);
                            Picasso.get().load(documentSnapshot.getString("imgL")).into(imgV);
                            txdesc.setText(documentSnapshot.getString("desc"));
                            int tipoD = documentSnapshot.getLong("tipo").intValue();
                            txTitle(tipoD);
                            pd.dismiss();
                        } else {
                            Toast.makeText(ScrollingActivity.this, "El documento no existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScrollingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getMenuData(CollectionReference refM) {
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
                        Toast.makeText(ScrollingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getImgData(CollectionReference refImg) {
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
                        Toast.makeText(ScrollingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void setActionBarTitle(String title) {
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(title);
        //toolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        toolbarLayout.setExpandedTitleMarginStart(30);
        toolbarLayout.setExpandedTitleMarginBottom(30);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //refreshMenuData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}