package com.example.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class gameoffline extends AppCompatActivity implements View.OnClickListener {

    private Button[][] boxes = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Point;
    private int player2Point;
    FirebaseFirestore fStore;
    String userID,buttonid;
    String previoususerid,previousText = "O";
    TextView username1,username2,wintext;
    FirebaseAuth fAuth;
    Button resetButton,leaveMatch;
    String matchPlayed,matchWon,matchLost;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    SenderModle senderModle = new SenderModle();
    Handler handler = new Handler();


    private TextView textViewPlayer1;

    private TextView textViewPlayer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameoffline);
        textViewPlayer1 = findViewById(R.id.player01Points);
        textViewPlayer2 = findViewById(R.id.player02Points);
        username1 = findViewById(R.id.player01);
        username2 = findViewById(R.id.player02);
        fStore = FirebaseFirestore.getInstance();
        wintext = findViewById(R.id.wintext);
        fAuth = FirebaseAuth.getInstance();
        leaveMatch = findViewById(R.id.leaveMatch);
        userID = fAuth.getCurrentUser().getUid().trim();
        resetButton = findViewById(R.id.resetButton);



        leaveMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),bottomNavigation.class);
                startActivity(intent);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
        System.out.println(userID);

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username1.setText(documentSnapshot.getString("username"));
            }
        });


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonid = "button" + i + j;
                int resID = getResources().getIdentifier(buttonid, "id", getPackageName());
                boxes[i][j] = findViewById(resID);
                boxes[i][j].setOnClickListener(this);
            }
        }
    }



    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        System.out.println(previousText);
        if (player1Turn){
            ((Button) v).setText("X");
        }
        else {
            ((Button) v).setText("O");
        }


        roundCount++;
        //else {
        //      Toast.makeText(this, "Please wait for your turn", Toast.LENGTH_SHORT).show();
        //   }
        if (checkWin()) {
            if (player1Turn) {
                player1Win();
            } else {
                player2Win();
            }
        } else if (roundCount == 9){
            draw();
        } else {
            player1Turn = !player1Turn;
        }

    }
    private boolean checkWin() {
        String[][] field = new String[3][3];

        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                field[i][j] = boxes[i][j].getText().toString();
            }
        }
        for (int i=0;i<3;i++){
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")){
                return true;
            }
        }

        for (int i=0;i<3;i++){
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")){
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")){
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")){
            return true;
        }

        return false;
    }

    private void player1Win(){
        player1Point++;
        Toast.makeText(this, username1.getText().toString()+" WON!", Toast.LENGTH_LONG).show();
        updatePointsText();
        resetBoard();
    }
    private void player2Win(){
        player2Point++;
        wintext.setVisibility(View.VISIBLE);
        //  if (userID.equals(reciveruserID)){
        //     wintext.setText("Congratulation!!\n\n"+" You Won");}
        //  else {
        //      wintext.setText("Sorry!!\n\n" + "You Lost");
        //     }
        Toast.makeText(this, "PLAYER 2 WON! ", Toast.LENGTH_LONG).show();
        updatePointsText();
        resetBoard();
    }
    private void draw(){
        wintext.setVisibility(View.VISIBLE);
        wintext.setText("DRAW!!");
        Toast.makeText(this, "MATCH DRAW! ", Toast.LENGTH_LONG).show();
        wintext.setVisibility(View.GONE);
        resetBoard();
    }
    private void updatePointsText(){
        textViewPlayer1.setText(" "+player1Point);
        textViewPlayer2.setText(" "+player2Point);
    }
    public void resetBoard(){
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                boxes[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
    }
    public void resetbutton(View view){
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                boxes[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
    }

    public void delay(int milisec){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(milisec);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }; runnable.run();
    }
}