package com.example.photostoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.EventListener;

public class MainActivity extends AppCompatActivity {

    private SparkButton sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = findViewById(R.id.spark_button);
        sp.playAnimation();
        sp.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {


                if (buttonState) {
                    // Button is active
                    openDialog();
                    Intent i= new Intent(MainActivity.this,TearmConditionActivity.class);
                    Toast.makeText(getApplicationContext(),"welcome For Click",Toast.LENGTH_SHORT).show();
                    startActivity(i);
                } else {
                    // Button is inactive
                    Toast.makeText(getApplicationContext(),"Please Click",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });


    }

    private void openDialog() {


        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


}