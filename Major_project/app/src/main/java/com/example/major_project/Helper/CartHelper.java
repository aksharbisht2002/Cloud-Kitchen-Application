package com.example.major_project.Helper;

import android.app.Application;
import android.content.Context;

import com.example.major_project.Interfaces.UserChoosenFood;
import com.example.major_project.Model.CartModel;
import com.example.major_project.Model.FoodModel;

import java.util.ArrayList;
import java.util.Stack;

public class CartHelper extends Application implements UserChoosenFood {

    private static Context context;
    Stack<CartModel> mList ;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mList = new Stack<>();
    }

    public static Context getmyContext(){
        return context;
    }

    public void clearList(){
        mList.clear();
    }

    @Override
    public int getListSize() {
        return mList.size();
    }

    @Override
    public void getDataInfo(CartModel model) {
        mList.add(model);
    }

    public  float getTotalPrice(){
        float price=0;
        for(int i=0 ;i<mList.size();i++){
            CartModel model = mList.get(i);
            price+= (Float.parseFloat(model.getPrice()) * model.getQnt());
        }
        return price;
    }

    @Override
    public Stack<CartModel> getItem() {
        return mList;
    }

}
