package com.example.rxmedicus1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

public class UserProfile extends AppCompatActivity {
    public static final String TAG = "UserProfile"; // Date of birth
    public FirebaseAuth firebaseAuth;
    public FirebaseStorage firebaseStorage;
    public StorageReference storageReference;
    public EditText username, age, address, income, mobno, BloodGroup;
    public ImageView userImage;
    public String DateofBirth;
    public String gender, NameString, AgeString, AddressString, DOBString, UserIncome, UserNumber, BGroup;
    public RadioGroup rgGender;
    public RadioButton rbOption;
    public Button UploadButton, PDF, AdhaarUpload;
    public Button InsuranceUpload;
    public TextView DisplayDate;
    private static int PICK_IMAGE = 123, PICK_DOCUMENT = 999, PICK_DOCUMENT1 = 998;
    public DatePickerDialog.OnDateSetListener DateSetListener;
    Uri imagePath, PDFPath, Document1Path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_DOCUMENT && resultCode == RESULT_OK && data.getData() != null) {
            PDFPath = data.getData();
        } else if (requestCode == PICK_DOCUMENT1 && resultCode == RESULT_OK && data.getData() != null) {
            Document1Path = data.getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        username = findViewById(R.id.tvName);
        AdhaarUpload = findViewById(R.id.PatientAdhaar);
        age = findViewById(R.id.Age);
        address = findViewById(R.id.UserAddress);
        BloodGroup = findViewById(R.id.Bloodgroup);
        income = findViewById(R.id.UserIncome);
        mobno = findViewById(R.id.UserMobNo);
        userImage = findViewById(R.id.imageView);
        UploadButton = findViewById(R.id.BtnUpld);
        rgGender = findViewById(R.id.rgMFO);
        InsuranceUpload = findViewById(R.id.uploadPDF);
        PDF = findViewById(R.id.PDFInsurance);
        DisplayDate = findViewById(R.id.UserDOB);
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(UserProfile.this, android.R.style.Theme_Black, DateSetListener, month, day, year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        try {
            DateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    Log.d(TAG, "Date of Birth " + dayOfMonth + "/" + month + "/" + year);
                    DateofBirth = dayOfMonth + "/" + month + "/" + year;
                    DisplayDate.setText(DateofBirth);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        userImage.setOnClickListener(new View.OnClickListener() { //picture upload
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*"); //application/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image "), PICK_IMAGE);
            }
        });
        PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setType("application/pdf"); //application/*
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select the file "), PICK_DOCUMENT);
            }
        });
        AdhaarUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setType("application/pdf"); //application/*
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select the file "), PICK_DOCUMENT1);
            }
        });
        InsuranceUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentStorage();
                Toast.makeText(UserProfile.this, "Insurance uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        });

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendUserData();
                    ImageStorage();
                    Document1Storage();
                    Toast.makeText(UserProfile.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserProfile.this, SecondActivity.class));
                } catch (Exception e) {
                    Toast.makeText(UserProfile.this, "Add the missing fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbOption = rgGender.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.rbMale:
                        gender = rbOption.getText().toString().trim();
                    case R.id.rbFemale:
                        gender = rbOption.getText().toString().trim();
                    case R.id.rbOth:
                        gender = rbOption.getText().toString().trim();
                }
            }
        });
    }

    private void sendUserData() {
        NameString = username.getText().toString().trim();
        AgeString = age.getText().toString().trim();
        AddressString = address.getText().toString();
        DOBString = DateofBirth.trim();
        UserIncome = income.getText().toString();
        UserNumber = mobno.getText().toString();
        BGroup = BloodGroup.getText().toString();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UsersProfile usersProfile = new UsersProfile(NameString, AgeString, gender, AddressString, DOBString, UserIncome, UserNumber, BGroup);
        myRef.setValue(usersProfile);
    }

    public void ImageStorage() {
        StorageReference imageRef = storageReference.child(firebaseAuth.getUid()).child("Images");
        UploadTask uploadTask = imageRef.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, "Failed! Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserProfile.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DocumentStorage() {
        StorageReference pdfRef = storageReference.child(firebaseAuth.getUid()).child("Insurance");
        UploadTask uploadTask1 = pdfRef.putFile(PDFPath);
        uploadTask1.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, "Failed! Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserProfile.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Document1Storage() {
        StorageReference pdfRef = storageReference.child(firebaseAuth.getUid()).child("AdhaarUpload");
        pdfRef.putFile(Document1Path);
    }
}