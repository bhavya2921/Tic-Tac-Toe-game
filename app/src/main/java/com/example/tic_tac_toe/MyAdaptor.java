 package com.example.tic_tac_toe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdaptor<onStop> extends RecyclerView.Adapter {
    Context context;
    ArrayList arrayList,arrayListName;

    public MyAdaptor(Context context,ArrayList arrayList){
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListName =  arrayListName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_item,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;
        viewHolderClass.imageview.setImageResource((Integer) arrayList.get(position));
        viewHolderClass.textview.setText(arrayListName.get(position).toString());
        viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListName.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView textview;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            imageview=(ImageView)itemView.findViewById(R.id.image);
            textview= (TextView)itemView.findViewById(R.id.text);
        }
    }

    
}