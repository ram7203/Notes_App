package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText loginemail, loginpassword;
    private RelativeLayout login, gotosignup;
    private TextView gotoforgotpassword;
    ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        loginemail = findViewById(R.id.loginemail);
        loginpassword = findViewById(R.id.loginpassword);
        login = findViewById(R.id.login);
        gotosignup = findViewById(R.id.gotosignup);
        gotoforgotpassword = findViewById(R.id.gotoforgotpassword);
        progressBar = findViewById(R.id.progressbarmainactivity);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();  //take instance of user to login

        //if user is already logged in
        if(firebaseUser!=null)
        {
            finish();
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        }

        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });
        gotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String mail = loginemail.getText().toString().trim();
                 String password = loginpassword.getText().toString().trim();
                 if(mail.isEmpty()||password.isEmpty())
                     Toast.makeText(MainActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                 else
                 {
                     //login the user

                     progressBar.setVisibility(View.VISIBLE);

                     firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful())
                             {
                                 checkmailverification();
                             }
                             else
                             {
                                 Toast.makeText(MainActivity.this, "Account does not exist!", Toast.LENGTH_SHORT).show();
                                 progressBar.setVisibility(View.INVISIBLE);
                             }
                         }
                     });
                 }
             }
         });
    }
    private void checkmailverification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true)
        {
            Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Verify your mail first!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}