package com.example.zalpia.room;

import com.google.firebase.firestore.GeoPoint;

public class Branches {
    GeoPoint location;
    String name,status;
    String nameAr;

    public Branches() {
    }

    public Branches(GeoPoint location, String name, String status) {
        this.location = location;
        this.name = name;
        this.status = status;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Branches{" +
                "location=" + location +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
