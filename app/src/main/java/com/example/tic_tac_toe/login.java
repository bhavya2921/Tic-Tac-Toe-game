package com.example.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText loginEmailId,loginEmailPassword;
    Button loginButton;
    ProgressBar loginProgressBar;
    FirebaseAuth loginFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmailId = findViewById(R.id.loginUserEmailId);
        loginEmailPassword = findViewById(R.id.loginUserEmailPassword);
        loginButton = findViewById(R.id.userLoginButton);
        loginFirebaseAuth = FirebaseAuth.getInstance();
        loginProgressBar = findViewById(R.id.progressBarLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginUserId = loginEmailId.getText().toString().trim();
                String loginUserPassword = loginEmailPassword.getText().toString();

                if(TextUtils.isEmpty(loginUserId)) {
                    loginEmailId.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(loginUserPassword)) {
                    loginEmailPassword.setError("Password is required");
                    return;
                }
                if(loginUserPassword.length()<6) {
                    loginEmailPassword.setError("Password must be atleast 6 characters");
                    return;
                }
                loginProgressBar.setVisibility(View.VISIBLE);
                loginFirebaseAuth.signInWithEmailAndPassword(loginUserId,loginUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(login.this, "login successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent (getApplicationContext(),bottomNavigation.class));
                        }
                        else {
                            Toast.makeText(login.this, "Error! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });
    }

    // This method is used to open register page
    public void openRegister(View view) {
        Intent registerIntent = new Intent( this, register.class);
        startActivity(registerIntent);
    }
}