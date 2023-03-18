package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    private EditText signupemail, signuppassword;
    private RelativeLayout signup;
    private TextView gotologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();
        signupemail = findViewById(R.id.signupemail);
        signuppassword = findViewById(R.id.signuppassword);
        signup = findViewById(R.id.signup);
        gotologin = findViewById(R.id.gotologin);

        firebaseAuth = FirebaseAuth.getInstance();

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = signupemail.getText().toString().trim();
                String password = signuppassword.getText().toString().trim();
                if(mail.isEmpty()||password.isEmpty())
                {
                    Toast.makeText(Signup.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<7)
                {
                    Toast.makeText(Signup.this, "Password must be greater than 7 characters!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //register user to firebase
                    firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Signup.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(Signup.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Signup.this, "Verification email sent! Verify and login again.", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    Intent intent = new Intent(Signup.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        else
        {
            Toast.makeText(this, "Failed to send verification email!", Toast.LENGTH_SHORT).show();
        }
    }
}