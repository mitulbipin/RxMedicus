package com.example.rxmedicus1;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class DoctorUploadActivity extends AppCompatActivity {
private EditText DocTrig,DocName,DocNum,DocResAddr,DocCliAddr,DocExp;
private Button Upload,DoctorLicenseUpload,DoctorAdhaarUpload;
private ImageView DocImg;
    public FirebaseAuth firebaseAuth;
    String DocSpecialisation;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseStorage firebaseStorage;
    public StorageReference storageReference;
    private static int PICK_IMAGE = 123,PICK_DOCUMENT=999,PICK_DOCUMENT1=998;
    Uri DocimagePath,DocPDFPath1,DocPDFPath2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            DocimagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),DocimagePath);
                DocImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == PICK_DOCUMENT && resultCode == RESULT_OK && data.getData() != null){
            DocPDFPath1 = data.getData();
        }
        else if(requestCode == PICK_DOCUMENT1 && resultCode == RESULT_OK && data.getData() != null){
            DocPDFPath2 = data.getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_upload);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        variables();
        DocTrig.setText("Doctor");
        DocTrig.setVisibility(View.GONE);
        Spinner SpecialisationSpinner = findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(DoctorUploadActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.specializations));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpecialisationSpinner.setAdapter(myAdapter);
        DocSpecialisation = SpecialisationSpinner.getSelectedItem().toString();
        DocImg.setOnClickListener(new View.OnClickListener() { //picture upload
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*"); //application/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image."), PICK_IMAGE);
            }
        });

        DoctorLicenseUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setType("application/pdf"); //application/*
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1,"Select the file."), PICK_DOCUMENT);
            }
        });

        DoctorAdhaarUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setType("application/pdf"); //application/*
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1,"Select the file."), PICK_DOCUMENT1);
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDoctorData();
                ImageStorage();
                DocumentStorage();
                Document1Storage();
                Toast.makeText(DoctorUploadActivity.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(DoctorUploadActivity.this, DoctorProfile.class));
            }
        });
    }
    private void variables(){
        DocTrig = findViewById(R.id.DoctorTrigger);
        DocName = findViewById(R.id.DoctorName);
        DocNum = findViewById(R.id.DoctorNumber);
        DocResAddr = findViewById(R.id.DoctorResAddress);
        DocCliAddr = findViewById(R.id.DoctorCliAddress);
        DocExp = findViewById(R.id.DoctorExperience);
        Upload = findViewById(R.id.DoctorUpload);
        DocImg = findViewById(R.id.DoctorImage);
        DoctorLicenseUpload = findViewById(R.id.DoctorLicense);
        DoctorAdhaarUpload = findViewById(R.id.DoctorAdhaar);
    }

    private void SendDoctorData(){
        String TrigString, NameString, NumString, ResAddrString, CLiAddrString, DocExpString,Specialisation;
        TrigString = DocTrig.getText().toString();
        NameString = DocName.getText().toString();
        NumString = DocNum.getText().toString();
        ResAddrString = DocResAddr.getText().toString();
        CLiAddrString = DocCliAddr.getText().toString();
        DocExpString = DocExp.getText().toString();
        Specialisation = DocSpecialisation;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        DoctorsProfile doctorsProfile = new DoctorsProfile(TrigString, NameString,
                                            NumString, ResAddrString, CLiAddrString, DocExpString, Specialisation);
        myRef.setValue(doctorsProfile);
    }

    public void ImageStorage(){
        StorageReference imageRef = storageReference.child(firebaseAuth.getUid()).child("DoctorImage");
        UploadTask uploadTask = imageRef.putFile(DocimagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorUploadActivity.this, "Failed! Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(DoctorUploadActivity.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DocumentStorage() // Doctors License
    {
        StorageReference pdfRef = storageReference.child(firebaseAuth.getUid()).child("DoctorLicense");
        UploadTask uploadTask1 = pdfRef.putFile(DocPDFPath1);
        uploadTask1.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorUploadActivity.this, "Failed! Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(DoctorUploadActivity.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Document1Storage()
    {
        StorageReference pdfRef = storageReference.child(firebaseAuth.getUid()).child("DoctorAdhaar");
        UploadTask uploadTask2 = pdfRef.putFile(DocPDFPath2);
        uploadTask2.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorUploadActivity.this, "Failed! Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(DoctorUploadActivity.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
