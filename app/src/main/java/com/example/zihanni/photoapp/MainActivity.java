package com.example.zihanni.photoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signup;
    private Button signin;
    private Button guest;
    private EditText email;
    private EditText password;


    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        guest = findViewById(R.id.guest);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        guest.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        auth.signOut();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null) {

                } else {

                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(authListener!=null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == signin) {
            signIn();
        } else if(view == signup) {
            signUp();
        } else {
            auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
            startActivity(intent);
        }

    }

    private void signUp() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        if(emailInput.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(passwordInput.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(emailInput,passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Sign Up Failure!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sign Up Success!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void signIn() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        if(emailInput.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(passwordInput.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        // validations are ok
        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        auth.signInWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Sign In Failure!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sign In Success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                    startActivity(intent);
                }
            }
        });


    }


}
