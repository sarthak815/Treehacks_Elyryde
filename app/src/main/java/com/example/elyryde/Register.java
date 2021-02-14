package com.example.elyryde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    Button register;
    EditText username1, password;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username1 = (EditText)findViewById(R.id.username_register);
        password = (EditText)findViewById(R.id.password_register);
        register = (Button)findViewById(R.id.registerbtn_register);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username1.getText().toString().trim();
                String password1 = password.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    username1.setError("Invalid Email");
                    username1.setFocusable(true);
                }
                else if(password1.length()<6){
                    password.setError("Password length must be greater than 6");
                    password.setFocusable(true);
                }
                else{
                    registerUser(email, password1);
                }
            }
        });
    }
    private void registerUser(String email, String password1) {

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            // Sign in success, update UI with the signed-in user's informatio
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Registering"+ user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, MapsActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.show();
                Toast.makeText(Register.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}