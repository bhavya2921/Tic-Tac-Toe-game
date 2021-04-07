package com.example.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bottomNavigation extends AppCompatActivity {

    FirebaseAuth fAuthentication;
    Handler handler = new Handler();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String senderuserID,reciveruserID;
    SenderModle senderModle = new SenderModle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fAuthentication = FirebaseAuth.getInstance();
        senderuserID = fAuthentication.getCurrentUser().getUid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseDatabase.getReference().child("sendRequest").child(senderuserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println(snapshot);
                        System.out.println(snapshot.exists());
                        if(snapshot.exists()){
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                senderModle = snapshot1.getValue(SenderModle.class);
                                System.out.println(senderuserID);
                                reciveruserID = senderModle.getReciverID();
                                System.out.println(senderModle.getSenderState());
                                System.out.println(senderModle.getSenderState().equals("received"));
                                if(senderModle.getSenderState().equals("accepted")){
                                    firebaseDatabase.getReference().child("sendRequest").child(senderuserID).child(reciveruserID.trim()).removeValue();
                                    firebaseDatabase.getReference().child("sendRequest").child(reciveruserID.trim()).child(senderuserID).removeValue();
                                    Intent intent = new Intent(getApplicationContext(),game.class);
                                    intent.putExtra("senderID",senderuserID);
                                    intent.putExtra("reciveID",reciveruserID);
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },500);

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
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();

    }

}