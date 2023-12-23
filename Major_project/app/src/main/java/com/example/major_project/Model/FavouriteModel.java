package com.example.major_project.Model;

public class FavouriteModel {
    String fid,fname,fprice,fpic,fdesc,ftype;
    public FavouriteModel() {
    }

    public FavouriteModel(String fid, String fname, String fprice, String fpic, String fdesc,String ftype) {
        this.fid = fid;
        this.fname = fname;
        this.fprice = fprice;
        this.fpic = fpic;
        this.fdesc = fdesc;
        this.ftype =ftype;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFprice() {
        return fprice;
    }

    public void setFprice(String fprice) {
        this.fprice = fprice;
    }

    public String getFpic() {
        return fpic;
    }

    public void setFpic(String fpic) {
        this.fpic = fpic;
    }

    public String getFdesc() {
        return fdesc;
    }

    public void setFdesc(String fdesc) {
        this.fdesc = fdesc;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }
}
