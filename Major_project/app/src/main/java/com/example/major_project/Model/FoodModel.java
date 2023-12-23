package com.example.major_project.Model;

public class FoodModel {
    String fid,category,type,name,desc,price,imgUri;
    public FoodModel() {}

    public FoodModel(String fid, String category, String type, String name, String desc, String price, String imgUri) {
        this.fid = fid;
        this.category = category;
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.imgUri = imgUri;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
