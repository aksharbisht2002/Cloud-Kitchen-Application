package com.example.major_project.Model;

public class CravingLayoutModel {
    String image;
    String text;
    String CravingId;
    public CravingLayoutModel(){}

    public CravingLayoutModel(String CravingId,String image, String text) {
        this.image = image;
        this.text = text;
        this.CravingId = CravingId;
    }

    public String getCravingId() {
        return CravingId;
    }

    public void setCravingId(String cravingId) {
        CravingId = cravingId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
