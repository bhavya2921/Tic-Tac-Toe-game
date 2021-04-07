 package com.example.tic_tac_toe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestoreDataSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.ViewHolder> {
    Context context;
    ArrayList<SenderModle> arrayList;
    TextView textview;
    ImageView imageView;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    Button declineButton;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String reciveruserID = fAuth.getCurrentUser().getUid().trim();
    String senderuserID;
    game modlegame = new game();


    public MyAdaptor(Context context,ArrayList<SenderModle> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_item3,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SenderModle senderModle = arrayList.get(position);
        DocumentReference documentReferenceget = fStore.collection("users").document(senderModle.getSenderID());
        documentReferenceget.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                holder.textview.setText(documentSnapshot.getString("username"));
                senderuserID = senderModle.getSenderID();
                System.out.println(documentSnapshot.getString("username"));
            }
        });
        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.getReference().child("sendRequest").child(senderuserID).child(reciveruserID.trim()).removeValue();
                firebaseDatabase.getReference().child("sendRequest").child(reciveruserID.trim()).child(senderuserID).removeValue();
            }
        });
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.getReference().child("sendRequest").child(senderuserID).child(reciveruserID.trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            firebaseDatabase.getReference().child("sendRequest").child(senderuserID).child(reciveruserID.trim()).
                                    setValue(new SenderModle(senderuserID, reciveruserID.trim(), "accepted")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(context.getApplicationContext(),game.class);
                intent.putExtra("senderID",senderuserID);
                intent.putExtra("reciveID",reciveruserID);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView textview;
        Button declineButton,acceptButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview=(ImageView)itemView.findViewById(R.id.image);
            textview= itemView.findViewById(R.id.senderUsername);
            declineButton = itemView.findViewById(R.id.declineButton);
            acceptButton = itemView.findViewById(R.id.acceptButton);
        }
    }
    
}