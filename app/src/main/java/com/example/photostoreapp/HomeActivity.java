package com.example.photostoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button chooseButton, saveButton, displayButton;
    private ImageView imageView;
    private EditText imageNameEditText;
    private ProgressBar progressBar;
    private Uri imageUri;


    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;


    private static final int Image_Request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseReference = FirebaseDatabase.getInstance().getReference("Upload");
        storageReference = FirebaseStorage.getInstance().getReference("Upload");


        chooseButton = findViewById(R.id.chooseImageButtonID);
        saveButton = findViewById(R.id.saveImageBtnID);
        displayButton = findViewById(R.id.displayImageBtnID);
        progressBar = findViewById(R.id.progressbarID);
        imageView = findViewById(R.id.imageViewID);
        imageNameEditText = findViewById(R.id.imageNameEditTExtID);


        saveButton.setOnClickListener(this);
        displayButton.setOnClickListener(this);
        chooseButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chooseImageButtonID:

                openFileChooser();

                break;
            case R.id.saveImageBtnID:
                if (uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(),"Uploading in progress",Toast.LENGTH_SHORT).show();

                }else {
                    saveData();
                }

                break;
            case R.id.displayImageBtnID:

                Intent intent = new Intent(HomeActivity.this,ImageActivity.class);
                startActivity(intent);
                break;
        }

    }

    //For image save

    private void saveData() {
        final String imageName = imageNameEditText.getText().toString().trim();

        if (imageName.isEmpty()) {
            imageNameEditText.setError("Enter the image name");
            imageNameEditText.requestFocus();
            return;
        }

        StorageReference ref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Toast.makeText(getApplicationContext(), "Image is stored successfully ", Toast.LENGTH_SHORT).show();

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!urlTask.isSuccessful());


                        Uri downloadUrl = urlTask.getResult();


                        Upload upload = new Upload(imageName,downloadUrl.toString());


                        String uploadID = databaseReference.push().getKey();
                        databaseReference.child(uploadID).setValue(upload);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

                        Toast.makeText(getApplicationContext(), "Image is not stored successfully", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    //For file chooser phone
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Image_Request);
    }


    //For Image Selate
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }
    //getting the extension of the image

    public String getFileExtension(Uri imageUri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
}