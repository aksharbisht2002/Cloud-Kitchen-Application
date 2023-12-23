package com.example.major_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.major_project.Adapter.CartAdapter;
import com.example.major_project.Adapter.CartExtraAdapter;
import com.example.major_project.Fragments.CheckOut;
import com.example.major_project.Helper.CartHelper;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.CartModel;
import com.example.major_project.Model.FoodModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    ImageView backBtn;
    TextView counter,subprce,grandprice,total;
    RecyclerView cart_rv,extra_rv;
    LinearLayout loader;
    ConstraintLayout dataLayout,nodataLayout;
    UserChoosenFood userChoosenFood;
    Button checkoutBtn;
    DatabaseReference demandReferece = FirebaseDatabase.getInstance().getReference("Demand");
    DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference("Food");
    ArrayList<CartModel> list = new ArrayList<>();
    CartAdapter cartAdapter;
    CartExtraAdapter cartExtraAdapter;
    String CustomerId;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);

        loader = findViewById(R.id.cartLoader);
        backBtn = findViewById(R.id.backBtn);
        counter = findViewById(R.id.cart_counter);
        subprce = findViewById(R.id.subprice);
        grandprice = findViewById(R.id.grandtotal);
        total = findViewById(R.id.totalprice);
        cart_rv = findViewById(R.id.cart_rv);
        extra_rv = findViewById(R.id.cart_extra_rv);
        checkoutBtn = findViewById(R.id.checkoutBtn);
        dataLayout = findViewById(R.id.dataLayout);
        nodataLayout = findViewById(R.id.nodataLayout);

        if (!(grandprice.getText().toString()).equals(null)){
            total.setText(grandprice.getText().toString());
        }

        preferences =this.getSharedPreferences("DelhiGarden", Context.MODE_PRIVATE);
        CustomerId = preferences.getString("CustomerId",null);


        cart_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        cartAdapter = new CartAdapter(this,subprce,grandprice,counter,total,dataLayout,nodataLayout,loader,userChoosenFood);
        cartAdapter.notifyDataSetChanged();
        cart_rv.setAdapter(cartAdapter);

        extra_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        cartExtraAdapter = new CartExtraAdapter(this,list,userChoosenFood,cartAdapter,loader);
        extra_rv.setAdapter(cartExtraAdapter);

        int data = ((UserChoosenFood) CartHelper.getmyContext()).getListSize();
        if (data==0){
            nodataLayout.setVisibility(View.VISIBLE);
            dataLayout.setVisibility(View.GONE);
        }else {
            loader.setVisibility(View.GONE);
            dataLayout.setVisibility(View.VISIBLE);
        }

        demandReferece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot data: snapshot.getChildren()) {
                        String fid = data.child("fid").getValue(String.class);
                        foodReference.child(fid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    FoodModel model = snapshot.getValue(FoodModel.class);
                                    String foodid = model.getFid();
                                    String name = model.getName();
                                    String price = model.getPrice();
                                    String img = model.getImgUri();
                                    String type = model.getType();
                                    list.add(new CartModel(foodid,name,price,img,type));
                                    if (cartExtraAdapter!=null){
                                        cartExtraAdapter.notifyDataSetChanged();
                                    }
                                }  else return;
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                }else {return;}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckOut checkout =new CheckOut();
                Bundle b = new Bundle();
                b.putString("price",total.getText().toString());
                b.putString("count",String.valueOf(((UserChoosenFood) CartHelper.getmyContext()).getListSize()));
                checkout.setArguments(b);
                getSupportFragmentManager().beginTransaction().add(R.id.cartLayout,checkout).commit();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}