package com.example.photostoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button chooseButton, saveButton, displayButton;
    private ImageView imageView;
    private EditText imageNameEditText;
    private TextView tt;
    private ProgressBar progressBar33;
    private Uri imageUri;
    private   int STORAGPERMISSIONCODE = 1;

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
        progressBar33 = findViewById(R.id.progressbar33ID);
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
                 if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(HomeActivity.this,"You have already granted this permission!",Toast.LENGTH_SHORT).show();

                 }
                 else {
                     requeststoragePermission();
                 }
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
//111
    private void requeststoragePermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
       new  AlertDialog.Builder(this)
               .setTitle("Permission needed")
               .setMessage("This permission is needed because of this and that ")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       ActivityCompat.requestPermissions(HomeActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGPERMISSIONCODE);
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               })
               .create().show();
        }
        else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGPERMISSIONCODE);
        }
    }
//111
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGPERMISSIONCODE ){
           if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               Toast.makeText(this,"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
           }
           else {
               Toast.makeText(this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
           }
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
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar33.setProgress(0);

                            }
                        },500);


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
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {


                double progress = (100.0 * taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                progressBar33.setProgress((int) progress);

            }
        });

    }


    //For file chooser/upload file phone
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