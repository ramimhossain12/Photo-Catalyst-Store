package com.example.photostoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signUpEmailEditText,signUpPasswordEditText, signUpconfirmEditText, username ;
    private TextView textView;
    private ProgressBar progressBar;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private  TextView text1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        RelativeLayout relativeLayout = findViewById(R.id.layout2ID);

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(100);
        animationDrawable.setExitFadeDuration(100);
        animationDrawable.start();

        this.setTitle("Sign Up Activity");
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbarID);
        signUpEmailEditText = findViewById(R.id.signUpEmailEditTextID);
        signUpPasswordEditText = findViewById(R.id.signUppasswordEditTextID);
        signUpconfirmEditText = findViewById(R.id.signUpconfirmpassEditTextID);
        username = findViewById(R.id.signUpusernameEditTextID);
        textView = findViewById(R.id.signInTextViewID);
        text1 = findViewById(R.id.back);
        signUpButton = findViewById(R.id.signUpButtonID);

        textView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.signUpButtonID:
                Intent in = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(in);
                userRegister();
                break;

            case  R.id.signInTextViewID:

                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
                break;


        }

    }


    private void userRegister() {

        String email = signUpEmailEditText.getText().toString().trim();
        String user = username.getText().toString().trim();
        String password = signUpPasswordEditText.getText().toString().trim();
        String confirmpassword = signUpconfirmEditText.getText().toString().trim();
        //checking the validity of the email
        if(email.isEmpty())
        {
            signUpEmailEditText.setError("Enter an email address");
            signUpEmailEditText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpEmailEditText.setError("Enter a valid email address");
            signUpEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(email.isEmpty())
        {
            signUpPasswordEditText.setError("Enter a password");
            signUpPasswordEditText.requestFocus();

            return;
        }
        progressBar.setVisibility(View.VISIBLE);




        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);


                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Register is successful", Toast.LENGTH_LONG).show();

                } else {


                    if (task.getException() instanceof FirebaseAuthUserCollisionException){

                        Toast.makeText(getApplicationContext(),"User is already Registered", Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });




    }
}
