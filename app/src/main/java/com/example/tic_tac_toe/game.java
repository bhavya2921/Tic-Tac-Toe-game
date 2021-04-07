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
import android.widget.RelativeLayout;
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

public class game extends AppCompatActivity implements View.OnClickListener {

    private Button[][] boxes = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int senderPoint;
    private int reciverPoint;
    FirebaseFirestore fStore;
    String userID,buttonid;
    String previoususerid,previousText = "O";
    boolean reciverturn = true, previousidsender= true, previoustextO = true,reciverturnS = true, previousidsenderS= true, previoustextOS = true;
    TextView username1,username2,wintext,turnText1,turnText2;
    RelativeLayout popup;
    String senderuserID,reciveruserID,turn = "reciver";
    FirebaseAuth fAuth;
    Button button00,button01,button02,button10,button11,button12,button20,button21,button22;
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
        setContentView(R.layout.activity_game);
        textViewPlayer1 = findViewById(R.id.player01Points);
        textViewPlayer2 = findViewById(R.id.player02Points);
        username1 = findViewById(R.id.player01);
        username2 = findViewById(R.id.player02);
        fStore = FirebaseFirestore.getInstance();
        senderuserID = getIntent().getStringExtra("senderID");
        reciveruserID = getIntent().getStringExtra("reciveID");
        previoususerid = senderuserID;
        wintext = findViewById(R.id.wintext);
        turnText1 = findViewById(R.id.turnText1);
        turnText2 = findViewById(R.id.turnText2);
        popup = findViewById(R.id.popup);
        fAuth = FirebaseAuth.getInstance();
        leaveMatch = findViewById(R.id.leaveMatch);
        userID = fAuth.getCurrentUser().getUid().trim();
        resetButton = findViewById(R.id.resetButton);
        button00 = findViewById(R.id.button00);
        button01 = findViewById(R.id.button01);
        button02 = findViewById(R.id.button02);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button20 = findViewById(R.id.button20);
        button21 = findViewById(R.id.button21);
        button22 = findViewById(R.id.button22);


        leaveMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),bottomNavigation.class);
                startActivity(intent);
                firebaseDatabase.getReference().child("sendRequest").child(senderuserID).child(reciveruserID.trim()).removeValue();
                firebaseDatabase.getReference().child("sendRequest").child(reciveruserID.trim()).child(senderuserID).removeValue();
            }
        });

        firebaseDatabase.getReference().child("sendRequest").child(senderuserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Toast.makeText(game.this, "Other player leaves the game", Toast.LENGTH_SHORT).show();
                    delay(2000);
                    Intent intent = new Intent(getApplicationContext(),bottomNavigation.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
        System.out.println(senderuserID+ "-------------");
        System.out.println(senderuserID==null);


       if(!(senderuserID==null)){
        System.out.println(senderuserID+"set----------------------------");

        DocumentReference documentReference0 = fStore.collection("game").document(senderuserID);
        Map<String,Object> user0 = new HashMap<>();
        user0.put(R.id.button00+"","");
        user0.put(R.id.button01+"","");
        user0.put(R.id.button02+"","");
        user0.put(R.id.button10+"","");
        user0.put(R.id.button11+"","");
        user0.put(R.id.button12+"","");
        user0.put(R.id.button20+"","");
        user0.put(R.id.button21+"","");
        user0.put(R.id.button22+"","");
        user0.put("senderPoint","0");
        user0.put("reciverPoint","0");
        user0.put("previousidsender",previousidsenderS);
        user0.put("previoustextO",previoustextOS);
        user0.put("roundcount","0");
        user0.put("reciverturn",reciverturnS);
        user0.put("win","");
    //    user0.put("previoususerid",senderuserID);
           documentReference0.set(user0).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   System.out.println("success");
               }
           });

           updateScreen();


        DocumentReference documentReference = fStore.collection("users").document(senderuserID);
        documentReference.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username1.setText(documentSnapshot.getString("username"));
            }
        });

        DocumentReference documentReference2 = fStore.collection("users").document(reciveruserID);
        documentReference2.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username2.setText(documentSnapshot.getString("username"));
            }
        }); }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonid = "button" + i + j;
                int resID = getResources().getIdentifier(buttonid, "id", getPackageName());
                boxes[i][j] = findViewById(resID);
                boxes[i][j].setOnClickListener(this);
            }
        }
    }

    public void updateScreen(){
         update.run();
    }

    private Runnable update = new Runnable() {
        @Override
        public void run() {

                    DocumentReference documentReference4 = fStore.collection("game").document(senderuserID);
                    documentReference4.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                            button00.setText(documentSnapshot.getString(R.id.button00+""));
                            button01.setText(documentSnapshot.getString(R.id.button01+""));
                            button02.setText(documentSnapshot.getString(R.id.button02+""));
                            button10.setText(documentSnapshot.getString(R.id.button10+""));
                            button11.setText(documentSnapshot.getString(R.id.button11+""));
                            button12.setText(documentSnapshot.getString(R.id.button12+""));
                            button20.setText(documentSnapshot.getString(R.id.button20+""));
                            button21.setText(documentSnapshot.getString(R.id.button21+""));
                            button22.setText(documentSnapshot.getString(R.id.button22+""));
                        //    previousText = documentSnapshot.getString("previousText");
                        //    previoususerid = documentSnapshot.getString("previousuesrid");
                            roundCount = Integer.parseInt( documentSnapshot.getString("roundcount"));
                            senderPoint = Integer.parseInt(documentSnapshot.getString("senderPoint"));
                            reciverPoint = Integer.parseInt(documentSnapshot.getString("reciverPoint"));
                            textViewPlayer1.setText(" "+senderPoint);
                            textViewPlayer2.setText(" "+ reciverPoint);
                            previousidsender = documentSnapshot.getBoolean("previousidsender");
                            previoustextO = documentSnapshot.getBoolean("previoustextO");
                            reciverturn = documentSnapshot.getBoolean("reciverturn");
                        //    turn = documentSnapshot.getString("turn");
                            if (reciverturn){
                                turnText1.setVisibility(View.GONE);
                                turnText2.setVisibility(View.VISIBLE);
                            } else{
                                turnText2.setVisibility(View.GONE);
                                turnText1.setVisibility(View.VISIBLE);
                            }
                            if(documentSnapshot.getString("win").equals("sender")){
                                Toast.makeText(game.this, username1.getText().toString()+" WON!", Toast.LENGTH_SHORT).show();
                            }
                                if((documentSnapshot.getString("win")).equals("reciver")){
                                    Toast.makeText(game.this, username2.getText().toString()+" WON", Toast.LENGTH_SHORT).show();
                                }

                            }


                        }
                    });
                            handler.postDelayed(this,100);
        }
    };



    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        System.out.println(previousText);
        System.out.println(previoususerid);
        System.out.println(userID.equals(senderuserID) + "qqqqqqqqqqqqqqqqqqq");
        System.out.println(previoususerid);
        System.out.println(userID);

        if ((senderuserID.equals(userID) && !previousidsender) || (reciveruserID.equals(userID) && previousidsender)){
      //  if (!previoususerid.equals(userID)) {
            if (previoustextO){
            ((Button) v).setText("X");

            System.out.println(previousText);
            DocumentReference documentReference6 = fStore.collection("game").document(senderuserID);
            documentReference6.update("previoustextO",false);
            documentReference6.update("previousidsender",false);
            documentReference6.update("reciverturn",false);
            previoustextO = false;
            previousText = "X";
            previousidsender = false;
            previoususerid = reciveruserID;
        }
            else {
                ((Button) v).setText("O");
                previousText = "O";
                previoustextO = true;
                DocumentReference documentReference7 = fStore.collection("game").document(senderuserID);
                documentReference7.update("previoustextO",true);
                documentReference7.update("previousidsender",true);
                documentReference7.update("reciverturn",true);
                previousidsender = true;
                previoususerid = senderuserID;
                System.out.println(previousText);
            }

        }  else {
            Toast.makeText(this, "Please wait for your turn", Toast.LENGTH_SHORT).show();
            return;
        }
        roundCount++;
        DocumentReference documentReference2 = fStore.collection("game").document(senderuserID);
        documentReference2.update(((Button) v).getId()+"",previousText);
        documentReference2.update("roundcount",roundCount+"");


        if (checkWin()) {
            if (previousidsender){
         //   if (previoususerid.equals(senderuserID)) {
                senderWin();
            } else {
                reciverWin();
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

    public void setDataToFirebase(){
        firebaseDatabase.getReference().child("game").child(senderuserID).child(reciveruserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              //  firebaseDatabase.getReference().child("game").child(senderuserID).child(reciveruserID)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void senderWin(){
        System.out.println("Sender win");
        DocumentReference documentReference2 = fStore.collection("game").document(senderuserID);
        documentReference2.update("senderPoint",(senderPoint+1)+"");
        documentReference2.update("win","sender");
        Toast.makeText(this, username1.getText().toString()+" WON!", Toast.LENGTH_LONG).show();
        updatePointsText();
        resetBoard();
    }
    private void reciverWin(){
        System.out.println("Reciver won");
        DocumentReference documentReference2 = fStore.collection("game").document(senderuserID);
        documentReference2.update("reciverPoint",(reciverPoint+1)+"");
        documentReference2.update("win","reciver");
        Toast.makeText(this, username2.getText().toString()+" WON!", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "PLAYER 2 WON! ", Toast.LENGTH_LONG).show();
        updatePointsText();
        resetBoard();
    }
    private void draw(){
        Toast.makeText(this, "MATCH DRAW! ", Toast.LENGTH_LONG).show();
        resetBoard();
    }
    private void updatePointsText(){
        textViewPlayer1.setText(" "+senderPoint);
        textViewPlayer2.setText(" "+ reciverPoint);
    }
    public void resetBoard(){
        reciverturnS = !reciverturnS;
        previousidsenderS = !previousidsenderS;
        previoustextOS = !previoustextOS;
        DocumentReference documentReference0 = fStore.collection("game").document(senderuserID);
        documentReference0.update(R.id.button00+"","");
        documentReference0.update(R.id.button01+"","");
        documentReference0.update(R.id.button02+"","");
        documentReference0.update(R.id.button10+"","");
        documentReference0.update(R.id.button11+"","");
        documentReference0.update(R.id.button12+"","");
        documentReference0.update(R.id.button20+"","");
        documentReference0.update(R.id.button21+"","");
        documentReference0.update(R.id.button22+"","");
        documentReference0.update("reciverturn",reciverturnS);
       // documentReference0.update("senderPoint","0");
       // documentReference0.update("reciverPoint","0");
        documentReference0.update("previousidsender",previousidsenderS);
        documentReference0.update("previoustextO",previoustextOS);
        documentReference0.update("roundcount","0");
        documentReference0.update("win","");
        roundCount = 0;
        player1Turn = true;
    }
    public void resetbutton(View view){
        resetBoard();
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