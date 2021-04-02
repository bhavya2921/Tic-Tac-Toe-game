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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RequestFragment extends Fragment {

    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    TextView username;
    View requestview;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    String senderuserID,reciveruserID;
    RecyclerView recyclerView;
    ArrayList<SenderModle> requests;
    SenderModle senderModle ;



    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requestview = inflater.inflate(R.layout.fragment_request, container, false);
        requests = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = requestview.findViewById(R.id.recycle2);
        fAuth = FirebaseAuth.getInstance();
        reciveruserID = fAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        recyclerView = requestview.findViewById(R.id.requestrecycle);


        MyAdaptor myAdaptor = new MyAdaptor(getContext(),requests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdaptor);
        System.out.println(reciveruserID);
        firebaseDatabase.getReference().child("sendRequest").child(reciveruserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                requests.clear();
                System.out.println(snapshot);
                System.out.println(snapshot.exists());
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        senderModle = snapshot1.getValue(SenderModle.class);
                        System.out.println(reciveruserID);
                        System.out.println(senderModle.getSenderState());
                        System.out.println(senderModle.getSenderState().equals("received"));
                        if(senderModle.getSenderState().equals("received")){
                            requests.add(senderModle);
                        }
                    }
                }
                myAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return requestview;

    }
}