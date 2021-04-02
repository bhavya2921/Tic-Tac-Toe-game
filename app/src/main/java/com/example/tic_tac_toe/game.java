package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class game extends AppCompatActivity implements View.OnClickListener {

    private Button[][] boxes = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Point;
    private int player2Point;
    FirebaseFirestore fStore;
    String userID;
    TextView username1,username2;
    FirebaseAuth fAuth;
    Button resetButton;
    String matchPlayed,matchWon,matchLost;


    private TextView textViewPlayer1;

    private TextView textViewPlayer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        textViewPlayer1 = findViewById(R.id.player01Points);
        textViewPlayer2 = findViewById(R.id.player02Points);
        username1 = findViewById(R.id.player01);
        username2 = findViewById(R.id.player02);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });



                DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username1.setText(documentSnapshot.getString("username"));
            }
        });




        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonid = "button" + i + j;
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
        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
        roundCount++;
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
        Toast.makeText(this, "PLAYER 2 WON! ", Toast.LENGTH_LONG).show();
        updatePointsText();
        resetBoard();
    }
    private void draw(){
        Toast.makeText(this, "MATCH DRAW! ", Toast.LENGTH_LONG).show();
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
}