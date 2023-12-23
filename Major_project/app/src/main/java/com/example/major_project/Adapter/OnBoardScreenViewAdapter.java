package com.example.major_project.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.major_project.Fragments.OnboardScreen.FirstOnBoardScreen;
import com.example.major_project.Fragments.OnboardScreen.SecondOnBoardScreen;
import com.example.major_project.Fragments.OnboardScreen.ThirdOnBoardingScreen;

public class OnBoardScreenViewAdapter extends FragmentPagerAdapter {

     private Context context;

    public OnBoardScreenViewAdapter(@NonNull FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

          switch (position){
              case 0:
                  FirstOnBoardScreen firstOnBoardScreen = new FirstOnBoardScreen();
                  return firstOnBoardScreen;
              case 1:
                  SecondOnBoardScreen secondOnBoardScreen = new SecondOnBoardScreen();
                  return secondOnBoardScreen;
              case 2:
                  ThirdOnBoardingScreen thirdOnBoardingScreen = new ThirdOnBoardingScreen();
                  return thirdOnBoardingScreen;

          }
        return  null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
