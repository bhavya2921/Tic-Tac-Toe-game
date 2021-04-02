package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuthentication;
    Button welcomePageRegisterButton,welcomePageLoginButton,logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuthentication = FirebaseAuth.getInstance();
        welcomePageLoginButton = findViewById(R.id.loginButtonWelcomePage);
        welcomePageRegisterButton = findViewById(R.id.registerButtonWelcomePage);
        logoutButton = findViewById(R.id.logoutButtonWelcomePage);

        //Button loginPageButton= (Button) findViewById(R.id.loginButton);
        //Button registerPageButton= (Button) findViewById(R.id.registerButton);

        welcomePageLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });

        welcomePageRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });

        if(fAuthentication.getCurrentUser() != null) {
            Intent profileIndent = new Intent(this,bottomNavigation.class);
            startActivity(profileIndent);
        } else {
            logoutButton.setVisibility(View.GONE);
        }
    }

    // This method is used to open login page
    public void openLoginPage(){
        Intent loginIntent = new Intent( this, login.class);
        startActivity(loginIntent);
    }

    // This method is used to open register page
    public void openRegisterPage(){
        Intent registerIntent = new Intent(this, register.class);
        startActivity(registerIntent);
    }

    public void userLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }
}