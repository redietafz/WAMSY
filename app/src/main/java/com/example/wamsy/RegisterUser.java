package com.example.wamsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser, registerAdmin;
    private EditText textPersonName, textEmailAddress, textPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        registerAdmin = (Button) findViewById(R.id.registerAdmin);
        registerAdmin.setOnClickListener(this);

        textPersonName = (EditText) findViewById(R.id.username);
        textEmailAddress = (EditText) findViewById(R.id.email);
        textPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
            case R.id.registerAdmin:
                registerAdmin();
                break;
        }
    }

    private void registerAdmin() {
        String username = textPersonName.getText().toString().trim();
        String email = textEmailAddress.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if(username.isEmpty()){
            textPersonName.setError("username is required");
            textPersonName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            textEmailAddress.setError("email is required");
            textEmailAddress.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textEmailAddress.setError("Please provide valid error");
            textEmailAddress.requestFocus();
            return;
        }

        if(password.isEmpty()){
            textPassword.setError("password is required");
            textPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            textPassword.setError("at least 6 character");
            textPassword.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User user = new User(username, email);

                        FirebaseDatabase.getInstance().getReference("Admin")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if(task.isSuccessful()){
                                    user.sendEmailVerification();
                                    Toast.makeText(RegisterUser.this, "Check your email", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                                else {
                                    Toast.makeText(RegisterUser.this, "Failed to register", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
    }

    private void registerUser(){
        String username = textPersonName.getText().toString().trim();
        String email = textEmailAddress.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if(username.isEmpty()){
            textPersonName.setError("username is required");
            textPersonName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            textEmailAddress.setError("email is required");
            textEmailAddress.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textEmailAddress.setError("Please provide valid error");
            textEmailAddress.requestFocus();
            return;
        }

        if(password.isEmpty()){
            textPassword.setError("password is required");
            textPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            textPassword.setError("at least 6 character");
            textPassword.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User user = new User(username, email);

                        FirebaseDatabase.getInstance().getReference("User")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if(task.isSuccessful()){
                                    user.sendEmailVerification();
                                    Toast.makeText(RegisterUser.this, "Check your email", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                                else {
                                    Toast.makeText(RegisterUser.this, "Failed to register", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                                }
                            });
                    }
                });
    }
}