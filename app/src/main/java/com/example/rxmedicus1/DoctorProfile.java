package com.example.rxmedicus1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class DoctorProfile extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Button DoctorUpload,FstoreTest;
    private TextView DoctorNameDisp;
    private TextView Name, Number, Experience, Specialisation, ClinicAddress, ResidencialAddress;
    private ImageView DocProfPic;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctorsmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogoutMenu: {
                firebaseDatabase.goOffline();
                firebaseAuth.signOut();
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DoctorProfile.this, DoctorMainActivity.class));
                finish(); //15th May 2019
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase =FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        variables(); BottomNavigation();
        DoctorNameDisp = findViewById(R.id.DoctorNameDisplay);
        DoctorNameDisp.setText("Hi " +firebaseAuth.getCurrentUser().getEmail().replace("@gmail.com","").toUpperCase());
        DoctorUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorProfile.this,DoctorUploadActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase =FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) // RIP
                {
                DoctorsProfile doctorsProfile = dataSnapshot.getValue(DoctorsProfile.class);
                Name.setText("Name:"+doctorsProfile.getDoctorsname());
                Number.setText("Contact Number:"+doctorsProfile.getDoctorsnumber());
                Experience.setText("Experience:"+doctorsProfile.getDoctorsexperience()+" years");
                Specialisation.setText("Specialisation:"+doctorsProfile.getDoctorsspecialisation());
                ClinicAddress.setText("Clinic Address:"+doctorsProfile.getCliniaddress());
                ResidencialAddress.setText("Residence Address:"+ doctorsProfile.getResidenciaddress());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorProfile.this,"Error in fetching the database ",Toast.LENGTH_SHORT).show();
            }
        });
        storageReference.child(firebaseAuth.getUid()).child("DoctorImage").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(DocProfPic);
                    }
                });
    }

    private void BottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavdoctor);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.BtmNavDoctorUpload:{
                        startActivity(new Intent(DoctorProfile.this,DoctorUploadActivity.class));break;}
                    case R.id.BtmNavPatient:{
                        startActivity(new Intent(DoctorProfile.this,PatientListActivity.class));break;}
                    case R.id.BtmNavDocCommunity:{
                        startActivity(new Intent(DoctorProfile.this,DoctorCommunityActivity.class));break;}
                    case R.id.BtmNavMedicine: break;
                }
                return false;
            }
        });
    }
    private void variables()
    {
        DoctorUpload = findViewById(R.id.DoctorUpload);
        Name = findViewById(R.id.NameDispDoc);
        Number = findViewById(R.id.NumDispDoc);
        Experience = findViewById(R.id.ExpDispDoc);
        Specialisation = findViewById(R.id.SpecDispDoc);
        ClinicAddress = findViewById(R.id.CliDispDoc);
        ResidencialAddress = findViewById(R.id.ResDispDoc);
        FstoreTest = findViewById(R.id.FirestoreTest);
        DocProfPic = findViewById(R.id.DoctorProfilePic);
    }
}
