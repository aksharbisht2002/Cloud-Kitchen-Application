package com.example.major_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrackOrder extends AppCompatActivity {
    TextView orderid;
    TextView status;
    TextView price;
    TextView mode;
    TextView itemcount;
    TextView deliveryboy_name;
    TextView customer_address;
    TextView nodata;
    ImageView backBtn;
    CircleImageView deliveryboy_profilepic,deliveryboy_call;
    LinearLayout expand_Layout,contentLayout;
    SharedPreferences preferences;
    ShimmerFrameLayout loader;
    DatabaseReference userorderReference = FirebaseDatabase.getInstance().getReference("UserOrders");
    DatabaseReference managerorderRefernce = FirebaseDatabase.getInstance().getReference("ManagerOrders");
    DatabaseReference deliveryboyReference =FirebaseDatabase.getInstance().getReference("DeliveryBoy");
    private String CustomerId;
    private String OrderId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order2);
        preferences =getSharedPreferences("DelhiGarden", Context.MODE_PRIVATE);
        CustomerId = preferences.getString("CustomerId",null);
        OrderId = preferences.getString("orderId","");


        orderid = findViewById(R.id.orderId);
        nodata = findViewById(R.id.nodata);
        itemcount = findViewById(R.id.order_totalItems);
        status = findViewById(R.id.orderStatus);
        price = findViewById(R.id.order_totalprice);
        mode =findViewById(R.id.order_paymentMode);
        deliveryboy_call = findViewById(R.id.deveryboy_callBtn);
        customer_address = findViewById(R.id.userAddress);
        backBtn = findViewById(R.id.backBtn);

        deliveryboy_name = findViewById(R.id.deliveryboy_name);
        deliveryboy_profilepic = findViewById(R.id.deliveryboy_profile_img);

        expand_Layout=findViewById(R.id.orderLayout_expand);
        contentLayout=findViewById(R.id.contentLayout);
        loader=findViewById(R.id.Shimmerloader);

        if( OrderId.isEmpty()){
            nodata.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
        }else {
            nodata.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
        }

        expand_Layout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
        loader.startShimmer();

        nodata.setVisibility(View.GONE);

        orderid.setText(OrderId);


        userorderReference.child(CustomerId).child(OrderId.substring(1)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.exists()){
                 status.setText(snapshot.child("status").getValue(String.class));
                 price.setText(snapshot.child("price").getValue(String.class));
                 mode.setText(snapshot.child("mop").getValue(String.class));
                 userorderReference.child(CustomerId).child(OrderId.substring(1)).child("Items").addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                      itemcount.setText(String.valueOf(snapshot.getChildrenCount()));
                         loader.setVisibility(View.GONE);
                         contentLayout.setVisibility(View.VISIBLE);
                         loader.startShimmer();
                     }
                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {}
                 });



             }  else {
                 nodata.setVisibility(View.VISIBLE);
                 contentLayout.setVisibility(View.GONE);
                 loader.setVisibility(View.GONE);
             }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        userorderReference.child(CustomerId).child(OrderId.substring(1)).child("status").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String val = snapshot.getValue(String.class);
                    status.setText(val);
                    if (val.equals("confirming")){
                        return;
                    }
                    else if (val.equals("preparing food")){
                        status.setText(val);
                    }else if (val.equals("On Delivery")){
                        managerorderRefernce.child("did").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String did = snapshot.getValue(String.class);
                                deliveryboyReference.child(did).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                     String name,img,phone;
                                     name=snapshot.child("name").getValue(String.class);
                                     img=snapshot.child("image").getValue(String.class);
                                     phone=snapshot.child("phone").getValue(String.class);
                                     deliveryboy_name.setText(name);
                                     Picasso.get().load(img).placeholder(R.drawable.person2).into(deliveryboy_profilepic);
                                     expand_Layout.setVisibility(View.VISIBLE);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });

                    }else {
                        nodata.setVisibility(View.VISIBLE);
                        contentLayout.setVisibility(View.GONE);
                        expand_Layout.setVisibility(View.GONE);
                    }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackOrder.this,Home.class));
            }
        });

    }
}