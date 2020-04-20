package com.example.appx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private CollectionReference collectionReference;
    private ImageView imageView;
    private TextView txDesc;
    private LinearLayout linearLayout;
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
        imageView = findViewById(R.id.contactImg);
        txDesc = findViewById(R.id.txvContactInfo);
        linearLayout = findViewById(R.id.linearcontact);

        getData(collectionReference);
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
