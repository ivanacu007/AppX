package com.example.appx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class PersonShop extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference collectionReference, contactReference;
    private ImageView imgMandado;
    private TextView txMandado;
    private String imgUrl, txt, number, number2, numberWhats;
    private LinearLayout linearLayout;
    private CardView btnCall, btnMsg;
    private CardView cvM;
    Context context;
    private final int REQUEST_PHONE_CALL = 1;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_shop);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("mandado");
        contactReference = db.collection("contacto");
        imgMandado = findViewById(R.id.imgMan);
        txMandado = findViewById(R.id.txvMandado);
        linearLayout = findViewById(R.id.linearMandado);
        btnCall = findViewById(R.id.btnManCall);
        btnMsg = findViewById(R.id.btnManMsj);
        cvM = findViewById(R.id.cardImgManda);
        getData();
        getContactData(contactReference);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer();
            }
        });

        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });


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

    public void getData() {
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            imgUrl = doc.getString("img");
                            txt = doc.getString("desc");
                            if (imgUrl != "") {
                                Picasso.get().load(imgUrl).into(imgMandado);
                            } else {
                                Picasso.get().load(R.drawable.noimg).into(imgMandado);
                            }
                            TextView textView = new TextView(PersonShop.this);
                            textView.setText(txt);
                            textView.setTextSize(16);
                            params.setMargins(0, 20, 0, 0);
                            textView.setLayoutParams(params);
                            linearLayout.addView(textView);
                            //txMandado.setText(txt);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
        PackageManager pm = PersonShop.this.getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
        if (isInstalled) {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + numberWhats;
            sendIntent.setData(Uri.parse(url));
            if (sendIntent.resolveActivity(PersonShop.this.getPackageManager()) != null) {
                startActivity(sendIntent);
            }
        } else {
            Toast.makeText(PersonShop.this, "No se encontró Whatsapp en tu dispositivo, contactanos por llamada.", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

/*    public void showCallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonShop.this);
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
    }*/

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
