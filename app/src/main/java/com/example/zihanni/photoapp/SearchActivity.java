package com.example.zihanni.photoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zihanni on 12/7/17.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button searchBtn;
    private EditText queryEditText;
    private ImageView imgView;
    private DatabaseReference mDatabase;
    private Switch sswitch;
    private List<Upload> res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        queryEditText = (EditText) findViewById(R.id.ccc);
        searchBtn = (Button) findViewById(R.id.ddd);
        imgView = (ImageView) findViewById(R.id.eee);
        sswitch = (Switch) findViewById(R.id.fff);

        res = new ArrayList<>();

        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == searchBtn) {
            // fire query
            if (sswitch.isChecked()) {
                // private mode
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {
                    Toast.makeText(getApplicationContext(), "You are currently viewing as guest, Please Sign In!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS_PRIVATE + "/"
                        + user.getUid());

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        res.clear();

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Upload upload = postSnapshot.getValue(Upload.class);
                            if (upload.description.contains(queryEditText.getText().toString().trim()))
                                res.add(upload);
                        }
                        if (res.size() > 0)
                            Glide.with(getApplicationContext()).load(res.get(0).getUrl()).into(imgView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            } else {
                mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS_PUBLIC + "/");

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        res.clear();

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Upload upload = postSnapshot.getValue(Upload.class);
                            if (upload.description.contains(queryEditText.getText().toString().trim()))
                                res.add(upload);
                        }
                        if (res.size() > 0)
                            Glide.with(getApplicationContext()).load(res.get(0).getUrl()).into(imgView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }
}
