package com.example.wamsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //inisiasi
    private TextView register, forgotPassword;
    private EditText textEmailAddress, textPassword;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        textEmailAddress = (EditText) findViewById(R.id.email);
        textPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //connecting register in main activity into register page
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class ));
        }
    }

    private void userLogin() {
        //mengubah teks ke string
        String email = textEmailAddress.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        //peringatan ketika email kosong
        if(email.isEmpty()){
            textEmailAddress.setError("Email is required");
            return;
        }

        //ketika email tidak sesuai, muncul peringatan
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textEmailAddress.setError("Please enter a valid email");
            return;
        }

        //peringatan ketika password kosong
        if (password.isEmpty()){
            textPassword.setError("Password is required");
            return;
        }

        //menentukan panjang katakter
        if(password.length() < 6){
            textPassword.setError("Password at least 6 character");
            return;
        }

        //menampilkan progres bar
        progressBar.setVisibility(View.VISIBLE);

        //login dengan menggunakan email dan password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Check your email to verify", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to Login", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}