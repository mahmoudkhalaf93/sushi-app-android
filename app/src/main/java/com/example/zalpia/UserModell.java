package com.example.zalpia;

import com.google.firebase.firestore.GeoPoint;

public class UserModell {

    String email,image,name,phone;

    public UserModell() {
    }

    public UserModell(String name, String email, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public UserModell(String name, String email, String phone, String image ) {
        this.email = email;
        this.image = image;
        this.name = name;
        this.phone = phone;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
