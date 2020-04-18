package com.example.rxmedicus1;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DoctorListActivity extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference,databaseReference2;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference,storageReference2;
    private TextView Name,Name2,Specialisation,Specialisation2;
    private ImageView imgDoc1,imgDoc2;
    private Button ProfileBtn,ProfileBtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        Name = findViewById(R.id.NameDoctor);
        Name2 = findViewById(R.id.NameDoctor1);
        Specialisation = findViewById(R.id.SpecDoctor);
        Specialisation2 = findViewById(R.id.textView18);
        imgDoc1 = findViewById(R.id.imageView4);
        imgDoc2 = findViewById(R.id.imageView6);
        ProfileBtn = findViewById(R.id.DoctorProfileView);
        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorListActivity.this, ProfileDoctor.class));
            }
        });
        ProfileBtn2 = findViewById(R.id.AgBtn);
        ProfileBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorListActivity.this,ProfileDoctor2.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("D5A5iUsWYuUlQ2iWYHiaUpgRTRx1");
        storageReference.child("DoctorImage").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(imgDoc1);
                    }
                });
        databaseReference = firebaseDatabase.getReference().child("D5A5iUsWYuUlQ2iWYHiaUpgRTRx1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Name.setText(dataSnapshot.child("doctorsname").getValue().toString());
                Specialisation.setText(dataSnapshot.child("doctorsspecialisation").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorListActivity.this, "Err", Toast.LENGTH_SHORT).show();
            }
        });
        storageReference2 = firebaseStorage.getReference("fJhwvQ1nO5eMD59Wd9Z2ZB64R5H3");
        storageReference2.child("DoctorImage").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(imgDoc2);
                    }
                });

        databaseReference2 = firebaseDatabase.getReference().child("fJhwvQ1nO5eMD59Wd9Z2ZB64R5H3");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Name2.setText(dataSnapshot.child("doctorsname").getValue().toString());
                Specialisation2.setText(dataSnapshot.child("doctorsspecialisation").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorListActivity.this, "Err", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
