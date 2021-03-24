package com.example.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class bottomNavigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        // bottom navigation view
        BottomNavigationView bottomnav = findViewById(R.id.bottomNavigationView);
        bottomnav.setOnNavigationItemSelectedListener(navListner);


        // Setting Home Fragment as main fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new HomeFragment()).commit();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){

                case R.id.item1:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.item2:
                    selectedFragment = new ProfileFragment();
                    break;

                case R.id.item3:
                    selectedFragment = new RequestFragment();
                    break;

                case R.id.item4:
                    selectedFragment = new BlogsFragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,selectedFragment).commit();
            return true;
        }
    };
    public void userLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }
}