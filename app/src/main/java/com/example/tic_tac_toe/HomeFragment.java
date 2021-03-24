package com.example.tic_tac_toe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    View fview;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;

  /*  RecyclerView recyclerview;

    ArrayList images,name;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    TextView username;
    MyAdaptor adaptor; */


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        fview = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = fview.findViewById(R.id.recycle);


        //fStore = FirebaseFirestore.getInstance();

        //userID = fAuth.getCurrentUser().getUid();
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
                holder.userName.setText(model.getUsername());

            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestoreList.setAdapter(adapter);


         //View Holder

    /*
        for(int i=0;i<Data.names.length;i++){
            // images.add(Data.images);
            name.add(Data.names);
        }
        MyAdaptor myAdapter = new MyAdaptor();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(MyAdaptor); */
        System.out.println(query);
        return fview;
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder{

        private TextView userName;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.text);
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
