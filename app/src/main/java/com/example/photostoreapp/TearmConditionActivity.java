package com.example.photostoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TearmConditionActivity extends AppCompatActivity {


    private TextView textView1,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tearm_condition);


        textView1 = findViewById(R.id.skipID);
        textView2 = findViewById(R.id.next1TextID);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(TearmConditionActivity.this,CondictionActivity.class);
                startActivity(n);
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(TearmConditionActivity.this,SignActivity.class);
                startActivity(o);
            }
        });
    }
}