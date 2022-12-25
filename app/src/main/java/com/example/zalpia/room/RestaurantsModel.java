package com.example.zalpia.room;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class RestaurantsModel {
String name,phone,nameAr;

ArrayList<Branches> branches;

    public RestaurantsModel() {
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public ArrayList<Branches> getBranches() {
        return branches;
    }

    public void setBranches(ArrayList<Branches> branches) {
        this.branches = branches;
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

}
