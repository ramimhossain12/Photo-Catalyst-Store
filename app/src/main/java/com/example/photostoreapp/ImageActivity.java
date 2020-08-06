package com.example.photostoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<Upload>  uploadList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar1;

    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        progressBar1 = findViewById(R.id.progressBarID3);

        recyclerView =findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadList = new ArrayList<>();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Upload");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                uploadList.clear();

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                {
                    Upload upload =  dataSnapshot1.getValue(Upload.class);
                    upload.setKey(dataSnapshot1.getKey());
                    uploadList.add(upload);
                }
                myAdapter = new MyAdapter(ImageActivity.this,uploadList);
                recyclerView.setAdapter(myAdapter);

                     myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                         @Override
                         public void onItemClick(int position) {
                            String  text = uploadList.get(position).getImageName();
                            Toast.makeText(getApplicationContext(),text+"is selected "+position,Toast.LENGTH_SHORT).show();

                         }

                         @Override
                         public void onDoAnyTask(int position) {
                             Toast.makeText(getApplicationContext(),"onDoAnyTask is selected ",Toast.LENGTH_SHORT).show();
                         }

                         @Override
                         public void onDelete(int position) {
                             Upload selectedItem = uploadList.get(position);
                             final String key = selectedItem.getKey();

                             StorageReference storageReference = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
                             storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {

                                     databaseReference.child(key).removeValue();
                                     Toast.makeText(getApplicationContext(),"Image Deleted",Toast.LENGTH_SHORT).show();

                                 }
                             });
                         }
                     });

                progressBar1.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(),"Error :"+error.getMessage(),Toast.LENGTH_LONG).show();
               progressBar1.setVisibility(View.INVISIBLE);
            }
        });


    }
}