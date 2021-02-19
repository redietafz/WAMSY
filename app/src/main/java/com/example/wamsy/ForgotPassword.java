package com.example.wamsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private EditText textEmailAAddress;
    private Button resetPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetPassword = (Button) findViewById(R.id.resetPassword);

        textEmailAAddress = (EditText) findViewById(R.id.email);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

       resetPassword.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
                resetPassword();
           }

       });
    }

    private void resetPassword() {
        String email = textEmailAAddress.getText().toString().trim();

        if (email.isEmpty()) {
            textEmailAAddress.setError("Email is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textEmailAAddress.setError("Please insert a valid email");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPassword.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}