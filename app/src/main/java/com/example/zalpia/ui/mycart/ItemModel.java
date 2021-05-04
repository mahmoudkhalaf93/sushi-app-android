package com.example.zalpia.ui.mycart;

import android.media.Image;

import com.example.zalpia.R;

public class ItemModel {
    String name="item test",description="very good item nice",price="90 EGP";
    int Image= R.drawable.zalpia;

    public ItemModel(String name, String description, String price, int image) {
        this.name = name;
        this.description = description;
        this.price = price;
        Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
