package com.example.rxmedicus1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
public class MainActivity extends AppCompatActivity {
 private EditText Name;
 private EditText Password;
 private TextView info;
 private Button login,doclogin;
 private TextView RegisterText;
 private int count = 5;
 private FirebaseAuth firebaseAuth;
 private ProgressDialog progressDialog;
 SwipeRefreshLayout swipeRefreshLayout;
 public int number = 2;
 TextView swipeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        swipeRefreshLayout = findViewById(R.id.Swipe) ;
        swipeText = findViewById(R.id.tvSwipe);
        doclogin = findViewById(R.id.DocLoginBtn);
        doclogin.setEnabled(false);
        login =findViewById(R.id.tvButton);
        login.setEnabled(false);
        Name = findViewById(R.id.tvUser);
        Password =findViewById(R.id.tvPass);
        Name.setEnabled(false);
        Password.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                number++;
                swipeText.setText("Number- "+number);
                if(number%2==0) {
                    swipeText.setText("Patient");

                    doclogin.setEnabled(false);
                    Name.setEnabled(true);Password.setEnabled(true);
                    login.setEnabled(true);
                }
                else
                    {
                    swipeText.setText("Doctor");
                    doclogin.setEnabled(true);
                    Name.setEnabled(false);Password.setEnabled(false);
                    }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 400);
            }
        });
        doclogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DoctorMainActivity.class));
            }
        });

        info =findViewById(R.id.tvAttempt);

        firebaseAuth = FirebaseAuth.getInstance();
        RegisterText = findViewById(R.id.tvSignup);
        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty())
                {Name.setError("Please enter the email ID");Name.requestFocus();}
                else if(Password.getText().toString().isEmpty())
                {Password.setError("Please enter the password");Password.requestFocus();}
                else
                validate(Name.getText().toString().trim(),Password.getText().toString().trim());
            }
        });
        RegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });
    }
private void validate(String Username,String Password){
        progressDialog.setMessage("Please wait till you're logged in!");
        progressDialog.show();
    firebaseAuth.signInWithEmailAndPassword(Username,
            Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                progressDialog.dismiss();
                CheckVerification();
            }
            else {
                Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            count--;
            info.setText("No. of attempts remaining " +count);
            if(count==0)
                login.setEnabled(false);
            }
        }
    });
}
private void CheckVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean flag = firebaseUser.isEmailVerified();
        if(flag) {
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }
            else {
            Toast.makeText(this, "Please verify your Email ID", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
}
}
