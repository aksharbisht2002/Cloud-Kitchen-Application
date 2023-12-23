package com.example.major_project.Helper;

public class LoginHelper {

    String cid,name,email,phone;
    public LoginHelper() {
    }

    public LoginHelper(String cid, String name, String email, String phone) {
        this.cid = cid;
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
