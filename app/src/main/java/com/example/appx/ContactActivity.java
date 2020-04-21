package com.example.appx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ContactActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference collectionReference, contactReference;
    private ImageView imageView;
    private TextView txDesc;
    private LinearLayout linearLayout;
    private String number, number2, numberWhats, smstext;
    private final int REQUEST_PHONE_CALL = 1;
    private Button btnCall, btnMessage;
    Context context;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("contacto");
        contactReference = db.collection("contacto");
        imageView = findViewById(R.id.contactImg);
        txDesc = findViewById(R.id.txvContactInfo);
        linearLayout = findViewById(R.id.linearcontact);
        btnCall = findViewById(R.id.btnConCall);
        btnMessage = findViewById(R.id.btnConMsj);

        getContactData(contactReference);
        getData(collectionReference);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCallDialog();
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getData(CollectionReference refData) {
        refData.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (doc.getString("coverimg").equals("")) {
                                Picasso.get().load(R.drawable.noimg).into(imageView);
                            } else {
                                Picasso.get().load(doc.getString("coverimg")).into(imageView);
                            }
                            //txDesc.setText(doc.getString("desc"));
                            TextView textView = new TextView(ContactActivity.this);
                            textView.setText(doc.getString("desc"));
                            textView.setTextSize(16);
                            params.setMargins(0, 10, 0, 0);
                            textView.setLayoutParams(params);
                            linearLayout.addView(textView);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
                            smstext = doc.getString("sms");
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
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            return;
        }
        startActivity(intent);
    }

    private void openWhatsApp() {
        numberWhats = "+521" + number;
        PackageManager pm = ContactActivity.this.getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
        if (isInstalled) {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + numberWhats + "&text=" + smstext;
            sendIntent.setData(Uri.parse(url));
            if (sendIntent.resolveActivity(ContactActivity.this.getPackageManager()) != null) {
                startActivity(sendIntent);
            }
        } else {
            Toast.makeText(ContactActivity.this, "No se encontró Whatsapp en tu dispositivo, contactanos por llamada.", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
