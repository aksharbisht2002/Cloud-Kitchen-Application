package com.example.major_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    TextView name_tv, phone_tv, email_tv, address_tv;
    ImageView noOrder,profilepic,editBtn,backBtn;
    ShimmerFrameLayout loader;
    Switch Biometric;
    RecyclerView orderhistory_rv;
    SharedPreferences preferences;
    DatabaseReference userOrderHistoryReference = FirebaseDatabase.getInstance().getReference("UserOrderHistory");
    DatabaseReference userRefernce = FirebaseDatabase.getInstance().getReference("Users");
    boolean isAuth;
    String CustomerId,name,phone,email,address;
    int profilepicCode;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name_tv = findViewById(R.id.name);
        phone_tv = findViewById(R.id.phone);
        email_tv = findViewById(R.id.email);
        address_tv = findViewById(R.id.address);
        profilepic = findViewById(R.id.profilepic);
        Biometric = findViewById(R.id.biometricSwitch);
        backBtn = findViewById(R.id.navIcon);
        editBtn = findViewById(R.id.cartIcon);
        loader = findViewById(R.id.Shimmerloader);
        orderhistory_rv = findViewById(R.id.orderhistory_rv);
        noOrder = findViewById(R.id.no_order_historyimg);
        loader.setVisibility(View.GONE);

        preferences = getSharedPreferences("DelhiGarden",MODE_PRIVATE);

        isAuth = preferences.getBoolean("Auth",false);
        CustomerId=preferences.getString("CustomerId",null);
        name= preferences.getString("name",null);
        email=preferences.getString("email",null);
        phone=preferences.getString("phone",null);
        address = preferences.getString("address",null);
        profilepicCode = Integer.parseInt(preferences.getString("profilepic",String.valueOf(R.drawable.person2)));

        if (isAuth){Biometric.setChecked(true);}else {Biometric.setChecked(false);}
        if (address==null){address_tv.setText("No Address Provided");}else {address_tv.setText(address);}
        name_tv.setText(name);
        phone_tv.setText(phone);
        email_tv.setText(email);
        profilepic.setImageResource(profilepicCode);


        userRefernce.child(CustomerId).child("profileImg").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.exists()){
                 loader.startShimmer();
                 loader.setVisibility(View.VISIBLE);
                 profilepic.setVisibility(View.GONE);
                 Picasso.get().load(snapshot.getValue(String.class)).placeholder(profilepicCode).into(profilepic, new Callback() {
                     @Override
                     public void onSuccess() {
                         loader.stopShimmer();
                         loader.setVisibility(View.GONE);
                         profilepic.setVisibility(View.VISIBLE);
                     }
                     @Override
                     public void onError(Exception e) {}
                 });
             }  else {
                 return;
             }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        userOrderHistoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.exists()){
                 noOrder.setVisibility(View.GONE);
                 orderhistory_rv.setVisibility(View.VISIBLE);


             } else {
                 noOrder.setVisibility(View.VISIBLE);
                 orderhistory_rv.setVisibility(View.GONE);
             }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this,EditProfile.class));
            }
        });

        Biometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth){Biometric.setChecked(true);}else {Biometric.setChecked(false);}
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Home.class));
            }
        });

    }
}