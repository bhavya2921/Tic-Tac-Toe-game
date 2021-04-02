package com.example.tic_tac_toe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    View fview;
    public static final String TAG = "TAG";
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;
    private String sender_STATE;
    private FirebaseDatabase firebaseDatabase;
    FirebaseAuth fAuth;
    String senderuserID,reciveruserID;
    FirebaseFirestore fStore;
    boolean requestSend = false;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        fview = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = fview.findViewById(R.id.recycle);
        sender_STATE = "no_request";
        firebaseDatabase = FirebaseDatabase.getInstance();




        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        senderuserID = fAuth.getCurrentUser().getUid();
        //user = fAuth.getCurrentUser();
        //username = view.findViewById(R.id.text);

        //images=new ArrayList();
        //name=new ArrayList();

        Query query = firebaseFirestore.collection("users");
        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query,ProductModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_item, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ProductModel model) {
                reciveruserID = model.getUserid().trim();
                if(!reciveruserID.trim().equals(senderuserID)){
                holder.userName.setText(model.getUsername()); }
                holder.sendRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reciveruserID = model.getUserid();
                        if(reciveruserID.trim().equals(senderuserID)){
                            Toast.makeText(getContext(), "YOU ARE SENDING REQUEST TO YOURSELF BEWAKOOF", Toast.LENGTH_SHORT).show();
                        } else {
                        firebaseDatabase.getReference().child("sendRequest").child(senderuserID).child(reciveruserID.trim()).setValue(new SenderModle(senderuserID,reciveruserID.trim(),"send"))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        firebaseDatabase.getReference().child("sendRequest").child(reciveruserID.trim()).child(senderuserID).setValue(new SenderModle(senderuserID,reciveruserID.trim(),"received"))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d(TAG,"onSuccess: Request is send to player "+ reciveruserID);
                                                    }
                                                });
                                    }
                                }); }




                        /*DocumentReference documentReference = fStore.collection("senderStatus").document(senderUserId);
                        Map<String,Object> user = new HashMap<>();
                        user.put("senderID",senderUserId);
                        user.put("reciverID",reciveruserID);
                        user.put("senderStat","SEND");
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"onSuccess: Request is send to player "+ reciveruserID);
                            }
                        });

                        DocumentReference documentReference2 = fStore.collection("reciverStatus").document(reciveruserID);
                        Map<String,Object> user2 = new HashMap<>();
                        user2.put("senderID",senderUserId);
                        user2.put("reciverID",reciveruserID);
                        user2.put("reciverStat","NOT_recived");
                        documentReference2.set(user2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"onSuccess: Request recived "+ reciveruserID);
                                holder.sendRequest.setText("cancel request");
                            }
                        }); */



                    }
                });
            }
        };


        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestoreList.setAdapter(adapter);


         //View Holder


        System.out.println(query);
        return fview;
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder{

        private TextView userName;
        private Button sendRequest;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.text);
            sendRequest = itemView.findViewById(R.id.newGame);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

}
