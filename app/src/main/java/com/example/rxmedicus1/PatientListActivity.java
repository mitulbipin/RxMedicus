package com.example.rxmedicus1;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PatientListActivity extends AppCompatActivity {
private TextView Pname1,Pname2,PatientAge1,PatientAge2,Pnum1,Pnum2,Pbgroup1,Pbgroup2;
private TextView InsuranceStatus1,InsuranceStatus2;
private ImageView InsuranceTick1,InsuranceTick2;
private ImageView Patient1,Patient2;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference,databaseReference2;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference,storageReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        variables();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference().child("8tXTeh0woIgGLiZxZYEzAfbdqvJ3");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pname1.setText(dataSnapshot.child("username").getValue().toString());
                PatientAge1.setText(dataSnapshot.child("age").getValue().toString() +"Years");
                Pnum1.setText(dataSnapshot.child("number").getValue().toString());
                Pbgroup1.setText(dataSnapshot.child("bloodgroup").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PatientListActivity.this, "Err", Toast.LENGTH_SHORT).show();
            }
        });

        storageReference = firebaseStorage.getReference("8tXTeh0woIgGLiZxZYEzAfbdqvJ3");
        storageReference.child("Images").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(Patient1);
                    }
                });

        storageReference.child("Insurance").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        InsuranceStatus1.setText("Insurance Uploaded");
                        InsuranceTick1.setImageResource(R.drawable.insurance_uploaded);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                InsuranceStatus1.setText("Insurance Not Uploaded");
                InsuranceTick1.setImageResource(R.drawable.insurance_not_uploaded);
            }
        });

        databaseReference2 = firebaseDatabase.getReference().child("Jsgbe0nUhfPdAqvKc1Hh68agHPQ2");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pname2.setText(dataSnapshot.child("username").getValue().toString());
                PatientAge2.setText(dataSnapshot.child("age").getValue().toString() +"Years");
                Pnum2.setText(dataSnapshot.child("number").getValue().toString());
                Pbgroup2.setText(dataSnapshot.child("bloodgroup").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PatientListActivity.this, "Err", Toast.LENGTH_SHORT).show();
            }
        });

        storageReference2 = firebaseStorage.getReference("Jsgbe0nUhfPdAqvKc1Hh68agHPQ2");
        storageReference2.child("Images").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(Patient2);
                    }
                });
        storageReference2.child("Insurance").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                InsuranceStatus2.setText("Insurance Uploaded");
                InsuranceTick2.setImageResource(R.drawable.insurance_uploaded);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                InsuranceStatus2.setText("Insurance Not Uploaded");
                InsuranceTick2.setImageResource(R.drawable.insurance_not_uploaded);
            }
        });
    }
    private void variables(){
        Pname1 = findViewById(R.id.textView25);
        Pname2 = findViewById(R.id.textView19);
        PatientAge1 = findViewById(R.id.textView16);
        PatientAge2 = findViewById(R.id.textView20);
        Pnum1 = findViewById(R.id.textView17);
        Pnum2 = findViewById(R.id.textView21);
        Pbgroup1 = findViewById(R.id.textView23);
        Pbgroup2 = findViewById(R.id.textView24);
        InsuranceStatus1 = findViewById(R.id.textView28);
        InsuranceStatus2 = findViewById(R.id.textView27);
        Patient1 = findViewById(R.id.imageView3);
        Patient2 = findViewById(R.id.imageView7);
        InsuranceTick1 = findViewById(R.id.imageView8);
        InsuranceTick2 = findViewById(R.id.imageView9);
    }

}
