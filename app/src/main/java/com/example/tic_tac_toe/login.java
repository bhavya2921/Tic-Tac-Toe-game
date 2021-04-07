package com.example.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText loginEmailId,loginEmailPassword;
    Button loginButton;
    ProgressBar loginProgressBar;
    FirebaseAuth loginFirebaseAuth;
    TextView forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmailId = findViewById(R.id.loginUserEmailId);
        loginEmailPassword = findViewById(R.id.loginUserEmailPassword);
        loginButton = findViewById(R.id.userLoginButton);
        loginFirebaseAuth = FirebaseAuth.getInstance();
        loginProgressBar = findViewById(R.id.progressBarLogin);
        forgotpassword = findViewById(R.id.forgotPassword);

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
                            Toast.makeText(login.this, "Error! "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            loginProgressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter your email to recive reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        String mail = resetMail.getText().toString();
                        loginFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "Error! Reset password link not sent"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
                passwordResetDialog.create().show();

            }
            }
        );

    }

    // This method is used to open register page
    public void openRegister(View view) {
        Intent registerIntent = new Intent( this, register.class);
        startActivity(registerIntent);
    }



}