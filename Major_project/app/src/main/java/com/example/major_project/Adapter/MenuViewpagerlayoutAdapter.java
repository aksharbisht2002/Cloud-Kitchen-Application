package com.example.major_project.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.major_project.Fragments.MenuFragment.Appatizer;
import com.example.major_project.Fragments.MenuFragment.Biriyani;
import com.example.major_project.Fragments.MenuFragment.Bread;
import com.example.major_project.Fragments.MenuFragment.Chicken;
import com.example.major_project.Fragments.MenuFragment.Chinese;
import com.example.major_project.Fragments.MenuFragment.Dessert;
import com.example.major_project.Fragments.MenuFragment.Drinks;
import com.example.major_project.Fragments.MenuFragment.Kebabs;
import com.example.major_project.Fragments.MenuFragment.LambAndGoat;
import com.example.major_project.Fragments.MenuFragment.SeaFood;
import com.example.major_project.Fragments.MenuFragment.Soup;
import com.example.major_project.Fragments.MenuFragment.SouthIndian;
import com.example.major_project.Fragments.MenuFragment.Veg;

public class MenuViewpagerlayoutAdapter extends FragmentStateAdapter {

    public MenuViewpagerlayoutAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0: return new Appatizer();
            case 1: return new Soup();
            case 2: return new Veg();
            case 3: return new Kebabs();
            case 4:return new Chicken();
            case 5:return new LambAndGoat();
            case 6:return new SouthIndian();
            case 7:return new SeaFood();
            case 8:return new Biriyani();
            case 9:return new Bread();
            case 10:return new Chinese();
            case 11:return new Dessert();
            default: return new Drinks();
        }
    }


    @Override
    public int getItemCount() {
        return 13;
    }
}
