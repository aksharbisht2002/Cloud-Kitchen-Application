package com.example.major_project.Helper;

public class OrderHelper {
    String oid,cid,name,phone,address,price,time,mop,type,status;
    public OrderHelper() {
    }

    public OrderHelper(String oid, String cid, String name, String phone, String address, String price,String time, String mop, String type,String status) {
        this.oid = oid;
        this.cid = cid;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.price = price;
        this.time = time;
        this.mop = mop;
        this.type = type;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMop() {
        return mop;
    }

    public void setMop(String mop) {
        this.mop = mop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
