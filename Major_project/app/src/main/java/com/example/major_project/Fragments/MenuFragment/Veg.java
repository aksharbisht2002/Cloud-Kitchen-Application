package com.example.major_project.Fragments.MenuFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.major_project.Adapter.BestSellerAdapter;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.FoodModel;
import com.example.major_project.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Veg extends Fragment {
    String catagory = "Shakahari";
    DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference("Food");
    DatabaseReference favouriteReference = FirebaseDatabase.getInstance().getReference("Favourite");
    DatabaseReference outofstockReference = FirebaseDatabase.getInstance().getReference("Outofstock");
    SharedPreferences preferences;
    RecyclerView rv;
    ArrayList<FoodModel> list=new ArrayList<>();
    ArrayList<String> flist = new ArrayList<>();
    ArrayList<String> olist = new ArrayList<>();
    UserChoosenFood userChoosenFood;
    private String CustomerId;
    private BestSellerAdapter adapter;
    ShimmerFrameLayout shimmerFrameLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_veg, container, false);
        rv=view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        shimmerFrameLayout = view.findViewById(R.id.shimmerCard);
        preferences = getActivity().getSharedPreferences("DelhiGarden", Context.MODE_PRIVATE);
        CustomerId = preferences.getString("CustomerId",null);

        rv.setVisibility(View.INVISIBLE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        adapter = new BestSellerAdapter(getContext(),list,flist,olist,CustomerId,favouriteReference,userChoosenFood);
        rv.setAdapter(adapter);

        foodReference.orderByChild("category").equalTo(catagory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rv.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                for (DataSnapshot data :
                        snapshot.getChildren()) {
                    FoodModel model = data.getValue(FoodModel.class);
                    list.add(model);
                    if (adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        outofstockReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()){return;}
                else {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String fid = data.child("fid").getValue(String.class);
                        olist.add(fid);
                        if (adapter!=null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        favouriteReference.child(CustomerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot data:
                            snapshot.getChildren()) {
                        String fid = data.child("fid").getValue(String.class);
                        flist.add(fid);
                        if (adapter!=null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return  view;
    }
}