package com.example.rxmedicus1;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private EditText UserPassword,UserEmail;
    public EditText Username;
    private Button RegButton;
    private TextView UserLogin;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Username = findViewById(R.id.tvName);
//            String nme = Username.getText().toString();
//         Intent i = new Intent(RegistrationActivity.this,SecondActivity.class);
//            i.putExtra("HERE",nme);
//           startActivity(i);

        UserPassword = findViewById(R.id.tvPassword);
        UserEmail = findViewById(R.id.tvEmail);
        RegButton = findViewById(R.id.button);
        UserLogin = findViewById(R.id.tvUserLogin);
        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
//        myRef.setValue(Username);
        RegButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            if(validate()==1)
                    {
                        String u_email = UserEmail.getText().toString().trim();
                        String u_password = UserPassword.getText().toString().trim();

                        firebaseAuth.createUserWithEmailAndPassword(u_email,u_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                            {
                            SendEmailVerification();
                            }
                        else
                        {Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();}
    }
});
                    }
                }
            });
            UserLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                }
            });
    }
    private int validate(){
        int result = 0;
        String name = Username.getText().toString();
        String password = UserPassword.getText().toString();
        String email = UserEmail.getText().toString();
        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }
        else {result = 1;}
        return result;
    }

    private void SendEmailVerification()
    {
         FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistrationActivity.this,"You have been registered successfully,Verification Email is sent to your Registered Email ID",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(RegistrationActivity.this,"Verification mail hasn't been sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
