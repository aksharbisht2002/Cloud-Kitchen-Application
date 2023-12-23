package com.example.major_project.Model;

public class OutOfStockModel {
    String fid;
    public OutOfStockModel() {}
    public OutOfStockModel(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
