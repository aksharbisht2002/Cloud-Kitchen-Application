package com.example.major_project.Helper;

public class OrderItemsHelper {
    String itemid,foodname,foodid,qnt;
    public OrderItemsHelper() {
    }

    public OrderItemsHelper(String itemid, String foodname, String foodid, String qnt) {
        this.itemid = itemid;
        this.foodname = foodname;
        this.foodid = foodid;
        this.qnt = qnt;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getQnt() {
        return qnt;
    }

    public void setQnt(String qnt) {
        this.qnt = qnt;
    }
}
