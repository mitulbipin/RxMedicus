package com.example.rxmedicus1;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileDoctor extends AppCompatActivity {
    private TextView Name, Number, ClinicAddr, Experience;
    private ImageView Verify, Pic;
    private Button Call;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_doctor);
        variables();
        Verify.setImageResource(R.drawable.ic_verified_user_black_24dp);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("D5A5iUsWYuUlQ2iWYHiaUpgRTRx1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Name.setText(dataSnapshot.child("doctorsname").getValue().toString());
                Number.setText("Number: " + dataSnapshot.child("doctorsnumber").getValue().toString());
                ClinicAddr.setText("Clinic Address: " + dataSnapshot.child("cliniaddress").getValue().toString());
                Experience.setText("Experience: " + dataSnapshot.child("doctorsexperience").getValue().toString() + " Years");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileDoctor.this, "Err", Toast.LENGTH_SHORT).show();
            }
        });
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("D5A5iUsWYuUlQ2iWYHiaUpgRTRx1");
        storageReference.child("DoctorImage").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(Pic);
                    }
                });
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:8867874388"));
                startActivity(callIntent);
            }
        });
    }

    private void variables() {
        Name = findViewById(R.id.NameDisp);
        Number = findViewById(R.id.NumDisp);
        Experience = findViewById(R.id.ExperienceDisp);
        ClinicAddr = findViewById(R.id.AddressDisp);
        Verify = findViewById(R.id.imageView5);
        Pic = findViewById(R.id.imageView2);
        Call = findViewById(R.id.button3);
    }
}
