package com.example.rxmedicus1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SupportingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporting);
        Intent intent = new Intent(SupportingActivity.this,RegistrationActivity.class);
        startActivity(intent);
    }
}
