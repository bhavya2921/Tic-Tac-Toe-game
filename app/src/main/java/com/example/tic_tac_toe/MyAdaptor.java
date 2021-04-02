 package com.example.tic_tac_toe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
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
                System.out.println(documentSnapshot.getString("username"));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview=(ImageView)itemView.findViewById(R.id.image);
            textview= itemView.findViewById(R.id.senderUsername);
        }
    }
    
}