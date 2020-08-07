package com.example.photostoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CondictionActivity extends AppCompatActivity {

    private TextView agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condiction);

        agree = findViewById(R.id.agreeID);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e= new Intent(CondictionActivity.this,SignActivity.class);
                startActivity(e);
            }
        });
    }
}