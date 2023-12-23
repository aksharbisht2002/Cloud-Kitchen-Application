package com.example.major_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.major_project.Adapter.MenuViewpagerlayoutAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Menu extends AppCompatActivity {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ImageView backBtn,cartBtn;
    TabLayout.Tab tab;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        viewPager2 = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        backBtn = findViewById(R.id.menu_backBtn);
        cartBtn = findViewById(R.id.cartIcon);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 onBackPressed();
            }
        });


        viewPager2.setAdapter(new MenuViewpagerlayoutAdapter(this));
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    switch (position){
                        case 0:  tab.setText("Appetizer");break;
                        case 1:  tab.setText("Soup");break;
                        case 2:  tab.setText("Veg");break;
                        case 3:  tab.setText("Kebabs");break;
                        case 4:  tab.setText("Chicken");break;
                        case 5:  tab.setText("Lamb & Goat");break;
                        case 6:  tab.setText("South Indian");break;
                        case 7:  tab.setText("Sea Food");break;
                        case 8:  tab.setText("Biryani");break;
                        case 9:  tab.setText("Bread");break;
                        case 10:  tab.setText("Chinese");break;
                        case 11:  tab.setText("Dessert");break;
                        case 12:  tab.setText("Drinks");break;

                    }
            }
        });
        mediator.attach();

        if ((getIntent().getExtras().getString("From")).equals("Craving")){
            int pos = Integer.parseInt(getIntent().getExtras().getString("pos","20"));
            if (pos!=20){
                switch (pos){
                    case 0: tab = tabLayout.getTabAt(2);tab.select();viewPager2.setCurrentItem(2);break;
                    case 1: tab = tabLayout.getTabAt(4);tab.select();viewPager2.setCurrentItem(4);break;
                    case 2: tab = tabLayout.getTabAt(8);tab.select();viewPager2.setCurrentItem(8);break;
                    case 3: tab = tabLayout.getTabAt(0);tab.select();viewPager2.setCurrentItem(0);break;
                    case 4: tab = tabLayout.getTabAt(10);tab.select();viewPager2.setCurrentItem(10);break;
                    case 5: tab = tabLayout.getTabAt(11);tab.select();viewPager2.setCurrentItem(11);break;
                    default: return;
                }
            }
        }


        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this, Cart.class));
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