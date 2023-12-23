package com.example.major_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.major_project.Adapter.OnBoardScreenViewAdapter;

public class OnBoardingScreen extends AppCompatActivity {

     TextView nextbtn,skipbtn;
     ViewPager viewPager;
     SharedPreferences preferences ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

         viewPager=findViewById(R.id.onboard_screen_viewpager);
         nextbtn=findViewById(R.id.nextbtn);
         skipbtn=findViewById(R.id.skipbtn);
         preferences = getSharedPreferences("DelhiGarden",MODE_PRIVATE);

         viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
             @Override
             public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
             @Override
             public void onPageSelected(int position) {
                 if(position == 2) {
                     nextbtn.setText("START");
                 }

             }

             @Override
             public void onPageScrollStateChanged(int state) {}
         });

         nextbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(getItem(0) < 2 ){
                     viewPager.setCurrentItem(getItem(1),true);
                 }
                 else{
                     Intent i = new Intent(OnBoardingScreen.this, LoginRegister.class);
                     startActivity(i);
                     finish();
                 }
             }
         });
         skipbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                if(getItem(0)==0){  viewPager.setCurrentItem(getItem(2),true); }
                else { viewPager.setCurrentItem(getItem(1),true); }
             }
         });

        OnBoardScreenViewAdapter onBoardScreenViewAdapter = new OnBoardScreenViewAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(onBoardScreenViewAdapter);

    }

    private int getItem(int i) {
       return  viewPager.getCurrentItem() + i ;
    }
}