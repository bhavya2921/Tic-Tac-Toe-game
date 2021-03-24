package com.example.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText userRegisterEmailId,userRegisterEmailPassword,userRegisterFullName,regisrerUserName;
    Button registerButton;
    FirebaseAuth fAuthentication;
    ProgressBar progressBar;
    TextView loginPageLink;
    FirebaseFirestore fStore;
    String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRegisterEmailId = findViewById(R.id.registerUserEmailId);
        userRegisterEmailPassword = findViewById(R.id.registerUserEmailPassword);
        userRegisterFullName = findViewById(R.id.registerUserFullName);
        registerButton = findViewById(R.id.userButtonToRegister);

        fAuthentication = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.registerProgressBar);
        loginPageLink = findViewById(R.id.registerPageLoginLink);
        regisrerUserName = findViewById(R.id.registerUsername);

        if(fAuthentication.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }



    registerButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        String registerUserId = userRegisterEmailId.getText().toString().trim();
        String registerUserPassword = userRegisterEmailPassword.getText().toString();
        String registerUserFullName = userRegisterFullName.getText().toString();
        String username = regisrerUserName.getText().toString();
        if(TextUtils.isEmpty(registerUserFullName)) {
            userRegisterFullName.setError("Name is required");
            return;
        }

        if(TextUtils.isEmpty(registerUserId)) {
            userRegisterEmailId.setError("Email is required");
            return;
        }
        if(TextUtils.isEmpty(registerUserPassword)) {
            userRegisterEmailPassword.setError("Password is required");
            return;
        }
        if(registerUserPassword.length()<6) {
            userRegisterEmailPassword.setError("Password must be atleast 6 characters");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        fAuthentication.createUserWithEmailAndPassword(registerUserId,registerUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser fuser = fAuthentication.getCurrentUser();
                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(register.this, "Verification email has been send to your email ID", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFaliure; Email not sent" + e.getMessage());
                        }
                    });
                    Toast.makeText(register.this, "Usre Created.", Toast.LENGTH_SHORT).show();
                    userID = fAuthentication.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("fName",registerUserFullName);
                    user.put("email",registerUserId);
                    user.put("username",username);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG,"onSuccess: user Profile is created for "+ userID);
                        }
                    });
                    startActivity(new Intent (getApplicationContext(),bottomNavigation.class));
                }
                else {
                    Toast.makeText(register.this, "Error! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
        }
    });
}
    public void openLoginPage(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent); }
}