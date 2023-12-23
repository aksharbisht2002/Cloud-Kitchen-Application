package com.example.major_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.major_project.Adapter.BestSellerAdapter;
import com.example.major_project.Adapter.FavouritesAdapter;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.FavouriteModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Favourite extends AppCompatActivity {
    RecyclerView rv;
    ImageView cartIcon,navIcon;
    ShimmerFrameLayout shimmerFrameLayout;
    FavouritesAdapter adapter;
    BestSellerAdapter bestSellerAdapter;
    DatabaseReference favouriteReference = FirebaseDatabase.getInstance().getReference("Favourite");
    DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference("Food");
    DatabaseReference outofstockReference = FirebaseDatabase.getInstance().getReference("Outofstock");

    ArrayList<FavouriteModel> list = new ArrayList<>() ;
    ArrayList<String> olist = new ArrayList<>();
    UserChoosenFood userChoosenFood;
    SharedPreferences preferences;
    String cid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        preferences =getSharedPreferences("DelhiGarden", Context.MODE_PRIVATE);
        cid = preferences.getString("CustomerId",null);

        rv=findViewById(R.id.favourite_rv);
        cartIcon =findViewById(R.id.cartIcon);
        navIcon = findViewById(R.id.navIcon);
        shimmerFrameLayout = findViewById(R.id.favourite_shimmer_layout);

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);
        shimmerFrameLayout.startShimmer();

        rv.setLayoutManager(new LinearLayoutManager(Favourite.this,LinearLayoutManager.VERTICAL,false));
        adapter =new FavouritesAdapter(this,cid,list,olist,favouriteReference,userChoosenFood);
        rv.setAdapter(adapter);

        favouriteReference.child(cid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot data:
                            snapshot.getChildren()) {
                        String fid = data.child("fid").getValue(String.class);
                        shimmerFrameLayout.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                               FavouriteModel model = data.getValue(FavouriteModel.class);
                               list.add(model);
                           if (adapter!=null){
                               adapter.notifyDataSetChanged();
                           }
                    }
                }else {return;}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        outofstockReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot data:snapshot.getChildren()) {
                        String fid = data.getValue(String.class);
                        olist.add(fid);
                        if (adapter!=null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                }else {return;}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        outofstockReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                outofstockReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot data:snapshot.getChildren()) {
                                String fid = data.getValue(String.class);
                                olist.add(fid);
                                if (adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }else {return;}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                outofstockReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot data:snapshot.getChildren()) {
                                String fid = data.getValue(String.class);
                                olist.add(fid);
                                if (adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }else {return;}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });



        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Favourite.this, Cart.class));
            }
        });
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(Favourite.this,Home.class));
            }
        });

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}