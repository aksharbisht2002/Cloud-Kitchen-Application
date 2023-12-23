package com.example.major_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.major_project.Fragments.LoginRegister.LogIn;
import com.example.major_project.Fragments.LoginRegister.ScreenLock;

public class LoginRegister extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        preferences = getSharedPreferences("DelhiGarden",MODE_PRIVATE);

        boolean logedin = preferences.getBoolean("Login",false);
        boolean checkAuth = preferences.getBoolean("Auth",false);

        if (logedin){
            if (checkAuth){
                getSupportFragmentManager().beginTransaction().add(R.id.LoginRegister,new ScreenLock()).commit();
            }else {
                startActivity(new Intent(LoginRegister.this,Home.class));
            }
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.LoginRegister,new LogIn()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}