package com.example.major_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.major_project.Adapter.BestSellerAdapter;
import com.example.major_project.Adapter.CravingLayoutAdapter;
import com.example.major_project.Adapter.PreviusOrderAdapter;
import com.example.major_project.Fragments.AboutUs;
import com.example.major_project.Fragments.FAQ;
import com.example.major_project.Helper.CartHelper;
import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.CravingLayoutModel;
import com.example.major_project.Model.FoodModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

     private  int seleceted =2;
    String CustomerId;
     RecyclerView previus_rv,bestseller_rv;
     ShimmerFrameLayout previusorder_shimmer,cravingmenu_shimmer,bestseller_shimmer;
     GridView craving_layout;
     LinearLayout previusLayout,takeaway,delivery,menu,profile;
     ImageView cart;
     ImageView takeawaypic,deliverypic,navIcon;
     TextView takeawaytxt,deliverytxt;
     SharedPreferences preferences;
     NavigationView navigationView;
     DrawerLayout drawerLayout;
    ArrayList<CravingLayoutModel> cravingList = new ArrayList<>();
    CravingLayoutAdapter cravingAdapter;
    ArrayList<FoodModel> previusorderList=new ArrayList<>();
    ArrayList<FoodModel> bestsellerList = new ArrayList<>();
    ArrayList<String> favouriteList = new ArrayList<>();
    ArrayList<String> outofstockList =new ArrayList<>();
    PreviusOrderAdapter previusOrderAdapter;
    BestSellerAdapter bestSellerAdapter;
    UserChoosenFood userChoosenFood;
    BadgeDrawable badge;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference cravingmenuReference = FirebaseDatabase.getInstance().getReference("CravingMenu");
     DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference("Food");
     DatabaseReference previusorderReference = FirebaseDatabase.getInstance().getReference("Previusorder");
     DatabaseReference bestsellerReference = FirebaseDatabase.getInstance().getReference("Bestseller");
     DatabaseReference favouriteReference = FirebaseDatabase.getInstance().getReference("Favourite");
     DatabaseReference outofstockReference = FirebaseDatabase.getInstance().getReference("Outofstock");


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences =getSharedPreferences("DelhiGarden",Context.MODE_PRIVATE);
        preferences.edit().putInt("DeliveryType",seleceted).apply();
         CustomerId = preferences.getString("CustomerId",null);

        previusorder_shimmer = findViewById(R.id.previousOrderShimmer);
        cravingmenu_shimmer = findViewById(R.id.shimmer_craving_menu_list);
        bestseller_shimmer = findViewById(R.id.bestseller_shimmer);

        previus_rv = findViewById(R.id.previusitem_rv);
        previusLayout = findViewById(R.id.previousOrder_layout);

        bestseller_rv = findViewById(R.id.bestseller_rv);
        craving_layout = findViewById(R.id.craving_menu_list);

        takeaway = findViewById(R.id.takeaway);
        takeawaypic = findViewById(R.id.takeawayimg);
        takeawaytxt = findViewById(R.id.takeawaytxt);

        delivery = findViewById(R.id.delivery);
        deliverypic = findViewById(R.id.deliveryimg);
        deliverytxt = findViewById(R.id.deliverytxt);

        navIcon = findViewById(R.id.navIcon);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.home_drawerLayout);

        menu = findViewById(R.id.bottomnav_menu);
        profile = findViewById(R.id.bottomnav_profile);

        cart = findViewById(R.id.cartIcon);

        previus_rv.setVisibility(View.GONE);
        previusorder_shimmer.setVisibility(View.VISIBLE);
        previusorder_shimmer.startShimmer();

        craving_layout.setVisibility(View.GONE);
        cravingmenu_shimmer.setVisibility(View.VISIBLE);
        cravingmenu_shimmer.startShimmer();

        previus_rv.setHasFixedSize(true);
        previus_rv.setLayoutManager(new LinearLayoutManager(Home.this,LinearLayoutManager.HORIZONTAL,false));
        previusOrderAdapter = new PreviusOrderAdapter(Home.this,previusorderList,outofstockList,userChoosenFood);
        previus_rv.setAdapter(previusOrderAdapter);

        bestseller_rv.setLayoutManager(new LinearLayoutManager(Home.this,LinearLayoutManager.VERTICAL,false));
        bestSellerAdapter = new BestSellerAdapter(Home.this,bestsellerList,favouriteList,outofstockList,CustomerId,favouriteReference,userChoosenFood);
        bestseller_rv.setAdapter(bestSellerAdapter);

        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);


        previusLayout.setVisibility(View.GONE);
        bestseller_rv.setNestedScrollingEnabled(true);

        bestseller_shimmer.setVisibility(View.VISIBLE);
        bestseller_rv.setVisibility(View.GONE);
        bestseller_shimmer.startShimmer();

        takeaway.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
               if (seleceted!=1){
                   seleceted=1;
                   preferences.edit().putInt("DeliveryType",seleceted).apply();
                   takeaway.setBackgroundResource(R.drawable.ordertype_bk);
                   takeawaytxt.setTextColor(getResources().getColor(android.R.color.white));
                   takeawaypic.setImageResource(R.drawable.takeaway_white);

                   delivery.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                   deliverytxt.setTextColor(getResources().getColor(android.R.color.black));
                   deliverypic.setImageResource(R.drawable.fast_delivery);

                   ScaleAnimation tanimation = new ScaleAnimation(0.5f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
                   tanimation.setDuration(200);
                   tanimation.setFillAfter(true);
                   takeaway.startAnimation(tanimation);
               }

            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (seleceted!=2){
                    seleceted=2;
                    preferences.edit().putInt("DeliveryType",seleceted).apply();
                    delivery.setBackground(getDrawable(R.drawable.ordertype_bk));
                    deliverytxt.setTextColor(getResources().getColor(android.R.color.white));
                    deliverypic.setImageResource(R.drawable.fastdelivery_white);

                    takeaway.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    takeawaytxt.setTextColor(getResources().getColor(android.R.color.black));
                    takeawaypic.setImageResource(R.drawable.takeaway);

                    ScaleAnimation danimation = new ScaleAnimation(0.5f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                    danimation.setDuration(200);
                    danimation.setFillAfter(true);
                    delivery.startAnimation(danimation);

                }

            }
        });

        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        cravingmenuReference.orderByChild("CravingId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 if (!snapshot.exists()){
                     Toast.makeText(Home.this, "Error: ", Toast.LENGTH_SHORT).show();
                 }else{
                     for (DataSnapshot data:
                          snapshot.getChildren()) {
                         CravingLayoutModel model = data.getValue(CravingLayoutModel.class);
                         cravingList.add(model);
                     }
                      cravingAdapter = new CravingLayoutAdapter(Home.this, cravingList);
                      craving_layout.setAdapter(cravingAdapter);
                      if (cravingList.size() == 6){
                          craving_layout.setVisibility(View.VISIBLE);
                          cravingmenu_shimmer.setVisibility(View.GONE);
                          cravingmenu_shimmer.stopShimmer();
                      }
                 }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        outofstockReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    return;
                }
               else {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String fid = data.getValue(String.class);
                        outofstockList.add(fid);
                        if (bestSellerAdapter!=null){
                            bestSellerAdapter.notifyDataSetChanged();
                        }if (previusOrderAdapter!=null){
                            previusOrderAdapter.notifyDataSetChanged();
                        }
                    }
                }
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
                        if (!snapshot.exists()){
                            return;
                        }
                        else {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                String fid = data.getValue(String.class);
                                outofstockList.add(fid);
                                if (bestSellerAdapter!=null){
                                    bestSellerAdapter.notifyDataSetChanged();
                                }if (previusOrderAdapter!=null){
                                    previusOrderAdapter.notifyDataSetChanged();
                                }
                            }
                        }
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
                        if (!snapshot.exists()){
                            return;
                        }
                        else {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                String fid = data.getValue(String.class);
                                outofstockList.add(fid);
                                if (bestSellerAdapter!=null){
                                    bestSellerAdapter.notifyDataSetChanged();
                                }if (previusOrderAdapter!=null){
                                    previusOrderAdapter.notifyDataSetChanged();
                                }
                            }
                        }
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

        favouriteReference.child(CustomerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){return;}
                else {
                    for (DataSnapshot data:
                         snapshot.getChildren()) {
                        String fid = data.child("fid").getValue().toString();
                        favouriteList.add(fid);
                        if (bestSellerAdapter!=null){
                            bestSellerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        previusorderReference.child(CustomerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (!snapshot.exists()) {
                     previusLayout.setVisibility(View.GONE);
                     bestseller_rv.setNestedScrollingEnabled(true);
                 } else {
                     int size = (int) snapshot.getChildrenCount();
                     previusLayout.setVisibility(View.VISIBLE);
                     bestseller_rv.setNestedScrollingEnabled(false);
                     for (DataSnapshot data: snapshot.getChildren()) {
                           String fid = data.getValue(String.class);
                         foodReference.child(fid).addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                 if (snapshot1.exists()){
                                     FoodModel model = snapshot1.getValue(FoodModel.class);
                                     previusorderList.add(model);
                                     if (previusOrderAdapter!=null){
                                         if (previusorderList.size() == size){
                                             previusorder_shimmer.stopShimmer();
                                             previus_rv.setVisibility(View.VISIBLE);
                                             previusorder_shimmer.setVisibility(View.GONE);
                                         }
                                         previusOrderAdapter.notifyDataSetChanged();
                                     }
                                 }else return;
                             }
                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {}
                         });
                     }
                 }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        bestsellerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   for (DataSnapshot data:
                        snapshot.getChildren()) {
                       String fid = data.child("fid").getValue(String.class);
                       foodReference.child(fid).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot1) {
                               if (snapshot1.exists()){
                                   bestseller_shimmer.setVisibility(View.GONE);
                                   bestseller_rv.setVisibility(View.VISIBLE);
                                   bestseller_shimmer.stopShimmer();
                                   FoodModel model = snapshot1.getValue(FoodModel.class);
                                   bestsellerList.add(model);
                                   if (bestSellerAdapter!=null){
                                       bestSellerAdapter.notifyDataSetChanged();
                                   }
                               }else return;
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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Home.this,Menu.class);
                i.putExtra("From","Menu");
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,Profile.class));
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Cart.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
                finish();
             } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home){
            Toast.makeText(Home.this, "Home", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_profile) {
            startActivity(new Intent(Home.this,Profile.class));
        }else if(itemId == R.id.nav_favourite){
            startActivity(new Intent(Home.this, Favourite.class));
        }else if(itemId == R.id.nav_track){
            startActivity(new Intent(Home.this, TrackOrder.class));
        } else if (itemId == R.id.nav_customersurvice) {
            getSupportFragmentManager().beginTransaction().add(R.id.home_drawerLayout,new FAQ()).commit();
        }else if (itemId == R.id.nav_aboutus){
            getSupportFragmentManager().beginTransaction().add(R.id.home_drawerLayout,new AboutUs()).commit();
        } else if (itemId == R.id.nav_logout) {
            preferences.edit().putBoolean("Login",false).apply();
            mAuth.signOut();
            ((UserChoosenFood) CartHelper.getmyContext()).clearList();
            startActivity(new Intent(Home.this,LoginRegister.class));
            Toast.makeText(Home.this, "Log Out", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}