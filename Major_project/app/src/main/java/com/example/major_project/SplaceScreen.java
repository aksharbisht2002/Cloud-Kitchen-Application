package com.example.major_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplaceScreen extends AppCompatActivity {
    ImageView bg,logo;
    TextView slogan;
    SharedPreferences pref;
    private float def_opacity= 1.0F;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


        pref=getSharedPreferences("DelhiGarden", Context.MODE_PRIVATE);

       bg=findViewById(R.id.slogan_bg);
       logo=findViewById(R.id.splase_white_logo);
       slogan=findViewById(R.id.splase_slogan);

       bg.setAlpha(def_opacity);
       logo.setAlpha(def_opacity);
       slogan.setAlpha(def_opacity);

        bg.animate().translationY(-3000).alpha(0).setDuration(900).setStartDelay(1500).start();
        logo.animate().translationY(2500).alpha(0).setDuration(1000).setStartDelay(1250).start();
        slogan.animate().translationY(2500).alpha(0).setDuration(1000).setStartDelay(1300).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirstTime = pref.getBoolean("firstTime",true);
               if(isFirstTime){
                   pref.edit().putBoolean("firstTime",false).apply();
                   Intent i = new Intent(SplaceScreen.this,OnBoardingScreen.class);
                   startActivity(i);
                   finish();
               }
               else {
                   Intent i = new Intent(SplaceScreen.this,LoginRegister.class);
                   startActivity(i);
                   finish();
               }

            }
        },2150);


    }

}