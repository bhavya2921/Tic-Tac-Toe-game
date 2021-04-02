package com.example.tic_tac_toe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


public class ProfileFragment extends Fragment {

    View fview;
    TextView fullName,username,email,verifyText,gamePlayed,gameWon,gameLost;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    public String usernameforblog;
    Button resendCode,playOffline;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fview = inflater.inflate(R.layout.fragment_profile, container, false);
        fullName = fview.findViewById(R.id.userProfileFullNmae);
        username = fview.findViewById(R.id.userProfileusername);
        email = fview.findViewById(R.id.userProfileEmail);
        playOffline = fview.findViewById(R.id.playOffline);
        gamePlayed = fview.findViewById(R.id.noMatchPlayed);
        gameWon = fview.findViewById(R.id.noMatchWon);
        gameLost = fview.findViewById(R.id.noMatchLost);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String usergamePlayed = gamePlayed.getText().toString();
        String usergameWon = gameWon.getText().toString();
        String usergameLost = gameLost.getText().toString();


        userID = fAuth.getCurrentUser().getUid();

        resendCode = fview.findViewById(R.id.verifyButton);
        verifyText = fview.findViewById(R.id.userProfileVerificationText);
        user = fAuth.getCurrentUser();
        playOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(),game.class);
                startActivity(intent);
            }
        });

        if(!user.isEmailVerified()) {
            resendCode.setVisibility(View.VISIBLE);
            verifyText.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Verification email has been send to your email ID", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure; Email not sent" + e.getMessage());
                        }
                    });
                }
            });

        }
        else {
            resendCode.setVisibility(View.GONE);
            verifyText.setVisibility(View.GONE);
        }

        DocumentReference documentReferenceget = fStore.collection("users").document(userID);
        documentReferenceget.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.getString("username"));
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
            }
        });
        DocumentReference documentReference = fStore.collection("gameStatestics").document(userID);
        documentReference.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
           @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                gamePlayed.setText(documentSnapshot.getString("Gplay"));
                gameWon.setText(documentSnapshot.getString("Gwon"));
                gameLost.setText(documentSnapshot.getString("Glost"));
           }
        });


        return fview;
    }
    public void userProfileLogout(View view) {
        fAuth.getInstance().signOut();

        startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
        getActivity().finish();
    }

}