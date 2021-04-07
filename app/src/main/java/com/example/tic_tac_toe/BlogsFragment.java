package com.example.tic_tac_toe;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class BlogsFragment extends Fragment {

    public static final String TAG = "TAG";
    EditText userBlog;
    FloatingActionButton postButton;
    FirebaseAuth fAuthentication;
    int noOfLike;
    String userID;
    View blogview;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;
    String usernameString;
    FirebaseDatabase firebaseDatabase;
    boolean blogIsLiked = false;
    ArrayList<SenderModle> blogs;
    SenderModle senderModle ;


    public BlogsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        blogview = inflater.inflate(R.layout.fragment_blogs, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        blogs = new ArrayList<>();
        mFirestoreList = blogview.findViewById(R.id.recycle2);
        fAuthentication = FirebaseAuth.getInstance();
        userID = fAuthentication.getCurrentUser().getUid();


        postButton = blogview.findViewById(R.id.postButton);
        userBlog = blogview.findViewById(R.id.blogText);


        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usernameString = documentSnapshot.getString("username");
                System.out.println(usernameString+ "   aaaaaaaaaaa");
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userBlogString = userBlog.getText().toString().trim();
                if(TextUtils.isEmpty(userBlogString)) {
                    userBlog.setError("please write your blog");
                    return;
                }
                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                documentReference.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        usernameString = documentSnapshot.getString("username");


              //  String usernameString = username.getText().toString();

                String blogid = userID+ " " + (int)(Math.random()*100000);
                DocumentReference documentReference2 = firebaseFirestore.collection("usersBlog").document(blogid);
                userID = fAuthentication.getCurrentUser().getUid();
                Map<String,Object> user = new HashMap<>();
                user.put("blog",userBlogString);
                user.put("username",usernameString);
                user.put("userid",userID);
                user.put("blogid",blogid);
                user.put("noOfLike","0");

                documentReference2.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"onSuccess: blog posted "+ userID);
                        Toast.makeText(getContext(), "BLOG POSTED ", Toast.LENGTH_SHORT).show();
                        userBlog.setText(null);
                    }
                });

            }
        });
            }
        });

        Query query = firebaseFirestore.collection("usersBlog");
        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query,ProductModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductModel, BlogsFragment.ProductsViewHolder>(options) {


            @NonNull
            @Override
            public BlogsFragment.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_item2, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BlogsFragment.ProductsViewHolder holder, int position, @NonNull ProductModel model) {
                System.out.println(model.getUsername() + "-----------------------------" + model.getNoOfLike());


                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                documentReference.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        usernameString = documentSnapshot.getString("username");
                        System.out.println(usernameString+ "   aaaaaaaaaaa");
                    }
                });

                holder.userName.setText(model.getUsername());
                holder.noOfLikeText.setText(model.getNoOfLike());

                System.out.println(usernameString + "direct");
                System.out.println(model.getUserid());
                System.out.println(userID.equals(model.getUserid()));
                if (userID.equals(model.getUserid())){
                    holder.deleteButton.setVisibility(View.VISIBLE);
                    holder.editButton.setVisibility(View.VISIBLE);
                }
                if (blogIsLiked) {
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24);
                }
                holder.userPostedBlog.setText(model.getBlog());

                holder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference documentReference2 = firebaseFirestore.collection("usersBlog").document(model.getBlogid());
                        documentReference2.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                userBlog.setText(documentSnapshot.getString("blog"));
                                System.out.println(documentSnapshot.getString("blog"));
                                documentReference2.delete();
                            }
                        });

                    }
                });

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference documentReference2 = firebaseFirestore.collection("usersBlog").document(model.getBlogid());
                        documentReference2.delete();
                        System.out.println(model.getUserid()+"  12  12");
                    }
                });

                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noOfLike = Integer.parseInt(model.getNoOfLike());
                        if(!blogIsLiked){
                        noOfLike+=1;
                        }
                        blogIsLiked = true;
                        if (blogIsLiked) {
                            holder.likeButton.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24);
                        }
                        DocumentReference documentReference2 = firebaseFirestore.collection("usersBlog").document(model.getBlogid());
                        userID = fAuthentication.getCurrentUser().getUid();
                        //Map<String,Object> user = new HashMap<>();
                        System.out.println(noOfLike+"11111111");
                        //user.put("noOfLike",""+noOfLike);
                        documentReference2.update("noOfLike",""+noOfLike);
                        System.out.println(model.getNoOfLike()+"2222222222222");
                        holder.noOfLikeText.setText(model.getNoOfLike());

                    }
                });

            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestoreList.setAdapter(adapter);



        //View Holder

        return blogview;
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder{

        private TextView userName;
        private TextView userPostedBlog;
        private ImageButton deleteButton;
        private ImageButton editButton;
        private ImageButton likeButton;
        private TextView noOfLikeText;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.blogUsername);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton =itemView.findViewById(R.id.editButton);
            userPostedBlog = itemView.findViewById(R.id.userBlog);
            likeButton = itemView.findViewById(R.id.likeButton);
            noOfLikeText = itemView.findViewById(R.id.noofLIKE);

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