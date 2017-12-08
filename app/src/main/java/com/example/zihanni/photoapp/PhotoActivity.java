package com.example.zihanni.photoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zihanni on 12/7/17.
 */

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    //recyclerview object
    private RecyclerView recyclerView;
    //adapter object
    private RecyclerView.Adapter adapter;
    //database reference
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    //list to hold all the uploaded images
    private List<Upload> uploads;

    private Button privateButton;
    private Button publicButton;
    private Button uploadButton;
    private Button searchButton;

    //progress dialog
    private ProgressDialog progressDialog;

    private boolean publicViewMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        privateButton = findViewById(R.id.privateButton);
        publicButton = findViewById(R.id.publicButton);
        uploadButton = findViewById(R.id.uploadButton);
        searchButton = findViewById(R.id.searchButton);

        progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        privateButton.setOnClickListener(this);
        publicButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS_PUBLIC + "/");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressDialog.dismiss();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }

                adapter = new MyAdapter(getApplicationContext(), uploads);

                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == privateButton) {
            publicViewMode = false;
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (user == null) {
                Toast.makeText(getApplicationContext(), "You are currently viewing as guest, Please Sign In!", Toast.LENGTH_SHORT).show();
                return;
            }

            mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS_PRIVATE + "/"
                    + user.getUid());

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    uploads.clear();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        uploads.add(upload);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else if (view == publicButton){
            publicViewMode = true;
            mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS_PUBLIC + "/");

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    uploads.clear();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        uploads.add(upload);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else if (view == uploadButton){
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        } else if (view == searchButton) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else {
            // do nothing
        }
    }


}
