package com.example.zalpia;

public class Products {

    String name,desc;
int image=R.drawable.sushiplat;

    public Products(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
    public Products() {

    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
}
