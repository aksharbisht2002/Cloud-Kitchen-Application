package com.example.major_project.Interfaces;

import com.example.major_project.Model.CartModel;
import com.example.major_project.Model.FoodModel;

import java.util.ArrayList;
import java.util.Stack;

public interface UserChoosenFood {
        void getDataInfo(CartModel model);
        Stack<CartModel> getItem();
        float getTotalPrice();
        void clearList();
        int getListSize();
}
