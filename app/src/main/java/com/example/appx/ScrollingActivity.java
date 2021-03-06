package com.example.appx;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ScrollView;
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
    public CollectionReference collectionReference, imgCollectionReference, contactReference;
    private TextView txdesc, tvtitle;
    private ImageView imgV;
    private FloatingActionButton fMsg, fCall;
    private LinearLayout linearLayout;
    private String number, number2;
    private String wMessage = "Mensaje de prueba perro";
    //private Context context;
    public List<ImageModel> modelList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CustomImageAdapter adapter;
    private String pdString = "Cargando datos...";
    private String numberWhats;
    public String aux;
    ImageModel model;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
        aux = "negocios/" + id;
        //String aux2 = "negocios/"+id;
        documentReference = db.document(aux);
        collectionReference = db.document(aux).collection("Menu");
        imgCollectionReference = db.document(aux).collection("Imagenes");
        contactReference = db.collection("contacto");
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

        getContactData(contactReference);
        getData(documentReference);
        getMenuData(collectionReference);
        getImgData(imgCollectionReference);

        fCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer();
            }
        });

        fMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });
    }

    private void openDialer() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void openWhatsApp() {
        numberWhats = "+521" + number;
        PackageManager pm = ScrollingActivity.this.getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
        if (isInstalled) {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + numberWhats;
            sendIntent.setData(Uri.parse(url));
            if (sendIntent.resolveActivity(ScrollingActivity.this.getPackageManager()) != null) {
                startActivity(sendIntent);
            }
        } else {
            Toast.makeText(ScrollingActivity.this, "No se encontró Whatsapp en tu dispositivo, contactanos por llamada.", Toast.LENGTH_LONG).show();
        }
    }

    private void getContactData(CollectionReference refCon) {
        refCon.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            number = doc.getString("numuno");
                            number2 = doc.getString("numdos");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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
                                params.setMargins(0, 0, 0, 10);
                                textView.setLayoutParams(params);
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
                            model = new ImageModel(
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

    public void showCallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScrollingActivity.this);
        builder.setCancelable(false);
        builder.setTitle(this.getString(R.string.optionCallDialog));
        builder.setPositiveButton(this.getString(R.string.confirmCall), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openDialer();
            }
        });
        builder.setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
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