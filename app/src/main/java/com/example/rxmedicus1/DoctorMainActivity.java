package com.example.rxmedicus1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DoctorMainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private EditText NumberInput,OTPInput;
    private EditText email,password;
    private TextView AssociateRx;
    private Button SendCode,Doclog;
    private int count=5;
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//    String codesent;
        Uri uri; // google forms
    private void variables()
    {
        NumberInput = findViewById(R.id.InputNumber); NumberInput.setEnabled(false);
        OTPInput = findViewById(R.id.OTP); OTPInput.setEnabled(false);
        SendCode = findViewById(R.id.SendCodeBtn); SendCode.setEnabled(false);
        email = findViewById(R.id.DoctorEmail); password = findViewById(R.id.DoctorPassword); // login using email ID and password
        Doclog = findViewById(R.id.DocLog);

        AssociateRx = findViewById(R.id.AssociationTV); //google forms
                AssociateRx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uri = Uri.parse("https://forms.gle/MKcgtfRAvidK5Kvq7");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        //StartFirebaseLogin();
        variables();
//        SendCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // sendVerificationCode();
//                String phoneNumber = NumberInput.getText().toString();
//             //   if(phoneNumber.length() < 10){NumberInput.setError("Please Enter a Valid Number.");NumberInput.requestFocus();}
//                PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                        phoneNumber,
//                        60,
//                        TimeUnit.SECONDS,
//                        DoctorMainActivity.this,
//                        mCallbacks);
//            }
//        });
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//              //  Toast.makeText(DoctorMainActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//               // Toast.makeText(DoctorMainActivity.this,"verification failed",Toast.LENGTH_SHORT).show();
//            }
//
////            @Override
////            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
////                super.onCodeSent(s, forceResendingToken);
////                codesent = s;
////                Toast.makeText(DoctorMainActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
////            }
//        };
//        Doclog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                verify();
//                startActivity(new Intent(DoctorMainActivity.this,DoctorProfile.class));
//            }
//        });
        firebaseAuth = FirebaseAuth.getInstance();
        Doclog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty())
                {email.setError("Please enter the email ID");email.requestFocus();}
                else if(password.getText().toString().isEmpty())
                {password.setError("Please enter the password");password.requestFocus();}
                else
                validate(email.getText().toString().trim(),password.getText().toString().trim());
            }
        });

    }
    private void validate(String Email, String Password)
    {
        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(DoctorMainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DoctorMainActivity.this,DoctorProfile.class));
                    finish();
                }
                else
                {
                    Toast.makeText(DoctorMainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    count--;
                    if(count==0) Doclog.setEnabled(false);
                }
            }
        });
    }

//private void sendVerificationCode()
//    {
//        String phoneNumber = NumberInput.getText().toString();
//        if(phoneNumber.isEmpty()){NumberInput.setError("Phone Number is Required.");NumberInput.requestFocus();}
//        if(phoneNumber.length() < 10){NumberInput.setError("Please Enter a Valid Number.");NumberInput.requestFocus();}
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                DoctorMainActivity.this,               // Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacks
//    }
//    private void StartFirebaseLogin(){
//    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//            Toast.makeText(DoctorMainActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            Toast.makeText(DoctorMainActivity.this,"verification failed",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            codesent = s;
//            Toast.makeText(DoctorMainActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
//        }
//    };
//}
//    private void verify()
//    {
//        String OTP = OTPInput.getText().toString();
//        // String Phone = NumberInput.getText().toString();
////        if(OTP.isEmpty())
////        {OTPInput.setError("Please enter the OTP sent to "+Phone);
////        OTPInput.requestFocus();
////        return;}
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, OTP);
//        signInWithPhoneAuthCredential(credential);
//    }
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task)
//                    {
//                        if (task.isSuccessful())
//                        {
//                            Toast.makeText( getApplicationContext(),"Login Successful!", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(DoctorMainActivity.this,DoctorProfile.class));
//                        } else
//                        {
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                Toast.makeText( getApplicationContext(),"Please Enter a Valid OTP", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }
}
