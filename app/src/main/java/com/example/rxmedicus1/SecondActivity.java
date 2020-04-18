package com.example.rxmedicus1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.BitSet;

public class SecondActivity extends AppCompatActivity
{
private FirebaseAuth firebaseAuth;
private FirebaseDatabase firebaseDatabase;
private FirebaseStorage firebaseStorage;
private StorageReference storageReference;
private TextView UserNameDisp,Pname,Pdob,PAge,Paddr,Pnumb,BloodGroup,Pgend;
private Button uploadButton;
private ImageView ProfilePic;
public void variables(){
        Pname = findViewById(R.id.ProfileName);
        Pdob = findViewById(R.id.ProfileDob);
        PAge = findViewById(R.id.ProfileAge);
        Paddr = findViewById(R.id.ProfileAddr);
        Pnumb = findViewById(R.id.ProfileNumber);
        Pgend = findViewById(R.id.ProfileGender);
        BloodGroup = findViewById(R.id.BldGrp);
        ProfilePic = findViewById(R.id.ImageView);
        UserNameDisp = findViewById(R.id.UserDisp);
        uploadButton = findViewById(R.id.SeconActivityButton);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        variables();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase =FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
            firebaseStorage = FirebaseStorage.getInstance();
              storageReference = firebaseStorage.getReference();
              storageReference.child(firebaseAuth.getUid()).child("Images").getDownloadUrl()
                      .addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                      Picasso.get().load(uri).fit().centerCrop().into(ProfilePic);
                  }
              });

              BottomNavigationView bottomNavigationView = findViewById(R.id.PatientBtmNavigation);
              bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                  @Override
                  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                      switch (menuItem.getItemId()){
                          case R.id.BtmNavHospital:{
                              startActivity(new Intent(SecondActivity.this,HospitalActivity.class));break;}
                          case R.id.BtmNavDoctor:{
                              startActivity(new Intent(SecondActivity.this,DoctorListActivity.class));break;}
                          case R.id.BtmNavPatientUpload:
                          {startActivity(new Intent(SecondActivity.this,UserProfile.class));break;}
                          case R.id.BtmNavMedicine: break;
                          case R.id.BtmNavDisease: break;
                      }
                      return false;
                  }
              });
        databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists())
        {
            UsersProfile usersProfile = dataSnapshot.getValue(UsersProfile.class);
            Pname.setText("Name (As per Aadhaar):" + usersProfile.getUsername());
            Pdob.setText("Date of birth:" + usersProfile.getDateofbirth());
            PAge.setText("Age:" + usersProfile.getAge());
            Paddr.setText("Address (As per Aadhaar):" + usersProfile.getAddress());
            Pnumb.setText("Contact Number:" + usersProfile.getNumber());
            Pgend.setText("Gender:" + usersProfile.getGender());
            BloodGroup.setText("Blood Group:" + usersProfile.getBloodgroup());
        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(SecondActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
    }
});
        UserNameDisp.setText("Hi " +firebaseAuth.getCurrentUser().getEmail().replace("@gmail.com",""));
        uploadButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(SecondActivity.this,UserProfile.class));
                }
});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    } //menu bar method 1

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //menu bar method 2
        switch (item.getItemId()) {
            case R.id.LogoutMenu: {
              logout();
              break;
            }
            case R.id.UploadMedicationMenu:
            {
                startActivity(new Intent(SecondActivity.this,MedicationActivity.class));

            }
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
private void logout()
{
    firebaseDatabase.goOffline();
    firebaseAuth.signOut();
    Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
    startActivity(new Intent(SecondActivity.this, MainActivity.class));
    finish(); //15th May 2019
}
}
